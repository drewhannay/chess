
package com.drewhannay.chesscrafter.models;

import com.google.common.base.Objects;

public class BidirectionalMovement
{
	public BidirectionalMovement(int row, int column)
	{
		mRow = row;
		mColumn = column;
	}

	public int getRowDistance()
	{
		return mRow;
	}

	public int getColumnDistance()
	{
		return mColumn;
	}

	@Override
	public String toString()
	{
		return mRow + " x " + mColumn; //$NON-NLS-1$
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof BidirectionalMovement))
			return false;

		BidirectionalMovement otherMovement = (BidirectionalMovement) other;

		return (Objects.equal(mRow, otherMovement.mRow)
				&& Objects.equal(mColumn, otherMovement.mColumn))
				|| (Objects.equal(mRow, otherMovement.mColumn)
				&& Objects.equal(mColumn, otherMovement.mRow));
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(mRow, mColumn);
	}

	private int mRow;
	private int mColumn;
}
