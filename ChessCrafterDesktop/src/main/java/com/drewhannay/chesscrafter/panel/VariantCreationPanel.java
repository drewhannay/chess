package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.MotionAdapter;
import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class VariantCreationPanel extends ChessPanel {

    private final GlassPane mGlassPane;
    private final DropManager mDropManager;
    private final MotionAdapter mMotionAdapter;

    private final TeamCreationPanel mTeamPanel;

    private Board mBoard;
    private BoardPanel mBoardPanel;

    public VariantCreationPanel(@NotNull GlassPane glassPane) {
        mDropManager = new DropManager(this::boardRefresh,
                pair -> setPiece(new Piece(Piece.TEAM_ONE, PieceTypeManager.getBishopPieceType()), pair.second));
        mGlassPane = glassPane;
        mMotionAdapter = new MotionAdapter(mGlassPane);

        mTeamPanel = new TeamCreationPanel(mDropManager, mGlassPane, coordinate -> mBoardPanel.getSquareLabels());

        initComponents();
    }

    public void onPieceTypeSelected(PieceType pieceType) {
        mTeamPanel.setPieceType(pieceType);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc;

        mBoard = new Board(BoardSize.CLASSIC_SIZE);
        mBoardPanel = new BoardPanel(mBoard.getBoardSize(), 0, mGlassPane, mDropManager, pair -> ImmutableSet.of());

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        add(mTeamPanel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(mBoardPanel, gbc);
    }

    private void boardRefresh() {
        mBoardPanel.updatePieceLocations(new Board[]{mBoard});
    }

    private void setPiece(Piece piece, BoardCoordinate coordinate) {
        mBoard.addPiece(piece, coordinate);
        boardRefresh();
    }
}
