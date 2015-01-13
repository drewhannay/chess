package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.controllers.GameController;
import com.drewhannay.chesscrafter.rules.Rules;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;

public final class Team {
    public Team(Rules rules, List<Piece> pieces) {
        mRules = rules;
        mPieces = pieces;
        mCapturedPieces = Lists.newCopyOnWriteArrayList();
    }

    public Rules getRules() {
        return mRules;
    }

    public List<Piece> getPieces() {
        return mPieces;
    }

    public int getTotalTeamSize() {
        int capturedSize = mCapturedPieces.size();
        int uncapturedSize = mPieces == null ? 0 : mPieces.size();
        return capturedSize + uncapturedSize;
    }

    /**
     * @return A List of Piece objects that belong to this Team but have been
     * captured by another Team
     */
    public List<Piece> getCapturedPieces() {
        return mCapturedPieces;
    }

    public List<Piece> getNonCapturedObjectivePieces() {
        List<Piece> objectivePieces = Lists.newArrayList();
        PieceType objectiveType = mRules.getObjectivePieceType();

        for (Piece piece : mPieces) {
            if (piece.getPieceType().equals(objectiveType))
                objectivePieces.add(piece);
        }

        return objectivePieces;
    }

    public boolean objectivePieceIsInCheck(int teamIndex) {
        //GameController.computeLegalDestinations();

        for (Piece piece : getNonCapturedObjectivePieces()) {
            if (GameController.isThreatened(piece.getCoordinates(), teamIndex))
                return true;
        }
        return false;
    }


    public void markPieceAsCaptured(Piece piece) {
        synchronized (this) {
            mPieces.remove(piece);
            if (!mCapturedPieces.contains(piece))
                mCapturedPieces.add(piece);
        }
    }

    public void markPieceAsNotCaptured(Piece piece) {
        synchronized (this) {
            mCapturedPieces.remove(piece);
            if (!mPieces.contains(piece))
                mPieces.add(piece);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Team))
            return false;

        Team otherTeam = (Team) other;

        return Objects.equal(mRules, otherTeam.mRules) && Objects.equal(mPieces, otherTeam.mPieces)
                && Objects.equal(mCapturedPieces, otherTeam.mCapturedPieces);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mRules, mPieces, mCapturedPieces);
    }

    private final Rules mRules;
    private final List<Piece> mPieces;
    private final List<Piece> mCapturedPieces;
}
