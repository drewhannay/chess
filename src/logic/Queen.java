package logic;

import javax.swing.ImageIcon;

/**
 * Queen.java
 * 
 * Logical representation of a standard Queen piece.
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class Queen extends Piece {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -5232607094129778529L;

	/**
	 * Constructor.
	 * Standard Queen Piece able to move and capture in all directions.
	 * @param isBlack If is on Black team.
	 * @param origin The Square the Piece is on.
	 * @param board The Board the Piece is on.
	 */
	public Queen(boolean isBlack, Square origin, Board board) {
		super("Queen", new ImageIcon("images/queen_dark.png"), new ImageIcon("images/queen_light.png"), isBlack,false,
				origin, board, null);
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

		Square dest;
		boolean done;
		if (captured)
			return 0;

		done = false;

		boolean wrapsAround = board.isWraparound();

		//East
		for (int c = curSquare.getCol() + 1; wrapsAround ? !done : c <= board.getMaxCol() && !done; c++) {
			if (c > board.getMaxCol()) {
				c = 1;
			}
			dest = board.getSquare(curSquare.getRow(), c);
			if (wrapsAround)
				if (getLegalDests().contains(dest)) {
					break;
				}
			done = !addLegalDest(dest);
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
					board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		//North
		done = false;

		for (int r = curSquare.getRow() + 1; r <= board.getMaxRow() && !done; r++) {
			dest = board.getSquare(r, curSquare.getCol());
			done = !addLegalDest(dest);
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
					board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		//West
		done = false;

		for (int c = curSquare.getCol() - 1; wrapsAround ? !done : c >= 1 && !done; c--) {
			if (c < 1) {
				c = board.getMaxCol();
			}
			dest = board.getSquare(curSquare.getRow(), c);
			if (wrapsAround)
				if (getLegalDests().contains(dest)) {
					break;
				}
			done = !addLegalDest(dest);
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
					board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		//South
		done = false;

		for (int r = curSquare.getRow() - 1; r >= 1 && !done; r--) {

			dest = board.getSquare(r, curSquare.getCol());
			done = !addLegalDest(dest);
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
					board.getGame().getOtherObjectivePiece(isBlack())))));
		}
		//northeast
		done = false;

		for (int r = curSquare.getRow() + 1, c = curSquare.getCol() + 1; r <= board.getMaxRow()
				&& c <= board.getMaxCol() && !done; r++, c++) {
			dest = board.getSquare(r, c);
			done = !addLegalDest(dest);
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
					board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		//southeast
		done = false;

		for (int r = curSquare.getRow() + 1, c = curSquare.getCol() - 1; r <= board.getMaxRow() && c > 0 && !done; r++, c--) {
			dest = board.getSquare(r, c);
			done = !addLegalDest(dest);
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
					board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		//southwest
		done = false;

		for (int r = curSquare.getRow() - 1, c = curSquare.getCol() - 1; r > 0 && c > 0 && !done; r--, c--) {
			dest = board.getSquare(r, c);
			done = !addLegalDest(dest);
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
					board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		//northwest
		done = false;

		for (int r = curSquare.getRow() - 1, c = curSquare.getCol() + 1; r > 0 && c <= board.getMaxCol() && !done; r--, c++) {

			dest = board.getSquare(r, c);
			done = !addLegalDest(dest);
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
					board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		return getLegalDests().size();
	}

}
