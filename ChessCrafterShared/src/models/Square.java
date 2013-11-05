
package models;

import com.google.common.base.Objects;

public final class Square
{
	public Square(int row, int column)
	{
		mRow = row;
		mColumn = column;
		mIsHabitable = true;
	}

	public int getCol()
	{
		return mColumn;
	}

	public int getRow()
	{
		return mRow;
	}

	public boolean isHabitable()
	{
		return mIsHabitable;
	}

	public void setIsHabitable(boolean isHabitable)
	{
		mIsHabitable = isHabitable;
	}

	@Override
	public String toString()
	{
		return toString(new boolean[] { false, false });
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Square))
			return false;

		Square otherSquare = (Square) other;
		return otherSquare.mRow == mRow && otherSquare.mColumn == mColumn;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(mRow, mColumn);
	}

	/**
	 * Get a String representation of this Square
	 * 
	 * @param unique
	 *            If the row and/or column of this square must be shown
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

	private final int mRow;
	private final int mColumn;

	private boolean mIsHabitable;
}
