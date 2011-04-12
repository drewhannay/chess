package logic;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Square.java
 * 
 * One Square on the Game Board.
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class Square implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -5408493670737541871L;

	/**
	 * The Piece occupying this Square
	 */
	private Piece piece;
	/**
	 * Row Index of Square
	 */
	private int row;//File
	/**
	 * Column Index of Square
	 */
	private int col;//Rank
	/**
	 * Customizable Color for this Square
	 */
	private Color background;
	/**
	 * JButton represents this Square in the GUI
	 */
	private JButton jb;
	/**
	 * If the Square is able to be occupied.
	 */
	private boolean isHabitable;

	/**
	 * Color indicating that a Square is a legal destination for moving pieces
	 */
	public static final Color HIGHLIGHT_COLOR = new Color(20, 129, 191);

	/**
	 * Constructor.
	 * Initializes Rows, Columns, and isHabitable.
	 * @param row Row index.
	 * @param col Column index.
	 */
	public Square(int row, int col) {
		this.row = row;
		this.col = col;
		isHabitable = true;
	}

	/**
	 * Getter method for index to Column
	 * @return Index to Column
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Getter method for Color of the Square
	 * @return Square Color
	 */
	public Color getColor() {
		return jb.getBackground();
	}

	/**
	 * Getter method for Piece occupying the Square
	 * @return Piece occupying the Square.
	 */
	public Piece getPiece() {
		return piece;
	}

	/**
	 * Getter method for index to Row
	 * @return Index to Row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Getter method for ability of Square to be occupied.
	 * @return If Square can be occupied.
	 */
	public boolean isHabitable() {
		return isHabitable;
	}

	/**
	 * Determines if the Square has an occupying Piece.
	 * @return If Square is occupied.
	 */
	public boolean isOccupied() {
		return (piece != null);
	}

	/**
	 * Refresh the GUI's view of this Square with the current accurate information.
	 */
	public void refresh() {
		jb.setContentAreaFilled(false);
		jb.setOpaque(true);
		if (piece != null) {//If there's a Piece here
			if (piece.getIcon() == null) {//And it has no Icon
				jb.setText(piece.getName());//Use it's name
			} else {
				jb.setIcon(piece.getIcon());//Otherwise, use it's Icon
			}
		} else {//If there's no Piece, clear the Icon and Text of the Square.
			jb.setIcon(null);
			jb.setText("");
		}
		resetColor();//Then reset the color too.
	}

	/**
	 * Reset temporary changes to the Color of the Square
	 */
	public void resetColor() {
		if (background != null) {
			//If a custom background color has been saved, use that.
			jb.setBackground(background);
			return;
		}
		jb.setBorder(null);
		//Otherwise make our normal light/dark pattern.
		if ((row % 2 != 0 && col % 2 != 0) || (row % 2 == 0 && col % 2 == 0)) {
			jb.setBackground(Color.LIGHT_GRAY);
			jb.setForeground(Color.getHSBColor(30, 70, 70));
		} else {
			jb.setBackground(Color.getHSBColor(30, 70, 70));
			jb.setForeground(Color.LIGHT_GRAY);
		}
	}

	/**
	 * Sets the JButton representing the Square.
	 * @param jb Modifying JButton.
	 */
	public void setButton(JButton jb) {
		this.jb = jb;
		refresh();//Refresh the view of the Square
	}

	/**
	 * Sets index to column
	 * @param col Index to column
	 */
	public void setCol(int col) {
		//TODO Make sure they're setting a valid coordinate
		this.col = col;
	}

	/**
	 * Sets the color of a square temporarily
	 * @param c the color to set the background
	 */
	public void setColor(Color c){
		jb.setBackground(c);
	}
	/**
	 * Sets the background Color of the Square permanently
	 * @param c New Color
	 */
	public void setBackgroundColor(Color c) {
		jb.setBackground(c);//Use the given Color for the background
		jb.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		if (c != Square.HIGHLIGHT_COLOR) {
			//If the Color is the highlight color, then change is only temporary. Don't store it.
			background = c;
		}
	}

	/**
	 * Sets if a Piece can move to Square.
	 * @param isHabitable If Square can be occupied.
	 */
	public void setHabitable(boolean isHabitable) {
		this.isHabitable = isHabitable;
	}

	/**
	 * Sets the Piece occupying the Square.
	 * @param p New occupying Piece.
	 * @return Old occupying Piece.
	 */
	public Piece setPiece(Piece p) {
		Piece oldPiece = piece;
		piece = p;
		if (piece != null) {
			piece.setSquare(this);
		}
		return oldPiece;
	}

	/**
	 * Sets index to Row
	 * @param row Index to row
	 */
	public void setRow(int row) {
		//TODO Make sure they're setting a valid coordinate
		this.row = row;
	}

	/**
	 * Get a String representation of this Square
	 * @param unique If the row and/or column of this square must be shown
	 * @return The String representation of this Square
	 */
	public String toString(boolean[] unique) {
		String files = "-abcdefgh";
		String toReturn = "";
		if (!unique[0]) {
			toReturn += files.charAt(col);
		}
		if (!unique[1]) {
			toReturn += row;
		}
		return toReturn;
	}
}
