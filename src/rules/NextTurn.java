package rules;

import gui.PlayGame;

public enum NextTurn
{
	CLASSIC,
	INCREASING_TOGETHER,
	INCREASING_SEPARATELY,
	DIFFERENT_NUMBER_OF_TURNS;

	public NextTurn init(int whiteMoves, int blackMoves, int increment)
	{
		m_numberOfWhiteMovesBeforeTurnChange = whiteMoves;
		m_numberOfBlackMovesBeforeTurnChange = blackMoves;
		m_turnIncrement = increment;
		m_currentNumberOfMovesMade = 0;
		m_isBlackMove = false;

		return this;
	}

	public boolean getNextTurn()
	{
		switch (this)
		{
		case CLASSIC:
			return classicNextTurn();
		case INCREASING_TOGETHER:
			return increasingTurnsTogether();
		case INCREASING_SEPARATELY:
			return increasingTurnsSeparately();
		case DIFFERENT_NUMBER_OF_TURNS:
			return differentNumberOfTurns();
		default:
			return false;
		}
	}

	public boolean undo()
	{
		switch (this)
		{
		case CLASSIC:
			return undoClassic();
		case INCREASING_TOGETHER:
			return undoIncreasingTurnsTogether();
		case INCREASING_SEPARATELY:
			return undoIncreasingTurnsSeparately();
		case DIFFERENT_NUMBER_OF_TURNS:
			return undoDifferentNumberOfTurns();
		default:
			return false;
		}
	}

	public int getWhiteMoves()
	{
		return m_numberOfWhiteMovesBeforeTurnChange;
	}

	public int getBlackMoves()
	{
		return m_numberOfBlackMovesBeforeTurnChange;
	}

	public int getIncrement()
	{
		return m_turnIncrement;
	}

	private boolean classicNextTurn()
	{
		m_isBlackMove = !m_isBlackMove;
		PlayGame.turn(m_isBlackMove);

		return m_isBlackMove;
	}

	private boolean undoClassic()
	{
		m_isBlackMove = !m_isBlackMove;
		PlayGame.turn(m_isBlackMove);

		return m_isBlackMove;
	}

	private boolean increasingTurnsTogether()
	{
		if (++m_currentNumberOfMovesMade >= m_numberOfWhiteMovesBeforeTurnChange)
		{
			m_isBlackMove = !m_isBlackMove;
			PlayGame.turn(m_isBlackMove);
			m_numberOfWhiteMovesBeforeTurnChange += m_turnIncrement;
			m_currentNumberOfMovesMade = 0;
		}
		return m_isBlackMove;
	}

	private boolean undoIncreasingTurnsTogether()
	{
		if (--m_currentNumberOfMovesMade < 0)
		{
			m_isBlackMove = !m_isBlackMove;
			PlayGame.turn(m_isBlackMove);
			m_numberOfWhiteMovesBeforeTurnChange -= m_turnIncrement;
			m_currentNumberOfMovesMade = m_numberOfWhiteMovesBeforeTurnChange - 1;
		}
		return m_isBlackMove;
	}

	private boolean increasingTurnsSeparately()
	{
		if (++m_currentNumberOfMovesMade >= (m_isBlackMove ? m_numberOfBlackMovesBeforeTurnChange
				: m_numberOfWhiteMovesBeforeTurnChange))
		{
			m_isBlackMove = !m_isBlackMove;
			PlayGame.turn(m_isBlackMove);
			m_numberOfBlackMovesBeforeTurnChange += m_turnIncrement;
			m_numberOfWhiteMovesBeforeTurnChange += m_turnIncrement;
			m_currentNumberOfMovesMade = 0;
		}
		return m_isBlackMove;
	}

	private boolean undoIncreasingTurnsSeparately()
	{
		if (--m_currentNumberOfMovesMade < 0)
		{
			m_isBlackMove = !m_isBlackMove;
			m_numberOfBlackMovesBeforeTurnChange -= m_turnIncrement;
			m_numberOfWhiteMovesBeforeTurnChange -= m_turnIncrement;
			PlayGame.turn(m_isBlackMove);

			m_currentNumberOfMovesMade = m_isBlackMove ? m_numberOfBlackMovesBeforeTurnChange : m_numberOfWhiteMovesBeforeTurnChange;
		}
		return m_isBlackMove;
	}

	private boolean differentNumberOfTurns()
	{
		if (++m_currentNumberOfMovesMade >= (m_isBlackMove ? m_numberOfBlackMovesBeforeTurnChange
				: m_numberOfWhiteMovesBeforeTurnChange))
		{
			m_isBlackMove = !m_isBlackMove;
			PlayGame.turn(m_isBlackMove);

			m_currentNumberOfMovesMade = 0;
		}
		return m_isBlackMove;
	}

	private boolean undoDifferentNumberOfTurns()
	{
		if (--m_currentNumberOfMovesMade < 0)
		{
			m_isBlackMove = !m_isBlackMove;
			PlayGame.turn(m_isBlackMove);

			m_currentNumberOfMovesMade = m_isBlackMove ? m_numberOfBlackMovesBeforeTurnChange : m_numberOfWhiteMovesBeforeTurnChange;
		}
		return m_isBlackMove;
	}

	private int m_numberOfWhiteMovesBeforeTurnChange;
	private int m_numberOfBlackMovesBeforeTurnChange;
	private int m_currentNumberOfMovesMade;
	private int m_turnIncrement;
	private boolean m_isBlackMove;
}
