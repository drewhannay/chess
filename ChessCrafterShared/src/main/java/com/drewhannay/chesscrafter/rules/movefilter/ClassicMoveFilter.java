package com.drewhannay.chesscrafter.rules.movefilter;

import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Piece;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public final class ClassicMoveFilter implements MoveFilter {

    private BoardCoordinate mObjectivePieceLocation;

    @Override
    public Set<BoardCoordinate> filterMoves(@NotNull Board board, @NotNull BoardCoordinate start, @NotNull Set<BoardCoordinate> moves) {
        Preconditions.checkArgument(board.doesPieceExistAt(start));

        Set<BoardCoordinate> filteredMoves = new HashSet<>(moves.size());

        int teamId = board.getPiece(start).getTeamId();

        for (BoardCoordinate move : moves) {
            Piece capturedPiece = board.movePiece(start, move);
            if (isObjectivePieceSafe(teamId, board)) {
                filteredMoves.add(move);
            }
            board.undoMovePiece(move, start);
            if (capturedPiece != null) {
                board.addPiece(capturedPiece, move);
            }
        }

        return filteredMoves;
    }

    private boolean isObjectivePieceSafe(int teamId, @NotNull Board board) {
        updateObjectivePieceLocation(teamId, board);

        BoardSize boardSize = board.getBoardSize();
        for (int x = 1; x <= boardSize.width; x++) {
            for (int y = 1; y <= boardSize.height; y++) {
                BoardCoordinate coordinate = BoardCoordinate.at(x, y);
                if (board.doesPieceExistAt(coordinate)) {
                    Set<BoardCoordinate> moves = board.getMovesFrom(coordinate);
                    if (moves.contains(mObjectivePieceLocation)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void updateObjectivePieceLocation(int teamId, @NotNull Board board) {
        if (mObjectivePieceLocation != null && isPieceObjectivePiece(teamId, board.getPiece(mObjectivePieceLocation))) {
            return;
        }

        BoardSize boardSize = board.getBoardSize();
        for (int x = 1; x <= boardSize.width; x++) {
            for (int y = 1; y <= boardSize.height; y++) {
                BoardCoordinate coordinate = BoardCoordinate.at(x, y);
                if (isPieceObjectivePiece(teamId, board.getPiece(coordinate))) {
                    mObjectivePieceLocation = coordinate;
                    return;
                }
            }
        }

        throw new IllegalStateException("No objective piece found");
    }

    private boolean isPieceObjectivePiece(int teamId, @Nullable Piece piece) {
        return piece != null && piece.getTeamId() == teamId && piece.isObjectivePiece();
    }
}
