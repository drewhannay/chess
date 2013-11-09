
package models;

public final class ChessCoordinates
{
	public ChessCoordinates(int row, int column, int boardIndex)
	{
		this.row = row;
		this.column = column;
		this.boardIndex = boardIndex;
	}

	public final int row;
	public final int column;
	public final int boardIndex;
	
	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof ChessCoordinates))
			return false;
		ChessCoordinates otherCoordinates = (ChessCoordinates) other;
		
		return otherCoordinates.boardIndex == boardIndex && otherCoordinates.row == row && otherCoordinates.column == column;
	}
	
	@Override
	public String toString()
	{
		return row+" "+column+" "+boardIndex;
	}
	
}
