package rules;

import java.util.List;

import utility.GuiUtility;
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
					String resultText = Messages.getString("gameOverExc") + result.winText() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$

					if (mGame.getThreats(objectivePiece) != null)
					{
						resultText += Messages.getString("pieceCausedFinalCheck") //$NON-NLS-1$
								+ Messages.getString("piecePlacedWasThe") //$NON-NLS-1$
								+ mGame.getThreats(objectivePiece)[0].getName() + Messages.getString("atLocation") //$NON-NLS-1$
								+ mGame.getThreats(objectivePiece)[0].getSquare().toString(new boolean[] { false, false }) + "\n"; //$NON-NLS-1$
					}
					result.setGuiText(resultText + Messages.getString("whatWouldYouLikeToDo")); //$NON-NLS-1$
					mGame.getLastMove().setResult(result);
					if (!mGame.getHistory().contains(mGame.getLastMove()))
						mGame.getHistory().add(mGame.getLastMove());

					// let the user see the final move
					GuiUtility.getChessCrafter().getPlayGameScreen().boardRefresh(mGame.getBoards());
					GuiUtility.getChessCrafter().getPlayGameScreen().endOfGame(result);
				}
			}
			// if the King isn't threatened, then it's stalemate
			else
			{
				if (mGame.getLastMove() != null)
				{
					mGame.getLastMove().setStalemate(true);
					Result result = Result.DRAW;
					result.setGuiText(Messages.getString("drawExc") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
					mGame.getLastMove().setResult(result);
					if (!mGame.getHistory().contains(mGame.getLastMove()))
					{
						mGame.getHistory().add(mGame.getLastMove());
					}
					// let the user see the final move
					GuiUtility.getChessCrafter().getPlayGameScreen().boardRefresh(mGame.getBoards());
					GuiUtility.getChessCrafter().getPlayGameScreen().endOfGame(result);
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
				result.setGuiText(Messages.getString("gameOverExcSpace") + result.winText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
				GuiUtility.getChessCrafter().getPlayGameScreen().endOfGame(result);
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
		result.setGuiText(Messages.getString("gameOverExcSpace") + result.winText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		GuiUtility.getChessCrafter().getPlayGameScreen().endOfGame(result);
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
		result.setGuiText(Messages.getString("gameOverExcSpace") + result.winText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		GuiUtility.getChessCrafter().getPlayGameScreen().endOfGame(result);
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
		result.setGuiText(Messages.getString("gameOverExcSpace") + result.winText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		GuiUtility.getChessCrafter().getPlayGameScreen().endOfGame(result);
	}

	private Game mGame;
	private Move mMove;
	private int mNumberOfChecks;
	private int mMaxNumberOfChecks;
	private String mPieceName;
	private boolean mIsBlackRuleSet;
}
