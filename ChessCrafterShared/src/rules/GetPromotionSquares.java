package rules;

import java.util.List;

import models.Piece;
import models.Square;

import com.google.common.collect.Lists;

public enum GetPromotionSquares
{
	CLASSIC, NO_PROMOTIONS;

	public List<Square> getPromotionSquares(Piece piece)
	{
		switch (this)
		{
		case CLASSIC:
			return classicPromoSquares(piece);
		case NO_PROMOTIONS:
		default:
			return null;
		}
	}

	private List<Square> classicPromoSquares(Piece piece)
	{
		List<Square> toReturn = Lists.newArrayList();
		int maxRow = piece.getBoard().getMaxRow();
		int maxCol = piece.getBoard().getMaxCol();
		for (int i = 1; i <= maxCol; i++)
		{
			if (piece.isBlack())
				toReturn.add(piece.getBoard().getSquare(1, i));
			else
				toReturn.add(piece.getBoard().getSquare(maxRow, i));
		}
		return toReturn;
	}
}
