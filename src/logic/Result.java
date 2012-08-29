package logic;

import java.io.Serializable;

/**
 * Result.java
 * 
 * Class to model the end result of a chess game.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class Result implements Serializable
{
	public static final int UNDECIDED = 0;
	public static final int DRAW = 1;
	public static final int WHITE_WIN = 2;
	public static final int BLACK_WIN = 3;

	public Result(int choice)
	{
		m_choice = choice;
	}

	public boolean isBlackWin()
	{
		return m_choice == BLACK_WIN;
	}

	public boolean isDraw()
	{
		return m_choice == DRAW;
	}

	public boolean isUndecided()
	{
		return m_choice == UNDECIDED;
	}

	public boolean isWhiteWin()
	{
		return m_choice == WHITE_WIN;
	}

	/**
	 * @return The String text of what should be on the end game popup box.
	 */
	public String winText()
	{
		switch (m_choice)
		{
		case Result.DRAW:
			return "Draw!";
		case Result.WHITE_WIN:
			return "White won!";
		case Result.BLACK_WIN:
			return "Black won!";
		default:
			return "";
		}
	}

	public String text()
	{
		return m_text;
	}

	public void setText(String text)
	{
		m_text = text;
	}

	@Override
	public String toString()
	{
		@SuppressWarnings("unused")
		String s = "";

		switch (m_choice)
		{
		case UNDECIDED:
			s = "?";
			break;
		case WHITE_WIN:
			s = "1-0";
			break;
		case DRAW:
			s = "1/2-1/2";
			break;
		case BLACK_WIN:
			s = "0-1";
			break;
		default:
			s = "?";
		}
		return m_text;
	}

	private static final long serialVersionUID = -6844368741916902616L;

	protected int m_choice = UNDECIDED;
	protected String m_text = "";
}
