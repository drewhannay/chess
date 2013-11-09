
package models;

import java.util.Arrays;
import java.util.List;
import models.turnkeeper.TurnKeeper;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public final class Game
{
	public Game(String gameType, Board[] boards, Team[] teams, TurnKeeper turnKeeper)
	{
		mGameType = gameType;
		mBoards = boards;
		mTeams = teams;
		mTurnKeeper = turnKeeper;

		mHistory = Lists.newArrayList();
	}

	public String getGameType()
	{
		return mGameType;
	}

	public Board[] getBoards()
	{
		return mBoards;
	}

	public Team[] getTeams()
	{
		return mTeams;
	}

	public TurnKeeper getTurnKeeper()
	{
		return mTurnKeeper;
	}

	public List<Move> getHistory()
	{
		return mHistory;
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Game))
			return false;

		Game otherGame = (Game) other;

		return Objects.equal(mGameType, otherGame.mGameType)
				&& Arrays.equals(mTeams, otherGame.mTeams)
				&& Arrays.equals(mBoards, otherGame.mBoards)
				&& Objects.equal(mTurnKeeper, otherGame.mTurnKeeper)
				&& Objects.equal(mHistory, otherGame.mHistory);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(mGameType, mTeams, mBoards, mTurnKeeper, mHistory);
	}

	private final String mGameType;
	private final Team[] mTeams;
	private final Board[] mBoards;
	private final TurnKeeper mTurnKeeper;
	private final List<Move> mHistory;
}
