
package rules;

import java.util.List;
import java.util.Map;
import java.util.Set;
import models.ChessCoordinates;
import models.Move;
import models.Piece;
import models.PieceType;
import rules.endconditions.CaptureObjectiveEndCondition;
import rules.endconditions.EndCondition;
import rules.legaldestinationcropper.LegalDestinationCropper;
import rules.postmoveaction.PostMoveAction;
import rules.promotionmethods.ClassicPromotionMethod;
import rules.promotionmethods.PromotionMethod;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class Rules
{
	public static final int DESTINATION_SAME_BOARD = 0;
	public static final int DESTINATION_OPPOSITE_BOARD = 1;

	public Rules()
	{
		// TODO: assign all this stuff properly
		mObjectivePieceType = null;
		mPromotionCoordinateList = Lists.newArrayList();
		mDestinationBoardType = DESTINATION_SAME_BOARD;
		mLegalDestinationCroppers = Lists.newArrayList();
		mPromotionMap = Maps.newHashMap();
		mPromotionMethod = new ClassicPromotionMethod();
		mPostMoveActions = Lists.newArrayList();
		mEndCondition = new CaptureObjectiveEndCondition();
	}

	/**
	 * public void performAfterMoveAction(MoveController move)
	 * public void checkEndOfGame(PieceController objectivePiece)
	 * public Piece promotePiece(Piece pieceToPromote, boolean
	 * pieceCanBePromoted, String pieceTypeToPromoteFrom)
	 */

	public PieceType getObjectivePieceType()
	{
		return mObjectivePieceType;
	}

	public List<ChessCoordinates> getPromotionCoordinateList()
	{
		return mPromotionCoordinateList;
	}

	public Piece promotePiece(Piece pieceToPromote)
	{
		return mPromotionMethod.promotePiece(pieceToPromote, mPromotionMap);
	}

	public Piece undoPromotion(Piece pieceToDemote)
	{
		return mPromotionMethod.undoPromotion(pieceToDemote);
	}

	public int getDestinationBoardIndex(int startIndex)
	{
		switch (mDestinationBoardType)
		{
		case DESTINATION_OPPOSITE_BOARD:
			return (startIndex + 1) % 2;
		case DESTINATION_SAME_BOARD:
		default:
			return startIndex;
		}
	}

	public void cropLegalDestinations()
	{
		for (LegalDestinationCropper cropper : mLegalDestinationCroppers)
			cropper.cropLegalDestinations();
	}

	public void performPostMoveActions(Move move)
	{
		for (PostMoveAction action : mPostMoveActions)
			action.perform(move);
	}

	public void undoPostMoveActions(Move move)
	{
		for (PostMoveAction action : mPostMoveActions)
			action.undo(move);
	}

	public void checkEndCondition()
	{
		mEndCondition.checkEndCondition();
	}

	public void undoCheckEndCondition()
	{
		mEndCondition.undo();
	}

	@Override
	public boolean equals(Object other)
	{
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
	public int hashCode()
	{
		return Objects.hashCode(mObjectivePieceType, mPromotionCoordinateList, mDestinationBoardType,
				mLegalDestinationCroppers, mPostMoveActions, mPromotionMap, mPromotionMethod, mEndCondition);
	}

	private final PieceType mObjectivePieceType;
	private final List<ChessCoordinates> mPromotionCoordinateList;
	private final int mDestinationBoardType;
	private final List<LegalDestinationCropper> mLegalDestinationCroppers;
	private final List<PostMoveAction> mPostMoveActions;
	private final Map<PieceType, Set<PieceType>> mPromotionMap;
	private final PromotionMethod mPromotionMethod;
	private final EndCondition mEndCondition;
}
