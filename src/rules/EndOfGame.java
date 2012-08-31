package rules;

import gui.PlayGame;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Result;

import com.google.common.collect.Maps;

/**
 * EndOfGame.java
 * 
 * Class to hold methods with various implementations of determining if the end
 * of the game should happen.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class EndOfGame implements Serializable
{
	/**
	 * Create a new EndOfGame object
	 * 
	 * @param methodName The name of the method
	 * @param maxChecks Set the instance variable
	 * @param pieceType Set the instance variable.
	 * @param isBlack Is this for the black or white team?
	 */
	public EndOfGame(String methodName, int maxChecks, String pieceType, boolean isBlack)
	{
		m_doMethod = doMethods.get(methodName);
		m_undoMethod = undoMethods.get(methodName);
		m_methodName = methodName;
		m_maxNumberOfChecks = maxChecks;
		m_pieceType = pieceType;
		m_isBlackRuleSet = isBlack;
		m_numberOfChecks = 0;
	}

	/**
	 * Capture all of a specified type to win.
	 * 
	 * @param objectivePiece Unused.
	 */
	public void captureAllOfType(Piece objectivePiece)
	{
		List<Piece> team = (!m_isBlackRuleSet ? m_game.getBlackTeam() : m_game.getWhiteTeam());
		for (Piece piece : team)
		{
			if (piece.getName().equals(m_pieceType) && !piece.isCaptured())
				return;
		}
		Result result = new Result(m_isBlackRuleSet ? Result.BLACK_WIN : Result.WHITE_WIN);
		result.setText("Game Over! " + result.winText() + "\n");
		PlayGame.endOfGame(result);
	}

	/**
	 * Place the opponent in check n times to win.
	 * 
	 * @param objectivePiece the objective piece to place in check.
	 */
	public void checkNTimes(Piece objectivePiece)
	{
		if (m_game.getLastMove() != null && m_game.getLastMove().isVerified() && m_game.getLastMove().isCheck()
				&& m_game.getLastMove().getPiece().isBlack() == m_isBlackRuleSet)
		{
			if (++m_numberOfChecks == m_maxNumberOfChecks)
			{
				Result result = new Result(!m_isBlackRuleSet ? Result.WHITE_WIN : Result.BLACK_WIN);
				result.setText("Game Over! " + result.winText() + "\n");
				PlayGame.endOfGame(result);
			}
		}
		m_move = m_game.getLastMove();
	}

	/**
	 * In classic chess, one wins by placing the opponent in checkmate.
	 * 
	 * @param objectivePiece The objective piece.
	 */
	public void classicEndOfGame(Piece objectivePiece)
	{

		if (m_game.getLegalMoveCount() == 0 || objectivePiece.isCaptured())
		{
			// if the King is threatened, it's check mate.
			if (objectivePiece == null || objectivePiece.isInCheck() || objectivePiece.isCaptured())
			{
				if (m_game.getLastMove() != null)
				{
					m_game.getLastMove().setCheckmate(true);
					Result result = new Result(m_game.isBlackMove() ? Result.WHITE_WIN : Result.BLACK_WIN);
					String resultText = "Game over! " + result.winText() + "\n";

					if (m_game.getThreats(objectivePiece) != null)
					{
						resultText += "The piece(s) that caused the final check are highlighted in Red. "
								+ "\nThe piece that placed the King in check was the "
								+ m_game.getThreats(objectivePiece)[0].getName() + " at location "
								+ m_game.getThreats(objectivePiece)[0].getSquare().toString(new boolean[] { false, false }) + "\n";
					}
					result.setText(resultText + "What would you like to do? \n");
					m_game.getLastMove().setResult(result);
					if (!m_game.getHistory().contains(m_game.getLastMove()))
						m_game.getHistory().add(m_game.getLastMove());

					// let the user see the final move
					PlayGame.boardRefresh(m_game.getBoards());
					PlayGame.endOfGame(result);
				}
			}
			// if the King isn't threatened, then it's stalemate
			else
			{
				if (m_game.getLastMove() != null)
				{
					m_game.getLastMove().setStalemate(true);
					Result result = new Result(Result.DRAW);
					result.setText("Draw! " + "\n");
					m_game.getLastMove().setResult(result);
					if (!m_game.getHistory().contains(m_game.getLastMove()))
					{
						m_game.getHistory().add(m_game.getLastMove());
					}
					// let the user see the final move
					PlayGame.boardRefresh(m_game.getBoards());
					PlayGame.endOfGame(result);
				}
			}
		}
	}

	/**
	 * Dummy undo method for methods that do not need undoing (more than 1)
	 */
	public void dummyUndo()
	{
	}

	/**
	 * Execute the appropriate method.
	 * 
	 * @param objectivePiece The objective piece.
	 */
	public void execute(Piece objectivePiece)
	{
		try
		{
			if (m_doMethod == null)
				m_doMethod = doMethods.get(m_methodName);

			m_doMethod.invoke(this, objectivePiece);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * In this case, the goal is either to lose all pieces or capture all the
	 * opponent's pieces based on the instance variable blackLosesPieces.
	 * 
	 * @param objectivePiece The objective piece; unused.
	 */
	public void loseAllPieces(Piece objectivePiece)
	{
		List<Piece> team = (!m_isBlackRuleSet ? m_game.getBlackTeam() : m_game.getWhiteTeam());
		for (Piece piece : team)
		{
			if (!piece.isCaptured())
				return;
		}
		Result result = new Result(m_isBlackRuleSet ? Result.BLACK_WIN : Result.WHITE_WIN);
		result.setText("Game Over! " + result.winText() + "\n");
		PlayGame.endOfGame(result);
	}

	/**
	 * Capture all the pieces.
	 * 
	 * @param objectivePiece Unused.
	 */
	public void captureAllPieces(Piece objectivePiece)
	{
		List<Piece> team = (m_isBlackRuleSet ? m_game.getBlackTeam() : m_game.getWhiteTeam());
		for (Piece piece : team)
		{
			if (!piece.isCaptured())
				return;
		}
		Result result = new Result(!m_isBlackRuleSet ? Result.BLACK_WIN : Result.WHITE_WIN);
		result.setText("Game Over! " + result.winText() + "\n");
		PlayGame.endOfGame(result);
	}

	public void setGame(Game game)
	{
		m_game = game;
	}

	/**
	 * Undo the appropriate method.
	 */
	public void undo()
	{
		try
		{
			if (m_undoMethod == null)
			{
				m_undoMethod = undoMethods.get(m_methodName);
			}
			m_undoMethod.invoke(this, (Object[]) null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Undo checkNTimes resets the counter on the number of checks.
	 */
	public void undoCheckNTimes()
	{
		if (m_move != null && m_move.isVerified() && m_move.isCheck())
			m_numberOfChecks--;
	}

	public String getMethodName()
	{
		return m_methodName;
	}

	/**
	 * The type to capture all of, if any.
	 * 
	 * @return The string representation of the type.
	 */
	public String getPieceType()
	{
		return m_pieceType;
	}

	private static final long serialVersionUID = 3818647273549549482L;

	private static Map<String, Method> doMethods = Maps.newHashMap();
	private static Map<String, Method> undoMethods = Maps.newHashMap();

	static
	{
		try
		{
			doMethods.put("classic", EndOfGame.class.getMethod("classicEndOfGame", Piece.class));
			undoMethods.put("classic", EndOfGame.class.getMethod("dummyUndo"));
			doMethods.put("checkNTimes", EndOfGame.class.getMethod("checkNTimes", Piece.class));
			undoMethods.put("checkNTimes", EndOfGame.class.getMethod("undoCheckNTimes"));
			doMethods.put("loseAllPieces", EndOfGame.class.getMethod("loseAllPieces", Piece.class));
			undoMethods.put("loseAllPieces", EndOfGame.class.getMethod("dummyUndo"));
			doMethods.put("captureAllPieces", EndOfGame.class.getMethod("captureAllPieces", Piece.class));
			undoMethods.put("captureAllPieces", EndOfGame.class.getMethod("dummyUndo"));
			doMethods.put("captureAllOfType", EndOfGame.class.getMethod("captureAllOfType", Piece.class));
			undoMethods.put("captureAllOfType", EndOfGame.class.getMethod("dummyUndo"));

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private transient Method m_doMethod;
	private transient Method m_undoMethod;

	private Game m_game;
	private Move m_move;
	private int m_numberOfChecks;
	private int m_maxNumberOfChecks;
	private String m_methodName;
	private String m_pieceType;
	private boolean m_isBlackRuleSet;
}
