package rules;

import gui.PlayGame;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * NextTurn.java
 * 
 * Class to hold methods controlling the turn flow of the game
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class NextTurn implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 9127502749298644757L;

	/**
	 * Whose move it is.
	 */
	private boolean isBlackMove;
	/**
	 * The name of the method to perform
	 */
	private String name;
	/**
	 * The method to perform.
	 */
	private transient Method doMethod;
	/**
	 * The method to undo.
	 */
	private transient Method undoMethod;
	/**
	 * A hashmap for convenience's sake.
	 */
	private static HashMap<String, Method> doMethods = new HashMap<String, Method>();
	/**
	 * A hashmap for convenience's sake.
	 */
	private static HashMap<String, Method> undoMethods = new HashMap<String, Method>();
	/**
	 * The number of moves white can currently make before the turn changes.
	 */
	private static int whiteMoves;
	/**
	 * The number of moves black can currently make before the turn changes.
	 */
	private static int blackMoves;
	/**
	 * The amount the number of turns increases by every turn.
	 */
	private static int increment;
	/**
	 * The current number of moves made.
	 */
	private static int currentNumMoves;

	static {
		try {
			doMethods.put("classic", NextTurn.class.getMethod("classicNextTurn"));
			undoMethods.put("classic", NextTurn.class.getMethod("classicUndo"));
			doMethods.put("increasing together", NextTurn.class.getMethod("increasingTurnsTogether"));
			undoMethods.put("increasing together", NextTurn.class.getMethod("undoIncreasingTurnsTogether"));
			doMethods.put("different turns", NextTurn.class.getMethod("differentNumTurns"));
			undoMethods.put("different turns", NextTurn.class.getMethod("undoDifferentNumTurns"));
			doMethods.put("increasing separately", NextTurn.class.getMethod("increasingTurnsSeparately"));
			undoMethods.put("increasing separately", NextTurn.class.getMethod("undoIncreasingTurnsSeparately"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param name The name of the method
	 * @param whiteMoves The number of white moves.
	 * @param blackMoves The number of black moves
	 * @param increment The increment.
	 */
	public NextTurn(String name, int whiteMoves, int blackMoves, int increment) {
		this.name = name;
		doMethod = doMethods.get(name);
		undoMethod = undoMethods.get(name);
		NextTurn.whiteMoves = whiteMoves;
		NextTurn.blackMoves = blackMoves;
		NextTurn.increment = increment;
		currentNumMoves = 0;
		isBlackMove = false;
	}

	/**
	 * In classic, each player gets 1 move.
	 * @return Whose turn it is.
	 */
	public boolean classicNextTurn() {
		
		isBlackMove = !isBlackMove;
		PlayGame.turn(isBlackMove);

		return isBlackMove;
	}

	/**
	 * Classic is undone by changing the turn back.
	 * @return Whose turn it is.
	 */
	public boolean classicUndo() {
		isBlackMove = !isBlackMove;

		PlayGame.turn(isBlackMove);

		return isBlackMove;
	}

	/**
	 * Black and white have different numbers of turns,
	 * but that number does not increase.
	 * @return Whose turn it is.
	 */
	public boolean differentNumTurns() {
		if (++currentNumMoves >= (isBlackMove ? blackMoves : whiteMoves)) {
			isBlackMove = !isBlackMove;
			PlayGame.turn(isBlackMove);

			currentNumMoves = 0;
		}
		return isBlackMove;
	}

	/**
	 * @return Whose turn it is.
	 */
	public boolean execute() {
		try {
			if (doMethod == null) {
				doMethod = doMethods.get(name);
			}
			return (Boolean) doMethod.invoke(this, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * White and black have different numbers of turns,
	 * and this number increases.
	 * @return Whose turn it is.
	 */
	public boolean increasingTurnsSeparately() {
		if (++currentNumMoves >= (isBlackMove ? blackMoves : whiteMoves)) {
			isBlackMove = !isBlackMove;
			PlayGame.turn(isBlackMove);
			blackMoves += increment;
			whiteMoves += increment;
			currentNumMoves = 0;
		}
		if (isBlackMove) {
			System.out.println("current move: " + currentNumMoves + " max moves: " + blackMoves);
		}
		return isBlackMove;
	}

	/**
	 * In this variant, the number of turns increases
	 * each round by the increment, but both players
	 * have the same number of turns.
	 * @return Whose turn it is.
	 */
	public boolean increasingTurnsTogether() {
		if (++currentNumMoves >= whiteMoves) {
			isBlackMove = !isBlackMove;
			PlayGame.turn(isBlackMove);
			whiteMoves += increment;
			currentNumMoves = 0;
		}
		return isBlackMove;
	}

	/**
	 * @return Whose turn it is.
	 */
	public boolean undo() {
		try {
			if (undoMethod == null) {
				undoMethod = undoMethods.get(name);
			}
			return (Boolean) undoMethod.invoke(this, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Undo this by changing the turn if appropriate.
	 * @return Whose turn it is.
	 */
	public boolean undoDifferentNumTurns() {
		if (--currentNumMoves < 0) {
			isBlackMove = !isBlackMove;
			PlayGame.turn(isBlackMove);

			currentNumMoves = isBlackMove ? blackMoves : whiteMoves;
		}
		return isBlackMove;
	}

	/**
	 * Undo the effects of increasingTurnsSeparately.
	 * @return Whose turn it is.
	 */
	public boolean undoIncreasingTurnsSeparately() {
		if (--currentNumMoves < 0) {
			isBlackMove = !isBlackMove;
			blackMoves -= increment;
			whiteMoves -= increment;
			PlayGame.turn(isBlackMove);

			currentNumMoves = isBlackMove ? blackMoves : whiteMoves;
		}
		return isBlackMove;
	}

	/**
	 * This is undone by decrementing the number
	 * of moves made, and if necessary, decrementing the amount
	 * of moves possible each round. Then the turn is changed, if
	 * appropriate.
	 * @return Whose turn it is.
	 */
	public boolean undoIncreasingTurnsTogether() {
		if (--currentNumMoves < 0) {
			isBlackMove = !isBlackMove;
			PlayGame.turn(isBlackMove);
			whiteMoves -= increment;
			currentNumMoves = whiteMoves;
		}
		return isBlackMove;
	}

}
