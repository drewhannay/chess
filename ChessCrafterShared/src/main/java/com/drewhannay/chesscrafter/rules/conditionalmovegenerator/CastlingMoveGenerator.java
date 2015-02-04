package com.drewhannay.chesscrafter.rules.conditionalmovegenerator;

import com.drewhannay.chesscrafter.logic.PathMaker;
import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.History;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.rules.movefilter.ClassicMoveFilter;
import com.drewhannay.chesscrafter.rules.movefilter.MoveFilter;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class CastlingMoveGenerator extends ConditionalMoveGenerator {
    public static final String NAME = "CastlingMoveGenerator";

    @NotNull
    @Override
    public Set<BoardCoordinate> generateMoves(@NotNull Board board, @NotNull BoardCoordinate start, @NotNull History history) {
        Piece movingPiece = board.getPiece(start);

        // must be moving the king
        if (!isKing(movingPiece)) {
            return Collections.emptySet();
        }

        // must not have moved the king in the past
        if (movingPiece.hasMoved()) {
            return Collections.emptySet();
        }

        Set<BoardCoordinate> moves = new HashSet<>(2);

        Piece queenSideRook = board.getPiece(BoardCoordinate.at(1, start.y));
        if (isCastleableRook(queenSideRook)) {
            if (!isPathBlocked(board, start, 2)) {
                moves.addAll(filterMoves(board, start, -1));
            }
        }

        Piece kingSideRook = board.getPiece(BoardCoordinate.at(board.getBoardSize().width, start.y));
        if (isCastleableRook(kingSideRook)) {
            if (!isPathBlocked(board, start, board.getBoardSize().width - 1)) {
                moves.addAll(filterMoves(board, start, 1));
            }
        }

        return moves;
    }

    private Set<BoardCoordinate> filterMoves(Board board, BoardCoordinate start, int direction) {
        BoardCoordinate firstStep = BoardCoordinate.at(start.x + direction, start.y);
        BoardCoordinate destination = BoardCoordinate.at(start.x + direction * 2, start.y);

        MoveFilter moveFilter = new ClassicMoveFilter();
        Set<BoardCoordinate> legalMoves = moveFilter.filterMoves(board, start, Sets.newHashSet(firstStep, destination));
        return legalMoves.size() == 2 ? Sets.newHashSet(destination) : Collections.<BoardCoordinate>emptySet();
    }

    private boolean isPathBlocked(Board board, BoardCoordinate start, int x) {
        PathMaker pathMaker = new PathMaker(start, BoardCoordinate.at(x, start.y));
        for (BoardCoordinate coordinate : pathMaker.getPathToDestination()) {
            if (board.doesPieceExistAt(coordinate)) {
                return true;
            }
        }

        return false;
    }

    private boolean isKing(Piece piece) {
        return piece.getInternalId().equals(PieceTypeManager.KING_ID);
    }

    private boolean isCastleableRook(@Nullable Piece rook) {
        return rook != null && !rook.hasMoved() && isRook(rook);
    }

    private boolean isRook(Piece piece) {
        return piece.getInternalId().equals(PieceTypeManager.ROOK_ID);
    }
}
