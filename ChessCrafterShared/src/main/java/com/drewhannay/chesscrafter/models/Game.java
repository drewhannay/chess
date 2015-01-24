package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.logic.Result;
import com.drewhannay.chesscrafter.models.turnkeeper.TurnKeeper;
import com.drewhannay.chesscrafter.rules.conditionalmovegenerator.ConditionalMoveGenerator;
import com.drewhannay.chesscrafter.rules.movefilter.MoveFilter;
import com.drewhannay.chesscrafter.rules.postmoveaction.PostMoveAction;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Set;

public final class Game {

    private final String mGameType;
    private final Team[] mTeams;
    private final Board[] mBoards;
    private final TurnKeeper mTurnKeeper;
    private final History mHistory;

    private Result mResult;

    public Game(@NotNull String gameType, @NotNull Board[] boards, @NotNull Team[] teams,
                @NotNull TurnKeeper turnKeeper, @Nullable History history) {
        mGameType = gameType;
        mBoards = boards;
        mTeams = teams;
        mTurnKeeper = turnKeeper;

        mHistory = new History(gameType, new ArrayList<Move>());
        if (history != null) {
            Preconditions.checkArgument(gameType.equals(history.variantName),
                    "History variantName {" + history.variantName + "} does not match gameType {" + gameType + "}");

            for (Move move : history.moves) {
                executeMove(move);
            }
        }
    }

    public String getGameType() {
        return mGameType;
    }

    // TODO: don't want this to be public
    public History getHistory() {
        return mHistory;
    }

    public Board[] getBoards() {
        return mBoards;
    }

    public Team[] getTeams() {
        return mTeams;
    }

    public TurnKeeper getTurnKeeper() {
        return mTurnKeeper;
    }

    public Result getResult() {
        return mResult;
    }

    public Piece getPiece(int boardIndex, BoardCoordinate coordinates) {
        Preconditions.checkPositionIndex(boardIndex, mBoards.length);

        return mBoards[boardIndex].getPiece(coordinates);
    }

    @NotNull
    public Set<BoardCoordinate> getMovesFrom(int boardIndex, @NotNull BoardCoordinate coordinate) {
        Board board = mBoards[boardIndex];
        Team team = getTeam(mTurnKeeper.getActiveTeamId());

        Set<BoardCoordinate> moves = board.getMovesFrom(coordinate);

        for (ConditionalMoveGenerator conditionalMoveGenerator : team.getConditionalMoveGenerators()) {
            moves.addAll(conditionalMoveGenerator.generateMoves(board, coordinate, mHistory));
        }

        for (MoveFilter moveFilter : team.getRules().getMoveFilters()) {
            moves = moveFilter.filterMoves(board, coordinate, moves);
        }

        return moves;
    }

    public MoveBuilder newMoveBuilder(@NotNull BoardCoordinate origin, @NotNull BoardCoordinate destination) {
        return new MoveBuilder(getTeam(mTurnKeeper.getActiveTeamId()), mBoards[0], origin, destination);
    }

    public void executeMove(@NotNull Move move) {
        Board board = mBoards[0];
        Team team = getTeam(mTurnKeeper.getActiveTeamId());

        Piece capturedPiece = board.movePiece(move.origin, move.destination);
        if (capturedPiece != null) {
            team.capturePiece(move, capturedPiece);
        }

        if (move.promotionType != null) {
            Piece promotedPiece = team.getPiecePromoter().promotePiece(board.getPiece(move.destination),
                    PieceTypeManager.INSTANCE.getPieceTypeByName(move.promotionType));
            board.addPiece(promotedPiece, move.destination);
        }

        for (PostMoveAction action : team.getRules().getPostMoveActions()) {
            action.perform(board, team, move, capturedPiece);
        }

        mTurnKeeper.finishTurn();

        mHistory.moves.add(move);

        Team newActiveTeam = getTeam(mTurnKeeper.getActiveTeamId());
        mResult = newActiveTeam.getRules().getEndCondition().checkEndCondition(this);
        // TODO: remove println
        System.out.println(mResult);
    }

    public void undoMove() {
        Preconditions.checkState(!mHistory.moves.isEmpty());

        Move move = mHistory.moves.remove(mHistory.moves.size() - 1);

        // TODO: Undo check end condition

        mTurnKeeper.undoFinishTurn();

        Board board = mBoards[0];
        Team team = getTeam(mTurnKeeper.getActiveTeamId());

        for (PostMoveAction action : team.getRules().getPostMoveActions()) {
            action.undo(board, team, move, mHistory.moves.isEmpty() ? null : mHistory.moves.get(mHistory.moves.size() - 1));
        }

        if (move.promotionType != null) {
            Piece demotedPiece = team.getPiecePromoter().undoPromotion();
            board.addPiece(demotedPiece, move.destination);
        }

        Piece capturedPiece = getTeam(mTurnKeeper.getActiveTeamId()).undoCapturePiece(move);
        mBoards[0].undoMovePiece(move.origin, move.destination, capturedPiece);


        if (capturedPiece != null) {
            mBoards[0].addPiece(capturedPiece, move.destination);
        }
    }

    private Team getTeam(int teamId) {
        for (Team team : mTeams) {
            if (team.getTeamId() == teamId) {
                return team;
            }
        }
        throw new IllegalArgumentException("invalid teamId");
    }
}
