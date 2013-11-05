
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
}
