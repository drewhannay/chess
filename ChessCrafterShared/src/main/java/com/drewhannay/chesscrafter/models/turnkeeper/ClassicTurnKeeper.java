
package models.turnkeeper;

import com.google.common.base.Objects;

public final class ClassicTurnKeeper extends TurnKeeper
{
	@Override
	public int getTeamIndexForNextTurn()
	{
		mCurrentTeamIndex = (mCurrentTeamIndex + 1) % 2;
		return mCurrentTeamIndex;
	}

	@Override
	public int undo()
	{
		return getTeamIndexForNextTurn();
	}

	@Override
	public boolean equals(Object other)
	{
		return other instanceof ClassicTurnKeeper && Objects.equal(mCurrentTeamIndex, ((ClassicTurnKeeper) other).mCurrentTeamIndex);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(mCurrentTeamIndex);
	}
}
