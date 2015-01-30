package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.label.SquareJLabel;
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

    private final Board mBoard;
    private final BoardPanel mBoardPanel;
    private final TeamCreationPanel mTeamPanel;

    public VariantCreationPanel(@NotNull GlassPane glassPane) {
        DropManager dropManager = new DropManager(this::boardRefresh,
                pair -> {
                    BoardCoordinate destination = ((SquareJLabel) pair.second).getCoordinates();
                    setPiece(new Piece(Piece.TEAM_ONE, PieceTypeManager.getBishopPieceType()), destination);
                });

        mBoard = new Board(BoardSize.CLASSIC_SIZE);
        mBoardPanel = new BoardPanel(mBoard.getBoardSize(), new SquareConfig(dropManager, glassPane), ImmutableSet::of);
        mTeamPanel = new TeamCreationPanel(dropManager, glassPane, mBoardPanel::getAllSquares);

        initComponents();
    }

    public void onPieceTypeSelected(PieceType pieceType) {
        mTeamPanel.setPieceType(pieceType);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc;

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
        mBoardPanel.updatePieceLocations(mBoard);
    }

    private void setPiece(Piece piece, BoardCoordinate coordinate) {
        mBoard.addPiece(piece, coordinate);
        boardRefresh();
    }
}
