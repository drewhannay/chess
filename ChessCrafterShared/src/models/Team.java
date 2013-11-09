
package models;

import java.util.List;
import rules.Rules;
import timer.ChessTimer;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public final class Team
{
	public Team(Rules rules, List<Piece> pieces)
	{
		mRules = rules;
		mPieces = pieces;
		mCapturedPieces = Lists.newArrayList();
	}

	public void setTimer(ChessTimer timer)
	{
		mTimer = timer;
	}

	public ChessTimer getTimer()
	{
		return mTimer;
	}

	public Rules getRules()
	{
		return mRules;
	}

	public List<Piece> getPieces()
	{
		return mPieces;
	}

	/**
	 * @return A List of Piece objects that belong to this Team but have been captured by another Team
	 */
	public List<Piece> getCapturedPieces()
	{
		return mCapturedPieces;
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Team))
			return false;

		Team otherTeam = (Team) other;

		return Objects.equal(mRules, otherTeam.mRules)
				&& Objects.equal(mPieces, otherTeam.mPieces)
				&& Objects.equal(mTimer, otherTeam.mTimer)
				&& Objects.equal(mCapturedPieces, otherTeam.mCapturedPieces);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(mRules, mPieces, mTimer, mCapturedPieces);
	}

	private final Rules mRules;
	private final List<Piece> mPieces;
	private final List<Piece> mCapturedPieces;

	private ChessTimer mTimer;
}
