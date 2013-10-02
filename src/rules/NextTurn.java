package rules;

import java.io.Serializable;

import gui.PlayGamePanel;

public class NextTurn implements Serializable {
	
	public enum NextTurnOption {
		CLASSIC, INCREASING_TOGETHER, INCREASING_SEPARATELY, DIFFERENT_NUMBER_OF_TURNS
	};

	/**
	 * 
	 */
	private static final long serialVersionUID = 4504947872189771271L;

	public NextTurn(NextTurnOption option, int whiteMoves, int blackMoves, int increment) {
		mNumberOfWhiteMovesBeforeTurnChange = whiteMoves;
		mNumberOfBlackMovesBeforeTurnChange = blackMoves;
		mTurnIncrement = increment;
		mCurrentNumberOfMovesMade = 0;
		mIsBlackMove = false;
		mNextTurnOption = option;
	}

	public boolean getNextTurn() {
		switch (mNextTurnOption) {
		case CLASSIC:
			return classicNextTurn();
		case INCREASING_TOGETHER:
			return increasingTurnsTogether();
		case INCREASING_SEPARATELY:
			return increasingTurnsSeparately();
		case DIFFERENT_NUMBER_OF_TURNS:
			return differentNumberOfTurns();
		default:
			return false;
		}
	}

	public boolean undo() {
		switch (mNextTurnOption) {
		case CLASSIC:
			return undoClassic();
		case INCREASING_TOGETHER:
			return undoIncreasingTurnsTogether();
		case INCREASING_SEPARATELY:
			return undoIncreasingTurnsSeparately();
		case DIFFERENT_NUMBER_OF_TURNS:
			return undoDifferentNumberOfTurns();
		default:
			return false;
		}
	}

	public int getWhiteMoves()
	{
		return mNumberOfWhiteMovesBeforeTurnChange;
	}

	public int getBlackMoves()
	{
		return mNumberOfBlackMovesBeforeTurnChange;
	}

	public int getIncrement()
	{
		return mTurnIncrement;
	}

	private boolean classicNextTurn()
	{
		mIsBlackMove = !mIsBlackMove;
		PlayGamePanel.turn(mIsBlackMove);

		return mIsBlackMove;
	}

	private boolean undoClassic()
	{
		mIsBlackMove = !mIsBlackMove;
		PlayGamePanel.turn(mIsBlackMove);

		return mIsBlackMove;
	}

	private boolean increasingTurnsTogether()
	{
		if (++mCurrentNumberOfMovesMade >= mNumberOfWhiteMovesBeforeTurnChange)
		{
			mIsBlackMove = !mIsBlackMove;
			PlayGamePanel.turn(mIsBlackMove);
			mNumberOfWhiteMovesBeforeTurnChange += mTurnIncrement;
			mCurrentNumberOfMovesMade = 0;
		}
		return mIsBlackMove;
	}

	private boolean undoIncreasingTurnsTogether()
	{
		if (--mCurrentNumberOfMovesMade < 0)
		{
			mIsBlackMove = !mIsBlackMove;
			PlayGamePanel.turn(mIsBlackMove);
			mNumberOfWhiteMovesBeforeTurnChange -= mTurnIncrement;
			mCurrentNumberOfMovesMade = mNumberOfWhiteMovesBeforeTurnChange - 1;
		}
		return mIsBlackMove;
	}

	private boolean increasingTurnsSeparately()
	{
		if (++mCurrentNumberOfMovesMade >= (mIsBlackMove ? mNumberOfBlackMovesBeforeTurnChange
				: mNumberOfWhiteMovesBeforeTurnChange))
		{
			mIsBlackMove = !mIsBlackMove;
			PlayGamePanel.turn(mIsBlackMove);
			mNumberOfBlackMovesBeforeTurnChange += mTurnIncrement;
			mNumberOfWhiteMovesBeforeTurnChange += mTurnIncrement;
			mCurrentNumberOfMovesMade = 0;
		}
		return mIsBlackMove;
	}

	private boolean undoIncreasingTurnsSeparately()
	{
		if (--mCurrentNumberOfMovesMade < 0)
		{
			mIsBlackMove = !mIsBlackMove;
			mNumberOfBlackMovesBeforeTurnChange -= mTurnIncrement;
			mNumberOfWhiteMovesBeforeTurnChange -= mTurnIncrement;
			PlayGamePanel.turn(mIsBlackMove);

			mCurrentNumberOfMovesMade = mIsBlackMove ? mNumberOfBlackMovesBeforeTurnChange
					: mNumberOfWhiteMovesBeforeTurnChange;
		}
		return mIsBlackMove;
	}

	private boolean differentNumberOfTurns()
	{
		if (++mCurrentNumberOfMovesMade >= (mIsBlackMove ? mNumberOfBlackMovesBeforeTurnChange
				: mNumberOfWhiteMovesBeforeTurnChange))
		{
			mIsBlackMove = !mIsBlackMove;
			PlayGamePanel.turn(mIsBlackMove);

			mCurrentNumberOfMovesMade = 0;
		}
		return mIsBlackMove;
	}

	private boolean undoDifferentNumberOfTurns()
	{
		if (--mCurrentNumberOfMovesMade < 0)
		{
			mIsBlackMove = !mIsBlackMove;
			PlayGamePanel.turn(mIsBlackMove);

			mCurrentNumberOfMovesMade = mIsBlackMove ? mNumberOfBlackMovesBeforeTurnChange
					: mNumberOfWhiteMovesBeforeTurnChange;
		}
		return mIsBlackMove;
	}

	private NextTurnOption mNextTurnOption;
	private int mNumberOfWhiteMovesBeforeTurnChange;
	private int mNumberOfBlackMovesBeforeTurnChange;
	private int mCurrentNumberOfMovesMade;
	private int mTurnIncrement;
	private boolean mIsBlackMove;
}
