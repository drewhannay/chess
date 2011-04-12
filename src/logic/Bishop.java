package logic;

import javax.swing.ImageIcon;

/**
 * Bishop.java Logical representation of a standard Bishop piece.
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas CSCI 335, Wheaton College,
 *         Spring 2011 Phase 2 April 7, 2011
 */
public class Bishop extends Piece {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -3432594638853838373L;

	/**
	 * Constructor. Standard Bishop Piece able to move and capture diagonally.
	 * @param isBlack If is on Black team.
	 * @param origin The Square the Piece is on.
	 * @param board The Board the Piece is on.
	 */
	public Bishop(boolean isBlack, Square origin, Board board) {
		super("Bishop", new ImageIcon("images/bishop_dark.png"), new ImageIcon(
				"images/bishop_light.png"), isBlack, origin, board, null);
	}

	/**
	 * Generate the ArrayList of legal destinations for this Piece
	 * @return The number of legal destinations for this Piece
	 */
	@Override
	public int genLegalDests(Board board) {
		getLegalDests().clear();
		getGuardSquares().clear();
		Square dest;
		boolean done;
		setPinnedBy(null);
		if (captured)
			return 0;

		// northeast
		done = false;

		for (int r = curSquare.getRow() + 1, c = curSquare.getCol() + 1; r <= board
				.getMaxRow()
				&& c <= board.getMaxCol() && !done; r++, c++) {

			dest = board.getSquare(r, c);
			done = !addLegalDest(dest);
			// done = (done || dest.isOccupied());
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest
					.getPiece().equals(board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		// south east
		done = false;

		for (int r = curSquare.getRow() + 1, c = curSquare.getCol() - 1; r <= board
				.getMaxRow()
				&& c > 0 && !done; r++, c--) {

			dest = board.getSquare(r, c);
			done = !addLegalDest(dest);
			// done = (done || dest.isOccupied());
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest
					.getPiece().equals(board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		// south west
		done = false;

		for (int r = curSquare.getRow() - 1, c = curSquare.getCol() - 1; r > 0
				&& c > 0 && !done; r--, c--) {

			dest = board.getSquare(r, c);
			done = !addLegalDest(dest);
			// done = (done || dest.isOccupied());
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest
					.getPiece().equals(board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		// northwest
		done = false;

		for (int r = curSquare.getRow() - 1, c = curSquare.getCol() + 1; r > 0
				&& c <= board.getMaxCol() && !done; r--, c++) {

			dest = board.getSquare(r, c);
			done = !addLegalDest(dest);
			// done = (done || dest.isOccupied());
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest
					.getPiece().equals(board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		return getLegalDests().size();
	}

}
