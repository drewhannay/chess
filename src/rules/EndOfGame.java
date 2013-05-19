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
		mMaxNumberOfChecks = maxNumberOfChecks;
		mPieceName = pieceName;
		mIsBlackRuleSet = isBlackRuleSet;

		return this;
	}

	public void setGame(Game game)
	{
		mGame = game;
	}

	public String getCaptureAllPieceName()
	{
		return mPieceName;
	}

	private void classicCheckEndOfGame(Piece objectivePiece)
	{
		if (mGame.getLegalMoveCount() == 0 || objectivePiece.isCaptured())
		{
			// if the King is threatened, it's check mate.
			if (objectivePiece == null || objectivePiece.isInCheck() || objectivePiece.isCaptured())
			{
				if (mGame.getLastMove() != null)
				{
					mGame.getLastMove().setCheckmate(true);
					Result result = mGame.isBlackMove() ? Result.WHITE_WIN : Result.BLACK_WIN;
					String resultText = "Game over! " + result.winText() + "\n";

					if (mGame.getThreats(objectivePiece) != null)
					{
						resultText += "The piece(s) that caused the final check are highlighted in Red. "
								+ "\nThe piece that placed the King in check was the "
								+ mGame.getThreats(objectivePiece)[0].getName() + " at location "
								+ mGame.getThreats(objectivePiece)[0].getSquare().toString(new boolean[] { false, false }) + "\n";
					}
					result.setGuiText(resultText + "What would you like to do? \n");
					mGame.getLastMove().setResult(result);
					if (!mGame.getHistory().contains(mGame.getLastMove()))
						mGame.getHistory().add(mGame.getLastMove());

					// let the user see the final move
					PlayGamePanel.boardRefresh(mGame.getBoards());
					PlayGamePanel.endOfGame(result);
				}
			}
			// if the King isn't threatened, then it's stalemate
			else
			{
				if (mGame.getLastMove() != null)
				{
					mGame.getLastMove().setStalemate(true);
					Result result = Result.DRAW;
					result.setGuiText("Draw! " + "\n");
					mGame.getLastMove().setResult(result);
					if (!mGame.getHistory().contains(mGame.getLastMove()))
					{
						mGame.getHistory().add(mGame.getLastMove());
					}
					// let the user see the final move
					PlayGamePanel.boardRefresh(mGame.getBoards());
					PlayGamePanel.endOfGame(result);
				}
			}
		}
	}

	private void checkForCheckNTimes()
	{
		if (mGame.getLastMove() != null && mGame.getLastMove().isVerified() && mGame.getLastMove().isCheck()
				&& mGame.getLastMove().getPiece().isBlack() == mIsBlackRuleSet)
		{
			if (++mNumberOfChecks == mMaxNumberOfChecks)
			{
				Result result = !mIsBlackRuleSet ? Result.WHITE_WIN : Result.BLACK_WIN;
				result.setGuiText("Game Over! " + result.winText() + "\n");
				PlayGamePanel.endOfGame(result);
			}
		}
		mMove = mGame.getLastMove();
	}

	private void undoCheckForCheckNTimes()
	{
		if (mMove != null && mMove.isVerified() && mMove.isCheck())
			mNumberOfChecks--;
	}

	private void checkLoseAllPieces()
	{
		List<Piece> team = (!mIsBlackRuleSet ? mGame.getBlackTeam() : mGame.getWhiteTeam());
		for (Piece piece : team)
		{
			if (!piece.isCaptured())
				return;
		}
		Result result = mIsBlackRuleSet ? Result.BLACK_WIN : Result.WHITE_WIN;
		result.setGuiText("Game Over! " + result.winText() + "\n");
		PlayGamePanel.endOfGame(result);
	}

	private void captureAllPieces()
	{
		List<Piece> team = (mIsBlackRuleSet ? mGame.getBlackTeam() : mGame.getWhiteTeam());
		for (Piece piece : team)
		{
			if (!piece.isCaptured())
				return;
		}
		Result result = !mIsBlackRuleSet ? Result.BLACK_WIN : Result.WHITE_WIN;
		result.setGuiText("Game Over! " + result.winText() + "\n");
		PlayGamePanel.endOfGame(result);
	}

	private void checkCaptureAllOfType()
	{
		List<Piece> team = (!mIsBlackRuleSet ? mGame.getBlackTeam() : mGame.getWhiteTeam());
		for (Piece piece : team)
		{
			if (piece.getName().equals(mPieceName) && !piece.isCaptured())
				return;
		}
		Result result = mIsBlackRuleSet ? Result.BLACK_WIN : Result.WHITE_WIN;
		result.setGuiText("Game Over! " + result.winText() + "\n");
		PlayGamePanel.endOfGame(result);
	}

	private Game mGame;
	private Move mMove;
	private int mNumberOfChecks;
	private int mMaxNumberOfChecks;
	private String mPieceName;
	private boolean mIsBlackRuleSet;
}
