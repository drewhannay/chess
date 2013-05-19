package rules;

import java.util.List;

import logic.Game;
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

	public void setGame(Game game)
	{
		mGame = game;
	}

	private List<Square> classicPromoSquares(Piece piece)
	{
		// if (!(p.getName().equals("Pawn")))
		// return null; //TODO uncomment these lines, make a promotion method
		// that gets from the user the promotion squares of each type (looks up
		// in a map). Can eventually replace this method.
		List<Square> toReturn = Lists.newArrayList();
		for (int i = 1; i <= mGame.getBoards()[0].getMaxRow(); i++)
		{
			if (piece.isBlack())
				toReturn.add(mGame.getBoards()[0].getSquare(1, i));
			else
				toReturn.add(mGame.getBoards()[0].getSquare(8, i));
		}
		return toReturn;
	}

	private Game mGame;
}
