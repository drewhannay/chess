package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.Result;
import com.drewhannay.chesscrafter.models.turnkeeper.TurnKeeper;
import com.drewhannay.chesscrafter.rules.conditionalmovegenerator.ConditionalMoveGenerator;
import com.drewhannay.chesscrafter.rules.movefilter.MoveFilter;
import com.drewhannay.chesscrafter.rules.postmoveaction.PostMoveAction;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Stack;

public final class Game {

    private final String mGameType;
    private final Team[] mTeams;
    private final Board[] mBoards;
    private final TurnKeeper mTurnKeeper;
    private final Stack<Move> mHistory;

    public Game(String gameType, Board[] boards, Team[] teams, TurnKeeper turnKeeper) {
        mGameType = gameType;
        mBoards = boards;
        mTeams = teams;
        mTurnKeeper = turnKeeper;
        mHistory = new Stack<>();
    }

    public String getGameType() {
        return mGameType;
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

    public Result executeMove(@NotNull Move move) {
        Board board = mBoards[0];
        Team team = getTeam(mTurnKeeper.getActiveTeamId());

        Piece capturedPiece = board.movePiece(move.origin, move.destination);
        if (capturedPiece != null) {
            team.capturePiece(move, capturedPiece);
        }

        if (move.promotionType != null) {
            Piece promotedPiece = team.getPiecePromoter().promotePiece(board.getPiece(move.destination), move.promotionType);
            board.addPiece(promotedPiece, move.destination);
        }

        for (PostMoveAction action : team.getRules().getPostMoveActions()) {
            action.perform(board, team, move, capturedPiece);
        }

        mTurnKeeper.finishTurn();

        Team newActiveTeam = getTeam(mTurnKeeper.getActiveTeamId());
        Result result = newActiveTeam.getRules().getEndCondition().checkEndCondition(this);
        // TODO: remove println
        System.out.println(result);
        mHistory.push(move);
        return result;
    }

    public void undoMove() {
        Preconditions.checkState(!mHistory.isEmpty());

        Move move = mHistory.pop();

        // TODO: Undo check end condition

        mTurnKeeper.undoFinishTurn();

        Board board = mBoards[0];
        Team team = getTeam(mTurnKeeper.getActiveTeamId());

        for (PostMoveAction action : team.getRules().getPostMoveActions()) {
            action.undo(board, team, move, mHistory.isEmpty() ? null : mHistory.peek());
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
