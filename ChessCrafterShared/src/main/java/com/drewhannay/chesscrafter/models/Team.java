package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.rules.Rules;
import com.drewhannay.chesscrafter.rules.promotionmethods.PiecePromoter;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Team {
    private final int mTeamId;
    private final Rules mRules;
    private final Map<Move, Piece> mCapturedPieces;
    private final PiecePromoter mPiecePromoter;

    public Team(int teamId, Rules rules, PiecePromoter piecePromoter) {
        mTeamId = teamId;
        mRules = rules;
        mPiecePromoter = piecePromoter;
        mCapturedPieces = new HashMap<>();
    }

    public int getTeamId() {
        return mTeamId;
    }

    public Rules getRules() {
        return mRules;
    }

    public PiecePromoter getPiecePromoter() {
        return mPiecePromoter;
    }

    public Collection<Piece> getCapturedOpposingPieces() {
        return mCapturedPieces.values();
    }

    public void capturePiece(@NotNull Move move, @NotNull Piece piece) {
        Preconditions.checkArgument(!mCapturedPieces.containsKey(move));
        mCapturedPieces.put(move, piece);
    }

    public Piece undoCapturePiece(@NotNull Move move) {
        return mCapturedPieces.remove(move);
    }
}
