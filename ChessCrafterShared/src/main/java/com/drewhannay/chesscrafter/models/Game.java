package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.models.turnkeeper.TurnKeeper;
import com.google.common.base.Preconditions;

public final class Game {

    private final String mGameType;
    private final Team[] mTeams;
    private final Board[] mBoards;
    private final TurnKeeper mTurnKeeper;

    public Game(String gameType, Board[] boards, Team[] teams, TurnKeeper turnKeeper) {
        mGameType = gameType;
        mBoards = boards;
        mTeams = teams;
        mTurnKeeper = turnKeeper;
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
}
