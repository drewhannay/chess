package logic;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import utility.ImageUtility;

import com.google.common.collect.Lists;

public class Piece implements Serializable
{
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
	 * @param movements Map of legal movements for this Piece
	 */
	public Piece(String name, boolean isBlack, Square curSquare, Board board, Map<Character, Integer> movements)
	{
		m_name = name;
		m_lightIcon = ImageUtility.getLightImage(name);
		m_darkIcon = ImageUtility.getDarkImage(name);
		setBlack(isBlack);
		m_curSquare = curSquare;
		setOriginalSquare(curSquare);
		// Tell the Square what Piece is on it.
		curSquare.setPiece(this);
		m_board = board;
		m_movements = movements;

		List<Square> legalDests = Lists.newArrayList();
		setLegalDests(legalDests);

		List<Square> guardSquares = Lists.newArrayList();
		setGuardSquares(guardSquares);
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
		// If the Square is not habitable, don't add it, but return true so we
		// can move past it.
		if (!dest.isHabitable())
			return true;

		if (dest.getRow() == m_curSquare.getRow() && dest.getCol() == m_curSquare.getCol())
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

		if (((isBlack() ? m_board.getGame().getBlackRules() : m_board.getGame().getWhiteRules()).objectivePiece(isBlack()) == this)
				&& (m_board.getGame().isBlackMove() == isBlack()))
		{
			List<Square> tmpLegalDests = getLegalDests();
			Iterator<Square> perlimMoves = tmpLegalDests.iterator();

			List<Square> legalDests = Lists.newArrayList();
			setLegalDests(legalDests);

			// Make sure the you don't move into check
			Square dest;
			while (perlimMoves.hasNext())
			{
				dest = perlimMoves.next();

				if (!m_board.getGame().isThreatened(dest, !isBlack()) && !m_board.getGame().isGuarded(dest, !isBlack()))
					addLegalDest(dest);
			}

			if (m_board.getGame().isClassicChess())
			{
				// Castling
				if (getMoveCount() == 0)
				{
					boolean blocked = false;
					// Castle Queen side
					Piece rook = m_board.getSquare(m_curSquare.getRow(), 1).getPiece();
					if (rook != null && rook.getMoveCount() == 0)
					{
						blocked = false;

						for (int c = (rook.getSquare().getCol() + 1); c <= m_curSquare.getCol() && !blocked; c++)
						{
							if (c < m_curSquare.getCol())
								blocked = m_board.getSquare(m_curSquare.getRow(), c).isOccupied();

							if (!blocked)
								blocked = m_board.getGame().isThreatened(m_board.getSquare(m_curSquare.getRow(), c), !isBlack());
						}

						if (!blocked)
							addLegalDest(m_board.getSquare(((isBlack()) ? 8 : 1), 3));
					}

					// Castle King side
					rook = m_board.getSquare(m_curSquare.getRow(), m_board.getMaxCol()).getPiece();
					if (rook != null && rook.getMoveCount() == 0)
					{
						blocked = false;

						for (int c = (rook.getSquare().getCol() - 1); c >= m_curSquare.getCol() && !blocked; c--)
						{
							if (c > m_curSquare.getCol())
								blocked = m_board.getSquare(m_curSquare.getRow(), c).isOccupied();

							if (!blocked)
								blocked = m_board.getGame().isThreatened(m_board.getSquare(m_curSquare.getRow(), c), !isBlack());
						}

						if (!blocked)
							addLegalDest(m_board.getSquare(((isBlack()) ? 8 : 1), 7));
					}
				}
			}
			return;
		}

		Square[] line = getLineOfSight(objectivePiece, false);
		Piece pin = null;
		Piece tmp;
		boolean done = false;

		if (m_captured)
			return;

		if (line != null)
		{
			List<Square> temp = Lists.newArrayList();
			for (Square sq : line)
			{
				if (m_legalDests.contains(sq) || sq.equals(m_curSquare))
					temp.add(sq);
			}
			line = new Square[temp.size()];
			temp.toArray(line);

			if (m_board.getGame().isStaleLegalDests())
				m_board.getGame().genLegalDests();

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
					}
					else
					{
						pin = tmp;
					}
				}
			}

			if (pin != null)
			{
				// need to AND moves with line (includes this square)
				List<Square> maintainPins = Arrays.asList(line);
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
		if (m_name.equals("Pawn"))
		{
			Square dest = null;
			int dir, row, col;
			setPinnedBy(null);
			if (m_captured)
				return 0;

			dir = (isBlack()) ? -1 : 1;

			// Take one step forward
			if (board.isRowValid(m_curSquare.getRow() + dir))
			{
				dest = board.getSquare(m_curSquare.getRow() + dir, m_curSquare.getCol());
				if (!dest.isOccupied() && !getLegalDests().contains(dest))
					addLegalDest(dest);
			}

			// Take an opposing piece
			if (board.isRowValid(row = (m_curSquare.getRow() + dir)))
			{
				col = m_curSquare.getCol();

				// if valid row
				// and the square is occupied (by the other team)
				// or it's not my move (so I'm threatening the square)
				if (board.isColValid((col + 1))
						&& ((board.getSquare(row, col + 1).isOccupied() && isBlack() != board.getSquare(row, col + 1).getPiece()
								.isBlack())))
				{
					addLegalDest(board.getSquare(row, col + 1));
				}
				if (board.isColValid((col - 1))
						&& ((board.getSquare(row, col - 1).isOccupied() && isBlack() != board.getSquare(row, col - 1).getPiece()
								.isBlack())))
				{
					addLegalDest(board.getSquare(row, col - 1));
				}
			}

			// two step
			if (getMoveCount() == 0 && board.isRowValid((m_curSquare.getRow() + (2 * dir))))
			{
				dest = board.getSquare((m_curSquare.getRow() + (2 * dir)), m_curSquare.getCol());
				if (!dest.isOccupied() && !board.getSquare((m_curSquare.getRow() + dir), m_curSquare.getCol()).isOccupied()
						&& !getLegalDests().contains(dest))
				{
					addLegalDest(dest);
				}
			}

			if (board.getGame().isClassicChess())
			{
				// enPassant
				if (isBlack() == board.isBlackTurn()
						&& ((!isBlack() && m_curSquare.getRow() == 5) || (isBlack() && m_curSquare.getRow() == 4)))
				{
					col = m_curSquare.getCol();
					row = isBlack() ? m_curSquare.getRow() - 1 : m_curSquare.getRow() + 1;
					if (board.isColValid(col + 1) && board.getEnpassantCol() == (col + 1))
						addLegalDest(board.getSquare(row, (col + 1)));

					if (board.isColValid(col - 1) && board.getEnpassantCol() == (col - 1))
						addLegalDest(board.getSquare(row, (col - 1)));
				}
			}
			return getLegalDests().size();
		}

		boolean done = false;
		Square dest;
		boolean wraparound = board.isWrapAround();
		/*
		 * East
		 */
		if (m_movements.containsKey('E'))
		{
			int northMax = m_movements.get('E') + m_curSquare.getCol();
			if (northMax > board.getMaxCol() || m_movements.get('E') == -1)
			{
				if (!wraparound)
					northMax = board.getMaxCol();
			}
			for (int c = m_curSquare.getCol() + 1; ((m_movements.get('E') == -1 && wraparound) ? true : c <= northMax) && !done; c++)
			{
				int j = c;
				if (wraparound)
				{
					if (j > board.getMaxCol())
						j = j % board.getMaxCol();
				}

				if (j == 0)
					break;

				dest = board.getSquare(m_curSquare.getRow(), j);
				done = !addLegalDest(dest);
				done = m_isLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * West
		 */
		if (m_movements.containsKey('W'))
		{
			int southMax = m_curSquare.getCol() - m_movements.get('W');
			if (southMax < 1 || m_movements.get('W') == -1)
			{
				if (!wraparound)
					southMax = 1;
			}
			for (int c = m_curSquare.getCol() - 1; ((m_movements.get('W') == -1 && wraparound) ? true : c >= southMax) && !done; c--)
			{
				int j = c;
				if (wraparound)
				{
					if (j < 1)
						j = board.getMaxCol() + j;
				}

				dest = board.getSquare(m_curSquare.getRow(), j);
				done = !addLegalDest(dest);
				done = m_isLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * North
		 */
		if (m_movements.containsKey('N'))
		{
			int eastMax = m_movements.get('N') + m_curSquare.getRow();

			if (eastMax >= board.getMaxRow() || m_movements.get('N') == -1)
				eastMax = board.getMaxRow();

			for (int r = m_curSquare.getRow() + 1; (r <= eastMax) && !done; r++)
			{
				int j = r;
				dest = board.getSquare(j, m_curSquare.getCol());
				done = !addLegalDest(dest);
				done = m_isLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * South
		 */
		if (m_movements.containsKey('S'))
		{
			int westMax = m_curSquare.getRow() - m_movements.get('S');

			if (westMax < 1 || m_movements.get('S') == -1)
				westMax = 1;

			for (int r = m_curSquare.getRow() - 1; (r >= westMax) && !done; r--)
			{
				int j = r;
				dest = board.getSquare(j, m_curSquare.getCol());
				done = !addLegalDest(dest);
				done = m_isLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * NorthEast
		 */
		done = false;
		if (m_movements.containsKey('R'))
		{
			int neMax = ((m_curSquare.getRow() >= m_curSquare.getCol()) ? m_curSquare.getRow() : m_curSquare.getCol())
					+ m_movements.get('R');

			if (neMax >= board.getMaxCol() || m_movements.get('R') == -1)
				neMax = board.getMaxCol();
			if (neMax >= board.getMaxRow() || m_movements.get('R') == -1)
				neMax = board.getMaxRow();

			for (int r = m_curSquare.getRow() + 1, c = m_curSquare.getCol() + 1; r <= neMax && c <= neMax && !done; r++, c++)
			{
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = m_isLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}

		/*
		 * SouthEast
		 */
		done = false;
		if (m_movements.containsKey('r'))
		{
			int eastMax = m_curSquare.getCol() + m_movements.get('r');

			if (eastMax >= board.getMaxCol() || m_movements.get('r') == -1)
				eastMax = board.getMaxCol();

			int southMin = m_curSquare.getRow() - m_movements.get('r');

			if (southMin <= 1 || m_movements.get('R') == -1)
				southMin = 1;

			for (int r = m_curSquare.getRow() - 1, c = m_curSquare.getCol() + 1; r >= southMin && c <= eastMax && !done; r--, c++)
			{
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = m_isLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * NorthWest
		 */
		done = false;
		if (m_movements.containsKey('L'))
		{
			int westMin = m_curSquare.getCol() - m_movements.get('L');
			if (westMin <= 1 || m_movements.get('L') == -1)
				westMin = 1;

			int NorthMax = m_curSquare.getRow() + m_movements.get('L');
			if (NorthMax >= board.getMaxRow() || m_movements.get('L') == -1)
				NorthMax = board.getMaxRow();

			for (int r = m_curSquare.getRow() + 1, c = m_curSquare.getCol() - 1; r <= NorthMax && c >= westMin && !done; r++, c--)
			{
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = m_isLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * SouthWest
		 */
		done = false;
		if (m_movements.containsKey('l'))
		{
			int westMin = m_curSquare.getCol() - m_movements.get('l');
			if (westMin <= 1 || m_movements.get('l') == -1)
				westMin = 1;

			int southMin = m_curSquare.getRow() - m_movements.get('l');
			if (southMin <= 1 || m_movements.get('l') == -1)
				southMin = 1;

			for (int r = m_curSquare.getRow() - 1, c = m_curSquare.getCol() - 1; r >= southMin && c >= westMin && !done; r--, c--)
			{
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = m_isLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
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
		if (m_movements.containsKey('x'))
		{
			int f, r;
			int Rank = m_movements.get('x');
			int File = m_movements.get('y');
			f = (m_curSquare.getRow() + File);
			r = (m_curSquare.getCol() + Rank);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
					r = r % board.getMaxCol();
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// two o'clock
			f = (m_curSquare.getRow() + Rank);
			r = (m_curSquare.getCol() + File);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
					r = r % board.getMaxCol();
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// four o'clock
			f = (m_curSquare.getRow() + File);
			r = (m_curSquare.getCol() - Rank);
			if (wraparound)
			{
				if (r < 1)
					r = board.getMaxCol() + r;
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// five o'clock
			f = (m_curSquare.getRow() + Rank);
			r = (m_curSquare.getCol() - File);
			if (wraparound)
			{
				if (r < 1)
					r = board.getMaxCol() + r;
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// seven o'clock
			f = (m_curSquare.getRow() - File);
			r = (m_curSquare.getCol() - Rank);
			if (wraparound)
			{
				if (r < 1)
					r = board.getMaxCol() + r;
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// eight o'clock
			f = (m_curSquare.getRow() - Rank);
			r = (m_curSquare.getCol() - File);
			if (wraparound)
			{
				if (r < 1)
					r = board.getMaxCol() + r;
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// ten o'clock
			f = (m_curSquare.getRow() - File);
			r = (m_curSquare.getCol() + Rank);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
					r = r % board.getMaxCol();
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// eleven o'clock
			f = (m_curSquare.getRow() - Rank);
			r = (m_curSquare.getCol() + File);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
					r = r % board.getMaxCol();
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));
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
		if ((isBlack() ? m_board.getGame().getBlackRules() : m_board.getGame().getWhiteRules()).objectivePiece(isBlack()) == this)
			return;
		if (king == null)
			return;
		Iterator<Square> oldLegalDests = getLegalDests().iterator();
		Square sq = null;

		if (m_captured)
			return;

		List<Square> legalDests = Lists.newArrayList();
		setLegalDests(legalDests);

		while (oldLegalDests.hasNext())
		{
			sq = oldLegalDests.next();

			if (threat.isBlockable(sq, king))
				getLegalDests().add(sq);
			else if (sq.equals(threat.getSquare()))
				getLegalDests().add(sq);
		}
	}

	public Board getBoard()
	{
		return m_board;
	}

	public List<Square> getGuardSquares()
	{
		return m_guardSquares;
	}

	public ImageIcon getIcon()
	{
		return isBlack() ? m_darkIcon : m_lightIcon;
	}

	public List<Square> getLegalDests()
	{
		if (m_board.getGame().isStaleLegalDests())
		{
			m_board.getGame().genLegalDests();
		}
		return m_legalDests;
	}

	/**
	 * Get the Squares between this piece and the target piece
	 * 
	 * @param targetRow The row of the target
	 * @param targetCol The column of the target
	 * @param inclusive Whether or not to include the target piece square
	 * @return The Squares between this Piece and the target piece
	 */
	public Square[] getLineOfSight(int targetRow, int targetCol, boolean inclusive)
	{
		if (m_name.equals("Pawn"))
			return null;
		if ((m_isBlack ? m_board.getGame().getBlackRules() : m_board.getGame().getWhiteRules()).objectivePiece(m_isBlack).equals(this))
			return null;
		Square[] returnSet = null;
		List<Square> returnTemp = Lists.newArrayList();
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
				{
					if (r != targetRow || inclusive)
						returnTemp.add(i++, m_board.getSquare(r, originCol));
				}
			}
			// South
			else
			{
				if (canAttack(targetRow, targetCol, 'S'))
				{
					for (r = (originRow - 1); r >= targetRow; r--)
					{
						if (r != targetRow || inclusive)
							returnTemp.add(i++, m_board.getSquare(r, originCol));
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
				{
					if (c != targetCol || inclusive)
						returnTemp.add(i++, m_board.getSquare(originRow, c));
				}
			}
			// West
			else
			{
				if (canAttack(targetRow, targetCol, 'W'))
				{
					for (c = (originCol - 1); c >= targetCol; c--)
					{
						if (c != targetCol || inclusive)
							returnTemp.add(i++, m_board.getSquare(originRow, c));
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
				{
					if (r != targetRow || inclusive)
						returnTemp.add(i++, m_board.getSquare(r, c));
				}
			}
			// Southwest
			else
			{
				if (canAttack(targetRow, targetCol, 'l'))
				{
					for (c = (originCol - 1), r = (originRow - 1); r >= targetRow; c--, r--)
					{
						if (r != targetRow || inclusive)
							returnTemp.add(i++, m_board.getSquare(r, c));
					}
				}
			}
		}
		// Second diagonal
		else if ((originCol - targetCol) == ((originRow - targetRow) * -1))
		{
			// Northwest
			if ((originRow - targetRow) < 0 && canAttack(targetRow, targetCol, 'L'))
			{
				for (c = (originCol - 1), r = (originRow + 1); r <= targetRow; c--, r++)
				{
					if (r != targetRow || inclusive)
						returnTemp.add(i++, m_board.getSquare(r, c));
				}
			}
			// Southeast
			else
			{
				if (canAttack(targetRow, targetCol, 'r'))
				{
					for (c = (originCol + 1), r = (originRow - 1); r >= targetRow; c++, r--)
					{
						if (r != targetRow || inclusive)
							returnTemp.add(i++, m_board.getSquare(r, c));
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
				returnSet[j++] = sq;
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
			if (m_movements.containsKey('S'))
				return (destRow - m_curSquare.getRow()) < m_movements.get('S') || m_movements.get('S') == -1;
		case 'N': // North
			if (m_movements.containsKey('N'))
				return (m_curSquare.getRow() - destRow) < m_movements.get('N') || m_movements.get('N') == -1;
		case 'E': // East
			if (m_movements.containsKey('E'))
				return (destCol - m_curSquare.getCol()) < m_movements.get('E') || m_movements.get('E') == -1;
		case 'W': // West
			if (m_movements.containsKey('W'))
				return (m_curSquare.getCol() - destCol) < m_movements.get('W') || m_movements.get('W') == -1;
		case 'R': // NorthEast
			if (m_movements.containsKey('R'))
				return (m_curSquare.getCol() - destCol) < m_movements.get('R') || m_movements.get('R') == -1;
		case 'r': // SouthEast
			if (m_movements.containsKey('r'))
				return (m_curSquare.getCol() - destCol) < m_movements.get('r') || m_movements.get('r') == -1;
		case 'L': // NorthWest
			if (m_movements.containsKey('L'))
				return (destCol - m_curSquare.getCol()) < m_movements.get('L') || m_movements.get('L') == -1;
		case 'l': // SouthWest
			if (m_movements.containsKey('l'))
				return (destCol - m_curSquare.getCol()) < m_movements.get('l') || m_movements.get('l') == -1;
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
		return getLineOfSight(target.getSquare().getRow(), target.getSquare().getCol(), inclusive);
	}

	public int getMoveCount()
	{
		return m_moveCount;
	}

	public String getName()
	{
		return m_name;
	}

	public Square getOriginalSquare()
	{
		return m_originalSquare;
	}

	public Piece getPinnedBy()
	{
		return m_pinnedBy;
	}

	public Square getSquare()
	{
		return m_curSquare;
	}

	public boolean isBlack()
	{
		return (m_isBlack);
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

		if (m_board.getGame().isStaleLegalDests())
			m_board.getGame().genLegalDests();

		lineOfSight = getLineOfSight(toBlock, false);
		int i = 0;
		while (!blockable && lineOfSight != null && i < lineOfSight.length)
			blockable = (toSave.equals(lineOfSight[i++]));

		return blockable;
	}

	public boolean isCaptured()
	{
		return m_captured;
	}

	public boolean isGuarding(Square sq)
	{
		if (!m_captured)
		{
			if (m_board.getGame().isStaleLegalDests())
				m_board.getGame().genLegalDests();
			return getGuardSquares().contains(sq);
		}
		else
		{
			return false;
		}
	}

	public boolean isInCheck()
	{
		return m_board.getGame().isThreatened(this);
	}

	/**
	 * Check if the given Square is a legal attack for this Piece
	 * 
	 * @param threatened The Square to attack
	 * @return If this Square is a legal attack
	 */
	public boolean isLegalAttack(Square threatened)
	{
		if (m_name.equals("Pawn"))
		{
			if (m_board.getGame().isStaleLegalDests())
				m_board.getGame().genLegalDests();

			if (m_captured)
				return false;

			if (threatened.getCol() == m_curSquare.getCol())
				return false;
			else
				return (isLegalDest(threatened) || (threatened.getRow() - m_curSquare.getRow() == ((isBlack()) ? -1 : 1) && Math
						.abs(threatened.getCol() - m_curSquare.getCol()) == 1));
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
		if (!m_captured)
		{
			if (m_board.getGame().isStaleLegalDests())
				m_board.getGame().genLegalDests();
			return getLegalDests().contains(dest);
		}
		else
		{
			return false;
		}
	}

	public void setBlack(boolean isBlack)
	{
		m_isBlack = isBlack;
	}

	public void setIsCaptured(boolean t)
	{
		// Clear both Lists so the Piece can't move anymore
		getLegalDests().clear();
		getGuardSquares().clear();
		setPinnedBy(null);
		m_captured = t;
	}

	public void setGuardSquares(List<Square> guardSquares)
	{
		m_guardSquares = guardSquares;
	}

	public void setLegalDests(List<Square> legalDests)
	{
		m_legalDests = legalDests;
	}

	public void setMoveCount(int moveCount)
	{
		m_moveCount = moveCount;
	}

	public void setOriginalSquare(Square originalSquare)
	{
		m_originalSquare = originalSquare;
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

	public void setPinnedBy(Piece pinnedBy)
	{
		m_pinnedBy = pinnedBy;
	}

	public void setSquare(Square curSquare)
	{
		m_curSquare = curSquare;
	}

	public void setIsLeaper()
	{
		m_isLeaper = true;
	}

	public List<String> getPromotesTo()
	{
		return m_promotesTo;
	}

	public void setPromotesTo(List<String> promotesTo)
	{
		m_promotesTo = promotesTo;

	}

	private static final long serialVersionUID = -6571501595221097922L;

	private boolean m_isBlack;
	protected boolean m_captured;
	protected List<Square> m_guardSquares;
	protected List<Square> m_legalDests;
	protected Square m_curSquare;
	protected Board m_board;
	protected String m_name;
	protected Map<Character, Integer> m_movements;
	protected ImageIcon m_lightIcon;
	protected ImageIcon m_darkIcon;
	protected boolean m_isLeaper = false;
	protected int m_moveCount;
	protected Piece m_pinnedBy;
	protected Square m_originalSquare;
	protected List<String> m_promotesTo = Lists.newArrayList();
}
