package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import logic.Game;
import logic.Piece;
import logic.Square;

/**
 * GetPromotionSquares.java
 * 
 * Class to hold methods with various implementations of 
 * returning a list of squares upon which pieces can promote.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class GetPromotionSquares implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -9005021406027456557L;

	/**
	 * The current Game object.
	 */
	private Game g;

	/**
	 * The name of the method to perform.
	 */
	private String name;
	/**
	 * The Method to perform.
	 */
	private transient Method doMethod;
	/**
	 * A hashmap for convenience's sake.
	 */
	private static HashMap<String, Method> doMethods = new HashMap<String, Method>();
	static {
		try {
			doMethods.put("classic", GetPromotionSquares.class.getMethod("classicPromoSquares", Piece.class));
			doMethods.put("noPromos", GetPromotionSquares.class.getMethod("noPromos", Piece.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param name The name of the method.
	 */
	public GetPromotionSquares(String name) {
		doMethod = doMethods.get(name);
		this.name = name;
	}

	/**
	 * In classic chess, the pawns promote
	 * in the back row.
	 * @param p The piece to check.
	 * @return The list of squares it promotes on.
	 */
	public List<Square> classicPromoSquares(Piece p) {
//		if (!(p.getName().equals("Pawn")))
//			return null; //TODO uncomment these lines, make a promotion method
		//that gets from the user the promotion squares of each type (looks up
		//in a hashmap). Can eventually replace this method.
		List<Square> toReturn = new ArrayList<Square>();
		for (int i = 1; i <= g.getBoards()[0].getMaxRow(); i++) {
			if (p.isBlack()) {
				toReturn.add(g.getBoards()[0].getSquare(1, i));
			} else {
				toReturn.add(g.getBoards()[0].getSquare(8, i));
			}
		}
		return toReturn;
	}

	/**
	 * Execute the right method.
	 * @param p The piece to check for
	 * the promotion squares of.
	 * @return The list of Squares representing
	 * where this piece promotes.
	 */
	@SuppressWarnings("unchecked")
	public List<Square> execute(Piece p) {
		try {
			if (doMethod == null) {
				doMethod = doMethods.get(name);
			}
			return (List<Square>) doMethod.invoke(this, p);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * For turning off promotion.
	 * @param p The piece to check
	 * @return null since no pieces may promote.
	 */
	public List<Square> noPromos(Piece p) {
		return null;
	}

	/**
	 * @param g Setter for g.
	 */
	public void setGame(Game g) {
		this.g = g;
	}

}
