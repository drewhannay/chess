package logic;

import javax.swing.ImageIcon;

/**
 * King.java
 * 
 * Logical representation of a standard King piece.
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class King extends Piece {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 2148173935406985224L;

	/**
	 * Constructor.
	 * Standard King Piece able to move and capture one unit in every direction.
	 * @param isBlack If is on Black team.
	 * @param origin The Square the Piece is on.
	 * @param board The Board the Piece is on.
	 */
	public King(boolean isBlack, Square origin, Board board) {
		super("King", new ImageIcon("images/king_dark.png"), new ImageIcon("images/king_light.png"), isBlack,true, origin,
				board, null);
	}

	/**
	 * Generate the ArrayList of legal destinations for this Piece
	 * @return The number of legal destinations for this Piece
	 */
	@Override
	public int genLegalDests(Board board) {
		boolean wraparound = board.isWraparound();
		setPinnedBy(null);
		getLegalDests().clear();
		getGuardSquares().clear();
		int upperCol = curSquare.getCol() + 1;
		for (int i = curSquare.getRow() - 1; i <= 1 + curSquare.getRow(); i++) {
			for (int j = curSquare.getCol() - 1; j <= upperCol; j++) {
				upperCol = curSquare.getCol() + 1;
				if (board.isRowValid(i)) {
					if (!wraparound && !board.isColValid(j)) {
						continue;
					}
					int k = j;
					if (k < 1) {
						k = board.getMaxCol();
						upperCol = board.getMaxCol() + 1;
					} else if (k > board.getMaxCol()) {
						k = 1;
					}
					addLegalDest(board.getSquare(i, k));
				}
			}
		}
		return getLegalDests().size();
	}

	/**
	 * @param target Trivial doc..this method will die
	 * @return Trivial doc..this method will die
	 */
	public boolean isBlockable(Square target) {
		//TODO This shouldn't be here because the King might not be the objective piece, 
		//in which case, he COULD block things
		return false;
	}

	/**
	 * @param blocker Trivial doc..this method will die
	 * @param target Trivial doc..this method will die
	 * @return Trivial doc..this method will die
	 */
	@Override
	public boolean isBlockable(Square blocker, Piece target) {
		//TODO This shouldn't be here because the King might not be the objective piece, 
		//in which case, he COULD block things
		return false;
	}

	/**
	 * Check if this piece can attack the given square
	 * @param target The Square being attacked
	 * @return If you can attack that Square
	 */
	@Override
	public boolean isLegalAttack(Square target) {
		if (Math.abs(target.getCol() - curSquare.getCol()) == 2)
			return false;
		else
			return isLegalDest(target);

	}

}
