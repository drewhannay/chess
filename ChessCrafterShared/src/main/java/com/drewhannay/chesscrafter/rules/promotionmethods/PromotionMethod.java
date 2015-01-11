
package rules.promotionmethods;

import java.util.Map;
import java.util.Set;
import models.Piece;
import models.PieceType;

public abstract class PromotionMethod
{
	public abstract Piece promotePiece(Piece pieceToPromote, Map<PieceType, Set<PieceType>> promotionMap);

	public abstract Piece undoPromotion(Piece pieceToDemote);
}
