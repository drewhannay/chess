package gui;

import java.io.Serializable;

/**
 * @author Cheney Hester, Yelemis, Drew, and Alisa
 * This helps to make the Jail which is where pieces are placed when they are removed
 * from the board being "taken". This could be used for variants where pieces can be
 * replaced back onto the board from this Jail.
 *
 */
public class Jail implements Serializable {

	/**
	 * For Serialization.
	 */
	private static final long serialVersionUID = 5949793107459893392L;

	/**
	 * Number of columns on the board.
	 */
	public int maxCol;
	/**
	 * Number of rows on the board.
	 */
	protected int maxRow;
	/**
	 * Two dimensional Array of Squares representing the Board
	 */
	protected JailSquare squares[][];

	/**
	 * Constructor.
	 * Initializes two dimensional Array of Squares
	 * @param rows Number of rows
	 * @param cols Number of columns
	 */
	public Jail(int rows, int cols) {
		maxRow = rows;
		maxCol = cols;
		squares = new JailSquare[rows][cols];

		for (int row = 0, col = 0; row < rows; row++) {
			for (col = 0; col < cols; col++) {
				//Initialize the Squares. Add one to the row and col to ignore counting from zero.
				squares[row][col] = new JailSquare((row + 1), (col + 1));
			}
		}
	}

	/**
	 * @return the Largest row of the board. (The largest because there could be two boards.)
	 */
	public int getMaxRow() {
		return maxRow;
	}

	/**
	 * Getter method for Squares at specified position.
	 * @param x Row number
	 * @param y Column number
	 * @return The Square at specified position.
	 */
	public JailSquare getSquare(int x, int y) {
		//Use x-1 and y-1 so that we can maintain the illusion of counting from 1.
		return squares[x - 1][y - 1];
	}

	/**
	 * Getter method for number of Columns
	 * @return Number of Columns
	 */
	public int numCols() {
		return maxCol;
	}

	/**
	 * Getter method for number of Rows
	 * @return Number of Rows
	 */
	public int numRows() {
		return maxRow;
	}

}
