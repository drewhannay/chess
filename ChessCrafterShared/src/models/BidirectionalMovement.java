
package models;

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
		if (other instanceof BidirectionalMovement)
		{
			BidirectionalMovement otherMovement = (BidirectionalMovement) other;
			return (otherMovement.getRowDistance() == mRow && otherMovement.getColumnDistance() == mColumn)
					|| (otherMovement.getRowDistance() == mColumn && otherMovement.getColumnDistance() == mRow);
		}
		return false;
	}

	private int mRow;
	private int mColumn;
}
