package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.rules.Rules;
import com.google.common.collect.Lists;

import java.util.List;

public final class Team {
    public Team(Rules rules) {
        mRules = rules;
        mCapturedPieces = Lists.newCopyOnWriteArrayList();
    }

    public Rules getRules() {
        return mRules;
    }

    @Deprecated
    public List<Piece> getPieces() {
        return null;
    }

    @Deprecated
    public int getTotalTeamSize() {
        return 0;
    }

    public List<Piece> getCapturedOpposingPieces() {
        return mCapturedPieces;
    }

    private final Rules mRules;
    private final List<Piece> mCapturedPieces;
}
