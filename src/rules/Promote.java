package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.swing.JOptionPane;

import logic.Game;
import logic.Piece;
import logic.PieceBuilder;

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

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -2346237261367453073L;

	/**
	 * The current Game object.
	 */
	private Game g;

	/**
	 * The name of the method to call.
	 */
	private String name;
	/**
	 * The method to call.
	 */
	private transient Method doMethod;
	/**
	 * The method to undo.
	 */
	private transient Method undoMethod;
	/**
	 * A hashmap for convenience.
	 */
	private static HashMap<String, Method> doMethods = new HashMap<String, Method>();
	/**
	 * A hashmap for convenience.
	 */
	private static HashMap<String, Method> undoMethods = new HashMap<String, Method>();

	/**
	 * What the piece was promoted from
	 */
	private static String lastPromoted;
	/**
	 * Used to allow promotion to work properly when loading a saved game.
	 */
	private String resetLastPromoted;
	/**
	 * What it was promoted to.
	 */
	private String klazz;

	static
	{
		try
		{
			doMethods.put("classic", Promote.class.getMethod(
					"classicPromotion", Piece.class, boolean.class,
					String.class));
			undoMethods.put("classic",
					Promote.class.getMethod("classicUndo", Piece.class));
			doMethods.put("noPromos", Promote.class.getMethod("noPromo",
					Piece.class, boolean.class, String.class));
			undoMethods.put("noPromos",
					Promote.class.getMethod("noPromoUndo", Piece.class));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param name The name of the method to use.
	 */
	public Promote(String name)
	{
		doMethod = doMethods.get(name);
		undoMethod = undoMethods.get(name);
		this.name = name;
	}

	/**
	 * In this case, only pawns can promote, allow the user to pick which class
	 * it promotes to.
	 * 
	 * @param p The piece to promote
	 * @param verified Whether it has been verified that this is ok
	 * @param promo What the piece was promoted to.
	 * @return The promoted Piece.
	 */
	public Piece classicPromotion(Piece p, boolean verified, String promo)
	{
		if (!verified
				&& (p.getPromotesTo() == null || p.getPromotesTo().size() == 0))
			return p;
		if (!verified)
		{
			lastPromoted = p.getName();
			klazz = p.getName();
		}
		if (verified && promo != null && !promo.equals(p.getName()))
		{
			try
			{

				Piece promoted = PieceBuilder.makePiece(promo, p.isBlack(),
						p.getSquare(), p.getBoard());
				if (promoted.isBlack())
				{
					g.getBlackTeam().set(g.getBlackTeam().indexOf(p), promoted);
				} else
				{
					g.getWhiteTeam().set(g.getWhiteTeam().indexOf(p), promoted);
				}
				promoted.getLegalDests().clear();
				promoted.setMoveCount(p.getMoveCount());
				return promoted;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (!verified
				&& (p.getPromotesTo() == null || p.getPromotesTo().size() == 0))
			return p;
		if (!verified && promo == null && g.isBlackMove() == p.isBlack())
		{
			klazz = "";
			if (p.getPromotesTo().size() == 1)
				klazz = p.getPromotesTo().get(0);
			while (klazz.equals(""))
			{
				String result = (String) JOptionPane.showInputDialog(null,
						"Select the Promotion type:", "Promo choice",
						JOptionPane.PLAIN_MESSAGE, null, p.getPromotesTo()
								.toArray(), null);

				if (result == null)
				{
					continue;
				}

				klazz = result;
				promo = result;
			}
		} else if (promo != null && p.getPromotesTo() != null
				&& p.getPromotesTo().contains(promo))
		{
			klazz = promo;
		}

		try
		{

			Piece promoted = PieceBuilder.makePiece(klazz, p.isBlack(),
					p.getSquare(), p.getBoard());
			if (promoted.isBlack())
			{
				g.getBlackTeam().set(g.getBlackTeam().indexOf(p), promoted);
			} else
			{
				g.getWhiteTeam().set(g.getWhiteTeam().indexOf(p), promoted);
			}
			promoted.getLegalDests().clear();
			promoted.setMoveCount(p.getMoveCount());
			return promoted;
		} catch (Exception e)
		{
			return p;
		}
	}

	/**
	 * Revert the piece back to what it was.
	 * 
	 * @param p The piece to unpromote
	 * @return The unpromoted piece.
	 */
	public Piece classicUndo(Piece p)
	{
		try
		{
			Piece promoted = classicPromotion(p, true, lastPromoted);
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
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param p The piece to promote
	 * @param verified Whether the piece can be promoted
	 * @param promo What to promote from.
	 * @return The promoted Piece.
	 */
	public Piece execute(Piece p, boolean verified, String promo)
	{
		if (lastPromoted == null)
			lastPromoted = resetLastPromoted;

		try
		{
			if (doMethod == null)
			{
				doMethod = doMethods.get(name);
			}
			Piece toReturn = (Piece) doMethod.invoke(this, p, verified, promo);
			resetLastPromoted = lastPromoted;
			return toReturn;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Don't allow promotion.
	 * 
	 * @param p The piece to "promote"
	 * @param b Unused
	 * @param c Unused
	 * @return the original piece.
	 */
	public Piece noPromo(Piece p, boolean b, String c)
	{
		return p;
	}

	/**
	 * Return the original piece
	 * 
	 * @param p The piece to "unpromote"
	 * @return The original piece.
	 */
	public Piece noPromoUndo(Piece p)
	{
		return p;
	}

	/**
	 * @param g Setter for g.
	 */
	public void setGame(Game g)
	{
		this.g = g;
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
			if (undoMethod == null)
			{
				undoMethod = undoMethods.get(name);
			}
			Piece toReturn = (Piece) undoMethod.invoke(this, p);
			// resetLastPromoted = lastPromoted;
			return toReturn;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
