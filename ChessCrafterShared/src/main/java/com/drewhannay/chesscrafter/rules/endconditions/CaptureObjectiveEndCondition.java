package com.drewhannay.chesscrafter.rules.endconditions;

import com.drewhannay.chesscrafter.logic.Status;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class CaptureObjectiveEndCondition extends EndCondition {

    public static final String NAME = "CaptureObjectiveEndCondition";

    private final int mTeamId;

    private BoardCoordinate mObjectivePieceLocation;

    public CaptureObjectiveEndCondition(int teamId) {
        mTeamId = teamId;
    }

    @Override
    public Status checkEndCondition(@NotNull Game game) {
        int boardIndex = 0;
        Board board = game.getBoards()[boardIndex];

        updateObjectivePieceLocation(mTeamId, board);

        int attackCount = getObjectivePieceAttackCount(mTeamId, board);
        int legalMoveCount = 0;

        BoardSize boardSize = board.getBoardSize();
        for (int x = 1; x <= boardSize.width; x++) {
            for (int y = 1; y <= boardSize.height; y++) {
                BoardCoordinate coordinate = BoardCoordinate.at(x, y);
                if (board.doesPieceExistAt(coordinate)) {
                    if (game.getPiece(boardIndex, coordinate).getTeamId() == mTeamId) {
                        Set<BoardCoordinate> moves = game.getMovesFrom(boardIndex, BoardCoordinate.at(x, y));
                        legalMoveCount += moves.size();
                    }
                }
            }
        }

        return legalMoveCount > 0 && attackCount == 1 ? Status.CHECK
                : legalMoveCount > 0 && attackCount > 1 ? Status.DOUBLE_CHECK
                : legalMoveCount == 0 && attackCount == 0 ? Status.STALEMATE
                : legalMoveCount == 0 && attackCount > 0 ? Status.CHECKMATE
                : Status.CONTINUE;
    }

    @Override
    public void undo() {
        // nothing to undo
    }

    private int getObjectivePieceAttackCount(int teamId, @NotNull Board board) {
        updateObjectivePieceLocation(teamId, board);

        int attackCount = 0;

        BoardSize boardSize = board.getBoardSize();
        for (int x = 1; x <= boardSize.width; x++) {
            for (int y = 1; y <= boardSize.height; y++) {
                BoardCoordinate coordinate = BoardCoordinate.at(x, y);
                if (board.doesPieceExistAt(coordinate)) {
                    Set<BoardCoordinate> moves = board.getMovesFrom(coordinate);
                    if (moves.contains(mObjectivePieceLocation)) {
                        attackCount++;
                    }
                }
            }
        }

        return attackCount;
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
