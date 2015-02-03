package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.logic.Result;
import com.drewhannay.chesscrafter.logic.Status;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class GamePanel extends ChessPanel {
    private final Game mGame;
    private final SquareConfig mSquareConfig;
    private final List<TeamStatusPanel> mTeamStatusPanels;
    private final JTabbedPane mTabbedPane;

    private BoardPanel[] mGameBoards;

    public GamePanel(@NotNull GlassPane glassPane, @NotNull Game game) {
        mGame = game;
        DropManager dropManager = new DropManager(this::boardRefresh, pair -> {
            BoardCoordinate origin = ((SquareJLabel) pair.first).getCoordinates();
            BoardCoordinate destination = ((SquareJLabel) pair.second).getCoordinates();
            playMove(mGame.newMoveBuilder(origin, destination));
        });

        mSquareConfig = new SquareConfig(dropManager, glassPane);
        mTeamStatusPanels = new ArrayList<>(mGame.getTeams().length);
        mTabbedPane = new JTabbedPane();

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
        Team activeTeam = mGame.getTeam(mGame.getTurnKeeper().getActiveTeamId());
        Color activeTeamColor = new Color(activeTeam.getTeamColor());

        JFrame promotionFrame = new JFrame();
        ChessPanel promotionPanel = new ChessPanel();
        JLabel promotionText = new JLabel("Choose a piece to promote to:");
        promotionText.setForeground(Color.white);
        promotionPanel.add(promotionText);
        for (PieceType pieceType : moveBuilder.getPromotionOptions()) {
            JButton label = new JButton(PieceIconUtility.getPieceIcon(pieceType.getName(), activeTeamColor));
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
        mTeamStatusPanels.forEach(panel -> panel.updateStatus(activeTeamId, status));
        mTeamStatusPanels.stream().filter(panel -> panel.getTeamId() == activeTeamId)
                .forEach(mTabbedPane::setSelectedComponent);
    }

    private void boardRefresh() {
        IntStream.range(0, mGameBoards.length).forEach(i -> mGameBoards[i].updatePieceLocations(mGame.getBoards()[i],
                teamId -> new Color(mGame.getTeam(teamId).getTeamColor())));
        mTeamStatusPanels.forEach(panel -> panel.getJail().updateJailPopulation(
                mGame.getTeam(panel.getTeamId()).getCapturedOpposingPieces(), new Color(mGame.getTeam(panel.getTeamId()).getTeamColor())));
    }

    public void declareDraw() {
        mGame.declareDraw();
    }

    public void endOfGame() {
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
                String teamName = mTeamStatusPanels.stream()
                        .filter(panel -> panel.getTeamId() == result.winningTeamId)
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

        switch (JOptionPane.showOptionDialog(this, panelMessage, panelTitle,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0])) {
            case JOptionPane.YES_OPTION:
                if (PreferenceUtility.getSaveLocationPreference().equals("default")) {
                    JOptionPane.showMessageDialog(this, Messages.getString("PlayGamePanel.sinceFirstTime", AppConstants.APP_NAME),
                            Messages.getString("PlayGamePanel.saveLocation"), JOptionPane.PLAIN_MESSAGE);
                    File directory = FileUtility.chooseDirectory();
                    if (directory != null) {
                        PreferenceUtility.setSaveLocationPreference(directory.getAbsolutePath());
                    }
                }
                String saveFileName = JOptionPane.showInputDialog(this, Messages.getString("PlayGamePanel.enterAName"),
                        Messages.getString("PlayGamePanel.saving"), JOptionPane.PLAIN_MESSAGE);
                try (FileOutputStream fileOut = new FileOutputStream(FileUtility.getGameFile(saveFileName))) {
                    String json = GsonUtility.toJson(mGame.getHistory());
                    fileOut.write(json.getBytes());
                    fileOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PreferenceUtility.clearTooltipListeners();
                // TODO: transition to review mode?
                break;
            case JOptionPane.NO_OPTION:
                // TODO: hacky method call
                ChessActions.NEW_GAME.getAction().actionPerformed(null);
                break;
            case JOptionPane.CANCEL_OPTION:
                PreferenceUtility.clearTooltipListeners();
                // TODO: transition to review mode?
                break;
        }
    }

    public void saveGame() {
        String fileName = JOptionPane.showInputDialog(null,
                Messages.getString("PlayGamePanel.enterAName"), Messages.getString("PlayGamePanel.saving"), JOptionPane.PLAIN_MESSAGE);
        if (fileName == null)
            return;

        try (FileOutputStream fileOut = new FileOutputStream(FileUtility.getGameFile(fileName))) {
            String json = GsonUtility.toJson(mGame.getHistory());
            fileOut.write(json.getBytes());
            fileOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        Board[] boards = mGame.getBoards();
        mGameBoards = new BoardPanel[boards.length];

        for (int index = 0; index < boards.length; index++) {
            int boardIndex = index;
            mGameBoards[boardIndex] = new BoardPanel(boards[boardIndex].getBoardSize(), mSquareConfig,
                    coordinate -> {
                        Piece piece = mGame.getPiece(boardIndex, coordinate);
                        if (piece != null && piece.getTeamId() == mGame.getTurnKeeper().getActiveTeamId()) {
                            return mGame.getMovesFrom(boardIndex, coordinate);
                        } else {
                            return Collections.emptySet();
                        }
                    });
            constraints.gridx = boardIndex;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(10, 0, 0, 0);

            add(mGameBoards[boardIndex], constraints);
        }

        createTeamPanels();

        constraints.gridx = (boards.length * 2);
        constraints.weightx = 0.3;
        constraints.insets = new Insets(0, 25, 0, 10);
        add(mTabbedPane, constraints);

        JButton undoButton = new JButton(Messages.getString("PlayGamePanel.undo"));
        undoButton.addActionListener(event -> {
            mGame.undoMove();
            refreshStatus();
            boardRefresh();
        });
        JButton forward = new JButton("->");
        JButton back = new JButton("<-");

        // add the undo button
        constraints.weighty = 0.0;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.BOTH;
        add(undoButton, constraints);

        // add the back button
        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.weighty = 0.0;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(0, 10, 10, 10);
        add(back, constraints);

        // add the forward  button
        constraints.gridx = 1;
        add(forward, constraints);

        boardRefresh();
        refreshStatus();
    }

    private void createTeamPanels() {
        Stream.of(mGame.getTeams()).forEach(team -> mTeamStatusPanels.add(new TeamStatusPanel(team)));
        mTeamStatusPanels.forEach(panel -> mTabbedPane.addTab(panel.getName(), panel));
    }
}
