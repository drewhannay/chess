package rules;

import gui.PlayGame;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import logic.Board;
import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Square;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * AfterMove.java
 * 
 * Class to hold methods with various tasks to be performed after a Move takes
 * place.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class AfterMove implements Serializable
{
	public List<String> getMethods()
	{
		return m_methodNames;
	}

	public void addMethod(String methodName)
	{
		m_doMethod.add(doMethods.get(methodName));
		m_undoMethod.add(undoMethods.get(methodName));
		m_methodNames.add(methodName);
	}

	/**
	 * Swap the color of the capturing piece.
	 * 
	 * @param move The move just performed
	 */
	public void captureTeamSwap(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece toSwap = move.getPiece();
		toSwap.getLegalDests().clear();
		toSwap.getGuardSquares().clear();
		toSwap.setPinnedBy(null);
		(toSwap.isBlack() ? m_game.getBlackTeam() : m_game.getWhiteTeam()).remove(toSwap);
		(!toSwap.isBlack() ? m_game.getBlackTeam() : m_game.getWhiteTeam()).add(toSwap);
		toSwap.setBlack(!toSwap.isBlack());
	}

	/**
	 * Does nothing in the classic move case.
	 * 
	 * @param m Not used; necessary for the reflection (in order to dynamically
	 * call a method, all the signatures must be the same)
	 */
	public void classicAfterMove(Move move)
	{
		// Nothing to be done after the move in classic chess.
	}

	/**
	 * Does nothing in the classic move case.
	 * 
	 * @param m Not used; necessary for the reflection (in order to dynamically
	 * call a method, all the signatures must be the same)
	 */
	public void classicUndo(Move move)
	{
		// Also nothing to undo
	}

	/**
	 * Execute the appropriate methods using reflection.
	 * 
	 * @param move The move that has just been performed.
	 */
	public void execute(Move move)
	{
		try
		{
			if (m_doMethod == null)
			{
				m_doMethod = Lists.newArrayList();
				for (String methodName : m_methodNames)
					m_doMethod.add(doMethods.get(methodName));
			}
			for (Method method : m_doMethod)
			{
				method.invoke(this, move);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Return the captured piece to its original square.
	 * 
	 * @param m The move just performed.
	 */
	public void goHome(Move m)
	{
		if (m.getCaptured() == null)
			return;
		Piece toHome = m.getCaptured();
		// if you captured a piece on it's original square, let capturing work
		// as normal
		if (toHome.getOriginalSquare() == toHome.getSquare())
			return;
		// otherwise, move the captured piece to it's original square
		toHome.getLegalDests().clear();
		toHome.getGuardSquares().clear();
		toHome.setPinnedBy(null);

		// if there was a piece on the original square, save it so we can put it
		// back if we undo
		m.setRemoved(toHome.getOriginalSquare().getPiece());
		if (m.getRemoved() != null)
			(m.getRemoved().isBlack() ? m_game.getBlackTeam() : m_game.getWhiteTeam()).remove(m.getRemoved());

		// actually set the captured piece on it's home square
		toHome.getOriginalSquare().setPiece(toHome);
		toHome.setIsCaptured(false);
	}

	/**
	 * The capturer places the captured piece, and the captured piece changes
	 * color.
	 * 
	 * @param move The move just performed.
	 */
	public void placeCapturedSwitch(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece toPlace = move.getCaptured();
		toPlace.getLegalDests().clear();
		toPlace.getGuardSquares().clear();
		toPlace.setPinnedBy(null);
		(toPlace.isBlack() ? m_game.getBlackTeam() : m_game.getWhiteTeam()).remove(toPlace);
		(!toPlace.isBlack() ? m_game.getBlackTeam() : m_game.getWhiteTeam()).add(toPlace);
		toPlace.setBlack(!toPlace.isBlack());
		toPlace.setIsCaptured(false);
		move.setOldPos(toPlace.getSquare());
		if (move.isVerified())
		{
			JOptionPane.showMessageDialog(null, "This piece is now on your side. Place it in an empty square.");
			PlayGame.setMustPlace(true);
			PlayGame.setPlacePiece(toPlace);
		}

	}

	/**
	 * Undo the effects of placeCapturedSwap
	 * 
	 * @param move The move to undo.
	 */
	public void undoPlaceCapturedSwitch(Move move)
	{
		if (move.getOldPos() == null)
		{
			return;
		}
		else
		{
			Piece toPlace = move.getCaptured();
			toPlace.getLegalDests().clear();
			toPlace.getGuardSquares().clear();
			toPlace.setPinnedBy(null);
			(toPlace.isBlack() ? m_game.getBlackTeam() : m_game.getWhiteTeam()).remove(toPlace);
			(!toPlace.isBlack() ? m_game.getBlackTeam() : m_game.getWhiteTeam()).add(toPlace);
			toPlace.setBlack(!toPlace.isBlack());
			toPlace.setIsCaptured(false);
			toPlace.getSquare().setPiece(null);
			toPlace.setSquare(move.getOldPos());
			move.getOldPos().setPiece(toPlace);
			toPlace.setIsCaptured(false);
			PlayGame.setMustPlace(false);
			PlayGame.setPlacePiece(null);
			move.setOldPos(null);
		}
	}

	/**
	 * The opponent places captured pieces.
	 * 
	 * @param move The move just performed.
	 */
	public void placeCaptured(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece toPlace = move.getCaptured();
		toPlace.getLegalDests().clear();
		toPlace.getGuardSquares().clear();
		toPlace.setPinnedBy(null);

		move.setOldPos(toPlace.getSquare());
		Piece objectivePiece = toPlace.isBlack() ? m_game.getBlackRules().objectivePiece(toPlace.isBlack()) : m_game.getWhiteRules()
				.objectivePiece(toPlace.isBlack());
		if (move.isVerified() && !(objectivePiece == toPlace))
		{
			toPlace.setIsCaptured(false);
			JOptionPane.showMessageDialog(null, "You have captured this piece. Now place it in an empty square.");
			PlayGame.setMustPlace(true);
			PlayGame.setPlacePiece(toPlace);
		}

	}

	/**
	 * Undo the effects of placeCaptured.
	 * 
	 * @param move The move to undo.
	 */
	public void undoPlaceCaptured(Move move)
	{
		if (move.getOldPos() == null)
		{
			return;
		}
		else
		{
			Piece toPlace = move.getCaptured();
			toPlace.getLegalDests().clear();
			toPlace.getGuardSquares().clear();
			toPlace.setPinnedBy(null);
			toPlace.setIsCaptured(false);
			toPlace.getSquare().setPiece(null);
			toPlace.setSquare(move.getOldPos());
			move.getOldPos().setPiece(toPlace);
			toPlace.setIsCaptured(false);
			PlayGame.setMustPlace(false);
			PlayGame.setPlacePiece(null);
			move.setOldPos(null);
		}
	}

	/**
	 * Setter method for the game object
	 * 
	 * @param game The game to set the instance variable to.
	 */
	public void setGame(Game game)
	{
		m_game = game;
	}

	/**
	 * Undo all the appropriate methods using reflection.
	 * 
	 * @param move The move that has just been performed.
	 */
	public void undo(Move move)
	{
		try
		{
			if (m_undoMethod == null)
			{
				m_undoMethod = Lists.newArrayList();
				for (String methodName : m_methodNames)
					m_undoMethod.add(undoMethods.get(methodName));
			}
			for (Method method : m_undoMethod)
				method.invoke(this, move);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Return the captured pieces to the appropriate square after undoing a
	 * move.
	 * 
	 * @param move The move just performed.
	 */
	public void undoGoHome(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece restore = move.getCaptured();
		restore.setIsCaptured(false);
		if (move.getRemoved() != null)
		{
			(move.getRemoved().isBlack() ? m_game.getBlackTeam() : m_game.getWhiteTeam()).add(move.getRemoved());
			move.getRemoved().setIsCaptured(false);
		}
		move.getCaptured().getSquare().setPiece(move.getRemoved());
		move.getDest().setPiece(restore);
	}

	/**
	 * Capture removes pieces from 8 surrounding squares, including the
	 * capturing piece - with the exception of pawns, unless the pawn is either
	 * the captured piece or the capturer.
	 * 
	 * @param move The move performed.
	 */
	public void atomicCapture(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece captured = move.getCaptured();
		Piece suicide = move.getPiece();
		Square curSquare = captured.getSquare();
		Board board = captured.getBoard();
		Square[] squares = new Square[9];
		int n = 0;
		boolean wraparound = board.isWrapAround();
		int upperCol = curSquare.getCol() + 1;
		for (int i = curSquare.getRow() - 1; i <= 1 + curSquare.getRow(); i++)
		{
			for (int j = curSquare.getCol() - 1; j <= upperCol; j++)
			{
				upperCol = curSquare.getCol() + 1;
				if (board.isRowValid(i))
				{
					if (!wraparound && !board.isColValid(j))
						continue;

					int k = j;
					if (k < 1)
					{
						k = board.getMaxCol();
						upperCol = board.getMaxCol() + 1;
					}
					else if (k > board.getMaxCol())
					{
						k = 1;
					}
					squares[n++] = board.getSquare(i, k);
				}
			}
		}
		List<Piece> exploded = Lists.newArrayList();
		for (Square s : squares)
		{
			if (s == null)
				continue;
			Piece p = s.getPiece();
			if (p != null && (!(p.getName().equals("Pawn")) && p != suicide) && p != captured)
			{
				exploded.add(p);
				p.setIsCaptured(true);
				p.getSquare().setPiece(null);
			}
		}
		exploded.add(suicide);
		suicide.setIsCaptured(true);
		suicide.getSquare().setPiece(null);
		Piece[] toReturn = new Piece[exploded.size()];
		move.setExploded(exploded.toArray(toReturn));
	}

	/**
	 * Undo atomicCapture
	 * 
	 * @param move The move performed.
	 */
	public void undoAtomicCapture(Move move)
	{
		if (move.getCaptured() == null)
		{
			return;
		}
		else
		{
			Piece[] exploded = move.getExploded();
			for (Piece p : exploded)
			{
				p.setIsCaptured(false);
				p.getSquare().setPiece(p);
			}
			exploded[exploded.length - 1].setMoveCount(exploded[exploded.length - 1].getMoveCount() - 1);
			move.setExploded(null);
		}
	}

	private static final long serialVersionUID = -5906257593787563104L;

	private static Map<String, Method> doMethods = Maps.newHashMap();
	private static Map<String, Method> undoMethods = Maps.newHashMap();
	static
	{
		try
		{
			doMethods.put("classic", AfterMove.class.getMethod("classicAfterMove", Move.class));
			doMethods.put("captureTeamSwap", AfterMove.class.getMethod("captureTeamSwap", Move.class));
			doMethods.put("goHome", AfterMove.class.getMethod("goHome", Move.class));
			doMethods.put("placeCapturedSwitch", AfterMove.class.getMethod("placeCapturedSwitch", Move.class));
			doMethods.put("placeCaptured", AfterMove.class.getMethod("placeCaptured", Move.class));
			doMethods.put("atomicCapture", AfterMove.class.getMethod("atomicCapture", Move.class));

			undoMethods.put("placeCapturedSwitch", AfterMove.class.getMethod("undoPlaceCapturedSwitch", Move.class));
			undoMethods.put("placeCaptured", AfterMove.class.getMethod("undoPlaceCaptured", Move.class));
			undoMethods.put("classic", AfterMove.class.getMethod("classicUndo", Move.class));
			undoMethods.put("captureTeamSwap", AfterMove.class.getMethod("captureTeamSwap", Move.class));
			undoMethods.put("goHome", AfterMove.class.getMethod("undoGoHome", Move.class));
			undoMethods.put("atomicCapture", AfterMove.class.getMethod("undoAtomicCapture", Move.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private transient List<Method> m_doMethod = Lists.newArrayList();
	private transient List<Method> m_undoMethod = Lists.newArrayList();

	private Game m_game;
	private List<String> m_methodNames = Lists.newArrayList();
}
