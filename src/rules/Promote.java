package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import javax.swing.JOptionPane;

import logic.Game;
import logic.Piece;
import logic.PieceBuilder;

import com.google.common.collect.Maps;

/**
 * Promote.java
 * 
 * Class to hold methods controlling promotion of different Piece types
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class Promote implements Serializable
{
	public Promote(String methodName)
	{
		m_doMethod = m_doMethods.get(methodName);
		m_undoMethod = m_undoMethods.get(methodName);
		m_methodName = methodName;
	}

	/**
	 * In this case, only pawns can promote, allow the user to pick which class
	 * it promotes to.
	 * 
	 * @param piece The piece to promote
	 * @param verified Whether it has been verified that this is ok
	 * @param promo What the piece was promoted to.
	 * @return The promoted Piece.
	 */
	public Piece classicPromotion(Piece piece, boolean verified, String promo)
	{
		if (!verified && (piece.getPromotesTo() == null || piece.getPromotesTo().size() == 0))
			return piece;
		if (!verified)
		{
			m_lastPromotedFromPieceName = piece.getName();
			m_promotedToClass = piece.getName();
		}
		if (verified && promo != null && !promo.equals(piece.getName()))
		{
			try
			{

				Piece promoted = PieceBuilder.makePiece(promo, piece.isBlack(), piece.getSquare(), piece.getBoard());
				if (promoted.isBlack())
					m_game.getBlackTeam().set(m_game.getBlackTeam().indexOf(piece), promoted);
				else
					m_game.getWhiteTeam().set(m_game.getWhiteTeam().indexOf(piece), promoted);
				promoted.getLegalDests().clear();
				promoted.setMoveCount(piece.getMoveCount());
				return promoted;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (!verified && (piece.getPromotesTo() == null || piece.getPromotesTo().size() == 0))
			return piece;
		if (!verified && promo == null && m_game.isBlackMove() == piece.isBlack())
		{
			m_promotedToClass = "";
			if (piece.getPromotesTo().size() == 1)
				m_promotedToClass = piece.getPromotesTo().get(0);
			while (m_promotedToClass.equals(""))
			{
				String result = (String) JOptionPane.showInputDialog(null, "Select the Promotion type:", "Promo choice",
						JOptionPane.PLAIN_MESSAGE, null, piece.getPromotesTo().toArray(), null);

				if (result == null)
					continue;

				m_promotedToClass = result;
				promo = result;
			}
		}
		else if (promo != null && piece.getPromotesTo() != null && piece.getPromotesTo().contains(promo))
		{
			m_promotedToClass = promo;
		}

		try
		{

			Piece promoted = PieceBuilder.makePiece(m_promotedToClass, piece.isBlack(), piece.getSquare(), piece.getBoard());
			if (promoted.isBlack())
				m_game.getBlackTeam().set(m_game.getBlackTeam().indexOf(piece), promoted);
			else
				m_game.getWhiteTeam().set(m_game.getWhiteTeam().indexOf(piece), promoted);
			promoted.getLegalDests().clear();
			promoted.setMoveCount(piece.getMoveCount());
			return promoted;
		}
		catch (Exception e)
		{
			return piece;
		}
	}

	/**
	 * Revert the piece back to what it was.
	 * 
	 * @param piece The piece to unpromote
	 * @return The unpromoted piece.
	 */
	public Piece classicUndo(Piece piece)
	{
		try
		{
			Piece promoted = classicPromotion(piece, true, m_lastPromotedFromPieceName);
			// Piece promoted = PieceBuilder.makePiece(lastPromoted,p.isBlack(),
			// p.getSquare(), p.getBoard());
			// if (promoted.isBlack()) {
			// g.getBlackTeam().set(g.getBlackTeam().indexOf(p), promoted);
			// } else {
			// g.getWhiteTeam().set(g.getWhiteTeam().indexOf(p), promoted);
			// }
			// promoted.getLegalDests().clear();
			// promoted.setMoveCount(p.getMoveCount());
			return promoted;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param piece The piece to promote
	 * @param verified Whether the piece can be promoted
	 * @param promo What to promote from.
	 * @return The promoted Piece.
	 */
	public Piece execute(Piece piece, boolean verified, String promo)
	{
		if (m_lastPromotedFromPieceName == null)
			m_lastPromotedFromPieceName = m_resetLastPromoted;

		try
		{
			if (m_doMethod == null)
				m_doMethod = m_doMethods.get(m_methodName);
			Piece toReturn = (Piece) m_doMethod.invoke(this, piece, verified, promo);
			m_resetLastPromoted = m_lastPromotedFromPieceName;
			return toReturn;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Don't allow promotion.
	 * 
	 * @param piece The piece to "promote"
	 * @param verified Unused
	 * @param promo Unused
	 * @return the original piece.
	 */
	public Piece noPromo(Piece piece, boolean verified, String promo)
	{
		return piece;
	}

	/**
	 * Return the original piece
	 * 
	 * @param piece The piece to "unpromote"
	 * @return The original piece.
	 */
	public Piece noPromoUndo(Piece piece)
	{
		return piece;
	}

	public void setGame(Game game)
	{
		m_game = game;
	}

	/**
	 * @param p The piece to unpromote
	 * @return The unpromoted piece.
	 */
	public Piece undo(Piece p)
	{
		try
		{
			// if(lastPromoted==null)
			// lastPromoted = resetLastPromoted;
			if (m_undoMethod == null)
			{
				m_undoMethod = m_undoMethods.get(m_methodName);
			}
			Piece toReturn = (Piece) m_undoMethod.invoke(this, p);
			// resetLastPromoted = lastPromoted;
			return toReturn;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static final long serialVersionUID = -2346237261367453073L;

	private static Map<String, Method> m_doMethods = Maps.newHashMap();
	private static Map<String, Method> m_undoMethods = Maps.newHashMap();
	private static String m_lastPromotedFromPieceName;

	static
	{
		try
		{
			m_doMethods.put("classic", Promote.class.getMethod("classicPromotion", Piece.class, boolean.class, String.class));
			m_undoMethods.put("classic", Promote.class.getMethod("classicUndo", Piece.class));
			m_doMethods.put("noPromos", Promote.class.getMethod("noPromo", Piece.class, boolean.class, String.class));
			m_undoMethods.put("noPromos", Promote.class.getMethod("noPromoUndo", Piece.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private transient Method m_doMethod;
	private transient Method m_undoMethod;

	private Game m_game;
	private String m_methodName;
	private String m_resetLastPromoted;
	private String m_promotedToClass;
}
