
package models;

import java.util.List;
import rules.Rules;
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

	public Rules getRules()
	{
		return mRules;
	}

	public List<Piece> getPieces()
	{
		return mPieces;
	}

	/**
	 * @return A List of Piece objects that belong to this Team but have been
	 *         captured by another Team
	 */
	public List<Piece> getCapturedPieces()
	{
		return mCapturedPieces;
	}

	public void markPieceAsCaptured(Piece piece)
	{
		mPieces.remove(piece);
		if (!mCapturedPieces.contains(piece))
			mCapturedPieces.add(piece);
	}

	public void markPieceAsNotCaptured(Piece piece)
	{
		mCapturedPieces.remove(piece);
		if (!mPieces.contains(piece))
			mPieces.add(piece);
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Team))
			return false;

		Team otherTeam = (Team) other;

		return Objects.equal(mRules, otherTeam.mRules) && Objects.equal(mPieces, otherTeam.mPieces)
				&& Objects.equal(mCapturedPieces, otherTeam.mCapturedPieces);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(mRules, mPieces, mCapturedPieces);
	}

	private final Rules mRules;
	private final List<Piece> mPieces;
	private final List<Piece> mCapturedPieces;
}
