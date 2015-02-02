package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.label.TeamLabel;
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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public final class GamePanel extends ChessPanel {
    private final Game mGame;
    private final SquareConfig mSquareConfig;

    private List<TeamLabel> mTeamLabels;
    private BoardPanel[] mGameBoards;
    private JailPanel[] mJails;

    public GamePanel(@NotNull GlassPane glassPane, @NotNull Game game) {
        mGame = game;
        DropManager dropManager = new DropManager(this::boardRefresh, pair -> {
            BoardCoordinate origin = ((SquareJLabel) pair.first).getCoordinates();
            BoardCoordinate destination = ((SquareJLabel) pair.second).getCoordinates();
            playMove(mGame.newMoveBuilder(origin, destination));
        });

        mSquareConfig = new SquareConfig(dropManager, glassPane);

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
        IntStream.range(0, mGameBoards.length).forEach(i -> mGameBoards[i].updatePieceLocations(mGame.getBoards()[i],
                teamId -> new Color(mGame.getTeam(teamId).getTeamColor())));
        IntStream.range(0, mJails.length).forEach(i -> mJails[i].updateJailPopulation(mGame.getTeams()[i].getCapturedOpposingPieces(),
                new Color(mGame.getTeams()[i].getTeamColor())));
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
        JButton undoButton = new JButton(Messages.getString("PlayGamePanel.undo"));
        undoButton.addActionListener(event -> {
            mGame.undoMove();
            refreshStatus();
            boardRefresh();
        });

        int twoBoardsGridBagOffset = 0;

        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        Team[] teams = mGame.getTeams();
        mTeamLabels = new ArrayList<>(teams.length);

        mJails = new JailPanel[teams.length];

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
            constraints.ipadx = 200;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(10, 0, 0, 0);

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

        //TODO Figure out a way to get the total number of pieces in the game for each team
        IntStream.range(0, mGame.getTeams().length).forEach(i -> mJails[i] = new JailPanel(16));

        JPanel jailAndLabelPanel = new JPanel();
        jailAndLabelPanel.setLayout(new GridBagLayout());
        jailAndLabelPanel.setOpaque(false);

        constraints.weightx = 0.5;
        constraints.weighty = 0.0;
        constraints.ipadx = 0;

        // add the Black Team Label
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 0, 10, 10);
        jailAndLabelPanel.add(mTeamLabels.get(1), constraints);

        // add the Black timer
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 10, 10);
        // TODO: This currently assumes a two-person game.
//		if (!ChessTimer.isNoTimer(mTimers[1]))
//			jailAndLabelPanel.add(new ChessTimerLabel(mTimers[1]), constraints);

        // add the Black Jail
        constraints.gridy = 2;
        constraints.weighty = 0.7;
        jailAndLabelPanel.add(mJails[1], constraints);

        // adds the undo button
        constraints.weighty = 0.0;
        constraints.gridy = 3;
        jailAndLabelPanel.add(undoButton, constraints);

        // adds the White Jail
        constraints.gridy = 4;
        constraints.weighty = 0.7;
        jailAndLabelPanel.add(mJails[0], constraints);

        // adds the White timer
        constraints.gridy = 5;
        // TODO: This assumes a two-player game
//		jailAndLabelPanel.add(new ChessTimerLabel(mTimers[0]), constraints);

        //Add the White Team Label
        constraints.gridy = 6;
        constraints.weighty = 0.275;
        constraints.insets = new Insets(0, 0, 10, 10);
        jailAndLabelPanel.add(mTeamLabels.get(0), constraints);

        constraints.gridx = (boards.length * 2);
        constraints.gridy = 0;
        constraints.weighty = 0.0;
        constraints.weightx = 0.3;
        constraints.insets = new Insets(0, 25, 0, 10);
        add(jailAndLabelPanel, constraints);

//		resetTimers();
        boardRefresh();
        refreshStatus();
    }

}
