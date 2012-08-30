package rules;

import gui.PlayGame;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * NextTurn.java
 * 
 * Class to hold methods controlling the turn flow of the game
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class NextTurn implements Serializable
{
	/**
	 * @param methodName The name of the method
	 * @param whiteMoves The number of white moves.
	 * @param blackMoves The number of black moves
	 * @param increment The increment.
	 */
	public NextTurn(String methodName, int whiteMoves, int blackMoves, int increment)
	{
		m_methodName = methodName;
		m_doMethod = m_doMethods.get(methodName);
		m_undoMethod = m_undoMethods.get(methodName);
		m_numberOfWhiteMovesBeforeTurnChange = whiteMoves;
		m_numberOfBlackMovesBeforeTurnChange = blackMoves;
		m_turnIncrement = increment;
		m_currentNumberOfMovesMade = 0;
		m_isBlackMove = false;
	}

	/**
	 * In classic, each player gets 1 move.
	 * 
	 * @return Whose turn it is.
	 */
	public boolean classicNextTurn()
	{

		m_isBlackMove = !m_isBlackMove;
		PlayGame.turn(m_isBlackMove);

		return m_isBlackMove;
	}

	/**
	 * Classic is undone by changing the turn back.
	 * 
	 * @return Whose turn it is.
	 */
	public boolean classicUndo()
	{
		m_isBlackMove = !m_isBlackMove;
		PlayGame.turn(m_isBlackMove);

		return m_isBlackMove;
	}

	/**
	 * Black and white have different numbers of turns, but that number does not
	 * increase.
	 * 
	 * @return Whose turn it is.
	 */
	public boolean differentNumTurns()
	{
		if (++m_currentNumberOfMovesMade >= (m_isBlackMove ? m_numberOfBlackMovesBeforeTurnChange : m_numberOfWhiteMovesBeforeTurnChange))
		{
			m_isBlackMove = !m_isBlackMove;
			PlayGame.turn(m_isBlackMove);

			m_currentNumberOfMovesMade = 0;
		}
		return m_isBlackMove;
	}

	/**
	 * @return Whose turn it is.
	 */
	public boolean execute()
	{
		try
		{
			if (m_doMethod == null)
				m_doMethod = m_doMethods.get(m_methodName);
			return (Boolean) m_doMethod.invoke(this, (Object[]) null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * White and black have different numbers of turns, and this number
	 * increases.
	 * 
	 * @return Whose turn it is.
	 */
	public boolean increasingTurnsSeparately()
	{
		if (++m_currentNumberOfMovesMade >= (m_isBlackMove ? m_numberOfBlackMovesBeforeTurnChange : m_numberOfWhiteMovesBeforeTurnChange))
		{
			m_isBlackMove = !m_isBlackMove;
			PlayGame.turn(m_isBlackMove);
			m_numberOfBlackMovesBeforeTurnChange += m_turnIncrement;
			m_numberOfWhiteMovesBeforeTurnChange += m_turnIncrement;
			m_currentNumberOfMovesMade = 0;
		}
		return m_isBlackMove;
	}

	/**
	 * In this variant, the number of turns increases each round by the
	 * increment, but both players have the same number of turns.
	 * 
	 * @return Whose turn it is.
	 */
	public boolean increasingTurnsTogether()
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

	/**
	 * This is undone by decrementing the number of moves made, and if
	 * necessary, decrementing the amount of moves possible each round. Then the
	 * turn is changed, if appropriate.
	 * 
	 * @return Whose turn it is.
	 */
	public boolean undoIncreasingTurnsTogether()
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

	/**
	 * @return Whose turn it is.
	 */
	public boolean undo()
	{
		try
		{
			if (m_undoMethod == null)
				m_undoMethod = m_undoMethods.get(m_methodName);
			return (Boolean) m_undoMethod.invoke(this, (Object[]) null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Undo this by changing the turn if appropriate.
	 * 
	 * @return Whose turn it is.
	 */
	public boolean undoDifferentNumTurns()
	{
		if (--m_currentNumberOfMovesMade < 0)
		{
			m_isBlackMove = !m_isBlackMove;
			PlayGame.turn(m_isBlackMove);

			m_currentNumberOfMovesMade = m_isBlackMove ? m_numberOfBlackMovesBeforeTurnChange : m_numberOfWhiteMovesBeforeTurnChange;
		}
		return m_isBlackMove;
	}

	/**
	 * Undo the effects of increasingTurnsSeparately.
	 * 
	 * @return Whose turn it is.
	 */
	public boolean undoIncreasingTurnsSeparately()
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

	/**
	 * Getter method for White's moves
	 */
	public int getWhiteMoves()
	{
		return m_numberOfWhiteMovesBeforeTurnChange;
	}

	/**
	 * Getter method for Black's moves
	 */
	public int getBlackMoves()
	{
		return m_numberOfBlackMovesBeforeTurnChange;
	}

	/**
	 * Getter method for the increment
	 */
	public int getIncrement()
	{
		return m_turnIncrement;
	}

	private static final long serialVersionUID = 9127502749298644757L;

	private static Map<String, Method> m_doMethods = Maps.newHashMap();
	private static Map<String, Method> m_undoMethods = Maps.newHashMap();

	static
	{
		try
		{
			m_doMethods.put("classic", NextTurn.class.getMethod("classicNextTurn"));
			m_undoMethods.put("classic", NextTurn.class.getMethod("classicUndo"));
			m_doMethods.put("increasing together", NextTurn.class.getMethod("increasingTurnsTogether"));
			m_undoMethods.put("increasing together", NextTurn.class.getMethod("undoIncreasingTurnsTogether"));
			m_doMethods.put("different turns", NextTurn.class.getMethod("differentNumTurns"));
			m_undoMethods.put("different turns", NextTurn.class.getMethod("undoDifferentNumTurns"));
			m_doMethods.put("increasing separately", NextTurn.class.getMethod("increasingTurnsSeparately"));
			m_undoMethods.put("increasing separately", NextTurn.class.getMethod("undoIncreasingTurnsSeparately"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private transient Method m_doMethod;
	private transient Method m_undoMethod;

	private String m_methodName;
	private int m_numberOfWhiteMovesBeforeTurnChange;
	private int m_numberOfBlackMovesBeforeTurnChange;
	private int m_turnIncrement;
	private int m_currentNumberOfMovesMade;
	private boolean m_isBlackMove;
}
