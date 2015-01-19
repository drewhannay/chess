package com.drewhannay.chesscrafter.rules;

import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.rules.endconditions.EndCondition;
import com.drewhannay.chesscrafter.rules.movefilter.MoveFilter;
import com.drewhannay.chesscrafter.rules.postmoveaction.PostMoveAction;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;

public final class Rules {
    public static final int DESTINATION_SAME_BOARD = 0;
    public static final int DESTINATION_OPPOSITE_BOARD = 1;

    public Rules(PieceType objectivePieceType, int destinationBoardType,
                 List<MoveFilter> moveFilters, List<PostMoveAction> postMoveActions, EndCondition endCondition) {
        mObjectivePieceType = objectivePieceType;
        mDestinationBoardType = destinationBoardType;
        mMoveFilters = Lists.newArrayList(moveFilters);
        mPostMoveActions = Lists.newArrayList(postMoveActions);
        mEndCondition = endCondition;
    }

    /**
     * public void performAfterMoveAction(MoveController move)
     * public void checkEndOfGame(PieceController objectivePiece)
     * public Piece promotePiece(Piece pieceToPromote, boolean
     * pieceCanBePromoted, String pieceTypeToPromoteFrom)
     */

    public PieceType getObjectivePieceType() {
        return mObjectivePieceType;
    }

    public int getDestinationBoardIndex(int startIndex) {
        switch (mDestinationBoardType) {
            case DESTINATION_OPPOSITE_BOARD:
                return (startIndex + 1) % 2;
            case DESTINATION_SAME_BOARD:
            default:
                return startIndex;
        }
    }

    public List<MoveFilter> getMoveFilters() {
        return mMoveFilters;
    }

    public void performPostMoveActions(Move move) {
        for (PostMoveAction action : mPostMoveActions)
            action.perform(move);
    }

    public void undoPostMoveActions(Move move) {
        for (PostMoveAction action : mPostMoveActions)
            action.undo(move);
    }

    public void checkEndCondition() {
        mEndCondition.checkEndCondition();
    }

    public void undoCheckEndCondition() {
        mEndCondition.undo();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Rules))
            return false;

        Rules otherRules = (Rules) other;

        return Objects.equal(mObjectivePieceType, otherRules.mObjectivePieceType)
                && Objects.equal(mDestinationBoardType, otherRules.mDestinationBoardType)
                && Objects.equal(mMoveFilters, otherRules.mMoveFilters)
                && Objects.equal(mPostMoveActions, otherRules.mPostMoveActions)
                && Objects.equal(mEndCondition, otherRules.mEndCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mObjectivePieceType, mDestinationBoardType,
                mMoveFilters, mPostMoveActions, mEndCondition);
    }

    private final PieceType mObjectivePieceType;
    private final int mDestinationBoardType;
    private final List<MoveFilter> mMoveFilters;
    private final List<PostMoveAction> mPostMoveActions;
    private final EndCondition mEndCondition;
}
