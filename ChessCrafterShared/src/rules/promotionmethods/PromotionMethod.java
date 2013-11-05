
package rules.promotionmethods;

import java.util.Map;
import java.util.Set;
import models.Piece;
import models.PieceType;

public class PromotionMethod
{
	// no promotion
	public Piece promotePiece(Piece pieceToPromote, Map<PieceType, Set<PieceType>> promotionMap)
	{
		return pieceToPromote;
	}

	public Piece undoPromotion(Piece pieceToDemote)
	{
		return pieceToDemote;
	}
}
