package logic;

import java.io.Serializable;
import java.util.List;

/**
 * Board.java Logical representation of the Chess Board.
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas CSCI 335, Wheaton College,
 *         Spring 2011 Phase 2 April 7, 2011
 */

public class Board implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -3660560968400318452L;
	/**
	 * Two dimensional Array of Squares representing the Board
	 */
	protected Square squares[][];

	/**
	 * Number of columns on the board.
	 */
	private int maxCol;
	/**
	 * Number of rows on the board.
	 */
	private int maxRow;
	/**
	 * If movement beyond the East and West edges of the board results in
	 * further relocation.
	 */
	private boolean wraparound;
	/**
	 * Enpassant is not currently possible
	 */
	public static final int NO_ENPASSANT = 0;
	/**
	 * The row which enpassant will be possible, if possible at all.
	 */
	protected int enpassantCol = NO_ENPASSANT;

	/**
	 * The Game that includes this Board
	 */
	private Game g;

	/**
	 * Constructor. Initializes two dimensional Array of Squares
	 * @param rows Number of rows
	 * @param cols Number of columns
	 * @param wraparound If board wraps around.
	 */
	public Board(int rows, int cols, boolean wraparound) {
		setMaxRow(rows);
		setMaxCol(cols);
		this.wraparound = wraparound;
		squares = new Square[rows][cols];

		for (int row = 0, col = 0; row < rows; row++) {
			for (col = 0; col < cols; col++) {
				// Initialize the Squares. Add one to the row and column to
				// ignore counting from zero.
				squares[row][col] = new Square((row + 1), (col + 1));
			}
		}
	}

	/**
	 * Getter method for enpassant column.
	 * @return The enpassant column.
	 */
	public int getEnpassantCol() {
		return enpassantCol;
	}

	/**
	 * @return the g
	 */
	public Game getGame() {
		return g;
	}

	/**
	 * @return the maxCol
	 */
	public int getMaxCol() {
		return maxCol;
	}

	/**
	 * @return the maxRow
	 */
	public int getMaxRow() {
		return maxRow;
	}

	/**
	 * Given a destination Square, return which piece could move there Index
	 * through the ArrayList containing the currently moving team and find the
	 * piece which is able to move to the destination square
	 * @param pieceKlass The class of the Piece that is moving
	 * @param origCol The origin column of the Piece that is moving
	 * @param origRow The origin row of the Piece that is moving
	 * @param dest The Square where the Piece is moving to
	 * @return The origin Square of the Piece that is moving
	 */
	public Square getOriginSquare(String pieceKlass, int origCol,
			int origRow, Square dest) {

		// This method should never be called for anything but Classic chess
		if (!getGame().isClassicChess())
			return null;

		if (origRow > getMaxRow() || origCol > getMaxCol())
			return null;

		List<Piece> movingTeam = (getGame().isBlackMove()) ? getGame().getBlackTeam() : getGame().getWhiteTeam();

		Piece p = null;
		Piece mover = null;

		for (int i = 0; i < movingTeam.size(); i++) {
			p = movingTeam.get(i);
			if (p.genLegalDests(this) > 0) {
				if ((p.getName().equals(pieceKlass)) && p.isLegalDest(dest)) {
					if ((origCol < 1 && origRow < 1)
							|| // If the origCol and origRow were both zero
							(origCol < 1 && p.getSquare().getRow() == origRow)
							|| // If just the origCol was zero
							(origRow < 1 && p.getSquare().getCol() == origCol)) { // If
						// just the origRow was zero
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

	/**
	 * Getter method for Squares at specified position.
	 * @param row Row number
	 * @param col Column number
	 * @return The Square at specified position.
	 */
	public Square getSquare(int row, int col) {
		// Use x-1 and y-1 so that we can maintain the illusion of counting from
		// 1.
		return squares[row - 1][col - 1];
	}

	/**
	 * Getter method to determine move order.
	 * @return If black is next to move.
	 */
	public boolean isBlackTurn() {
		return getGame().isBlackMove();
	}

	/**
	 * Determines if specified Column is within bounds of the Array.
	 * @param y Column Number
	 * @return If Column Number is within Array bounds.
	 */
	public boolean isColValid(int y) {
		return y <= getMaxCol() && y > 0;
	}

	/**
	 * @param dest The Square on which to check uniqueness
	 * @param p The Piece type which is trying to move to the Square
	 * @return An array indicating uniqueness for Row and Column
	 */
	protected boolean[] isDestUniqueForClass(Square dest, Piece p) {
		boolean[] unique = { true, true }; // Row, Column
		List<Piece> movingTeam = (getGame().isBlackMove()) ? getGame().getBlackTeam() : getGame().getWhiteTeam();
		List<Square> dests = null;
		Piece piece = null;

		if (p.getName().equals("King"))
			return unique;

		for (int i = 0; i < movingTeam.size(); i++) {
			piece = movingTeam.get(i);

			if (piece != p && !piece.isCaptured()
					&& piece.getClass() == p.getClass()) {
				dests = piece.getLegalDests();

				if (dests.contains(dest)) {
					if (p.getSquare().getRow() == piece.getSquare().getRow()) {
						unique[0] = false; // Row is not unique
					}
					if (p.getSquare().getCol() == piece.getSquare().getCol()) {
						unique[1] = false; // Column is not unique
					}

					// Special case of knights and rooks, etc.
					if (unique[0] == true && unique[1] == true) {
						unique[1] = false;
					}
				}
			}
		}
		return unique;
	}

	/**
	 * Determine if a Move is legal to be executed
	 * @param m The Move on which to check legality
	 * @return Whether or not the Move is legal
	 */
	public boolean isLegalMove(Move m) throws Exception {
		if (m == null)
			return false;
//		try {
			m.execute();
			m.undo();
//		} catch (Exception e) {
//			return false;
//		}
		return true;
	}

	/**
	 * Determines if specified Row is within bounds of the Array.
	 * @param x Row Number
	 * @return If Row Number is within Array bounds.
	 */
	public boolean isRowValid(int x) {
		return x <= getMaxRow() && x > 0;
	}

	/**
	 * Getter method for board wraparound
	 * @return Whether or not the Board has wraparound
	 */
	public boolean isWraparound() {
		return wraparound;
	}

	/**
	 * Getter method for number of Columns
	 * @return Number of Columns
	 */
	public int numCols() {
		return getMaxCol();
	}

	/**
	 * Getter method for number of Rows
	 * @return Number of Rows
	 */
	public int numRows() {
		return getMaxRow();
	}

	/**
	 * Sets column to allow EnPassant.
	 * @param enpassantCol Column to allow EnPassant.
	 */
	public void setEnpassantCol(int enpassantCol) {
		this.enpassantCol = enpassantCol;
	}

	/**
	 * Setter method for the Game instance variable
	 * @param g The Game to be set
	 */
	public void setGame(Game g) {
		this.g = g;
	}

	/**
	 * @param maxCol the maxCol to set
	 */
	public void setMaxCol(int maxCol) {
		this.maxCol = maxCol;
	}

	/**
	 * @param maxRow the maxRow to set
	 */
	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

}
