package rules;

import java.util.List;

import logic.Piece;
import logic.Square;

import com.google.common.collect.Lists;

public enum GetPromotionSquares
{
	CLASSIC,
	NO_PROMOTIONS;

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
		// if (!(p.getName().equals("Pawn")))
		// return null; //TODO uncomment these lines, make a promotion method
		// that gets from the user the promotion squares of each type (looks up
		// in a map). Can eventually replace this method.
		List<Square> toReturn = Lists.newArrayList();
		int maxRow = piece.getBoard().getMaxRow();
		for (int i = 1; i <= maxRow; i++)
		{
			if (piece.isBlack())
				toReturn.add(piece.getBoard().getSquare(1, i));
			else
				toReturn.add(piece.getBoard().getSquare(8, i));
		}
		return toReturn;
	}
}
