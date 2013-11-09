
package models;

import java.util.List;
import rules.Rules;
import timer.ChessTimer;
import com.google.common.base.Objects;

public final class Team
{
	public Team(Rules rules, List<Piece> pieces)
	{
		mRules = rules;
		mPieces = pieces;
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

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Team))
			return false;

		Team otherTeam = (Team) other;

		return Objects.equal(mRules, otherTeam.mRules)
				&& Objects.equal(mPieces, otherTeam.mPieces)
				&& Objects.equal(mTimer, otherTeam.mTimer);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(mRules, mPieces, mTimer);
	}

	private final Rules mRules;
	private final List<Piece> mPieces;

	private ChessTimer mTimer;
}
