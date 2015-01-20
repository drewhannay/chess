package com.drewhannay.chesscrafter.rules;

import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.rules.endconditions.EndCondition;
import com.drewhannay.chesscrafter.rules.movefilter.MoveFilter;
import com.drewhannay.chesscrafter.rules.postmoveaction.PostMoveAction;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;

public final class Rules {
    public Rules(List<MoveFilter> moveFilters, List<PostMoveAction> postMoveActions, EndCondition endCondition) {
        mMoveFilters = Lists.newArrayList(moveFilters);
        mPostMoveActions = Lists.newArrayList(postMoveActions);
        mEndCondition = endCondition;
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

    public EndCondition getEndCondition() {
        return mEndCondition;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Rules))
            return false;

        Rules otherRules = (Rules) other;

        return Objects.equal(mMoveFilters, otherRules.mMoveFilters)
                && Objects.equal(mPostMoveActions, otherRules.mPostMoveActions)
                && Objects.equal(mEndCondition, otherRules.mEndCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mMoveFilters, mPostMoveActions, mEndCondition);
    }

    private final List<MoveFilter> mMoveFilters;
    private final List<PostMoveAction> mPostMoveActions;
    private final EndCondition mEndCondition;
}
