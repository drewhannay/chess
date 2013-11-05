
package models;

import com.google.common.base.Objects;

public final class Piece
{
	public Piece(long id, PieceType pieceType, ChessCoordinates coordinates)
	{
		mId = id;
		mPieceType = pieceType;
		mOriginalCoordinates = coordinates;

		mCoordinates = coordinates;
	}

	public long getId()
	{
		return mId;
	}

	public PieceType getPieceType()
	{
		return mPieceType;
	}

	public ChessCoordinates getOriginalCoordinates()
	{
		return mOriginalCoordinates;
	}

	public ChessCoordinates getCoordinates()
	{
		return mCoordinates;
	}

	public boolean isCaptured()
	{
		return mIsCaptured;
	}

	public void setIsCaptured(boolean isCaptured)
	{
		mIsCaptured = isCaptured;
	}

	public int getMoveCount()
	{
		return mMoveCount;
	}

	@Override
	public boolean equals(Object other)
	{
		return other instanceof Piece && ((Piece) other).mId == mId;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(mId, mPieceType, mOriginalCoordinates);
	}

	private final long mId;
	private final PieceType mPieceType;
	private final ChessCoordinates mOriginalCoordinates;

	private boolean mIsCaptured;
	private int mMoveCount;
	private ChessCoordinates mCoordinates;
}
