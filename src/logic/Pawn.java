package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Pawn.java
 * 
 * Logical representation of a standard Pawn piece.
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class Pawn extends Piece {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 4260559454758977410L;

	/**
	 * Constructor.
	 * Standard Pawn Piece able to move and capture unidirectionally.
	 * @param isBlack If is on Black team.
	 * @param origin The Square the Piece is on.
	 * @param board The Board the Piece is on.
	 */
	public Pawn(boolean isBlack, Square origin, Board board) {
		super("Pawn", new ImageIcon("images/pawn_dark.png"), new ImageIcon("images/pawn_light.png"), isBlack,origin,
				board, null);
	}

	/**
	 * Pawns can't be pinned, so override this method and do nothing
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
		Square dest = null;
		int dir, row, col;
		setPinnedBy(null);
		if (captured)
			return 0;

		dir = (isBlack()) ? -1 : 1;

		//Take one step forward
		if (board.isRowValid(curSquare.getRow() + dir)) {
			dest = board.getSquare(curSquare.getRow() + dir, curSquare.getCol());
			if (!dest.isOccupied() && !getLegalDests().contains(dest)) {
				addLegalDest(dest);
			}
		}

		//Take an opposing piece 
		if (board.isRowValid(row = (curSquare.getRow() + dir))) {
			col = curSquare.getCol();

			//if valid row
			//and the square is occupied (by the other team)
			//    or  it's not my move (so I'm threatening the square)
			if (board.isColValid((col + 1)) && ((board.getSquare(row, col + 1).isOccupied()
					&& isBlack() != board.getSquare(row, col + 1).getPiece().isBlack())
					/*|| board.isBlackMove != isBlack*/) && !getLegalDests().contains(board.getSquare(row, col + 1))) {
				addLegalDest(board.getSquare(row, col + 1));
			}
			if (board.isColValid((col - 1)) && ((board.getSquare(row, col - 1).isOccupied()
					&& isBlack() != board.getSquare(row, col - 1).getPiece().isBlack())
					/*|| board.isBlackMove != isBlack*/) && !getLegalDests().contains(board.getSquare(row, col - 1))) {
				addLegalDest(board.getSquare(row, col - 1));
			}
		}

		//two step
		if (getMoveCount() == 0 && board.isRowValid((curSquare.getRow() + (2 * dir)))) {
			dest = board.getSquare((curSquare.getRow() + (2 * dir)), curSquare.getCol());
			if (!dest.isOccupied() && !board.getSquare((curSquare.getRow() + dir), curSquare.getCol()).isOccupied()
					&& !getLegalDests().contains(dest)) {
				addLegalDest(dest);
			}
		}

		if (board.getGame().isClassicChess()) {
			//enPassant
			if (isBlack() == board.isBlackTurn()
					&& ((!isBlack() && curSquare.getRow() == 5) || (isBlack() && curSquare.getRow() == 4))) {
				col = curSquare.getCol();
				row = isBlack() ? curSquare.getRow() - 1 : curSquare.getRow() + 1;
				if (board.isColValid(col + 1) && board.getEnpassantCol() == (col + 1)) {
					addLegalDest(board.getSquare(row, (col + 1)));
				}

				if (board.isColValid(col - 1) && board.getEnpassantCol() == (col - 1)) {
					addLegalDest(board.getSquare(row, (col - 1)));
				}
			}
		}
		return getLegalDests().size();
	}

	/**
	 * Generate legal destinations that will save the King piece from check
	 * @param king The King piece
	 * @param threat The Piece threatening the King piece
	 */
	@Override
	protected void genLegalDestsSaveKing(Piece king, Piece threat) {
		if (king == null)
			return;
		Iterator<Square> oldLegals = getLegalDests().iterator();
		Square sq = null;

		if (captured)
			return;

		setLegalDests(new ArrayList<Square>(2));

		while (oldLegals.hasNext()) {
			sq = oldLegals.next();

			if (threat.isBlockable(sq, king)) {
				getLegalDests().add(sq);
			} else if (sq.equals(threat.getSquare())) {
				getLegalDests().add(sq);
			} else if (threat instanceof Pawn && threat.getSquare().getCol() == board.getEnpassantCol()
					&& sq.getCol() == board.getEnpassantCol()) {
				getLegalDests().add(sq);
			}
		}
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

	/**
	 * Check if the given Square is a valid attack destination for this Pawn
	 * @param target The Square to attack
	 * @return If this Square is a legal attack
	 */
	@Override
	public boolean isLegalAttack(Square target) {
		if (board.getGame().isStaleLegalDests()) {
			board.getGame().genLegalDests();
		}

		if (captured)
			return false;

		if (target.getCol() == curSquare.getCol())
			return false;
		else
			return (isLegalDest(target) || (target.getRow() - curSquare.getRow() == ((isBlack()) ? -1 : 1) && Math
					.abs(target.getCol() - curSquare.getCol()) == 1));
	}
}
