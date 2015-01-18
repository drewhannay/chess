package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.models.turnkeeper.TurnKeeper;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

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

    public Piece getPiece(int boardIndex, ChessCoordinate coordinates) {
        Preconditions.checkPositionIndex(boardIndex, mBoards.length);

        return mBoards[boardIndex].getPiece(coordinates);
    }

    public void executeMove(@NotNull Move move) {
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

        // TODO: PostMoveAction
        // TODO: Check end condition

        mTurnKeeper.finishTurn();

        mHistory.push(move);
    }

    public void undoMove() {
        Preconditions.checkState(!mHistory.isEmpty());

        Move move = mHistory.pop();

        mTurnKeeper.undoFinishTurn();

        Board board = mBoards[0];
        Team team = getTeam(mTurnKeeper.getActiveTeamId());

        // TODO: Undo check end condition
        // TODO: Undo PostMoveAction

        if (move.promotionType != null) {
            Piece demotedPiece = team.getPiecePromoter().undoPromotion();
            board.addPiece(demotedPiece, move.destination);
        }

        mBoards[0].undoMovePiece(move.destination, move.origin);

        Piece capturedPiece = getTeam(mTurnKeeper.getActiveTeamId()).undoCapturePiece(move);
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
