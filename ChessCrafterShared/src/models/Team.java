
package models;

import java.util.List;
import rules.Rules;
import timer.ChessTimer;

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

	private final Rules mRules;
	private final List<Piece> mPieces;

	private ChessTimer mTimer;
}
