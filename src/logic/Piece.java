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
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class Piece implements Serializable
{

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
	 * Whether or not this Piece may jump over other Pieces in movement.
	 */
	protected boolean leaper = false;
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
	 * What the piece can promote to; if size one, promotion is automatic and is
	 * not prompted.
	 */
	protected ArrayList<String> promotesTo = new ArrayList<String>();

	/**
	 * Constructor Initialize instance variables.
	 * 
	 * @param name The name of this Piece
	 * @param lightIcon The Icon representing the white version of Piece in the
	 * GUI
	 * @param darkIcon The Icon representing the black version of Piece in the
	 * GUI
	 * @param isBlack The color of this Piece
	 * @param curSquare The Square this Piece occupies
	 * @param board The Board this Piece occupies
	 * @param movements HashMap of legal movements for this Piece
	 */
	public Piece(String name, ImageIcon darkIcon, ImageIcon lightIcon,
			boolean isBlack, Square curSquare, Board board,
			HashMap<Character, Integer> movements)
	{
		this.name = name;
		this.lightIcon = lightIcon;
		this.darkIcon = darkIcon;
		setBlack(isBlack);
		this.curSquare = curSquare;
		setOriginalSquare(curSquare);
		curSquare.setPiece(this);// Tell the Square what Piece is on it.
		this.board = board;
		this.movements = movements;
		// Initialize the ArrayLists
		setLegalDests(new ArrayList<Square>());
		setGuardSquares(new ArrayList<Square>());
		setMoveCount(0);
	}

	/**
	 * Add a legal destination to the ArrayList.
	 * 
	 * @param dest The Square to be added to the List
	 * @return If the Square was successfully added to the ArrayList
	 */
	public boolean addLegalDest(Square dest)
	{
		if (!dest.isHabitable())
			// If the Square is not habitable, don't add it, but return true so
			// we can move past it.
			return true;
		if (dest.getRow() == curSquare.getRow()
				&& dest.getCol() == curSquare.getCol())
			return false;
		if (dest.isOccupied() && dest.getPiece().isBlack() == isBlack())
		{
			// If the destination has a Piece from the same team, we must be
			// guarding that Piece
			getGuardSquares().add(dest);
			return false;
		}
		// Otherwise, add the Piece and return true.
		getLegalDests().add(dest);
		return true;
	}

	/**
	 * Adjust the legal destinations of this piece if they are forced to
	 * continue protecting the objective piece from a member of the enemy team
	 * 
	 * @param objectivePiece The piece to protect
	 * @param enemyTeam The enemy team
	 */
	public void adjustPinsLegalDests(Piece objectivePiece, List<Piece> enemyTeam)
	{

		if (((isBlack() ? board.getGame().getBlackRules() : board.getGame()
				.getWhiteRules()).objectivePiece(isBlack()) == this)
				&& (board.getGame().isBlackMove() == isBlack()))
		{
			List<Square> tmpLegalDests = getLegalDests();
			Iterator<Square> perlimMoves = tmpLegalDests.iterator();

			setLegalDests(new ArrayList<Square>());

			// Make sure the you don't move into check
			Square dest;
			while (perlimMoves.hasNext())
			{
				dest = perlimMoves.next();

				if (!board.getGame().isThreatened(dest, !isBlack())
						&& !board.getGame().isGuarded(dest, !isBlack()))
				{
					addLegalDest(dest);
				}
			}

			if (board.getGame().isClassicChess())
			{
				// Castling
				if (getMoveCount() == 0)
				{
					boolean blocked = false;
					// Castle Queen side
					Piece rook = board.getSquare(curSquare.getRow(), 1)
							.getPiece();
					if (rook != null && rook.getMoveCount() == 0)
					{
						blocked = false;

						for (int c = (rook.getSquare().getCol() + 1); c <= curSquare
								.getCol() && !blocked; c++)
						{

							if (c < curSquare.getCol())
							{
								blocked = board
										.getSquare(curSquare.getRow(), c)
										.isOccupied();
							}
							if (!blocked)
							{
								blocked = board.getGame().isThreatened(
										board.getSquare(curSquare.getRow(), c),
										!isBlack());
							}
						}

						if (!blocked)
						{
							addLegalDest(board.getSquare(((isBlack()) ? 8 : 1),
									3));
						}
					}

					// Castle King side
					rook = board.getSquare(curSquare.getRow(),
							board.getMaxCol()).getPiece();
					if (rook != null && rook.getMoveCount() == 0)
					{
						blocked = false;

						for (int c = (rook.getSquare().getCol() - 1); c >= curSquare
								.getCol() && !blocked; c--)
						{

							if (c > curSquare.getCol())
							{
								blocked = board
										.getSquare(curSquare.getRow(), c)
										.isOccupied();
							}

							if (!blocked)
							{
								blocked = board.getGame().isThreatened(
										board.getSquare(curSquare.getRow(), c),
										!isBlack());
							}
						}

						if (!blocked)
						{
							addLegalDest(board.getSquare(((isBlack()) ? 8 : 1),
									7));
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

		if (line != null)
		{
			ArrayList<Square> temp = new ArrayList<Square>();
			for (Square sq : line)
			{
				if (legalDests.contains(sq) || sq.equals(curSquare))
					temp.add(sq);
			}
			line = new Square[temp.size()];
			temp.toArray(line);

			if (board.getGame().isStaleLegalDests())
			{
				board.getGame().genLegalDests();
			}

			// Start i at 1 since 0 is this Piece
			for (int i = 1; i < line.length && !done; i++)
			{

				tmp = line[i].getPiece();

				if (tmp != null)
				{

					// two pieces blocking the attack is not a pin
					if (pin != null)
					{
						pin = null;
						done = true;
					}

					// friend in the way
					else if (tmp.isBlack() == isBlack())
					{
						done = true;
					} else
					{
						pin = tmp;
					}
				}
			}

			if (pin != null)
			{
				// need to AND moves with line
				List<Square> maintainPins = Arrays.asList(line); // includes
																	// this
																	// square
				pin.setPinned(this, maintainPins);
			}
		}
	}

	/**
	 * Generate the ArrayList of legal destinations for this Piece
	 * 
	 * @param board The Board on which to look for legal destinations
	 * @return The number of legal destinations for this Piece
	 */
	public int genLegalDests(Board board)
	{
		// Clear both ArrayLists
		getLegalDests().clear();
		getGuardSquares().clear();
		setPinnedBy(null);
		// Boolean to tell when we are done generating destinations

		/*
		 * Special genLegalDests for Pawns, to incorporate enPassant, special
		 * initial movement, and diagonal capturing.
		 */
		if (name.equals("Pawn"))
		{
			Square dest = null;
			int dir, row, col;
			setPinnedBy(null);
			if (captured)
				return 0;

			dir = (isBlack()) ? -1 : 1;

			// Take one step forward
			if (board.isRowValid(curSquare.getRow() + dir))
			{
				dest = board.getSquare(curSquare.getRow() + dir,
						curSquare.getCol());
				if (!dest.isOccupied() && !getLegalDests().contains(dest))
				{
					addLegalDest(dest);
				}
			}

			// Take an opposing piece
			if (board.isRowValid(row = (curSquare.getRow() + dir)))
			{
				col = curSquare.getCol();

				// if valid row
				// and the square is occupied (by the other team)
				// or it's not my move (so I'm threatening the square)
				if (board.isColValid((col + 1))
						&& ((board.getSquare(row, col + 1).isOccupied() && isBlack() != board
								.getSquare(row, col + 1).getPiece().isBlack())))
				{
					addLegalDest(board.getSquare(row, col + 1));
				}
				if (board.isColValid((col - 1))
						&& ((board.getSquare(row, col - 1).isOccupied() && isBlack() != board
								.getSquare(row, col - 1).getPiece().isBlack())))
				{
					addLegalDest(board.getSquare(row, col - 1));
				}
			}

			// two step
			if (getMoveCount() == 0
					&& board.isRowValid((curSquare.getRow() + (2 * dir))))
			{
				dest = board.getSquare((curSquare.getRow() + (2 * dir)),
						curSquare.getCol());
				if (!dest.isOccupied()
						&& !board.getSquare((curSquare.getRow() + dir),
								curSquare.getCol()).isOccupied()
						&& !getLegalDests().contains(dest))
				{
					addLegalDest(dest);
				}
			}

			if (board.getGame().isClassicChess())
			{
				// enPassant
				if (isBlack() == board.isBlackTurn()
						&& ((!isBlack() && curSquare.getRow() == 5) || (isBlack() && curSquare
								.getRow() == 4)))
				{
					col = curSquare.getCol();
					row = isBlack() ? curSquare.getRow() - 1 : curSquare
							.getRow() + 1;
					if (board.isColValid(col + 1)
							&& board.getEnpassantCol() == (col + 1))
					{
						addLegalDest(board.getSquare(row, (col + 1)));
					}

					if (board.isColValid(col - 1)
							&& board.getEnpassantCol() == (col - 1))
					{
						addLegalDest(board.getSquare(row, (col - 1)));
					}
				}
			}
			return getLegalDests().size();
		}

		boolean done = false;
		Square dest;
		boolean wraparound = board.isWraparound();
		/*
		 * East
		 */
		if (movements.containsKey('E'))
		{
			int northMax = movements.get('E') + curSquare.getCol();
			if (northMax > board.getMaxCol() || movements.get('E') == -1)
				if (!wraparound)
				{
					northMax = board.getMaxCol();
				}
			for (int c = curSquare.getCol() + 1; ((movements.get('E') == -1 && wraparound) ? true
					: c <= northMax)
					&& !done; c++)
			{
				int j = c;
				if (wraparound)
					if (j > board.getMaxCol())
					{
						j = j % board.getMaxCol();
					}
				if (j == 0)
				{
					break;
				}
				dest = board.getSquare(curSquare.getRow(), j);
				done = !addLegalDest(dest);
				done = leaper ? false : (done || (dest.isOccupied() && !(board
						.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * West
		 */
		if (movements.containsKey('W'))
		{
			int southMax = curSquare.getCol() - movements.get('W');
			if (southMax < 1 || movements.get('W') == -1)
				if (!wraparound)
				{
					southMax = 1;
				}
			for (int c = curSquare.getCol() - 1; ((movements.get('W') == -1 && wraparound) ? true
					: c >= southMax)
					&& !done; c--)
			{
				int j = c;
				if (wraparound)
					if (j < 1)
					{
						j = board.getMaxCol() + j;
					}

				dest = board.getSquare(curSquare.getRow(), j);
				done = !addLegalDest(dest);
				done = leaper ? false : (done || (dest.isOccupied() && !(board
						.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * North
		 */
		if (movements.containsKey('N'))
		{
			int eastMax = movements.get('N') + curSquare.getRow();
			if (eastMax >= board.getMaxRow() || movements.get('N') == -1)
			{
				eastMax = board.getMaxRow();
			}
			for (int r = curSquare.getRow() + 1; (r <= eastMax) && !done; r++)
			{
				int j = r;
				dest = board.getSquare(j, curSquare.getCol());
				done = !addLegalDest(dest);
				done = leaper ? false : (done || (dest.isOccupied() && !(board
						.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * South
		 */
		if (movements.containsKey('S'))
		{
			int westMax = curSquare.getRow() - movements.get('S');
			if (westMax < 1 || movements.get('S') == -1)
			{
				westMax = 1;
			}
			for (int r = curSquare.getRow() - 1; (r >= westMax) && !done; r--)
			{
				int j = r;
				dest = board.getSquare(j, curSquare.getCol());
				done = !addLegalDest(dest);
				done = leaper ? false : (done || (dest.isOccupied() && !(board
						.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * NorthEast
		 */
		done = false;
		if (movements.containsKey('R'))
		{
			int neMax = ((curSquare.getRow() >= curSquare.getCol()) ? curSquare
					.getRow() : curSquare.getCol()) + movements.get('R');
			if (neMax >= board.getMaxCol() || movements.get('R') == -1)
			{
				neMax = board.getMaxCol();
			}
			if (neMax >= board.getMaxRow() || movements.get('R') == -1)
			{
				neMax = board.getMaxRow();
			}
			for (int r = curSquare.getRow() + 1, c = curSquare.getCol() + 1; r <= neMax
					&& c <= neMax && !done; r++, c++)
			{
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = leaper ? false : (done || (dest.isOccupied() && !(board
						.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}

		/*
		 * SouthEast
		 */
		done = false;
		if (movements.containsKey('r'))
		{
			int eastMax = curSquare.getCol() + movements.get('r');
			if (eastMax >= board.getMaxCol() || movements.get('r') == -1)
			{
				eastMax = board.getMaxCol();
			}
			int southMin = curSquare.getRow() - movements.get('r');
			if (southMin <= 1 || movements.get('R') == -1)
			{
				southMin = 1;
			}
			for (int r = curSquare.getRow() - 1, c = curSquare.getCol() + 1; r >= southMin
					&& c <= eastMax && !done; r--, c++)
			{

				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = leaper ? false : (done || (dest.isOccupied() && !(board
						.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * NorthWest
		 */
		done = false;
		if (movements.containsKey('L'))
		{
			int westMin = curSquare.getCol() - movements.get('L');
			if (westMin <= 1 || movements.get('L') == -1)
			{
				westMin = 1;
			}
			int NorthMax = curSquare.getRow() + movements.get('L');
			if (NorthMax >= board.getMaxRow() || movements.get('L') == -1)
			{
				NorthMax = board.getMaxRow();
			}
			for (int r = curSquare.getRow() + 1, c = curSquare.getCol() - 1; r <= NorthMax
					&& c >= westMin && !done; r++, c--)
			{

				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = leaper ? false : (done || (dest.isOccupied() && !(board
						.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * SouthWest
		 */
		done = false;
		if (movements.containsKey('l'))
		{
			int westMin = curSquare.getCol() - movements.get('l');
			if (westMin <= 1 || movements.get('l') == -1)
			{
				westMin = 1;
			}
			int southMin = curSquare.getRow() - movements.get('l');
			if (southMin <= 1 || movements.get('l') == -1)
			{
				southMin = 1;
			}
			for (int r = curSquare.getRow() - 1, c = curSquare.getCol() - 1; r >= southMin
					&& c >= westMin && !done; r--, c--)
			{
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = leaper ? false : (done || (dest.isOccupied() && !(board
						.isBlackTurn() != isBlack() && dest.getPiece().equals(
						board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}

		/*
		 * Knight / Leaper Movements
		 * 
		 * 
		 * Store of Knight Movements are as followed:
		 * 
		 * A Piece can move x File by y Rank squares at a time.
		 * 
		 * IE: A knight can move 1 by 2 or 2 by 1, but not 1 by 1 or 2 by 2
		 */
		if (movements.containsKey('x'))
		{
			int f, r;
			int Rank = movements.get('x');
			int File = movements.get('y');
			f = (curSquare.getRow() + File);
			r = (curSquare.getCol() + Rank);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
				{
					r = r % board.getMaxCol();
				}
			}
			if (board.isRowValid(f) && board.isColValid(r))
			{
				addLegalDest(board.getSquare(f, r));
			}
			// two o'clock
			f = (curSquare.getRow() + Rank);
			r = (curSquare.getCol() + File);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
				{
					r = r % board.getMaxCol();
				}
			}
			if (board.isRowValid(f) && board.isColValid(r))
			{
				addLegalDest(board.getSquare(f, r));
			}

			// four o'clock
			f = (curSquare.getRow() + File);
			r = (curSquare.getCol() - Rank);
			if (wraparound)
			{
				if (r < 1)
				{
					r = board.getMaxCol() + r;
				}
			}
			if (board.isRowValid(f) && board.isColValid(r))
			{
				addLegalDest(board.getSquare(f, r));
			}

			// five o'clock
			f = (curSquare.getRow() + Rank);
			r = (curSquare.getCol() - File);
			if (wraparound)
			{
				if (r < 1)
				{
					r = board.getMaxCol() + r;
				}
			}
			if (board.isRowValid(f) && board.isColValid(r))
			{
				addLegalDest(board.getSquare(f, r));
			}

			// seven o'clock
			f = (curSquare.getRow() - File);
			r = (curSquare.getCol() - Rank);
			if (wraparound)
			{
				if (r < 1)
				{
					r = board.getMaxCol() + r;
				}
			}
			if (board.isRowValid(f) && board.isColValid(r))
			{
				addLegalDest(board.getSquare(f, r));
			}

			// eight o'clock
			f = (curSquare.getRow() - Rank);
			r = (curSquare.getCol() - File);
			if (wraparound)
			{
				if (r < 1)
				{
					r = board.getMaxCol() + r;
				}
			}
			if (board.isRowValid(f) && board.isColValid(r))
			{
				addLegalDest(board.getSquare(f, r));
			}

			// ten o'clock
			f = (curSquare.getRow() - File);
			r = (curSquare.getCol() + Rank);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
				{
					r = r % board.getMaxCol();
				}
			}
			if (board.isRowValid(f) && board.isColValid(r))
			{
				addLegalDest(board.getSquare(f, r));
			}

			// eleven o'clock
			f = (curSquare.getRow() - Rank);
			r = (curSquare.getCol() + File);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
				{
					r = r % board.getMaxCol();
				}
			}
			if (board.isRowValid(f) && board.isColValid(r))
			{
				addLegalDest(board.getSquare(f, r));
			}
		}
		return getLegalDests().size();
	}

	/**
	 * Generate legal destinations that will save the King piece from check
	 * 
	 * @param king The King piece
	 * @param threat The Piece threatening the King piece
	 */
	protected void genLegalDestsSaveKing(Piece king, Piece threat)
	{
		if ((isBlack() ? board.getGame().getBlackRules() : board.getGame()
				.getWhiteRules()).objectivePiece(isBlack()) == this)
			return;
		if (king == null)
			return;
		Iterator<Square> oldLegalDests = getLegalDests().iterator();
		Square sq = null;

		if (captured)
			return;

		setLegalDests(new ArrayList<Square>());

		while (oldLegalDests.hasNext())
		{
			sq = oldLegalDests.next();

			if (threat.isBlockable(sq, king))
			{
				getLegalDests().add(sq);
			} else if (sq.equals(threat.getSquare()))
			{
				getLegalDests().add(sq);
			}
		}
	}

	/**
	 * Getter method for the Board this Piece is on.
	 * 
	 * @return The Board this Piece occupies.
	 */
	public Board getBoard()
	{
		return board;
	}

	/**
	 * @return the guardSquares
	 */
	public List<Square> getGuardSquares()
	{
		return guardSquares;
	}

	/**
	 * Getter method for the Icon of this Piece
	 * 
	 * @return The Icon of this Piece
	 */
	public ImageIcon getIcon()
	{
		return isBlack() ? darkIcon : lightIcon;
	}

	/**
	 * Getter method for ArrayList of legal destinations
	 * 
	 * @return The ArrayList of legal destinations
	 */
	public List<Square> getLegalDests()
	{
		if (board.getGame().isStaleLegalDests())
		{
			board.getGame().genLegalDests();
		}
		return legalDests;
	}

	/**
	 * Get the Squares between this piece and the target piece
	 * 
	 * @param targetRow The row of the target
	 * @param targetCol The column of the target
	 * @param inclusive Whether or not to include the target piece square
	 * @return The Squares between this Piece and the target piece
	 */
	public Square[] getLineOfSight(int targetRow, int targetCol,
			boolean inclusive)
	{
		if (name.equals("Pawn"))
			return null;
		if ((isBlack ? board.getGame().getBlackRules() : board.getGame()
				.getWhiteRules()).objectivePiece(isBlack).equals(this))
			return null;
		Square[] returnSet = null;
		ArrayList<Square> returnTemp = new ArrayList<Square>();
		int originCol = getSquare().getCol();
		int originRow = getSquare().getRow();
		int r = 0;// Row
		int c = 0;// Column
		int i = 0;// Return Square counter

		// Same column
		if (originCol == targetCol)
		{
			// North
			if (originRow < targetRow && canAttack(targetRow, targetCol, 'N'))
			{
				for (r = (originRow + 1); r <= targetRow; r++)
					if (r != targetRow || inclusive)
					{
						returnTemp.add(i++, board.getSquare(r, originCol));
					}
			}
			// South
			else
			{
				if (canAttack(targetRow, targetCol, 'S'))
				{
					for (r = (originRow - 1); r >= targetRow; r--)
						if (r != targetRow || inclusive)
						{
							returnTemp.add(i++, board.getSquare(r, originCol));
						}
				}
			}
		}

		// Same Row
		else if (originRow == targetRow)
		{
			// East
			if (originCol < targetCol && canAttack(targetRow, targetCol, 'E'))
			{
				for (c = (originCol + 1); c <= targetCol; c++)
					if (c != targetCol || inclusive)
					{
						returnTemp.add(i++, board.getSquare(originRow, c));
					}
			}
			// West
			else
			{
				if (canAttack(targetRow, targetCol, 'W'))
				{
					for (c = (originCol - 1); c >= targetCol; c--)
						if (c != targetCol || inclusive)
						{
							returnTemp.add(i++, board.getSquare(originRow, c));
						}
				}
			}
		}

		// First diagonal
		else if ((originCol - targetCol) == (originRow - targetRow))
		{
			// Northeast
			if (originRow < targetRow && canAttack(targetRow, targetCol, 'R'))
			{
				for (c = (originCol + 1), r = (originRow + 1); r <= targetRow; c++, r++)
					if (r != targetRow || inclusive)
					{
						returnTemp.add(i++, board.getSquare(r, c));
					}
			}
			// Southwest
			else
			{
				if (canAttack(targetRow, targetCol, 'l'))
				{
					for (c = (originCol - 1), r = (originRow - 1); r >= targetRow; c--, r--)
						if (r != targetRow || inclusive)
						{
							returnTemp.add(i++, board.getSquare(r, c));
						}
				}
			}
		}
		// Second diagonal
		else if ((originCol - targetCol) == ((originRow - targetRow) * -1))
		{
			// Northwest
			if ((originRow - targetRow) < 0
					&& canAttack(targetRow, targetCol, 'L'))
			{
				for (c = (originCol - 1), r = (originRow + 1); r <= targetRow; c--, r++)
					if (r != targetRow || inclusive)
					{
						returnTemp.add(i++, board.getSquare(r, c));
					}
			}
			// Southeast
			else
			{
				if (canAttack(targetRow, targetCol, 'r'))
				{
					for (c = (originCol + 1), r = (originRow - 1); r >= targetRow; c++, r--)
						if (r != targetRow || inclusive)
						{
							returnTemp.add(i++, board.getSquare(r, c));
						}
				}
			}
		}

		if (i != 0)
		{// If i is zero, they weren't in line so return the null array
			returnSet = new Square[i + 1];
			returnSet[0] = getSquare();

			int j = 1;
			for (Square sq : returnTemp)
			{
				returnSet[j++] = sq;
			}
		}

		return returnSet;
	}

	/**
	 * @param destRow The row you wish to move to
	 * @param destCol The col you wish to move to
	 * @param direction The direction that space is from you.
	 * @return Whether you are allowed to take that space and/or the piece on
	 * it.
	 */
	public boolean canAttack(int destRow, int destCol, char direction)
	{
		switch (direction)
		{
		case 'S': // South
			if (movements.containsKey('S'))
				return (destRow - curSquare.getRow()) < movements.get('S')
						|| movements.get('S') == -1;

		case 'N': // North
			if (movements.containsKey('N'))
				return (curSquare.getRow() - destRow) < movements.get('N')
						|| movements.get('N') == -1;

		case 'E': // East
			if (movements.containsKey('E'))
				return (destCol - curSquare.getCol()) < movements.get('E')
						|| movements.get('E') == -1;

		case 'W': // West
			if (movements.containsKey('W'))
				return (curSquare.getCol() - destCol) < movements.get('W')
						|| movements.get('W') == -1;
		case 'R': // NorthEast
			if (movements.containsKey('R'))
				return (curSquare.getCol() - destCol) < movements.get('R')
						|| movements.get('R') == -1;
		case 'r': // SouthEast
			if (movements.containsKey('r'))
				return (curSquare.getCol() - destCol) < movements.get('r')
						|| movements.get('r') == -1;
		case 'L': // NorthWest
			if (movements.containsKey('L'))
				return (destCol - curSquare.getCol()) < movements.get('L')
						|| movements.get('L') == -1;
		case 'l': // SouthWest
			if (movements.containsKey('l'))
				return (destCol - curSquare.getCol()) < movements.get('l')
						|| movements.get('l') == -1;
		default:
			return false;
		}

	}

	/**
	 * Get the Squares between this piece and the target piece
	 * 
	 * @param target The piece in the line of sight
	 * @param inclusive Whether or not to include the target piece square
	 * @return The Squares between this Piece and the target piece
	 */
	public Square[] getLineOfSight(Piece target, boolean inclusive)
	{
		return getLineOfSight(target.getSquare().getRow(), target.getSquare()
				.getCol(), inclusive);
	}

	/**
	 * @return the moveCount
	 */
	public int getMoveCount()
	{
		return moveCount;
	}

	/**
	 * Getter method for the name of this Piece
	 * 
	 * @return The name of this Piece
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the originalSquare
	 */
	public Square getOriginalSquare()
	{
		return originalSquare;
	}

	/**
	 * Getter method for the piece pinning this piece
	 * 
	 * @return The piece pinning this piece
	 */
	public Piece getPinnedBy()
	{
		return pinnedBy;
	}

	/**
	 * Getter method for the Square this Piece is on.
	 * 
	 * @return The Square this Piece occupies.
	 */
	public Square getSquare()
	{
		return curSquare;
	}

	/**
	 * Getter method for the color of this Piece
	 * 
	 * @return The color of this Piece
	 */
	public boolean isBlack()
	{
		return (isBlack);
	}

	/**
	 * Check if the given Square can be saved from the given target by this
	 * piece
	 * 
	 * @param toSave The Square to save
	 * @param toBlock The Piece to block
	 * @return If the given Square can be saved
	 */
	public boolean isBlockable(Square toSave, Piece toBlock)
	{
		boolean blockable = false;
		Square[] lineOfSight = null;

		if (board.getGame().isStaleLegalDests())
		{
			board.getGame().genLegalDests();
		}

		lineOfSight = getLineOfSight(toBlock, false);
		int i = 0;
		while (!blockable && lineOfSight != null && i < lineOfSight.length)
		{
			blockable = (toSave.equals(lineOfSight[i++]));
		}

		return blockable;
	}

	/**
	 * Getter method for the captured state of this Piece
	 * 
	 * @return If the Piece has been captured
	 */
	public boolean isCaptured()
	{
		return captured;
	}

	/**
	 * Check if this piece is guarding the given square
	 * 
	 * @param sq The Square to assess
	 * @return If this piece is guarding the given square
	 */
	public boolean isGuarding(Square sq)
	{
		if (!captured)
		{
			if (board.getGame().isStaleLegalDests())
			{
				board.getGame().genLegalDests();
			}
			return getGuardSquares().contains(sq);
		} else
			return false;
	}

	/**
	 * See if this Piece is in check
	 * 
	 * @return If this Piece is in check
	 */
	public boolean isInCheck()
	{
		return board.getGame().isThreatened(this);
	}

	/**
	 * Check if the given Square is a legal attack for this Piece
	 * 
	 * @param threatened The Square to attack
	 * @return If this Square is a legal attack
	 */
	public boolean isLegalAttack(Square threatened)
	{
		if (name.equals("Pawn"))
		{
			if (board.getGame().isStaleLegalDests())
			{
				board.getGame().genLegalDests();
			}
			if (captured)
				return false;

			if (threatened.getCol() == curSquare.getCol())
				return false;

			else
				return (isLegalDest(threatened) || (threatened.getRow()
						- curSquare.getRow() == ((isBlack()) ? -1 : 1) && Math
						.abs(threatened.getCol() - curSquare.getCol()) == 1));
		}
		return isLegalDest(threatened);
	}

	/**
	 * Determine if a Square is a legal destination
	 * 
	 * @param dest The Square in question
	 * @return Whether or not that Square is a legal destination
	 */
	public boolean isLegalDest(Square dest)
	{
		if (!captured)
		{
			if (board.getGame().isStaleLegalDests())
			{
				board.getGame().genLegalDests();
			}
			return getLegalDests().contains(dest);
		} else
			return false;
	}

	/**
	 * @param isBlack the isBlack to set
	 */
	public void setBlack(boolean isBlack)
	{
		this.isBlack = isBlack;
	}

	/**
	 * Setter method for the captured state of the Piece
	 * 
	 * @param t Whether or not the Piece has been captured.
	 */
	public void setCaptured(boolean t)
	{
		// Clear both ArrayLists so the Piece can't move anymore
		getLegalDests().clear();
		getGuardSquares().clear();
		setPinnedBy(null);
		captured = t;
	}

	/**
	 * @param guardSquares the guardSquares to set
	 */
	public void setGuardSquares(List<Square> guardSquares)
	{
		this.guardSquares = guardSquares;
	}

	/**
	 * @param legalDests the legalDests to set
	 */
	public void setLegalDests(List<Square> legalDests)
	{
		this.legalDests = legalDests;
	}

	/**
	 * @param moveCount the moveCount to set
	 */
	public void setMoveCount(int moveCount)
	{
		this.moveCount = moveCount;
	}

	/**
	 * @param originalSquare the originalSquare to set
	 */
	public void setOriginalSquare(Square originalSquare)
	{
		this.originalSquare = originalSquare;
	}

	/**
	 * Limit the legal destinations of this piece if it is pinned by another
	 * piece
	 * 
	 * @param pinner The piece pinning this Piece
	 * @param lineOfSight The legal destinations to retain
	 */
	protected void setPinned(Piece pinner, List<Square> lineOfSight)
	{
		setPinnedBy(pinner);
		getLegalDests().retainAll(lineOfSight);
	}

	/**
	 * @param pinnedBy the pinnedBy to set
	 */
	public void setPinnedBy(Piece pinnedBy)
	{
		this.pinnedBy = pinnedBy;
	}

	/**
	 * Setter method for the Square this Piece is on.
	 * 
	 * @param curSquare The Square to set
	 */
	public void setSquare(Square curSquare)
	{
		this.curSquare = curSquare;
	}

	/**
	 * Setter method for the Piece's ability to leap over other Pieces.
	 */
	public void setLeaper()
	{
		leaper = true;
	}

	/**
	 * Getter for promotesTo
	 * 
	 * @return The list of type names it can promote to.
	 */
	public ArrayList<String> getPromotesTo()
	{
		return promotesTo;
	}

	/**
	 * Setter for promotesTo
	 * 
	 * @param promotesTo The new ArrayList for promotesTo.
	 */
	public void setPromotesTo(ArrayList<String> promotesTo)
	{
		this.promotesTo = promotesTo;

	}

}
