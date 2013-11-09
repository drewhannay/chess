
package models;

import java.util.Arrays;
import java.util.List;
import models.turnkeeper.TurnKeeper;
import timer.ChessTimer;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
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

	/**
	 * Calling this method signifies that this Game object no longer represents a variant template,
	 * but rather an in-progress Game
	 * 
	 * @param timers
	 *            An array of ChessTimer objects to be used for this instance of the Game
	 *            where the indexes correspond to indexes into the Team array
	 */
	public void startGame(ChessTimer[] timers)
	{
		Preconditions.checkArgument(timers != null);
		for (ChessTimer timer : timers)
			Preconditions.checkArgument(timer != null);

		mTimers = timers;
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

	public ChessTimer[] getTimers()
	{
		return mTimers;
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

	private ChessTimer[] mTimers;
}
