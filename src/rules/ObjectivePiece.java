package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import logic.Game;
import logic.Piece;

import com.google.common.collect.Maps;

/**
 * ObjectivePiece.java
 * 
 * Class to hold methods returning the objective of the game, if there is one.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class ObjectivePiece implements Serializable
{
	public ObjectivePiece(String methodName, String objectivePieceName)
	{
		m_doMethod = m_doMethods.get(methodName);
		m_methodName = methodName;
		m_objectivePieceName = objectivePieceName;
	}

	/**
	 * In classic, the king is the objective piece.
	 * 
	 * @param isBlack Whether the piece is black
	 * @return The objective piece on the same team.
	 */
	public Piece classicObjectivePiece(boolean isBlack)
	{
		try
		{
			if (isBlack)
			{
				for (Piece p : m_game.getBlackTeam())
				{
					if (p.getName().equals("King"))
						return p;
				}
			}
			for (Piece p : m_game.getWhiteTeam())
			{
				if (p.getName().equals("King"))
					return p;
			}
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * In this case, return the custom objective piece.
	 * 
	 * @param isBlack whether the piece is black
	 * @return The corresponding objective piece.
	 */
	public Piece customObjectivePiece(boolean isBlack)
	{
		try
		{
			if (isBlack)
			{
				for (Piece p : m_game.getBlackTeam())
				{
					if (p.getName().equals(m_objectivePieceName))
						return p;
				}
			}
			for (Piece p : m_game.getWhiteTeam())
			{
				if (p.getName().equals(m_objectivePieceName))
					return p;
			}
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Perform the right method.
	 * 
	 * @param isBlack Is this piece black?
	 * @return The objective piece.
	 */
	public Piece execute(boolean isBlack)
	{
		try
		{
			if (m_doMethod == null)
				m_doMethod = m_doMethods.get(m_methodName);
			return (Piece) m_doMethod.invoke(this, isBlack);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return null in the case that there is no objective.
	 * 
	 * @param isBlack Whether this piece is black.
	 * @return null in this case.
	 */
	public Piece noObjectivePiece(boolean isBlack)
	{
		return null;
	}

	public void setGame(Game game)
	{
		m_game = game;
	}

	public String getObjectivePieceName()
	{
		return m_objectivePieceName;
	}

	private static final long serialVersionUID = 6945649584920313635L;

	private static Map<String, Method> m_doMethods = Maps.newHashMap();

	static
	{
		try
		{
			m_doMethods.put("classic", ObjectivePiece.class.getMethod("classicObjectivePiece", boolean.class));
			m_doMethods.put("no objective", ObjectivePiece.class.getMethod("noObjectivePiece", boolean.class));
			m_doMethods.put("custom objective", ObjectivePiece.class.getMethod("customObjectivePiece", boolean.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private transient Method m_doMethod;

	private Game m_game;
	private String m_methodName;
	private String m_objectivePieceName;
}
