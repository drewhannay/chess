
package rules.promotionmethods;

import java.util.Map;
import java.util.Set;
import models.Piece;
import models.PieceType;

public final class NoPromotionMethod extends PromotionMethod
{
	@Override
	public Piece promotePiece(Piece pieceToPromote, Map<PieceType, Set<PieceType>> promotionMap)
	{
		return pieceToPromote;
	}

	@Override
	public Piece undoPromotion(Piece pieceToDemote)
	{
		return pieceToDemote;
	}

	@Override
	public boolean equals(Object other)
	{
		return other instanceof NoPromotionMethod;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
}
