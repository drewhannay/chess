package logic;

import java.io.Serializable;
import java.util.List;

public class Board implements Serializable
{
	public static final int NO_ENPASSANT = 0;

	public Board(int numRows, int numColumns, boolean wrapsAround)
	{
		setMaxRow(numRows);
		setMaxCol(numColumns);
		mWrapsAround = wrapsAround;
		mSquares = new Square[numRows][numColumns];

		for (int row = 0, column = 0; row < numRows; row++)
		{
			// Initialize the Squares. Add one to the row and column to ignore
			// counting from zero
			for (column = 0; column < numColumns; column++)
				mSquares[row][column] = new Square((row + 1), (column + 1));
		}
	}

	public int getEnpassantCol()
	{
		return mEnpassantColumn;
	}

	public Game getGame()
	{
		return mGame;
	}

	public int getMaxCol()
	{
		return mMaxColumn;
	}

	public int getMaxRow()
	{
		return mMaxRow;
	}

	/**
	 * Given a destination Square, return which piece could move there Index
	 * through the ArrayList containing the currently moving team and find the
	 * piece which is able to move to the destination square
	 * 
	 * @param pieceKlass The class of the Piece that is moving
	 * @param origCol The origin column of the Piece that is moving
	 * @param origRow The origin row of the Piece that is moving
	 * @param dest The Square where the Piece is moving to
	 * @return The origin Square of the Piece that is moving
	 */
	public Square getOriginSquare(String pieceKlass, int origCol, int origRow, Square dest)
	{
		// This method should never be called for anything but Classic chess
		if (!getGame().isClassicChess())
			return null;

		if (origRow > getMaxRow() || origCol > getMaxCol())
			return null;

		List<Piece> movingTeam = (getGame().isBlackMove()) ? getGame().getBlackTeam() : getGame().getWhiteTeam();

		Piece p = null;
		Piece mover = null;

		for (int i = 0; i < movingTeam.size(); i++)
		{
			p = movingTeam.get(i);
			if (p.genLegalDests(this) > 0)
			{
				if ((p.getName().equals(pieceKlass)) && p.isLegalDest(dest))
				{
					if ((origCol < 1 && origRow < 1) || // If the origCol and
														// origRow were both
														// zero
							(origCol < 1 && p.getSquare().getRow() == origRow) || // If
																					// just
																					// the
																					// origCol
																					// was
																					// zero
							(origRow < 1 && p.getSquare().getCol() == origCol)) // If
																				// just
																				// the
																				// origRow
																				// was
																				// zero
					{
						mover = p;
						break;
					}
				}
			}
		}
		if (mover == null)
			return null;

		return mover.getSquare();
	}

	public Square getSquare(int row, int col)
	{
		// Use x-1 and y-1 so that we can maintain the illusion of counting from
		// 1
		return mSquares[row - 1][col - 1];
	}

	public boolean isBlackTurn()
	{
		return getGame().isBlackMove();
	}

	/**
	 * @param dest The Square on which to check uniqueness
	 * @param p The Piece type which is trying to move to the Square
	 * @return An array indicating uniqueness for Row and Column
	 */
	protected boolean[] isDestUniqueForClass(Square dest, Piece p)
	{
		boolean[] unique = { true, true }; // Row, Column
		List<Piece> movingTeam = (getGame().isBlackMove()) ? getGame().getBlackTeam() : getGame().getWhiteTeam();
		List<Square> dests = null;
		Piece piece = null;

		if (p.getName().equals(Messages.getString("Board.king"))) //$NON-NLS-1$
			return unique;

		for (int i = 0; i < movingTeam.size(); i++)
		{
			piece = movingTeam.get(i);

			if (piece != p && !piece.isCaptured() && piece.getClass() == p.getClass())
			{
				dests = piece.getLegalDests();

				if (dests.contains(dest))
				{
					if (p.getSquare().getRow() == piece.getSquare().getRow())
						unique[0] = false; // Row is not unique
					if (p.getSquare().getCol() == piece.getSquare().getCol())
						unique[1] = false; // Column is not unique

					// Special case of knights and rooks, etc.
					if (unique[0] == true && unique[1] == true)
						unique[1] = false;
				}
			}
		}
		return unique;
	}

	/**
	 * Determine if a Move is legal to be executed
	 * 
	 * @param move The Move on which to check legality
	 * @return Whether or not the Move is legal
	 * @throws Exception Throws if there was an illegal move, or failure to
	 * undo.
	 */
	public boolean isLegalMove(Move move) throws Exception
	{
		if (move == null)
			return false;

		move.execute();
		move.undo();

		return true;
	}

	public boolean isRowValid(int row)
	{
		return row <= getMaxRow() && row > 0;
	}

	public boolean isColValid(int column)
	{
		return column <= getMaxCol() && column > 0;
	}

	public boolean isWrapAround()
	{
		return mWrapsAround;
	}

	public int numCols()
	{
		return getMaxCol();
	}

	public int numRows()
	{
		return getMaxRow();
	}

	public void setEnpassantCol(int enpassantCol)
	{
		mEnpassantColumn = enpassantCol;
	}

	public void setGame(Game g)
	{
		mGame = g;
	}

	public void setMaxCol(int maxCol)
	{
		mMaxColumn = maxCol;
	}

	public void setMaxRow(int maxRow)
	{
		mMaxRow = maxRow;
	}

	public Board makeCopyWithWrapSelection(boolean wrapAround)
	{
		Board toReturn = this;
		mWrapsAround = wrapAround;
		return toReturn;
	}

	private static final long serialVersionUID = -3660560968400318452L;

	private Game mGame;
	protected Square mSquares[][];
	private int mMaxRow;
	private int mMaxColumn;
	private int mEnpassantColumn = NO_ENPASSANT;
	private boolean mWrapsAround;
}
