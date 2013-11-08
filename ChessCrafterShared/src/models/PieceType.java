
package models;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class PieceType
{
	// TODO: this should eventually be removed, when our variant creation is good enough to create pawns
	public static final String PAWN_NAME = "Pawn";

	public PieceType(String name, PieceMovements pieceMovements, boolean isLeaper)
	{
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
		Preconditions.checkArgument(pieceMovements != null);

		mName = name;
		mPieceMovements = pieceMovements;
		mIsLeaper = isLeaper;
	}

	/**
	 * NOTE: this value should NEVER be displayed to the user!
	 * Clients are responsible for maintaining a mapping between PieceTypes and user-facing names
	 * 
	 * @return a unique identifier for this PieceType
	 */
	public String getName()
	{
		return mName;
	}

	public PieceMovements getPieceMovements()
	{
		return mPieceMovements;
	}

	public boolean isLeaper()
	{
		return mIsLeaper;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof PieceType)
		{
			PieceType otherPieceType = (PieceType) other;
			boolean equal = Objects.equal(mName, otherPieceType.mName);
			if (equal)
			{
				// do not allow PieceTypes with the same name but different
				// movement attributes
				Preconditions.checkState(Objects.equal(mPieceMovements, otherPieceType.mPieceMovements));
				Preconditions.checkState(mIsLeaper == otherPieceType.mIsLeaper);
			}

			return equal;
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	private final String mName;
	private final PieceMovements mPieceMovements;
	private final boolean mIsLeaper;
}
