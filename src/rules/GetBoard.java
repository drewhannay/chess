package rules;

import logic.Board;
import logic.Game;

public enum GetBoard
{
	CLASSIC,
	OPPOSITE_BOARD;

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
		m_game = game;
	}

	private Board classicGetBoard(Board startBoard)
	{
		return startBoard;
	}

	private Board getOppositeBoard(Board startBoard)
	{
		if (startBoard.equals(m_game.getBoards()[0]))
			return m_game.getBoards()[1];
		return m_game.getBoards()[0];
	}

	private Game m_game;
}
