package com.drewhannay.chesscrafter.rules;

import com.drewhannay.chesscrafter.models.ChessCoordinate;
import com.drewhannay.chesscrafter.models.Move;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.rules.endconditions.EndCondition;
import com.drewhannay.chesscrafter.rules.legaldestinationcropper.LegalDestinationCropper;
import com.drewhannay.chesscrafter.rules.postmoveaction.PostMoveAction;
import com.drewhannay.chesscrafter.rules.promotionmethods.PromotionMethod;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Rules {
    public static final int DESTINATION_SAME_BOARD = 0;
    public static final int DESTINATION_OPPOSITE_BOARD = 1;

    public Rules(PieceType objectivePieceType, List<ChessCoordinate> promotionCoordinateList, int destinationBoardType,
                 List<LegalDestinationCropper> legalDestinationCroppers, Map<PieceType, Set<PieceType>> promotionMap,
                 PromotionMethod promotionMethod, List<PostMoveAction> postMoveActions, EndCondition endCondition) {
        mObjectivePieceType = objectivePieceType;
        mPromotionCoordinateList = Lists.newArrayList(promotionCoordinateList);
        mDestinationBoardType = destinationBoardType;
        mLegalDestinationCroppers = Lists.newArrayList(legalDestinationCroppers);
        mPromotionMap = Maps.newHashMap(promotionMap);
        mPromotionMethod = promotionMethod;
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

    public List<ChessCoordinate> getPromotionCoordinateList() {
        return mPromotionCoordinateList;
    }

    public Piece promotePiece(Piece pieceToPromote) {
        return mPromotionMethod.promotePiece(pieceToPromote, mPromotionMap);
    }

    public Piece undoPromotion(Piece pieceToDemote) {
        return mPromotionMethod.undoPromotion(pieceToDemote);
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

    public void cropLegalDestinations() {
        for (LegalDestinationCropper cropper : mLegalDestinationCroppers)
            cropper.cropLegalDestinations();
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
                && Objects.equal(mPromotionCoordinateList, otherRules.mPromotionCoordinateList)
                && Objects.equal(mDestinationBoardType, otherRules.mDestinationBoardType)
                && Objects.equal(mLegalDestinationCroppers, otherRules.mLegalDestinationCroppers)
                && Objects.equal(mPostMoveActions, otherRules.mPostMoveActions)
                && Objects.equal(mPromotionMethod, otherRules.mPromotionMethod)
                && Objects.equal(mEndCondition, otherRules.mEndCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mObjectivePieceType, mPromotionCoordinateList, mDestinationBoardType,
                mLegalDestinationCroppers, mPostMoveActions, mPromotionMap, mPromotionMethod, mEndCondition);
    }

    private final PieceType mObjectivePieceType;
    private final List<ChessCoordinate> mPromotionCoordinateList;
    private final int mDestinationBoardType;
    private final List<LegalDestinationCropper> mLegalDestinationCroppers;
    private final List<PostMoveAction> mPostMoveActions;
    private final Map<PieceType, Set<PieceType>> mPromotionMap;
    private final PromotionMethod mPromotionMethod;
    private final EndCondition mEndCondition;
}
