package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import logic.Board;
import logic.Game;

import com.google.common.collect.Maps;

/**
 * GetBoard.java
 * 
 * Class to hold methods with various ways of determining which Board a piece
 * should move to.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class GetBoard implements Serializable
{
	/**
	 * @param name The name of the method.
	 */
	public GetBoard(String name)
	{
		m_doMethod = m_doMethods.get(name);
		m_methodName = name;
	}

	/**
	 * @param startBoard The original board
	 * @return The same board.
	 */
	public Board classicGetBoard(Board startBoard)
	{
		return startBoard;
	}

	/**
	 * Execute the appropriate method.
	 * 
	 * @param startBoard The original board
	 * @return The board requested.
	 */
	public Board execute(Board startBoard)
	{
		try
		{
			if (m_doMethod == null)
				m_doMethod = m_doMethods.get(m_methodName);

			return (Board) m_doMethod.invoke(this, startBoard);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param startBoard The original board
	 * @return The other board, if any.
	 */
	public Board oppositeBoard(Board startBoard)
	{
		if (startBoard.equals(m_game.getBoards()[0]))
			return m_game.getBoards()[1];
		return m_game.getBoards()[0];
	}

	public void setGame(Game game)
	{
		m_game = game;
	}

	private static final long serialVersionUID = -2155872379580860065L;

	private static Map<String, Method> m_doMethods = Maps.newHashMap();

	static
	{
		try
		{
			m_doMethods.put("classic", GetBoard.class.getMethod("classicGetBoard", Board.class));
			m_doMethods.put("oppositeBoard", GetBoard.class.getMethod("oppositeBoard", Board.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private transient Method m_doMethod;

	private Game m_game;
	private String m_methodName;
}
