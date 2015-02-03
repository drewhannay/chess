package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.logic.Result;
import com.drewhannay.chesscrafter.logic.Status;
import com.drewhannay.chesscrafter.models.turnkeeper.TurnKeeper;
import com.drewhannay.chesscrafter.rules.conditionalmovegenerator.ConditionalMoveGenerator;
import com.drewhannay.chesscrafter.rules.movefilter.MoveFilter;
import com.drewhannay.chesscrafter.rules.postmoveaction.PostMoveAction;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
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

    private int mHistoryIndex;
    private Status mStatus;

    public Game(@NotNull String gameType, @NotNull Board[] boards, @NotNull Team[] teams,
                @NotNull TurnKeeper turnKeeper, @Nullable History history) {
        mGameType = gameType;
        mBoards = boards;
        mTeams = teams;
        mTurnKeeper = turnKeeper;

        mStatus = Status.CONTINUE;

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

    public Status getStatus() {
        return mStatus;
    }

    public Piece getPiece(int boardIndex, BoardCoordinate coordinates) {
        Preconditions.checkPositionIndex(boardIndex, mBoards.length);

        return mBoards[boardIndex].getPiece(coordinates);
    }

    @NotNull
    public Set<BoardCoordinate> getMovesFrom(int boardIndex, @NotNull BoardCoordinate coordinate) {
        if (mHistory.isComplete()) {
            return ImmutableSet.of();
        }

        Board board = mBoards[boardIndex];
        Team team = getTeam(mTurnKeeper.getActiveTeamId());

        Set<BoardCoordinate> moves = board.getMovesFrom(coordinate);

        for (ConditionalMoveGenerator conditionalMoveGenerator : team.getConditionalMoveGenerators()) {
            moves.addAll(conditionalMoveGenerator.generateMoves(board, coordinate, mHistory));
        }

        for (MoveFilter moveFilter : team.getMoveFilters()) {
            moves = moveFilter.filterMoves(board, coordinate, moves);
        }

        return moves;
    }

    public MoveBuilder newMoveBuilder(@NotNull BoardCoordinate origin, @NotNull BoardCoordinate destination) {
        return new MoveBuilder(getTeam(mTurnKeeper.getActiveTeamId()), mBoards[0], origin, destination);
    }

    public void declareDraw() {
        Preconditions.checkState(!mHistory.isComplete());
        Preconditions.checkState(canUndoMove());

        mStatus = Status.DRAW;
        mHistory.setResult(new Result(mStatus, null));
        mHistoryIndex = mHistory.moves.size();
    }

    public boolean hasNextMove() {
        return mHistory.isComplete() && mHistoryIndex < mHistory.moves.size();
    }

    public void nextMove() {
        Preconditions.checkState(mHistory.isComplete());
        Preconditions.checkState(mHistoryIndex < mHistory.moves.size());

        Move move = mHistory.moves.get(mHistoryIndex);
        mHistoryIndex++;
        doExecuteMove(move);
    }

    public boolean hasPreviousMove() {
        return mHistory.isComplete() && !mHistory.moves.isEmpty() && mHistoryIndex > 0;
    }

    public void previousMove() {
        Preconditions.checkState(mHistory.isComplete());
        Preconditions.checkState(mHistoryIndex > 0);

        mHistoryIndex--;
        Move move = mHistory.moves.get(mHistoryIndex);
        doUndoMove(move);
    }

    public void executeMove(@NotNull Move move) {
        Preconditions.checkState(!mHistory.isComplete());

        doExecuteMove(move);
    }

    private void doExecuteMove(@NotNull Move move) {
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

        for (PostMoveAction action : team.getPostMoveActions()) {
            action.perform(board, team, move, capturedPiece);
        }

        mTurnKeeper.finishTurn();

        if (!mHistory.isComplete()) {
            mHistory.moves.add(move);
        }

        Team newActiveTeam = getTeam(mTurnKeeper.getActiveTeamId());
        mStatus = newActiveTeam.getEndCondition().checkEndCondition(this);

        if (Status.END_OF_GAME_STATUS.contains(mStatus)) {
            Integer winningTeamId = mStatus == Status.CHECKMATE ? team.getTeamId() : null;
            mHistory.setResult(new Result(mStatus, winningTeamId));
            mHistoryIndex = mHistory.moves.size();
        }
    }

    public boolean canUndoMove() {
        return !mHistory.isComplete() && !mHistory.moves.isEmpty();
    }

    public void undoMove() {
        Preconditions.checkState(!mHistory.isComplete());
        Preconditions.checkState(!mHistory.moves.isEmpty());

        Move move = mHistory.moves.remove(mHistory.moves.size() - 1);
        doUndoMove(move);
    }

    public void doUndoMove(@NotNull Move move) {

        // TODO: Undo check end condition

        mTurnKeeper.undoFinishTurn();

        Board board = mBoards[0];
        Team team = getTeam(mTurnKeeper.getActiveTeamId());

        // TODO: test this more closely to make sure it's correct
        mStatus = team.getEndCondition().checkEndCondition(this);

        for (PostMoveAction action : team.getPostMoveActions()) {
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

    public Team getTeam(int teamId) {
        for (Team team : mTeams) {
            if (team.getTeamId() == teamId) {
                return team;
            }
        }
        throw new IllegalArgumentException("invalid teamId");
    }
}
