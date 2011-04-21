package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;

import logic.Board;
import logic.Game;

/**
 * GetBoard.java
 * 
 * Class to hold methods with various ways of determining
 * which Board a piece should move to.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class GetBoard implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -2155872379580860065L;

	/**
	 * The current Game object.
	 */
	private Game g;

	/**
	 * The name of the method; for reading in the object.
	 */
	private String name;
	/**
	 * The method to perform
	 */
	private transient Method doMethod;
	/**
	 * A hashmap for convience's sake to look up the method by name.
	 */
	private static HashMap<String, Method> doMethods = new HashMap<String, Method>();
	static {
		try {
			doMethods.put("classic", GetBoard.class.getMethod("classicGetBoard", Board.class));
			doMethods.put("oppositeBoard", GetBoard.class.getMethod("oppositeBoard", Board.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param name The name of the method.
	 */
	public GetBoard(String name) {
		doMethod = doMethods.get(name);
		this.name = name;
	}

	/**
	 * @param startBoard The original board
	 * @return The same board.
	 */
	public Board classicGetBoard(Board startBoard) {
		return startBoard;
	}

	/**
	 * Execute the appropriate method.
	 * @param startBoard The original board
	 * @return The board requested.
	 */
	public Board execute(Board startBoard) {
		try {
			if (doMethod == null) {
				doMethod = doMethods.get(name);
			}
			return (Board) doMethod.invoke(this, startBoard);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param startBoard The original board
	 * @return The other board, if any.
	 */
	public Board oppositeBoard(Board startBoard) {
		if (startBoard.equals(g.getBoards()[0]))
			return g.getBoards()[1];
		return g.getBoards()[0];
	}

	/**
	 * @param g Setter for g.
	 */
	public void setGame(Game g) {
		this.g = g;
	}

}
