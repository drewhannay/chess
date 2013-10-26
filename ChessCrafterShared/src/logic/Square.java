package logic;

import java.io.Serializable;

public class Square implements Serializable
{
	// TODO: future refactorings will hopefully render this interface
	// unnecessary
	public interface SquareStateListener
	{
		public void onSetIsThreatSquare();

		public void onStateChanged();

		// TODO: this method definitely shouldn't be necessary
		public void onJailStateChanged();
	}

	public Square(int row, int column)
	{
		mRow = row;
		mColumn = column;
		mIsHabitable = true;
	}

	public void setSquareStateListener(SquareStateListener listener)
	{
		mListener = listener;
	}

	public void setIsThreatSquare(boolean isThreatSquare)
	{
		if (mListener != null)
			mListener.onSetIsThreatSquare();
	}

	public void setStateChanged()
	{
		if (mListener != null)
			mListener.onStateChanged();
	}

	public void setJailStateChanged()
	{
		if (mListener != null)
			mListener.onJailStateChanged();
	}

	public int getCol()
	{
		return mColumn;
	}

	public Piece getPiece()
	{
		return mPiece;
	}

	public int getRow()
	{
		return mRow;
	}

	public boolean isHabitable()
	{
		return mIsHabitable;
	}

	public boolean isOccupied()
	{
		return (mPiece != null);
	}

	public void setIsHabitable(boolean isHabitable)
	{
		mIsHabitable = isHabitable;
	}

	/**
	 * Sets the Piece occupying the Square.
	 * 
	 * @param p New occupying Piece.
	 * @return Old occupying Piece.
	 */
	public Piece setPiece(Piece p)
	{
		Piece oldPiece = mPiece;
		mPiece = p;
		if (mPiece != null)
		{
			mPiece.setSquare(this);
		}
		return oldPiece;
	}

	public void setCol(int col)
	{
		// TODO Make sure they're setting a valid coordinate
		mColumn = col;
	}

	public void setRow(int row)
	{
		// TODO Make sure they're setting a valid coordinate
		mRow = row;
	}

	@Override
	public String toString()
	{
		return toString(new boolean[] { false, false });
	}

	/**
	 * Get a String representation of this Square
	 * 
	 * @param unique If the row and/or column of this square must be shown
	 * @return The String representation of this Square
	 */
	public String toString(boolean[] unique)
	{
		String files = "-abcdefgh"; //$NON-NLS-1$
		String toReturn = ""; //$NON-NLS-1$

		if (!unique[0])
			toReturn += files.charAt(mColumn);
		if (!unique[1])
			toReturn += mRow;

		return toReturn;
	}

	private static final long serialVersionUID = -5408493670737541871L;

	private Piece mPiece;
	private int mRow;// File
	private int mColumn;// Rank
	private boolean mIsHabitable;

	private SquareStateListener mListener;
}
