
package models.turnkeeper;

public final class IncreasingTogetherTurnKeeper extends TurnKeeper
{
	@Override
	public int getTeamIndexForNextTurn()
	{
		// TODO: implement this
		// if (++mCurrentNumberOfMovesMade >=
		// mNumberOfWhiteMovesBeforeTurnChange)
		// {
		// mIsBlackMove = !mIsBlackMove;
		// GuiUtility.getChessCrafter().getPlayGameScreen(null).turn(mIsBlackMove);
		// mNumberOfWhiteMovesBeforeTurnChange += mTurnIncrement;
		// mCurrentNumberOfMovesMade = 0;
		// }
		// return mIsBlackMove;
		return 0;
	}

	@Override
	public int undo()
	{
		// TODO: implement this;
		// if (--mCurrentNumberOfMovesMade < 0)
		// {
		// mIsBlackMove = !mIsBlackMove;
		// GuiUtility.getChessCrafter().getPlayGameScreen(null).turn(mIsBlackMove);
		// mNumberOfWhiteMovesBeforeTurnChange -= mTurnIncrement;
		// mCurrentNumberOfMovesMade = mNumberOfWhiteMovesBeforeTurnChange - 1;
		// }
		// return mIsBlackMove;
		return 0;
	}
}
