package rules;

import models.Board;
import models.Game;

public enum GetBoard
{
	CLASSIC, OPPOSITE_BOARD;

	public Board getBoard(Board startBoard)
	{
		switch (this)
		{
		case CLASSIC:
			return classicGetBoard(startBoard);
		case OPPOSITE_BOARD:
			return getOppositeBoard(startBoard);
		default:
			return null;
		}
	}

	public void setGame(Game game)
	{
		mGame = game;
	}

	private Board classicGetBoard(Board startBoard)
	{
		return startBoard;
	}

	private Board getOppositeBoard(Board startBoard)
	{
		if (startBoard.equals(mGame.getBoards()[0]))
			return mGame.getBoards()[1];
		return mGame.getBoards()[0];
	}

	private Game mGame;
}
