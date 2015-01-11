
package models.turnkeeper;

import com.google.common.base.Objects;

public final class DifferentTurnCountTurnKeeper extends TurnKeeper
{
	@Override
	public int getTeamIndexForNextTurn()
	{
		// TODO Auto-generated method stub
		// if (++mCurrentNumberOfMovesMade >= (mIsBlackMove ?
		// mNumberOfBlackMovesBeforeTurnChange :
		// mNumberOfWhiteMovesBeforeTurnChange))
		// {
		// mIsBlackMove = !mIsBlackMove;
		// GuiUtility.getChessCrafter().getPlayGameScreen(null).turn(mIsBlackMove);
		//
		// mCurrentNumberOfMovesMade = 0;
		// }
		// return mIsBlackMove;
		return 0;
	}

	@Override
	public int undo()
	{
		// TODO Auto-generated method stub
		// if (--mCurrentNumberOfMovesMade < 0)
		// {
		// mIsBlackMove = !mIsBlackMove;
		// GuiUtility.getChessCrafter().getPlayGameScreen(null).turn(mIsBlackMove);
		//
		// mCurrentNumberOfMovesMade = mIsBlackMove ?
		// mNumberOfBlackMovesBeforeTurnChange :
		// mNumberOfWhiteMovesBeforeTurnChange;
		// }
		// return mIsBlackMove;
		return 0;
	}

	@Override
	public boolean equals(Object other)
	{
		return other instanceof DifferentTurnCountTurnKeeper && Objects.equal(mCurrentTeamIndex, ((DifferentTurnCountTurnKeeper) other).mCurrentTeamIndex);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(mCurrentTeamIndex);
	}
}
