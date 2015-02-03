package com.drewhannay.chesscrafter.panel;

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
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
    private final BoardPanel[] mGameBoards;
    private final List<TeamStatusPanel> mTeamStatusPanels;
    private final JTabbedPane mTabbedPane;

    private final JButton mUndoButton;
    private final JButton mForwardButton;
    private final JButton mBackButton;

    public GamePanel(@NotNull GlassPane glassPane, @NotNull Game game) {
        mGame = game;
        DropManager dropManager = new DropManager(this::refresh, pair -> {
            BoardCoordinate origin = ((SquareJLabel) pair.first).getCoordinates();
            BoardCoordinate destination = ((SquareJLabel) pair.second).getCoordinates();
            playMove(mGame.newMoveBuilder(origin, destination));
        });

        mSquareConfig = new SquareConfig(dropManager, glassPane);
        mGameBoards = new BoardPanel[mGame.getBoards().length];
        mTeamStatusPanels = new ArrayList<>(mGame.getTeams().length);
        mTabbedPane = new JTabbedPane();

        mUndoButton = new JButton(Messages.getString("PlayGamePanel.undo"));
        mForwardButton = new JButton("->");
        mBackButton = new JButton("<-");

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        Board[] boards = mGame.getBoards();
        IntStream.range(0, boards.length).forEach(boardIndex -> {
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
            constraints.gridheight = 2;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(10, 0, 0, 0);

            add(mGameBoards[boardIndex], constraints);
        });

        Stream.of(mGame.getTeams()).forEach(team -> mTeamStatusPanels.add(new TeamStatusPanel(team)));
        mTeamStatusPanels.forEach(panel -> mTabbedPane.addTab(panel.getName(), panel));

        constraints.gridx = boards.length * 2;
        constraints.weightx = 0.3;
        constraints.gridheight = 1;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(0, 25, 0, 10);
        add(mTabbedPane, constraints);

        mUndoButton.addActionListener(event -> {
            mGame.undoMove();
            refresh();
        });

        mForwardButton.addActionListener(event -> {
            mGame.nextMove();
            refresh();
        });
        mBackButton.addActionListener(event -> {
            mGame.previousMove();
            refresh();
        });

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);

        // add the undo button
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        //gbc.weighty = 0.2;
        //gbc.insets = new Insets(0, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        buttonPanel.add(mUndoButton, gbc);

        // add the back button
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        buttonPanel.add(mBackButton, gbc);

        // add the forward  button
        gbc.gridx = 1;
        buttonPanel.add(mForwardButton, gbc);

        constraints.gridy = 1;
        constraints.gridx = boards.length * 2;
        constraints.weighty = 1.0;
        add(buttonPanel, constraints);

        refresh();
    }

    public void declareDraw() {
        mGame.declareDraw();
    }

    public void endOfGame() {
        Result result = mGame.getHistory().getResult();

        Preconditions.checkNotNull(result);

        refreshNavigationButtonState();
    }

    public void saveGame() {
        String fileName = JOptionPane.showInputDialog(null, Messages.getString("PlayGamePanel.enterAName"),
                Messages.getString("PlayGamePanel.saving"), JOptionPane.PLAIN_MESSAGE);
        if (fileName == null) {
            return;
        }

        try (FileOutputStream fileOut = new FileOutputStream(FileUtility.getGameFile(fileName))) {
            String json = GsonUtility.toJson(mGame.getHistory());
            fileOut.write(json.getBytes());
            fileOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playMove(MoveBuilder moveBuilder) {
        if (moveBuilder.needsPromotion() && !moveBuilder.hasPromotionType()) {
            createPromotionPopup(moveBuilder);
        } else {
            mGame.executeMove(moveBuilder.build());
            refresh();
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

    private void refresh() {
        refreshStatus();
        refreshBoard();
        refreshNavigationButtonState();
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

    private void refreshBoard() {
        IntStream.range(0, mGameBoards.length).forEach(i -> mGameBoards[i].updatePieceLocations(mGame.getBoards()[i],
                teamId -> new Color(mGame.getTeam(teamId).getTeamColor())));
        mTeamStatusPanels.forEach(panel -> panel.getJail().updateJailPopulation(
                mGame.getTeam(panel.getTeamId()).getCapturedOpposingPieces(),
                teamId -> new Color(mGame.getTeam(teamId).getTeamColor())));
    }

    private void refreshNavigationButtonState() {
        mUndoButton.setVisible(mGame.canUndoMove());
        mUndoButton.setEnabled(mGame.canUndoMove());

        mForwardButton.setVisible(mGame.hasNextMove());
        mBackButton.setVisible(mGame.hasPreviousMove());

        mForwardButton.setEnabled(mGame.hasNextMove());
        mBackButton.setEnabled(mGame.hasPreviousMove());
    }
}
