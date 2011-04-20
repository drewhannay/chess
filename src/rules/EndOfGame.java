package rules;

import gui.PlayGame;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Result;

/**
 * EndOfGame.java
 * 
 * Class to hold methods with various implementations of
 * determining if the end of the game should happen.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class EndOfGame implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 3818647273549549482L;

	/**
	 * The current Game object.
	 */
	private Game g;
	/**
	 * The Move object.
	 */
	private Move m;
	/**
	 * Whether or not black is to lose pieces;
	 * used so we can implement loseAllPieces and
	 * captureAllPieces in the same method.
	 */
	private boolean blackLosesPieces;
	/**
	 * The number of times the opponent has been placed in check.
	 */
	private int checks;
	/**
	 * The maximum number of checks to place the opponent in before
	 * the game is over.
	 */
	private int maxChecks;
	/**
	 * The name of the method.
	 */
	private String name;
	/**
	 * The type of the piece to capture all of.
	 */
	private String type;
	/**
	 * The method to perform.
	 */
	private transient Method doMethod;
	/**
	 * The method to undo.
	 */
	private transient Method undoMethod;
	/**
	 * A hashmap for convenience's sake to lookup the methods by name.
	 */
	private static HashMap<String, Method> doMethods = new HashMap<String, Method>();
	/**
	 * A hashmap for convenience's sake to lookup the undo methods by name.
	 */
	private static HashMap<String, Method> undoMethods = new HashMap<String, Method>();
	static {
		try {
			doMethods.put("classic", EndOfGame.class.getMethod("classicEndOfGame", Piece.class));
			undoMethods.put("classic", EndOfGame.class.getMethod("dummyUndo"));
			doMethods.put("checkNTimes", EndOfGame.class.getMethod("checkNTimes", Piece.class));
			undoMethods.put("checkNTimes", EndOfGame.class.getMethod("undoCheckNTimes"));
			doMethods.put("loseAllPieces", EndOfGame.class.getMethod("loseAllPieces", Piece.class));
			undoMethods.put("loseAllPieces", EndOfGame.class.getMethod("dummyUndo"));
			doMethods.put("captureAllOfType", EndOfGame.class.getMethod("captureAllOfType", Piece.class));
			undoMethods.put("captureAllOfType", EndOfGame.class.getMethod("dummyUndo"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a new EndOfGame object
	 * @param name The name of the method
	 * @param blackLosesPieces Set the instance variable
	 * @param maxChecks Set the instance variable
	 * @param type Set the instance variable.
	 */
	public EndOfGame(String name, boolean blackLosesPieces, int maxChecks, String type) {
		doMethod = doMethods.get(name);
		undoMethod = undoMethods.get(name);
		this.name = name;
		this.blackLosesPieces = blackLosesPieces;
		this.maxChecks = maxChecks;
		this.type = type;
		checks = 0;
	}

	/**
	 * Capture all of a specified type to win.
	 * @param objectivePiece Unused.
	 */
	public void captureAllOfType(Piece objectivePiece) {
		List<Piece> team = (g.isBlackMove() ? g.getBlackTeam() : g.getWhiteTeam());
		for (Piece p : team) {
			if (p.getName().equals(type) && !p.isCaptured())
				return;
		}
		Result r = new Result(!g.isBlackMove() ? Result.BLACK_WIN : Result.WHITE_WIN);
		r.setText("Game Over! " + r.winText() + "\n");
		PlayGame.endOfGame(r);
	}

	/**
	 * Place the opponent in check n times to
	 * win. 
	 * @param objectivePiece the objective piece to place in check.
	 */
	public void checkNTimes(Piece objectivePiece) {
		if (g.getLastMove() != null && g.getLastMove().isVerified() && g.getLastMove().isCheck()) {
			if (++checks == maxChecks) {
				Result r = new Result(g.isBlackMove() ? Result.WHITE_WIN : Result.BLACK_WIN);
				r.setText("Game Over! " + r.winText() + "\n");
				PlayGame.endOfGame(r);

			}

		}
		m = g.getLastMove();
	}

	/**
	 * In classic chess, one wins by placing the opponent in checkmate.
	 * @param objectivePiece The objective piece.
	 */
	public void classicEndOfGame(Piece objectivePiece) {
		if (g.getLegalMoveCount() == 0
				|| (g.isBlackMove() ? g.getBlackRules() : g.getWhiteRules()).objectivePiece(g.isBlackMove())
						.isCaptured()) {
			// if the King is threatened, it's check mate.
			if (objectivePiece == null||objectivePiece.isInCheck() || objectivePiece.isCaptured()) {
				if (g.getLastMove() != null) {
					g.getLastMove().setCheckmate(true);
					Result r = new Result(g.isBlackMove() ? Result.WHITE_WIN : Result.BLACK_WIN);
					r.setText("Game over! " + r.winText() +  "\n" + "The piece(s) that caused the final check are highlighted in Red. \n" + "The piece that placed the King in check was the " + g.getThreats(objectivePiece)[0].getName() + " at location " + g.getThreats(objectivePiece)[0].getSquare().toString(new boolean[]{false,false}) + "\n"
							+ "What would you like to do? \n");
					g.getLastMove().setResult(r);
					if (!g.getHistory().contains(g.getLastMove())) {
						g.getHistory().add(g.getLastMove());
					}
					PlayGame.boardRefresh(g.getBoards());// Let the user see the final move.
					PlayGame.endOfGame(r);
				}
			}
			// if the King isn't threatened, then it's stale mate.
			else {
				if (g.getLastMove() != null) {
					g.getLastMove().setStalemate(true);
					Result r = new Result(Result.DRAW);
					r.setText("Draw! " + "\n");
					g.getLastMove().setResult(r);
					if (!g.getHistory().contains(g.getLastMove())) {
						g.getHistory().add(g.getLastMove());
					}
					PlayGame.boardRefresh(g.getBoards());// Let the user see the final move.
					PlayGame.endOfGame(r);
				}
			}
		}
	}

	/**
	 * Dummy undo method for methods that do not need undoing (more than 1)
	 */
	public void dummyUndo() {
	}

	/**
	 * Execute the appropriate method.
	 * @param objectivePiece The objective piece.
	 */
	public void execute(Piece objectivePiece) {
		try {
			if (doMethod == null) {
				doMethod = doMethods.get(name);
			}
			doMethod.invoke(this, objectivePiece);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * In this case, the goal is either to lose
	 * all pieces or capture all the opponent's pieces based on the 
	 * instance variable blackLosesPieces.
	 * @param objectivePiece The objective piece; unused.
	 */
	public void loseAllPieces(Piece objectivePiece) {
		List<Piece> team = (blackLosesPieces ? g.getBlackTeam() : g.getWhiteTeam());
		for (Piece p : team) {
			if (!p.isCaptured())
				return;
		}
		Result r = new Result(blackLosesPieces ? Result.BLACK_WIN : Result.WHITE_WIN);
		r.setText("Game Over! " + r.winText() + "\n");
		PlayGame.endOfGame(r);
	}

	/**
	 * Setter for g
	 * @param g The new Game object.
	 */
	public void setGame(Game g) {
		this.g = g;
	}

	/**
	 * Undo the appropriate method.
	 */
	public void undo() {
		try {
			if (undoMethod == null) {
				undoMethod = undoMethods.get(name);
			}
			undoMethod.invoke(this, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Undo checkNTimes resets the counter on the 
	 * number of checks.
	 */
	public void undoCheckNTimes() {
		if (m != null && m.isVerified() && m.isCheck()) {
			--checks;
		}
	}

}
