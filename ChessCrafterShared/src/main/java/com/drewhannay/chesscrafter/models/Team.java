package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.rules.conditionalmovegenerator.ConditionalMoveGenerator;
import com.drewhannay.chesscrafter.rules.endconditions.EndCondition;
import com.drewhannay.chesscrafter.rules.movefilter.MoveFilter;
import com.drewhannay.chesscrafter.rules.postmoveaction.PostMoveAction;
import com.drewhannay.chesscrafter.rules.promotionmethods.PiecePromoter;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Team {
    private final int mTeamId;
    private final Set<ConditionalMoveGenerator> mConditionalMoveGenerators;
    private final Set<MoveFilter> mMoveFilters;
    private final Set<PostMoveAction> mPostMoveActions;
    private final EndCondition mEndCondition;
    private final PiecePromoter mPiecePromoter;
    private final Map<Move, Piece> mCapturedPieces;

    public Team(int teamId,
                @NotNull Set<ConditionalMoveGenerator> conditionalMoveGenerators,
                @NotNull Set<MoveFilter> moveFilters,
                @NotNull Set<PostMoveAction> postMoveActions,
                @NotNull EndCondition endCondition,
                @NotNull PiecePromoter piecePromoter) {
        mTeamId = teamId;
        mConditionalMoveGenerators = conditionalMoveGenerators;
        mMoveFilters = moveFilters;
        mPostMoveActions = postMoveActions;
        mEndCondition = endCondition;
        mPiecePromoter = piecePromoter;
        mCapturedPieces = new HashMap<>();
    }

    public int getTeamId() {
        return mTeamId;
    }

    public Set<ConditionalMoveGenerator> getConditionalMoveGenerators() {
        return mConditionalMoveGenerators;
    }

    public Set<MoveFilter> getMoveFilters() {
        return mMoveFilters;
    }

    public Set<PostMoveAction> getPostMoveActions() {
        return mPostMoveActions;
    }

    public EndCondition getEndCondition() {
        return mEndCondition;
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

    @Nullable
    public Piece undoCapturePiece(@NotNull Move move) {
        return mCapturedPieces.remove(move);
    }
}
