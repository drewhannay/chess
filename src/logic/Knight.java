package logic;

import java.util.List;

import javax.swing.ImageIcon;

/**
 * Knight.java
 * 
 * Logical representation of a standard Knight piece.
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class Knight extends Piece {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -3882195313763202783L;

	/**
	 * Constructor.
	 * Standard Knight Piece able to move and capture in specialized "L" movement.
	 * @param isBlack If is on Black team.
	 * @param origin The Square the Piece is on.
	 * @param board The Board the Piece is on.
	 */
	public Knight(boolean isBlack, Square origin, Board board) {
		super("Knight", new ImageIcon("images/knight_dark.png"), new ImageIcon("images/knight_light.png"), isBlack,false,
				origin, board, null);
	}

	/**
	 * Knights can't be pinned, so override this method and do nothing
	 * @param king Irrelevant
	 * @param enemyTeam Irrelevant
	 */
	@Override
	public void adjustPinsLegalDests(Piece king, List<Piece> enemyTeam) {
	}

	/**
	 * Generate the ArrayList of legal destinations for this Piece
	 * @return The number of legal destinations for this Piece
	 */
	@Override
	public int genLegalDests(Board board) {
		getLegalDests().clear();
		getGuardSquares().clear();
		setPinnedBy(null);
		boolean wraparound = board.isWraparound();
		int f, r;

		if (captured)
			return 0;
		f = (curSquare.getRow() + 1);
		r = (curSquare.getCol() + 2);
		if (wraparound) {
			if (r > board.getMaxCol() + 1) {
				r = 2;
			} else if (r > board.getMaxCol()) {
				r = 1;
			}
		}
		if (board.isRowValid(f) && board.isColValid(r)) {
			addLegalDest(board.getSquare(f, r));
		}

		//two o'clock
		f = (curSquare.getRow() + 2);
		r = (curSquare.getCol() + 1);
		if (wraparound) {
			if (r > board.getMaxCol() + 1) {
				r = 2;
			} else if (r > board.getMaxCol()) {
				r = 1;
			}
		}
		if (board.isRowValid(f) && board.isColValid(r)) {
			addLegalDest(board.getSquare(f, r));
		}

		//four o'clock
		f = (curSquare.getRow() + 2);
		r = (curSquare.getCol() - 1);
		if (wraparound) {
			if (r < 0) {
				r = board.getMaxCol() - 1;
			} else if (r < 1) {
				r = board.getMaxCol();
			}
		}
		if (board.isRowValid(f) && board.isColValid(r)) {
			addLegalDest(board.getSquare(f, r));
		}

		//five o'clock
		f = (curSquare.getRow() + 1);
		r = (curSquare.getCol() - 2);
		if (wraparound) {
			if (r < 0) {
				r = board.getMaxCol() - 1;
			} else if (r < 1) {
				r = board.getMaxCol();
			}
		}
		if (board.isRowValid(f) && board.isColValid(r)) {
			addLegalDest(board.getSquare(f, r));
		}

		//seven o'clock
		f = (curSquare.getRow() - 1);
		r = (curSquare.getCol() - 2);
		if (wraparound) {
			if (r < 0) {
				r = board.getMaxCol() - 1;
			} else if (r < 1) {
				r = board.getMaxCol();
			}
		}
		if (board.isRowValid(f) && board.isColValid(r)) {
			addLegalDest(board.getSquare(f, r));
		}

		//eight o'clock
		f = (curSquare.getRow() - 2);
		r = (curSquare.getCol() - 1);
		if (wraparound) {
			if (r < 0) {
				r = board.getMaxCol() - 1;
			} else if (r < 1) {
				r = board.getMaxCol();
			}
		}
		if (board.isRowValid(f) && board.isColValid(r)) {
			addLegalDest(board.getSquare(f, r));
		}

		//ten o'clock
		f = (curSquare.getRow() - 2);
		r = (curSquare.getCol() + 1);
		if (wraparound) {
			if (r > board.getMaxCol() + 1) {
				r = 2;
			} else if (r > board.getMaxCol()) {
				r = 1;
			}
		}
		if (board.isRowValid(f) && board.isColValid(r)) {
			addLegalDest(board.getSquare(f, r));
		}

		//eleven o'clock
		f = (curSquare.getRow() - 1);
		r = (curSquare.getCol() + 2);
		if (wraparound) {
			if (r > board.getMaxCol() + 1) {
				r = 2;
			} else if (r > board.getMaxCol()) {
				r = 1;
			}
		}
		if (board.isRowValid(f) && board.isColValid(r)) {
			addLegalDest(board.getSquare(f, r));
		}
		return getLegalDests().size();
	}

	/**
	 * @param target Trivial doc..this method will die
	 * @return Trivial doc..this method will die
	 */
	public boolean isBlockable(Square target) {
		return false;
	}

	/**
	 * @param blocker Trivial doc..this method will die
	 * @param target Trivial doc..this method will die
	 * @return Trivial doc..this method will die
	 */
	@Override
	public boolean isBlockable(Square blocker, Piece target) {
		return false;
	}

}
