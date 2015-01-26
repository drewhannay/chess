package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.logic.Result;
import com.drewhannay.chesscrafter.logic.Status;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.MoveBuilder;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.Team;
import com.drewhannay.chesscrafter.utility.AppConstants;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayGamePanel extends ChessPanel {

    private static final long serialVersionUID = -2507232401817253688L;

    private List<TeamLabel> mTeamLabels;
    private BoardPanel[] mGameBoards;
    private JailPanel[] mJails;

    private final Game mGame;
    private final DropManager mDropManager;

    private JMenu mOptionsMenu;
    private GlassPane mGlobalGlassPane;

    public PlayGamePanel(@NotNull Game game) {
        mGame = game;
        mDropManager = new DropManager(this::boardRefresh, pair -> playMove(mGame.newMoveBuilder(pair.first, pair.second)));
        mGlobalGlassPane = new GlassPane();
        mGlobalGlassPane.setOpaque(false);

        Driver.getInstance().setGlassPane(mGlobalGlassPane);

        initComponents();
    }

    private void playMove(MoveBuilder moveBuilder) {
        if (moveBuilder.needsPromotion() && !moveBuilder.hasPromotionType()) {
            createPromotionPopup(moveBuilder);
        } else {
            mGame.executeMove(moveBuilder.build());
            refreshStatus();
            boardRefresh();
        }
    }

    private void createPromotionPopup(MoveBuilder moveBuilder) {
        JFrame promotionFrame = new JFrame();
        ChessPanel promotionPanel = new ChessPanel();
        JLabel promotionText = new JLabel("Choose a piece to promote to:");
        promotionText.setForeground(Color.white);
        promotionPanel.add(promotionText);
        for (PieceType pieceType : moveBuilder.getPromotionOptions()) {
            JButton label = new JButton(PieceIconUtility.getPieceIcon(pieceType.getName(), 48, mGame.getTurnKeeper().getActiveTeamId()));
            label.addActionListener(e -> {
                moveBuilder.setPromotionType(pieceType);
                playMove(moveBuilder);
                promotionFrame.dispose();
            });
            promotionPanel.add(label);
        }

        promotionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        promotionFrame.setSize(200, 200);
        promotionFrame.add(promotionPanel);
        promotionFrame.setLocationRelativeTo(null);
        promotionFrame.setVisible(true);
    }

    private void refreshStatus() {
        Status status = mGame.getStatus();
        if (Status.END_OF_GAME_STATUS.contains(status)) {
            endOfGame();
        }

        int activeTeamId = mGame.getTurnKeeper().getActiveTeamId();
        mTeamLabels.stream()
                .filter(teamLabel -> teamLabel.getTeamId() == activeTeamId)
                .forEach(teamLabel -> {
                    if (Status.IN_CHECK_STATUS.contains(status)) {
                        teamLabel.setInCheck();
                    } else {
                        teamLabel.setActive();
                    }
                });
        mTeamLabels.stream()
                .filter(teamLabel -> teamLabel.getTeamId() != activeTeamId)
                .forEach(TeamLabel::setInActive);
    }

    private void boardRefresh() {
        for (BoardPanel panel : mGameBoards) {
            panel.updatePieceLocations(mGame.getBoards());
        }
        int panelCount = 0;
        for (Team team : mGame.getTeams()) {
            mJails[panelCount++].updateJailPopulation(team.getCapturedOpposingPieces());
        }
    }

    private void endOfGame() {
        Result result = mGame.getHistory().getResult();

        Preconditions.checkNotNull(result);

        String panelTitle;
        String panelMessage;
        switch (result.status) {
            case DRAW:
                panelTitle = Messages.getString("PlayGamePanel.declareDraw");
                panelMessage = Messages.getString("PlayGamePanel.drawWhatNow");
                break;
            case STALEMATE:
                panelTitle = Messages.getString("PlayGamePanel.declareDraw");
                panelMessage = Messages.getString("PlayGamePanel.drawWhatNow");
                break;
            case CHECKMATE:
                String teamName = mTeamLabels.stream()
                        .filter(teamLabel -> teamLabel.getTeamId() == result.winningTeamId)
                        .findFirst().get().getName();
                panelTitle = teamName + " Wins!";
                panelMessage = Messages.getString("PlayGamePanel.gameOver");
                break;
            default:
                throw new IllegalStateException("Cannot end game with status:" + result.status);
        }

        Object[] options = new String[]{
                Messages.getString("PlayGamePanel.saveRecord"),
                Messages.getString("PlayGamePanel.newGame"),
                Messages.getString("PlayGamePanel.mainMenu")
        };

        switch (JOptionPane.showOptionDialog(Driver.getInstance(), panelMessage, panelTitle,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0])) {
            case JOptionPane.YES_OPTION:
                if (PreferenceUtility.getSaveLocationPreference().equals("default")) {
                    JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("PlayGamePanel.sinceFirstTime", AppConstants.APP_NAME),
                            Messages.getString("PlayGamePanel.saveLocation"), JOptionPane.PLAIN_MESSAGE);
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
                    if (returnVal == JFileChooser.APPROVE_OPTION)
                        PreferenceUtility.setSaveLocationPreference(fileChooser.getSelectedFile().getAbsolutePath());
                }
                String saveFileName = JOptionPane.showInputDialog(Driver.getInstance(), Messages.getString("PlayGamePanel.enterAName"),
                        Messages.getString("PlayGamePanel.saving"), JOptionPane.PLAIN_MESSAGE);
                try (FileOutputStream fileOut = new FileOutputStream(FileUtility.getCompletedGamesFile(saveFileName))) {
                    String json = GsonUtility.toJson(mGame.getHistory());
                    fileOut.write(json.getBytes());
                    fileOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Driver.getInstance().setFileMenuVisibility(true);
                PreferenceUtility.clearTooltipListeners();
                Driver.getInstance().revertToMainPanel();
                break;
            case JOptionPane.NO_OPTION:
                Driver.getInstance().setUpNewGame();
                break;
            case JOptionPane.CANCEL_OPTION:
                PreferenceUtility.clearTooltipListeners();
                Driver.getInstance().revertToMainPanel();
                Driver.getInstance().setFileMenuVisibility(true);
                break;
        }
    }

    public void saveGame() {
        String fileName = JOptionPane.showInputDialog(Driver.getInstance(),
                Messages.getString("PlayGamePanel.enterAName"), Messages.getString("PlayGamePanel.saving"), JOptionPane.PLAIN_MESSAGE);
        if (fileName == null)
            return;

        try (FileOutputStream fileOut = new FileOutputStream(FileUtility.getGamesInProgressFile(fileName))) {
            String json = GsonUtility.toJson(mGame.getHistory());
            fileOut.write(json.getBytes());
            fileOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JMenu createMenuBar() {
        mOptionsMenu = new JMenu(Messages.getString("PlayGamePanel.menu"));

        JMenuItem drawMenuItem = new JMenuItem(Messages.getString("PlayGamePanel.declareDraw"), KeyEvent.VK_D);
        JMenuItem saveMenuItem = new JMenuItem(Messages.getString("PlayGamePanel.saveAndQuit"), KeyEvent.VK_S);

        drawMenuItem.addActionListener(e -> {
            mOptionsMenu.setVisible(false);
            mGame.declareDraw();
            endOfGame();
        });

        saveMenuItem.addActionListener(event -> {
//				mWhiteTimer.stopTimer();
//				mBlackTimer.stopTimer();
            saveGame();

            mOptionsMenu.setVisible(false);
            PreferenceUtility.clearTooltipListeners();
            Driver.getInstance().revertToMainPanel();
        });

        mOptionsMenu.add(drawMenuItem);
        mOptionsMenu.add(saveMenuItem);

        return mOptionsMenu;
    }

    private void resizeElements(int width, int height){
        for (BoardPanel board : mGameBoards) {
            board.rescaleBoard((double) width, (double)height);
        }

        for (JailPanel jailPanel : mJails) {
            jailPanel.rescaleBoard((double) width, (double)height);
        }
    }

    private void initComponents() {
        JButton undoButton = new JButton(Messages.getString("PlayGamePanel.undo"));
        undoButton.addActionListener(event -> {
            mGame.undoMove();
            refreshStatus();
            boardRefresh();
        });

        int twoBoardsGridBagOffset = 0;
        if (mOptionsMenu == null || !mOptionsMenu.isVisible())
            Driver.getInstance().setMenu(createMenuBar());

        Driver.getInstance().setOptionsMenuVisibility(true);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        Team[] teams = mGame.getTeams();
        mTeamLabels = new ArrayList<>(teams.length);

        mJails = new JailPanel[teams.length];

        Board[] boards = mGame.getBoards();
        mGameBoards = new BoardPanel[boards.length];

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeElements(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });

        int gridx = 0;

        for (int boardIndex = 0; boardIndex < boards.length; boardIndex++) {
            constraints.gridheight = boards[boardIndex].getBoardSize().height + 2; // Added 2 for row labels? I dunno.
            constraints.gridy = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.insets = new Insets(10, 0, 0, 0);
            constraints.gridx = gridx;
            constraints.gridwidth = boards[boardIndex].getBoardSize().width + 2; // Added 2 for row labels?
            gridx += constraints.gridwidth;

            mGameBoards[boardIndex] = new BoardPanel(boards[boardIndex].getBoardSize(), boardIndex,
                    mGlobalGlassPane, mDropManager, pair -> {
                Piece piece = mGame.getPiece(pair.first, pair.second);
                if (piece != null && piece.getTeamId() == mGame.getTurnKeeper().getActiveTeamId()) {
                    return mGame.getMovesFrom(pair.first, pair.second);
                } else {
                    return Collections.emptySet();
                }
            });
            add(mGameBoards[boardIndex], constraints);
        }

        for (Team team : teams) {
            if (mGame.getGameType().equals("Classic")) {
                if (team.getTeamId() == 1)
                    mTeamLabels.add(new TeamLabel(team.getTeamId(), Messages.getString("PlayGamePanel.whiteTeam")));
                else
                    mTeamLabels.add(new TeamLabel(team.getTeamId(), Messages.getString("PlayGamePanel.blackTeam")));
            } else
                mTeamLabels.add(new TeamLabel(team.getTeamId(), "Team " + team.getTeamId()));
        }

        for (int teamIndex = 0; teamIndex < mGame.getTeams().length; teamIndex++) {
            //TODO Figure out a way to get the total number of pieces in the game for each team
            mJails[teamIndex] = new JailPanel(16, teamIndex);
        }

        // add the Black Team Label
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 0, 10, 0);
        add(mTeamLabels.get(1), constraints);

        // add the Black timer
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 2;
        constraints.insets = new Insets(0, 25, 10, 25);
        // TODO: This currently assumes a two-person game.
//		if (!ChessTimer.isNoTimer(mTimers[1]))
//			add(new ChessTimerLabel(mTimers[1]), constraints);

        // add the Black Jail
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 3;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 3;
        add(mJails[1], constraints);

        // adds the undo button
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 6;
        add(undoButton, constraints);

        // adds the White Jail
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 3;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 7;
        add(mJails[0], constraints);

        // adds the White timer
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 6;
        constraints.insets = new Insets(0, 0, 0, 0);

        //Add the White Team Label
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 10;
        constraints.insets = new Insets(10, 0, 10, 0);
        add(mTeamLabels.get(0), constraints);

        // TODO: This assumes a two-player game
//		add(new ChessTimerLabel(mTimers[0]), constraints);
//		resetTimers();
        Driver.getInstance().pack();
        boardRefresh();
        refreshStatus();
    }
}
