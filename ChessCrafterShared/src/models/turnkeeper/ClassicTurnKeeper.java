
package models.turnkeeper;

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
}
