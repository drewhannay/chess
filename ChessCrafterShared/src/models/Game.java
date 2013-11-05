
package models;

import java.util.List;
import models.turnkeeper.TurnKeeper;
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

	private final String mGameType;
	private final Team[] mTeams;
	private final Board[] mBoards;
	private final TurnKeeper mTurnKeeper;
	private final List<Move> mHistory;
}
