package logic;

import javax.swing.ImageIcon;

/**
 * Rook.java
 * 
 * Logical representation of a standard Rook piece.
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class Rook extends Piece {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 4809496587331500075L;

	/**
	 * Constructor.
	 * Standard Rook Piece able to move and capture horizontally or vertically.
	 * @param isBlack If is on Black team.
	 * @param origin The Square the Piece is on.
	 * @param board The Board the Piece is on.
	 */
	public Rook(boolean isBlack, Square origin, Board board) {
		super("Rook", new ImageIcon("images/rook_dark.png"), new ImageIcon("images/rook_light.png"), isBlack,false, origin,
				board, null);
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

		boolean wraparound = board.isWraparound();
		done = false;

		//East
		for (int c = curSquare.getCol() + 1; wraparound ? !done : c <= board.getMaxCol() && !done; c++) {
			if (c > board.getMaxCol()) {
				c = 1;
			}
			dest = board.getSquare(curSquare.getRow(), c);
			if (wraparound)
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
		for (int c = curSquare.getCol() - 1; wraparound ? !done : c >= 1 && !done; c--) {
			if (c < 1) {
				c = board.getMaxCol();
			}
			dest = board.getSquare(curSquare.getRow(), c);
			if (wraparound)
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
			//done = (done || dest.isOccupied());
			done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
					board.getGame().getOtherObjectivePiece(isBlack())))));
		}

		return getLegalDests().size();
	}

	/**
	 * Get the Squares between this piece and the target piece
	 * @param targetRow The row of the target
	 * @param targetCol The column of the target
	 * @param inclusive Whether or not to include the target piece square
	 * @return The Squares between this Piece and the target piece
	 */
	@Override
	public Square[] getLineOfSight(int targetRow, int targetCol, boolean inclusive) {
		Square[] returnSet = null;
		Square[] returnTemp = new Square[7];
		int originCol = getSquare().getCol();
		int originRow = getSquare().getRow();
		int r = 0;//Row
		int c = 0;//Column
		int i = 0;//Return Square counter

		//Same column
		if (originCol == targetCol) {
			//North
			if (originRow < targetRow) {
				for (r = (originRow + 1); r <= targetRow; r++)
					if (r != targetRow || inclusive) {
						returnTemp[i++] = board.getSquare(r, originCol);
					}
			}
			//South
			else {
				for (r = (originRow - 1); r >= targetRow; r--)
					if (r != targetRow || inclusive) {
						returnTemp[i++] = board.getSquare(r, originCol);
					}
			}
		}

		//Same Row
		else if (originRow == targetRow) {
			//East
			if (originCol < targetCol) {
				for (c = (originCol + 1); c <= targetCol; c++)
					if (c != targetCol || inclusive) {
						returnTemp[i++] = board.getSquare(originRow, c);
					}
			}
			//West
			else {
				for (c = (originCol - 1); c >= targetCol; c--)
					if (c != targetCol || inclusive) {
						returnTemp[i++] = board.getSquare(originRow, c);
					}
			}
		}

		if (i != 0) {//If i is zero, they weren't in line so return the null array
			returnSet = new Square[i + 1];
			returnSet[0] = this.getSquare();
			System.arraycopy(returnTemp, 0, returnSet, 1, i);//Copy over the returnTemp array moving forward a position
		}

		return returnSet;
	}

	/**
	 * Get the Squares between this piece and the target piece
	 * @param target The piece in the line of sight
	 * @param inclusive Whether or not to include the target piece square
	 * @return The Squares between this Piece and the target piece
	 */
	@Override
	public Square[] getLineOfSight(Piece target, boolean inclusive) {
		return getLineOfSight(target.getSquare().getRow(), target.getSquare().getCol(), inclusive);
	}

}
