package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;

import logic.Game;
import logic.Piece;

/**
 * ObjectivePiece.java
 * 
 * Class to hold methods returning the objective of the game,
 * if there is one.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class ObjectivePiece implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 6945649584920313635L;

	/**
	 * The current game.
	 */
	private Game g;

	/**
	 * The name of the method to perform.
	 */
	private String name;
	/**
	 * The name of the objective piece.
	 */
	private String objectiveName;
	/**
	 * The method to perform.
	 */
	private transient Method doMethod;
	/**
	 * A hashmap for convenience.
	 */
	private static HashMap<String, Method> doMethods = new HashMap<String, Method>();
	static {
		try {
			doMethods.put("classic", ObjectivePiece.class.getMethod("classicObjectivePiece", boolean.class));
			doMethods.put("no objective", ObjectivePiece.class.getMethod("noObjectivePiece", boolean.class));
			doMethods.put("custom objective", ObjectivePiece.class.getMethod("customObjectivePiece", boolean.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param name The name of the method.
	 * @param objectiveName The name of the objective piece.
	 */
	public ObjectivePiece(String name, String objectiveName) {
		doMethod = doMethods.get(name);
		this.name = name;
		this.objectiveName = objectiveName;
	}

	/**
	 * In classic, the king is the objective piece.
	 * @param isBlack Whether the piece is black 
	 * @return The objective piece on the same team.
	 */
	public Piece classicObjectivePiece(boolean isBlack) {
		try {
			if (isBlack) {
				for (Piece p : g.getBlackTeam())
					if (p.getName().equals("King"))
						return p;
					
			}
			for (Piece p : g.getWhiteTeam())
				if (p.getName().equals("King"))
					return p;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * In this case, return the custom objective piece.
	 * @param isBlack whether the piece is black
	 * @return The corresponding objective piece.
	 */
	public Piece customObjectivePiece(boolean isBlack) {
		try {
			if (isBlack) {
				for (Piece p : g.getBlackTeam())
					if (p.getName().equals(objectiveName))
						return p;
			}
			for (Piece p : g.getWhiteTeam())
				if (p.getName().equals(objectiveName))
					return p;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Perform the right method.
	 * @param isBlack Is this piece black?
	 * @return The objective piece.
	 */
	public Piece execute(boolean isBlack) {
		try {
			if (doMethod == null) {
				doMethod = doMethods.get(name);
			}
			return (Piece) doMethod.invoke(this, isBlack);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return null in the case that there is no objective.
	 * @param isBlack Whether this piece is black.
	 * @return null in this case.
	 */
	public Piece noObjectivePiece(boolean isBlack) {
		return null;
	}

	/**
	 * @param g Setter for g.
	 */
	public void setGame(Game g) {
		this.g = g;
	}

}
