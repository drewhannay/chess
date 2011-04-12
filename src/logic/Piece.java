package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Piece.java
 * 
 * Class modeling a Piece in a chess game.
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class Piece implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -6571501595221097922L;

	/**
	 * The color of this Piece
	 */
	private boolean isBlack;
	/**
	 * Whether or not this Piece has been captured
	 */
	protected boolean captured;
	/**
	 * List of Squares this Piece is guarding
	 */
	protected List<Square> guardSquares;
	/**
	 * List of legal Squares for this Piece to move to
	 */
	protected List<Square> legalDests;
	/**
	 * The current position of this Piece on the Board
	 */
	protected Square curSquare;
	/**
	 * The Board this Piece is on
	 */
	protected Board board;
	/**
	 * The name of this Piece
	 */
	protected String name;
	/**
	 * The directions and distances of movement for a custom made Piece
	 */
	protected HashMap<Character, Integer> movements;
	/**
	 * The ImageIcon representing the white version of Piece in the GUI
	 */
	protected ImageIcon lightIcon;
	/**
	 * The ImageIcon representing the black version of Piece in the GUI
	 */
	protected ImageIcon darkIcon;
	/**
	 * The number of times this Piece has moved
	 */
	protected int moveCount;
	/**
	 * The Piece pinning this Piece
	 */
	protected Piece pinnedBy;
	/**
	 * The Square that this Piece started on when the Game began
	 */
	protected Square originalSquare;

	/**
	 * Constructor
	 * Initialize instance variables.
	 * @param name The name of this Piece
	 * @param lightIcon The Icon representing the white version of Piece in the GUI
	 * @param darkIcon The Icon representing the black version of Piece in the GUI
	 * @param isBlack The color of this Piece
	 * @param isObjective Is this piece the objective?
	 * @param curSquare The Square this Piece occupies
	 * @param board The Board this Piece occupies
	 * @param movements HashMap of legal movements for this Piece
	 */
	public Piece(String name, ImageIcon darkIcon, ImageIcon lightIcon, boolean isBlack,Square curSquare, Board board,
			HashMap<Character, Integer> movements) {
		this.name = name;
		this.lightIcon = lightIcon;
		this.darkIcon = darkIcon;
		this.setBlack(isBlack);
		this.curSquare = curSquare;
		setOriginalSquare(curSquare);
		curSquare.setPiece(this);//Tell the Square what Piece is on it.
		this.board = board;
		this.movements = movements;
		//Initialize the ArrayLists
		setLegalDests(new ArrayList<Square>());
		setGuardSquares(new ArrayList<Square>());
		setMoveCount(0);
	}

	/**
	 * Add a legal destination to the ArrayList.
	 * @param dest The Square to be added to the List
	 * @return If the Square was successfully added to the ArrayList
	 */
	public boolean addLegalDest(Square dest) {
		if (!dest.isHabitable())
			//If the Square is not habitable, don't add it, but return true so we can move past it.
			return true;
		if (dest.getRow() == curSquare.getRow() && dest.getCol() == curSquare.getCol())
			return false;
		if (dest.isOccupied() && dest.getPiece().isBlack() == isBlack()) {
			//If the destination has a Piece from the same team, we must be guarding that Piece
			getGuardSquares().add(dest);
			return false;
		}
		//Otherwise, add the Piece and return true.
		getLegalDests().add(dest);
		return true;
	}

	/**
	 * Adjust the legal destinations of this piece if they are forced to continue
	 * protecting the objective piece from a member of the enemy team
	 * @param objectivePiece The piece to protect
	 * @param enemyTeam The enemy team
	 */
	public void adjustPinsLegalDests(Piece objectivePiece, List<Piece> enemyTeam) {

		if (((isBlack() ? board.getGame().getBlackRules() : board.getGame().getWhiteRules()).objectivePiece(isBlack()) == this)
				&& (board.getGame().isBlackMove() == isBlack())) {
			List<Square> tmpLegalDests = getLegalDests();
			Iterator<Square> perlimMoves = tmpLegalDests.iterator();

			setLegalDests(new ArrayList<Square>());

			//Make sure the you don't move into check
			Square dest;
			while (perlimMoves.hasNext()) {
				dest = perlimMoves.next();

				if (!board.getGame().isThreatened(dest, !isBlack()) && !board.getGame().isGuarded(dest, !isBlack())) {
					addLegalDest(dest);
				}
			}

			if (board.getGame().isClassicChess()) {
				//Castling
				if (getMoveCount() == 0) {
					boolean blocked = false;
					//Castle Queen side
					Piece rook = board.getSquare(curSquare.getRow(), 1).getPiece();
					if (rook != null && rook.getMoveCount() == 0) {
						blocked = false;

						for (int c = (rook.getSquare().getCol() + 1); c <= curSquare.getCol() && !blocked; c++) {

							if (c < curSquare.getCol()) {
								blocked = board.getSquare(curSquare.getRow(), c).isOccupied();
							}
							if (!blocked) {
								blocked = board.getGame().isThreatened(board.getSquare(curSquare.getRow(), c),
										!isBlack());
							}
						}

						if (!blocked) {
							addLegalDest(board.getSquare(((isBlack()) ? 8 : 1), 3));
						}
					}

					//Castle King side
					rook = board.getSquare(curSquare.getRow(), board.getMaxCol()).getPiece();
					if (rook != null && rook.getMoveCount() == 0) {
						blocked = false;

						for (int c = (rook.getSquare().getCol() - 1); c >= curSquare.getCol() && !blocked; c--) {

							if (c > curSquare.getCol()) {
								blocked = board.getSquare(curSquare.getRow(), c).isOccupied();
							}

							if (!blocked) {
								blocked = board.getGame().isThreatened(board.getSquare(curSquare.getRow(), c),
										!isBlack());
							}
						}

						if (!blocked) {
							addLegalDest(board.getSquare(((isBlack()) ? 8 : 1), 7));
						}
					}
				}
			}
			return;
		}

		Square[] line = getLineOfSight(objectivePiece, false);
		Piece pin = null;
		Piece tmp;
		boolean done = false;

		if (captured)
			return;

		if (line != null) {
			if (board.getGame().isStaleLegalDests()) {
				board.getGame().genLegalDests();
			}

			//Start i at 1 since 0 is this Piece
			for (int i = 1; i < line.length && !done; i++) {

				tmp = line[i].getPiece();

				if (tmp != null) {

					//two pieces blocking the attack is not a pin
					if (pin != null) {
						pin = null;
						done = true;
					}

					//friend in the way
					else if (tmp.isBlack() == this.isBlack()) {
						done = true;
					} else {
						pin = tmp;
					}
				}
			}

			if (pin != null) {
				//need to AND moves with line
				List<Square> maintainPins = Arrays.asList(line); //includes this square
				pin.setPinned(this, maintainPins);
			}
		}
	}

	/**
	 * Generate the ArrayList of legal destinations for this Piece
	 * @param board The Board on which to look for legal destinations
	 * @return The number of legal destinations for this Piece
	 */
	public int genLegalDests(Board board) {
		//Clear both ArrayLists
		getLegalDests().clear();
		getGuardSquares().clear();
		setPinnedBy(null);

		//Boolean to tell when we are done generating destinations
		boolean done = false;
		Square dest;
		boolean wraparound = board.isWraparound();

		/*
		 * East
		 */
		if (movements.containsKey('E')) {
			int northMax = movements.get('E') + curSquare.getCol();
			if (northMax > board.getMaxCol() || movements.get('E') == -1)
				if (!wraparound) {
					northMax = board.getMaxCol();
				}
			for (int c = curSquare.getCol() + 1; ((movements.get('E') == -1 && wraparound) ? true : c <= northMax)
					&& !done; c++) {
				int j = c;
				if (wraparound)
					if (j > board.getMaxCol()) {
						j = j % board.getMaxCol();
					}
				if (j == 0) {
					break;
				}
				dest = board.getSquare(curSquare.getRow(), j);
				done = !addLegalDest(dest);
				done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * West
		 */
		if (movements.containsKey('W')) {
			int southMax = curSquare.getCol() - movements.get('W');
			if (southMax < 1 || movements.get('W') == -1)
				if (!wraparound) {
					southMax = 1;
				}
			for (int c = curSquare.getCol() - 1; ((movements.get('W') == -1 && wraparound) ? true : c >= southMax)
					&& !done; c--) {
				int j = c;
				if (wraparound)
					if (j < 1) {
						j = board.getMaxCol() + j;
					}

				dest = board.getSquare(curSquare.getRow(), j);
				done = !addLegalDest(dest);
				done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * North
		 */
		if (movements.containsKey('N')) {
			int eastMax = movements.get('N') + curSquare.getRow();
			if (eastMax >= board.getMaxRow() || movements.get('N') == -1) {
				eastMax = board.getMaxRow();
			}
			for (int r = curSquare.getRow() + 1; (r <= eastMax) && !done; r++) {
				int j = r;
				dest = board.getSquare(j, curSquare.getCol());
				done = !addLegalDest(dest);
				done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * South
		 */
		if (movements.containsKey('S')) {
			int westMax = curSquare.getRow() - movements.get('S');
			if (westMax < 1 || movements.get('S') == -1) {
				westMax = 1;
			}
			for (int r = curSquare.getRow() - 1; (r >= westMax) && !done; r--) {
				int j = r;
				dest = board.getSquare(j, curSquare.getCol());
				done = !addLegalDest(dest);
				done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * NorthEast
		 */
		done = false;
		if (movements.containsKey('R')) {
			int neMax = ((curSquare.getRow() >= curSquare.getCol()) ? curSquare.getRow() : curSquare.getCol())
					+ movements.get('R');
			if (neMax >= board.getMaxCol() || movements.get('R') == -1) {
				neMax = board.getMaxCol();
			}
			if (neMax >= board.getMaxRow() || movements.get('R') == -1) {
				neMax = board.getMaxRow();
			}
			for (int r = curSquare.getRow() + 1, c = curSquare.getCol() + 1; r <= neMax && c <= neMax && !done; r++, c++) {
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}

		/*
		 * SouthEast
		 */
		done = false;
		if (movements.containsKey('r')) {
			int eastMax = curSquare.getCol() + movements.get('r');
			if (eastMax >= board.getMaxCol() || movements.get('r') == -1) {
				eastMax = board.getMaxCol();
			}
			int southMin = curSquare.getRow() - movements.get('r');
			if (southMin <= 1 || movements.get('R') == -1) {
				southMin = 1;
			}
			for (int r = curSquare.getRow() - 1, c = curSquare.getCol() + 1; r >= southMin && c <= eastMax && !done; r--, c++) {

				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * NorthWest
		 */
		done = false;
		if (movements.containsKey('L')) {
			int westMin = curSquare.getCol() - movements.get('L');
			if (westMin <= 1 || movements.get('L') == -1) {
				westMin = 1;
			}
			int NorthMax = curSquare.getRow() + movements.get('L');
			if (NorthMax >= board.getMaxRow() || movements.get('L') == -1) {
				NorthMax = board.getMaxRow();
			}
			for (int r = curSquare.getRow() + 1, c = curSquare.getCol() - 1; r <= NorthMax && c >= westMin && !done; r++, c--) {

				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * SouthWest
		 */
		done = false;
		if (movements.containsKey('l')) {
			int westMin = curSquare.getCol() - movements.get('l');
			if (westMin <= 1 || movements.get('l') == -1) {
				westMin = 1;
			}
			int southMin = curSquare.getRow() - movements.get('l');
			if (southMin <= 1 || movements.get('l') == -1) {
				southMin = 1;
			}
			for (int r = curSquare.getRow() - 1, c = curSquare.getCol() - 1; r >= southMin && c >= westMin && !done; r--, c--) {
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		return getLegalDests().size();
	}

	/**
	 * Generate legal destinations that will save the King piece from check
	 * @param king The King piece
	 * @param threat The Piece threatening the King piece
	 */
	protected void genLegalDestsSaveKing(Piece king, Piece threat) {
		if ((isBlack() ? board.getGame().getBlackRules() : board.getGame().getWhiteRules()).objectivePiece(isBlack()) == this)
			return;
		if (king == null)
			return;
		Iterator<Square> oldLegalDests = getLegalDests().iterator();
		Square sq = null;

		if (captured)
			return;

		setLegalDests(new ArrayList<Square>(3));

		while (oldLegalDests.hasNext()) {
			sq = oldLegalDests.next();

			if (threat.isBlockable(sq, king)) {
				getLegalDests().add(sq);
			} else if (sq.equals(threat.getSquare())) {
				getLegalDests().add(sq);
			}
		}
	}

	/**
	 * Getter method for the Board this Piece is on.
	 * @return The Board this Piece occupies.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @return the guardSquares
	 */
	public List<Square> getGuardSquares() {
		return guardSquares;
	}

	/**
	 * Getter method for the Icon of this Piece
	 * @return The Icon of this Piece
	 */
	public ImageIcon getIcon() {
		return isBlack() ? darkIcon : lightIcon;
	}

	/**
	 * Getter method for ArrayList of legal destinations
	 * @return The ArrayList of legal destinations
	 */
	public List<Square> getLegalDests() {
		if (board.getGame().isStaleLegalDests()) {
			board.getGame().genLegalDests();
		}
		return legalDests;
	}

	/**
	 * Get the Squares between this piece and the target piece
	 * @param targetRow The row of the target
	 * @param targetCol The column of the target
	 * @param inclusive Whether or not to include the target piece square
	 * @return The Squares between this Piece and the target piece
	 */
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

		//First diagonal
		else if ((originCol - targetCol) == (originRow - targetRow)) {
			//Northeast
			if (originRow < targetRow) {
				for (c = (originCol + 1), r = (originRow + 1); r <= targetRow; c++, r++)
					if (r != targetRow || inclusive) {
						returnTemp[i++] = board.getSquare(r, c);
					}
			}
			//Southwest
			else {
				for (c = (originCol - 1), r = (originRow - 1); r >= targetRow; c--, r--)
					if (r != targetRow || inclusive) {
						returnTemp[i++] = board.getSquare(r, c);
					}
			}
		}
		//Second diagonal
		else if ((originCol - targetCol) == ((originRow - targetRow) * -1)) {
			//Northwest
			if ((originRow - targetRow) < 0) {
				for (c = (originCol - 1), r = (originRow + 1); r <= targetRow; c--, r++)
					if (r != targetRow || inclusive) {
						returnTemp[i++] = board.getSquare(r, c);
					}
			}
			//Southeast
			else {
				for (c = (originCol + 1), r = (originRow - 1); r >= targetRow; c++, r--)
					if (r != targetRow || inclusive) {
						returnTemp[i++] = board.getSquare(r, c);
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
	public Square[] getLineOfSight(Piece target, boolean inclusive) {
		return getLineOfSight(target.getSquare().getRow(), target.getSquare().getCol(), inclusive);
	}

	/**
	 * @return the moveCount
	 */
	public int getMoveCount() {
		return moveCount;
	}

	/**
	 * Getter method for the name of this Piece
	 * @return The name of this Piece
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the originalSquare
	 */
	public Square getOriginalSquare() {
		return originalSquare;
	}

	/**
	 * Getter method for the piece pinning this piece
	 * @return The piece pinning this piece
	 */
	public Piece getPinnedBy() {
		return pinnedBy;
	}

	/**
	 * Getter method for the Square this Piece is on.
	 * @return The Square this Piece occupies.
	 */
	public Square getSquare() {
		return curSquare;
	}

	/**
	 * Getter method for the color of this Piece
	 * @return The color of this Piece
	 */
	public boolean isBlack() {
		return (isBlack);
	}

	/**
	 * Check if the given Square can be saved from the given target
	 * by this piece
	 * @param toSave The Square to save
	 * @param toBlock The Piece to block
	 * @return If the given Square can be saved
	 */
	public boolean isBlockable(Square toSave, Piece toBlock) {
		boolean blockable = false;
		Square[] lineOfSight = null;

		if (board.getGame().isStaleLegalDests()) {
			board.getGame().genLegalDests();
		}

		lineOfSight = getLineOfSight(toBlock, false);
		int i = 0;
		while (!blockable && lineOfSight != null && i < lineOfSight.length) {
			blockable = (toSave.equals(lineOfSight[i++]));
		}

		return blockable;
	}

	/**
	 * Getter method for the captured state of this Piece
	 * @return If the Piece has been captured
	 */
	public boolean isCaptured() {
		return captured;
	}

	/**
	 * Check if this piece is guarding the given square
	 * @param sq The Square to assess
	 * @return If this piece is guarding the given square
	 */
	public boolean isGuarding(Square sq) {
		if (!captured) {
			if (board.getGame().isStaleLegalDests()) {
				board.getGame().genLegalDests();
			}
			return getGuardSquares().contains(sq);
		} else
			return false;
	}

	/**
	 * See if this Piece is in check
	 * @return If this Piece is in check
	 */
	public boolean isInCheck() {
		return board.getGame().isThreatened(this);
	}

	/**
	 * Check if the given Square is a legal attack for this Piece
	 * @param threatened The Square to attack
	 * @return If this Square is a legal attack
	 */
	public boolean isLegalAttack(Square threatened) {
		return isLegalDest(threatened);
	}

	/**
	 * Determine if a Square is a legal destination
	 * @param dest The Square in question
	 * @return Whether or not that Square is a legal destination
	 */
	public boolean isLegalDest(Square dest) {
		if (!captured) {
			if (board.getGame().isStaleLegalDests()) {
				board.getGame().genLegalDests();
			}
			return getLegalDests().contains(dest);
		} else
			return false;
	}

	/**
	 * @param isBlack the isBlack to set
	 */
	public void setBlack(boolean isBlack) {
		this.isBlack = isBlack;
	}

	/**
	 * Setter method for the captured state of the Piece
	 * @param t Whether or not the Piece has been captured.
	 */
	public void setCaptured(boolean t) {
		//Clear both ArrayLists so the Piece can't move anymore
		getLegalDests().clear();
		getGuardSquares().clear();
		setPinnedBy(null);
		captured = t;
	}

	/**
	 * @param guardSquares the guardSquares to set
	 */
	public void setGuardSquares(List<Square> guardSquares) {
		this.guardSquares = guardSquares;
	}

	/**
	 * @param legalDests the legalDests to set
	 */
	public void setLegalDests(List<Square> legalDests) {
		this.legalDests = legalDests;
	}

	/**
	 * @param moveCount the moveCount to set
	 */
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}

	/**
	 * @param originalSquare the originalSquare to set
	 */
	public void setOriginalSquare(Square originalSquare) {
		this.originalSquare = originalSquare;
	}

	/**
	 * Limit the legal destinations of this piece if it is pinned by another 
	 * piece
	 * @param pinner The piece pinning this Piece
	 * @param lineOfSight The legal destinations to retain
	 */
	protected void setPinned(Piece pinner, List<Square> lineOfSight) {
		setPinnedBy(pinner);
		getLegalDests().retainAll(lineOfSight);
	}

	/**
	 * @param pinnedBy the pinnedBy to set
	 */
	public void setPinnedBy(Piece pinnedBy) {
		this.pinnedBy = pinnedBy;
	}

	/**
	 * Setter method for the Square this Piece is on.
	 * @param curSquare The Square to set
	 */
	public void setSquare(Square curSquare) {
		this.curSquare = curSquare;
	}

}