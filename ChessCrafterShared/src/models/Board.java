
package models;

import com.google.common.base.Preconditions;

public final class Board
{
	public static final int NO_ENPASSANT = 0;

	public Board(int rowCount, int columnCount, boolean wrapsAround)
	{
		mRowCount = rowCount;
		mColumnCount = columnCount;
		mIsWrapAroundBoard = wrapsAround;

		mSquares = new Square[rowCount][columnCount];

		for (int row = 0, column = 0; row < rowCount; row++)
		{
			// add one to the row and column to ignore counting from zero
			for (column = 0; column < columnCount; column++)
				mSquares[row][column] = new Square((row + 1), (column + 1));
		}
	}

	public int getRowCount()
	{
		return mRowCount;
	}

	public int getColumnCount()
	{
		return mColumnCount;
	}

	public boolean isWrapAroundBoard()
	{
		return mIsWrapAroundBoard;
	}

	public Square getSquare(int row, int column)
	{
		Preconditions.checkArgument(isRowValid(row) && isColumnValid(column));

		// use x-1 and y-1 so we can maintain the illusion of counting from 1
		return mSquares[row - 1][column - 1];
	}

	public boolean isRowValid(int row)
	{
		return row <= getRowCount() && row > 0;
	}

	public boolean isColumnValid(int column)
	{
		return column <= getColumnCount() && column > 0;
	}

	public int getEnpassantCol()
	{
		return mEnPassantColumn;
	}

	public void setEnpassantCol(int enpassantCol)
	{
		mEnPassantColumn = enpassantCol;
	}

	private final int mRowCount;
	private final int mColumnCount;
	private final boolean mIsWrapAroundBoard;
	private final Square mSquares[][];

	private int mEnPassantColumn;
}
