package logic;

import java.io.Serializable;

/**
 * Result.java
 * 
 * Class to model the end result of a chess game.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class Result implements Serializable{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -6844368741916902616L;
	/**
	 * Indicate an undecided result
	 */
	public static final int UNDECIDED = 0;
	/**
	 * Indicate a draw
	 */
	public static final int DRAW = 1;
	/**
	 * Indicate a white win
	 */
	public static final int WHITE_WIN = 2;
	/**
	 * Indicate a black win
	 */
	public static final int BLACK_WIN = 3;

	/**
	 * Indicate the choice for this Result
	 */
	protected int choice = UNDECIDED;
	/**
	 * The text to print for this Result
	 */
	protected String text = "";

	/**
	 * Constructor
	 * @param i The choice for this Result
	 */
	public Result(int i) {
		choice = i;
	}

	/**
	 * Check if this game is a black win
	 * @return If this game is a black win
	 */
	public boolean isBlackWin() {
		return choice == BLACK_WIN;
	}

	/**
	 * Check if this game is a draw
	 * @return If this game is a draw
	 */
	public boolean isDraw() {
		return choice == DRAW;
	}

	/**
	 * Check if this game is undecided
	 * @return If this game is undecided
	 */
	public boolean isUndecided() {
		return choice == UNDECIDED;
	}

	/**
	 * Check if this game is a white win
	 * @return If this game is a white win
	 */
	public boolean isWhiteWin() {
		return choice == WHITE_WIN;
	}


	public String winText() {
		return choice == WHITE_WIN ? "White won!" : "Black won!";
	}
	
	/**
	 * Getter method for the text of this Result
	 * @return The text of this Result
	 */
	public String text(){
		return text;
	}
	
	/**
	 * Setter method for the text of this Result
	 * @param text The text of this Result
	 */
	public void setText(String text){
		this.text = text;
	}

	/**
	 * @return The String representation of this Result
	 */
	@Override
	public String toString() {
		String s = "";

		switch (choice) {
		case UNDECIDED:
			s = "?";
			break;
		case WHITE_WIN:
			s = "1-0";
			break;
		case DRAW:
			s = "1/2-1/2";
			break;
		case BLACK_WIN:
			s = "0-1";
			break;
		default:
			s = "?";
		}
		return text;
	}

}
