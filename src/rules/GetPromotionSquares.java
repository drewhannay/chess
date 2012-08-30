package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import logic.Game;
import logic.Piece;
import logic.Square;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * GetPromotionSquares.java
 * 
 * Class to hold methods with various implementations of returning a list of
 * squares upon which pieces can promote.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class GetPromotionSquares implements Serializable
{
	public GetPromotionSquares(String methodName)
	{
		m_doMethod = m_doMethods.get(methodName);
		m_methodName = methodName;
	}

	/**
	 * In classic chess, the pawns promote in the back row.
	 * 
	 * @param piece The piece to check.
	 * @return The list of squares it promotes on.
	 */
	public List<Square> classicPromoSquares(Piece piece)
	{
		// if (!(p.getName().equals("Pawn")))
		// return null; //TODO uncomment these lines, make a promotion method
		// that gets from the user the promotion squares of each type (looks up
		// in a map). Can eventually replace this method.
		List<Square> toReturn = Lists.newArrayList();
		for (int i = 1; i <= m_game.getBoards()[0].getMaxRow(); i++)
		{
			if (piece.isBlack())
				toReturn.add(m_game.getBoards()[0].getSquare(1, i));
			else
				toReturn.add(m_game.getBoards()[0].getSquare(8, i));
		}
		return toReturn;
	}

	/**
	 * Execute the right method.
	 * 
	 * @param piece The piece to check for the promotion squares of.
	 * @return The list of Squares representing where this piece promotes.
	 */
	@SuppressWarnings("unchecked")
	public List<Square> execute(Piece piece)
	{
		try
		{
			if (m_doMethod == null)
				m_doMethod = m_doMethods.get(m_methodName);
			return (List<Square>) m_doMethod.invoke(this, piece);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * For turning off promotion.
	 * 
	 * @param piece The piece to check
	 * @return null since no pieces may promote.
	 */
	public List<Square> noPromos(Piece piece)
	{
		return null;
	}

	public void setGame(Game game)
	{
		m_game = game;
	}

	private static final long serialVersionUID = -9005021406027456557L;

	private static Map<String, Method> m_doMethods = Maps.newHashMap();

	static
	{
		try
		{
			m_doMethods.put("classic", GetPromotionSquares.class.getMethod("classicPromoSquares", Piece.class));
			m_doMethods.put("noPromos", GetPromotionSquares.class.getMethod("noPromos", Piece.class));
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
