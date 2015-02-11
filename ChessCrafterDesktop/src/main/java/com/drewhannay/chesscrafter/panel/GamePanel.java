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
import com.drewhannay.chesscrafter.files.FileManager;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;
import com.google.common.base.Preconditions;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
        setLayout(new MigLayout("", "[grow,fill,center][pref!]", "[grow,center,fill]"));
        Board[] boards = mGame.getBoards();

        JPanel boardPanels = new JPanel();
        boardPanels.setOpaque(false);
        boardPanels.setLayout(new MigLayout("center"));
        boardPanels.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                Stream.of(mGameBoards).forEach(board -> board.updateDimensions(e.getComponent().getWidth(),
                        e.getComponent().getHeight()));
                revalidate();
                repaint();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

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
            boardPanels.add(mGameBoards[boardIndex], "center");
        });

        Stream.of(mGame.getTeams()).forEach(team -> mTeamStatusPanels.add(new TeamStatusPanel(team)));
        mTeamStatusPanels.forEach(panel -> mTabbedPane.addTab(panel.getName(), panel));

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

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new MigLayout("wrap", "[fill]", "[fill]"));
        detailsPanel.setOpaque(false);

        // add the tabbed pane
        detailsPanel.add(mTabbedPane, "top, gapy 0px 10px");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        // add the undo button
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        buttonPanel.add(mUndoButton, gbc);

        // add the back button
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        buttonPanel.add(mBackButton, gbc);
        buttonPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        // add the forward  button
        gbc.gridx = 1;
        buttonPanel.add(mForwardButton, gbc);
        detailsPanel.add(buttonPanel, "top");

        add(boardPanels);
        add(detailsPanel, "top,wmax 235");

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

        if (!FileManager.INSTANCE.writeHistory(mGame.getHistory(), fileName)) {
            // TODO: report failure to user
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
        promotionFrame.setLayout(new GridBagLayout());

        JPanel promotionTextPanel = new ChessPanel();
        promotionTextPanel.setOpaque(false);

        JLabel promotionText = new JLabel("Choose a piece to promote to:");
        promotionText.setForeground(Color.white);

        promotionTextPanel.add(promotionText);

        ChessPanel promotionPanel = new ChessPanel();
        int gridSize = (moveBuilder.getPromotionOptions().size() / 2) + (moveBuilder.getPromotionOptions().size() % 2);
        promotionPanel.setLayout(new GridLayout(gridSize, gridSize));
        for (PieceType pieceType : moveBuilder.getPromotionOptions()) {
            JButton label = new JButton(PieceIconUtility.getPieceIcon(pieceType.getName(), activeTeamColor));
            label.setPreferredSize(new Dimension(100, 100));
            label.setBackground(activeTeamColor != Color.GRAY ? Color.GRAY : Color.getHSBColor(30, 70, 70));
            label.setContentAreaFilled(false);
            label.setOpaque(true);
            label.addActionListener(e -> {
                moveBuilder.setPromotionType(pieceType);
                playMove(moveBuilder);
                promotionFrame.dispose();
            });
            promotionPanel.add(label);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        promotionFrame.add(promotionTextPanel, gbc);
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        promotionFrame.add(promotionPanel, gbc);

        promotionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        promotionFrame.setLocationRelativeTo(null);
        promotionFrame.pack();
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
        mUndoButton.setVisible(!mGame.hasPreviousMove() && !mGame.hasNextMove());
        mUndoButton.setEnabled(mGame.canUndoMove());

        mForwardButton.setVisible(mGame.hasPreviousMove() || mGame.hasNextMove());
        mBackButton.setVisible(mGame.hasPreviousMove() || mGame.hasNextMove());

        mForwardButton.setEnabled(mGame.hasNextMove());
        mBackButton.setEnabled(mGame.hasPreviousMove());
    }
}
