
package models;

import com.google.common.base.Objects;

public final class ChessCoordinates
{
	public ChessCoordinates(int row, int column, int boardIndex)
	{
		this.row = row;
		this.column = column;
		this.boardIndex = boardIndex;
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof ChessCoordinates))
			return false;

		ChessCoordinates otherCoordinates = (ChessCoordinates) other;

		return Objects.equal(row, otherCoordinates.row)
				&& Objects.equal(column, otherCoordinates.column)
				&& Objects.equal(boardIndex, otherCoordinates.boardIndex);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(row, column, boardIndex);
	}

	public final int row;
	public final int column;
	public final int boardIndex;
	
	@Override
	public String toString()
	{
		return row+" "+column+" "+boardIndex;  //$NON-NLS-1$//$NON-NLS-2$
	}
	
}
