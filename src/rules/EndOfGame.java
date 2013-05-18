package rules;

import gui.PlayGamePanel;

import java.util.List;

import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Result;

public enum EndOfGame
{
	CLASSIC,
	CHECK_N_TIMES,
	LOSE_ALL_PIECES,
	CAPTURE_ALL_PIECES,
	CAPTURE_ALL_OF_TYPE;

	public void checkEndOfGame(Piece objectivePiece)
	{
		switch (this)
		{
		case CLASSIC:
			classicCheckEndOfGame(objectivePiece);
			break;
		case CHECK_N_TIMES:
			checkForCheckNTimes();
			break;
		case LOSE_ALL_PIECES:
			checkLoseAllPieces();
			break;
		case CAPTURE_ALL_PIECES:
			captureAllPieces();
			break;
		case CAPTURE_ALL_OF_TYPE:
			checkCaptureAllOfType();
			break;
		default:
			break;
		}
	}

	public void undo()
	{
		switch (this)
		{
		case CHECK_N_TIMES:
			undoCheckForCheckNTimes();
			break;
		case CLASSIC:
		case LOSE_ALL_PIECES:
		case CAPTURE_ALL_PIECES:
		case CAPTURE_ALL_OF_TYPE:
		default:
			break;
		}
	}

	public EndOfGame init(int maxNumberOfChecks, String pieceName, boolean isBlackRuleSet)
	{
		m_maxNumberOfChecks = maxNumberOfChecks;
		m_pieceName = pieceName;
		m_isBlackRuleSet = isBlackRuleSet;

		return this;
	}

	public void setGame(Game game)
	{
		m_game = game;
	}

	public String getCaptureAllPieceName()
	{
		return m_pieceName;
	}

	private void classicCheckEndOfGame(Piece objectivePiece)
	{
		if (m_game.getLegalMoveCount() == 0 || objectivePiece.isCaptured())
		{
			// if the King is threatened, it's check mate.
			if (objectivePiece == null || objectivePiece.isInCheck() || objectivePiece.isCaptured())
			{
				if (m_game.getLastMove() != null)
				{
					m_game.getLastMove().setCheckmate(true);
					Result result = m_game.isBlackMove() ? Result.WHITE_WIN : Result.BLACK_WIN;
					String resultText = "Game over! " + result.winText() + "\n";

					if (m_game.getThreats(objectivePiece) != null)
					{
						resultText += "The piece(s) that caused the final check are highlighted in Red. "
								+ "\nThe piece that placed the King in check was the "
								+ m_game.getThreats(objectivePiece)[0].getName() + " at location "
								+ m_game.getThreats(objectivePiece)[0].getSquare().toString(new boolean[] { false, false }) + "\n";
					}
					result.setGuiText(resultText + "What would you like to do? \n");
					m_game.getLastMove().setResult(result);
					if (!m_game.getHistory().contains(m_game.getLastMove()))
						m_game.getHistory().add(m_game.getLastMove());

					// let the user see the final move
					PlayGamePanel.boardRefresh(m_game.getBoards());
					PlayGamePanel.endOfGame(result);
				}
			}
			// if the King isn't threatened, then it's stalemate
			else
			{
				if (m_game.getLastMove() != null)
				{
					m_game.getLastMove().setStalemate(true);
					Result result = Result.DRAW;
					result.setGuiText("Draw! " + "\n");
					m_game.getLastMove().setResult(result);
					if (!m_game.getHistory().contains(m_game.getLastMove()))
					{
						m_game.getHistory().add(m_game.getLastMove());
					}
					// let the user see the final move
					PlayGamePanel.boardRefresh(m_game.getBoards());
					PlayGamePanel.endOfGame(result);
				}
			}
		}
	}

	private void checkForCheckNTimes()
	{
		if (m_game.getLastMove() != null && m_game.getLastMove().isVerified() && m_game.getLastMove().isCheck()
				&& m_game.getLastMove().getPiece().isBlack() == m_isBlackRuleSet)
		{
			if (++m_numberOfChecks == m_maxNumberOfChecks)
			{
				Result result = !m_isBlackRuleSet ? Result.WHITE_WIN : Result.BLACK_WIN;
				result.setGuiText("Game Over! " + result.winText() + "\n");
				PlayGamePanel.endOfGame(result);
			}
		}
		m_move = m_game.getLastMove();
	}

	private void undoCheckForCheckNTimes()
	{
		if (m_move != null && m_move.isVerified() && m_move.isCheck())
			m_numberOfChecks--;
	}

	private void checkLoseAllPieces()
	{
		List<Piece> team = (!m_isBlackRuleSet ? m_game.getBlackTeam() : m_game.getWhiteTeam());
		for (Piece piece : team)
		{
			if (!piece.isCaptured())
				return;
		}
		Result result = m_isBlackRuleSet ? Result.BLACK_WIN : Result.WHITE_WIN;
		result.setGuiText("Game Over! " + result.winText() + "\n");
		PlayGamePanel.endOfGame(result);
	}

	private void captureAllPieces()
	{
		List<Piece> team = (m_isBlackRuleSet ? m_game.getBlackTeam() : m_game.getWhiteTeam());
		for (Piece piece : team)
		{
			if (!piece.isCaptured())
				return;
		}
		Result result = !m_isBlackRuleSet ? Result.BLACK_WIN : Result.WHITE_WIN;
		result.setGuiText("Game Over! " + result.winText() + "\n");
		PlayGamePanel.endOfGame(result);
	}

	private void checkCaptureAllOfType()
	{
		List<Piece> team = (!m_isBlackRuleSet ? m_game.getBlackTeam() : m_game.getWhiteTeam());
		for (Piece piece : team)
		{
			if (piece.getName().equals(m_pieceName) && !piece.isCaptured())
				return;
		}
		Result result = m_isBlackRuleSet ? Result.BLACK_WIN : Result.WHITE_WIN;
		result.setGuiText("Game Over! " + result.winText() + "\n");
		PlayGamePanel.endOfGame(result);
	}

	private Game m_game;
	private Move m_move;
	private int m_numberOfChecks;
	private int m_maxNumberOfChecks;
	private String m_pieceName;
	private boolean m_isBlackRuleSet;
}
