package rules;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import logic.Game;
import logic.Move;
import logic.Piece;

/**
 * AfterMove.java
 * 
 * Class to hold methods with various tasks to be performed after
 * a Move takes place.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class AfterMove implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -5906257593787563104L;

	/**
	 * The game object associate with this instance of AfterMove.
	 */
	private Game g;

	/**
	 * The names of the methods (since there can be more than one) -
	 * used to restore the methods after reading in the object
	 * using Serialization.
	 */
	private List<String> names = new ArrayList<String>();
	/**
	 * The list of methods to perform.
	 */
	private transient List<Method> doMethod = new ArrayList<Method>();
	/**
	 * The list of methods to undo.
	 */
	private transient List<Method> undoMethod = new ArrayList<Method>();
	/**
	 * A hashmap for convenience's sake to look up the do methods easily.
	 */
	private static HashMap<String, Method> doMethods = new HashMap<String, Method>();
	/**
	 * A hashmap for convenience's sake to look up the undo methods easily.
	 */
	private static HashMap<String, Method> undoMethods = new HashMap<String, Method>();
	static {
		try {
			doMethods.put("classic", AfterMove.class.getMethod("classicAfterMove", Move.class));
			doMethods.put("captureTeamSwap", AfterMove.class.getMethod("captureTeamSwap", Move.class));
			doMethods.put("goHome", AfterMove.class.getMethod("goHome", Move.class));
			undoMethods.put("classic", AfterMove.class.getMethod("classicUndo", Move.class));
			undoMethods.put("captureTeamSwap", AfterMove.class.getMethod("captureTeamSwap", Move.class));
			undoMethods.put("goHome", AfterMove.class.getMethod("undoGoHome", Move.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a method to the current list of them.
	 * @param name The method to add.
	 */
	public void addMethod(String name) {
		doMethod.add(doMethods.get(name));
		undoMethod.add(undoMethods.get(name));
		this.names.add(name);
	}

	/**
	 * Swap the color of the capturing piece.
	 * @param m The move just performed
	 */
	public void captureTeamSwap(Move m) {
		if (m.getCaptured() == null)
			return;
		Piece toSwap = m.getPiece();
		toSwap.getLegalDests().clear();
		toSwap.getGuardSquares().clear();
		toSwap.setPinnedBy(null);
		(toSwap.isBlack() ? g.getBlackTeam() : g.getWhiteTeam()).remove(toSwap);
		(!toSwap.isBlack() ? g.getBlackTeam() : g.getWhiteTeam()).add(toSwap);
		toSwap.setBlack(!toSwap.isBlack());
	}

	/**
	 * Does nothing in the classic move case.
	 * @param m Not used; necessary for the reflection 
	 * (in order to dynamically call a method, all the signatures
	 * must be the same)
	 */
	public void classicAfterMove(Move m) {
		//Nothing to be done after the move in classic chess.
	}

	/**
	 * Does nothing in the classic move case.
	 * @param m Not used; necessary for the reflection 
	 * (in order to dynamically call a method, all the signatures
	 * must be the same)
	 */
	public void classicUndo(Move m) {
		//Also nothing to undo
	}

	/**
	 * Execute the appropriate methods
	 * using reflection.
	 * @param m The move that has just been performed.
	 */
	public void execute(Move m) {
		try {
			if (doMethod == null) {
				doMethod = new ArrayList<Method>();
				for (String s : names) {
					doMethod.add(doMethods.get(s));
				}
			}
			for (Method curr : doMethod) {
				curr.invoke(this, m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the captured piece to its original square.
	 * @param m The move just performed.
	 */
	public void goHome(Move m) {
		if (m.getCaptured() == null)
			return;
		Piece toHome = m.getCaptured();
		//If you captured a piece on it's original square, let capturing work as normal
		if (toHome.getOriginalSquare() == toHome.getSquare())
			return;
		//Otherwise, move the captured piece to it's original square
		toHome.getLegalDests().clear();
		toHome.getGuardSquares().clear();
		toHome.setPinnedBy(null);

		//If there was a piece on the original square, save it so we can put it back there if we undo.
		m.setRemoved(toHome.getOriginalSquare().getPiece());
		if (m.getRemoved() != null) {
			System.out.println(m.getRemoved().toString());
			(m.getRemoved().isBlack() ? g.getBlackTeam() : g.getWhiteTeam()).remove(m.getRemoved());
		}

		//Actually set the captured piece on it's home square
		toHome.getOriginalSquare().setPiece(toHome);
		toHome.setCaptured(false);
	}

	/**
	 * Setter method for the game object
	 * @param g The game to set the instance variable to.
	 */
	public void setGame(Game g) {
		this.g = g;
	}

	/**
	 * Undo all the appropriate methods using reflection.
	 * @param m The move that has just been performed.
	 */
	public void undo(Move m) {
		try {
			if (undoMethod == null) {
				undoMethod = new ArrayList<Method>();
				for (String s : names) {
					undoMethod.add(undoMethods.get(s));
				}
			}
			for (Method curr : undoMethod) {
				curr.invoke(this, m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the captured pieces to the appropriate square after undoing a move.
	 * @param m The move just performed.
	 */
	public void undoGoHome(Move m) {
		if (m.getCaptured() == null)
			return;
		Piece restore = m.getCaptured();
		restore.setCaptured(false);
		if (m.getRemoved() != null) {
			(m.getRemoved().isBlack() ? g.getBlackTeam() : g.getWhiteTeam()).add(m.getRemoved());
			m.getRemoved().setCaptured(false);
		}
		m.getCaptured().getSquare().setPiece(m.getRemoved());
		m.getDest().setPiece(restore);
	}

}
