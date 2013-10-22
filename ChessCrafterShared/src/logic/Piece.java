package logic;

import java.io.IOException;
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
	 * @throws IOException
	 */
	public Piece(String name, boolean isBlack, Square curSquare, Board board, Map<Character, Integer> movements) throws IOException
	{
		mName = name;

		mLightIcon = ImageUtility.getLightImage(name);
		mDarkIcon = ImageUtility.getDarkImage(name);

		setBlack(isBlack);
		mCurrentSquare = curSquare;
		setOriginalSquare(curSquare);
		// Tell the Square what Piece is on it.
		curSquare.setPiece(this);
		mBoard = board;
		mMovements = movements;

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

		if (dest.getRow() == mCurrentSquare.getRow() && dest.getCol() == mCurrentSquare.getCol())
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

		if (((isBlack() ? mBoard.getGame().getBlackRules() : mBoard.getGame().getWhiteRules()).objectivePiece(isBlack()) == this)
				&& (mBoard.getGame().isBlackMove() == isBlack()))
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

				if (!mBoard.getGame().isThreatened(dest, !isBlack()) && !mBoard.getGame().isGuarded(dest, !isBlack()))
					addLegalDest(dest);
			}

			if (mBoard.getGame().isClassicChess())
			{
				// Castling
				if (getMoveCount() == 0)
				{
					boolean blocked = false;
					// Castle Queen side
					Piece rook = mBoard.getSquare(mCurrentSquare.getRow(), 1).getPiece();
					if (rook != null && rook.getMoveCount() == 0)
					{
						blocked = false;

						for (int c = (rook.getSquare().getCol() + 1); c <= mCurrentSquare.getCol() && !blocked; c++)
						{
							if (c < mCurrentSquare.getCol())
								blocked = mBoard.getSquare(mCurrentSquare.getRow(), c).isOccupied();

							if (!blocked)
								blocked = mBoard.getGame().isThreatened(mBoard.getSquare(mCurrentSquare.getRow(), c), !isBlack());
						}

						if (!blocked)
							addLegalDest(mBoard.getSquare(((isBlack()) ? 8 : 1), 3));
					}

					// Castle King side
					rook = mBoard.getSquare(mCurrentSquare.getRow(), mBoard.getMaxCol()).getPiece();
					if (rook != null && rook.getMoveCount() == 0)
					{
						blocked = false;

						for (int c = (rook.getSquare().getCol() - 1); c >= mCurrentSquare.getCol() && !blocked; c--)
						{
							if (c > mCurrentSquare.getCol())
								blocked = mBoard.getSquare(mCurrentSquare.getRow(), c).isOccupied();

							if (!blocked)
								blocked = mBoard.getGame().isThreatened(mBoard.getSquare(mCurrentSquare.getRow(), c), !isBlack());
						}

						if (!blocked)
							addLegalDest(mBoard.getSquare(((isBlack()) ? 8 : 1), 7));
					}
				}
			}
			return;
		}

		Square[] line = getLineOfSight(objectivePiece, false);
		Piece pin = null;
		Piece tmp;
		boolean done = false;

		if (mCaptured)
			return;

		if (line != null)
		{
			List<Square> temp = Lists.newArrayList();
			for (Square sq : line)
			{
				if (mLegalDests.contains(sq) || sq.equals(mCurrentSquare))
					temp.add(sq);
			}
			line = new Square[temp.size()];
			temp.toArray(line);

			if (mBoard.getGame().isStaleLegalDests())
				mBoard.getGame().genLegalDests();

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
		if (mName.equals(Messages.getString("pawn"))) //$NON-NLS-1$
		{
			Square dest = null;
			int dir, row, col;
			setPinnedBy(null);
			if (mCaptured)
				return 0;

			dir = (isBlack()) ? -1 : 1;

			// Take one step forward
			if (board.isRowValid(mCurrentSquare.getRow() + dir))
			{
				dest = board.getSquare(mCurrentSquare.getRow() + dir, mCurrentSquare.getCol());
				if (!dest.isOccupied() && !getLegalDests().contains(dest))
					addLegalDest(dest);
			}

			// Take an opposing piece
			if (board.isRowValid(row = (mCurrentSquare.getRow() + dir)))
			{
				col = mCurrentSquare.getCol();

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
			if (getMoveCount() == 0 && board.isRowValid((mCurrentSquare.getRow() + (2 * dir))))
			{
				dest = board.getSquare((mCurrentSquare.getRow() + (2 * dir)), mCurrentSquare.getCol());
				if (!dest.isOccupied() && !board.getSquare((mCurrentSquare.getRow() + dir), mCurrentSquare.getCol()).isOccupied()
						&& !getLegalDests().contains(dest))
				{
					addLegalDest(dest);
				}
			}

			if (board.getGame().isClassicChess())
			{
				// enPassant
				if (isBlack() == board.isBlackTurn()
						&& ((!isBlack() && mCurrentSquare.getRow() == 5) || (isBlack() && mCurrentSquare.getRow() == 4)))
				{
					col = mCurrentSquare.getCol();
					row = isBlack() ? mCurrentSquare.getRow() - 1 : mCurrentSquare.getRow() + 1;
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
		if (mMovements.containsKey(PieceBuilder.EAST))
		{
			int northMax = mMovements.get(PieceBuilder.EAST) + mCurrentSquare.getCol();
			if (northMax > board.getMaxCol() || mMovements.get(PieceBuilder.EAST) == -1)
			{
				if (!wraparound)
					northMax = board.getMaxCol();
			}
			for (int c = mCurrentSquare.getCol() + 1; ((mMovements.get(PieceBuilder.EAST) == -1 && wraparound) ? true : c <= northMax)
					&& !done; c++)
			{
				int j = c;
				if (wraparound)
				{
					if (j > board.getMaxCol())
						j = j % board.getMaxCol();
				}

				if (j == 0)
					break;

				dest = board.getSquare(mCurrentSquare.getRow(), j);
				done = !addLegalDest(dest);
				done = mIsLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * West
		 */
		if (mMovements.containsKey(PieceBuilder.WEST))
		{
			int southMax = mCurrentSquare.getCol() - mMovements.get(PieceBuilder.WEST);
			if (southMax < 1 || mMovements.get(PieceBuilder.WEST) == -1)
			{
				if (!wraparound)
					southMax = 1;
			}
			for (int c = mCurrentSquare.getCol() - 1; ((mMovements.get(PieceBuilder.WEST) == -1 && wraparound) ? true : c >= southMax)
					&& !done; c--)
			{
				int j = c;
				if (wraparound)
				{
					if (j < 1)
						j = board.getMaxCol() + j;
				}

				dest = board.getSquare(mCurrentSquare.getRow(), j);
				done = !addLegalDest(dest);
				done = mIsLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * North
		 */
		if (mMovements.containsKey(PieceBuilder.NORTH))
		{
			int eastMax = mMovements.get(PieceBuilder.NORTH) + mCurrentSquare.getRow();

			if (eastMax >= board.getMaxRow() || mMovements.get(PieceBuilder.NORTH) == -1)
				eastMax = board.getMaxRow();

			for (int r = mCurrentSquare.getRow() + 1; (r <= eastMax) && !done; r++)
			{
				int j = r;
				dest = board.getSquare(j, mCurrentSquare.getCol());
				done = !addLegalDest(dest);
				done = mIsLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		done = false;
		/*
		 * South
		 */
		if (mMovements.containsKey(PieceBuilder.SOUTH))
		{
			int westMax = mCurrentSquare.getRow() - mMovements.get(PieceBuilder.SOUTH);

			if (westMax < 1 || mMovements.get(PieceBuilder.SOUTH) == -1)
				westMax = 1;

			for (int r = mCurrentSquare.getRow() - 1; (r >= westMax) && !done; r--)
			{
				int j = r;
				dest = board.getSquare(j, mCurrentSquare.getCol());
				done = !addLegalDest(dest);
				done = mIsLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * NorthEast
		 */
		done = false;
		if (mMovements.containsKey(PieceBuilder.NORTHEAST))
		{
			int neMax = ((mCurrentSquare.getRow() >= mCurrentSquare.getCol()) ? mCurrentSquare.getRow() : mCurrentSquare.getCol())
					+ mMovements.get(PieceBuilder.NORTHEAST);

			if (neMax >= board.getMaxCol() || mMovements.get(PieceBuilder.NORTHEAST) == -1)
				neMax = board.getMaxCol();
			if (neMax >= board.getMaxRow() || mMovements.get(PieceBuilder.NORTHEAST) == -1)
				neMax = board.getMaxRow();

			for (int r = mCurrentSquare.getRow() + 1, c = mCurrentSquare.getCol() + 1; r <= neMax && c <= neMax && !done; r++, c++)
			{
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = mIsLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}

		/*
		 * SouthEast
		 */
		done = false;
		if (mMovements.containsKey(PieceBuilder.SOUTHEAST))
		{
			int eastMax = mCurrentSquare.getCol() + mMovements.get(PieceBuilder.SOUTHEAST);

			if (eastMax >= board.getMaxCol() || mMovements.get(PieceBuilder.SOUTHEAST) == -1)
				eastMax = board.getMaxCol();

			int southMin = mCurrentSquare.getRow() - mMovements.get(PieceBuilder.SOUTHEAST);

			if (southMin <= 1 || mMovements.get(PieceBuilder.SOUTHEAST) == -1)
				southMin = 1;

			for (int r = mCurrentSquare.getRow() - 1, c = mCurrentSquare.getCol() + 1; r >= southMin && c <= eastMax && !done; r--, c++)
			{
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = mIsLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * NorthWest
		 */
		done = false;
		if (mMovements.containsKey(PieceBuilder.NORTHWEST))
		{
			int westMin = mCurrentSquare.getCol() - mMovements.get(PieceBuilder.NORTHWEST);
			if (westMin <= 1 || mMovements.get(PieceBuilder.NORTHWEST) == -1)
				westMin = 1;

			int NorthMax = mCurrentSquare.getRow() + mMovements.get(PieceBuilder.NORTHWEST);
			if (NorthMax >= board.getMaxRow() || mMovements.get(PieceBuilder.NORTHWEST) == -1)
				NorthMax = board.getMaxRow();

			for (int r = mCurrentSquare.getRow() + 1, c = mCurrentSquare.getCol() - 1; r <= NorthMax && c >= westMin && !done; r++, c--)
			{
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = mIsLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
						.equals(board.getGame().getOtherObjectivePiece(isBlack())))));
			}
		}
		/*
		 * SouthWest
		 */
		done = false;
		if (mMovements.containsKey(PieceBuilder.SOUTHWEST))
		{
			int westMin = mCurrentSquare.getCol() - mMovements.get(PieceBuilder.SOUTHWEST);
			if (westMin <= 1 || mMovements.get(PieceBuilder.SOUTHWEST) == -1)
				westMin = 1;

			int southMin = mCurrentSquare.getRow() - mMovements.get(PieceBuilder.SOUTHWEST);
			if (southMin <= 1 || mMovements.get(PieceBuilder.SOUTHWEST) == -1)
				southMin = 1;

			for (int r = mCurrentSquare.getRow() - 1, c = mCurrentSquare.getCol() - 1; r >= southMin && c >= westMin && !done; r--, c--)
			{
				dest = board.getSquare(r, c);
				done = !addLegalDest(dest);
				done = mIsLeaper ? false : (done || (dest.isOccupied() && !(board.isBlackTurn() != isBlack() && dest.getPiece()
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
		if (mMovements.containsKey(PieceBuilder.KNIGHT_ONE))
		{
			int f, r;
			int Rank = mMovements.get(PieceBuilder.KNIGHT_ONE);
			int File = mMovements.get(PieceBuilder.KNIGHT_TWO);
			f = (mCurrentSquare.getRow() + File);
			r = (mCurrentSquare.getCol() + Rank);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
					r = r % board.getMaxCol();
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// two o'clock
			f = (mCurrentSquare.getRow() + Rank);
			r = (mCurrentSquare.getCol() + File);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
					r = r % board.getMaxCol();
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// four o'clock
			f = (mCurrentSquare.getRow() + File);
			r = (mCurrentSquare.getCol() - Rank);
			if (wraparound)
			{
				if (r < 1)
					r = board.getMaxCol() + r;
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// five o'clock
			f = (mCurrentSquare.getRow() + Rank);
			r = (mCurrentSquare.getCol() - File);
			if (wraparound)
			{
				if (r < 1)
					r = board.getMaxCol() + r;
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// seven o'clock
			f = (mCurrentSquare.getRow() - File);
			r = (mCurrentSquare.getCol() - Rank);
			if (wraparound)
			{
				if (r < 1)
					r = board.getMaxCol() + r;
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// eight o'clock
			f = (mCurrentSquare.getRow() - Rank);
			r = (mCurrentSquare.getCol() - File);
			if (wraparound)
			{
				if (r < 1)
					r = board.getMaxCol() + r;
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// ten o'clock
			f = (mCurrentSquare.getRow() - File);
			r = (mCurrentSquare.getCol() + Rank);
			if (wraparound)
			{
				if (r > board.getMaxCol() + 1)
					r = r % board.getMaxCol();
			}

			if (board.isRowValid(f) && board.isColValid(r))
				addLegalDest(board.getSquare(f, r));

			// eleven o'clock
			f = (mCurrentSquare.getRow() - Rank);
			r = (mCurrentSquare.getCol() + File);
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
		if ((isBlack() ? mBoard.getGame().getBlackRules() : mBoard.getGame().getWhiteRules()).objectivePiece(isBlack()) == this)
			return;
		if (king == null)
			return;
		Iterator<Square> oldLegalDests = getLegalDests().iterator();
		Square sq = null;

		if (mCaptured)
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
		return mBoard;
	}

	public List<Square> getGuardSquares()
	{
		return mGuardSquares;
	}

	public ImageIcon getIcon()
	{
		return isBlack() ? mDarkIcon : mLightIcon;
	}

	public List<Square> getLegalDests()
	{
		if (mBoard.getGame().isStaleLegalDests())
		{
			mBoard.getGame().genLegalDests();
		}
		return mLegalDests;
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
		if (mName.equals(Messages.getString("pawn"))) //$NON-NLS-1$
			return null;
		if ((mIsBlack ? mBoard.getGame().getBlackRules() : mBoard.getGame().getWhiteRules()).objectivePiece(mIsBlack).equals(this))
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
			if (originRow < targetRow && canAttack(targetRow, targetCol, PieceBuilder.NORTH))
			{
				for (r = (originRow + 1); r <= targetRow; r++)
				{
					if (r != targetRow || inclusive)
						returnTemp.add(i++, mBoard.getSquare(r, originCol));
				}
			}
			// South
			else
			{
				if (canAttack(targetRow, targetCol, PieceBuilder.SOUTH))
				{
					for (r = (originRow - 1); r >= targetRow; r--)
					{
						if (r != targetRow || inclusive)
							returnTemp.add(i++, mBoard.getSquare(r, originCol));
					}
				}
			}
		}

		// Same Row
		else if (originRow == targetRow)
		{
			// East
			if (originCol < targetCol && canAttack(targetRow, targetCol, PieceBuilder.EAST))
			{
				for (c = (originCol + 1); c <= targetCol; c++)
				{
					if (c != targetCol || inclusive)
						returnTemp.add(i++, mBoard.getSquare(originRow, c));
				}
			}
			// West
			else
			{
				if (canAttack(targetRow, targetCol, PieceBuilder.WEST))
				{
					for (c = (originCol - 1); c >= targetCol; c--)
					{
						if (c != targetCol || inclusive)
							returnTemp.add(i++, mBoard.getSquare(originRow, c));
					}
				}
			}
		}

		// First diagonal
		else if ((originCol - targetCol) == (originRow - targetRow))
		{
			// Northeast
			if (originRow < targetRow && canAttack(targetRow, targetCol, PieceBuilder.NORTHEAST))
			{
				for (c = (originCol + 1), r = (originRow + 1); r <= targetRow; c++, r++)
				{
					if (r != targetRow || inclusive)
						returnTemp.add(i++, mBoard.getSquare(r, c));
				}
			}
			// Southwest
			else
			{
				if (canAttack(targetRow, targetCol, PieceBuilder.SOUTHWEST))
				{
					for (c = (originCol - 1), r = (originRow - 1); r >= targetRow; c--, r--)
					{
						if (r != targetRow || inclusive)
							returnTemp.add(i++, mBoard.getSquare(r, c));
					}
				}
			}
		}
		// Second diagonal
		else if ((originCol - targetCol) == ((originRow - targetRow) * -1))
		{
			// Northwest
			if ((originRow - targetRow) < 0 && canAttack(targetRow, targetCol, PieceBuilder.NORTHWEST))
			{
				for (c = (originCol - 1), r = (originRow + 1); r <= targetRow; c--, r++)
				{
					if (r != targetRow || inclusive)
						returnTemp.add(i++, mBoard.getSquare(r, c));
				}
			}
			// Southeast
			else
			{
				if (canAttack(targetRow, targetCol, PieceBuilder.SOUTHEAST))
				{
					for (c = (originCol + 1), r = (originRow - 1); r >= targetRow; c++, r--)
					{
						if (r != targetRow || inclusive)
							returnTemp.add(i++, mBoard.getSquare(r, c));
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
		case PieceBuilder.SOUTH: // South
			if (mMovements.containsKey(PieceBuilder.SOUTH))
				return (destRow - mCurrentSquare.getRow()) < mMovements.get(PieceBuilder.SOUTH)
						|| mMovements.get(PieceBuilder.SOUTH) == -1;
		case PieceBuilder.NORTH: // North
			if (mMovements.containsKey(PieceBuilder.NORTH))
				return (mCurrentSquare.getRow() - destRow) < mMovements.get(PieceBuilder.NORTH)
						|| mMovements.get(PieceBuilder.NORTH) == -1;
		case PieceBuilder.EAST: // East
			if (mMovements.containsKey(PieceBuilder.EAST))
				return (destCol - mCurrentSquare.getCol()) < mMovements.get(PieceBuilder.EAST)
						|| mMovements.get(PieceBuilder.EAST) == -1;
		case PieceBuilder.WEST: // West
			if (mMovements.containsKey(PieceBuilder.WEST))
				return (mCurrentSquare.getCol() - destCol) < mMovements.get(PieceBuilder.WEST)
						|| mMovements.get(PieceBuilder.WEST) == -1;
		case PieceBuilder.NORTHEAST: // NorthEast
			if (mMovements.containsKey(PieceBuilder.NORTHEAST))
				return (mCurrentSquare.getCol() - destCol) < mMovements.get(PieceBuilder.NORTHEAST)
						|| mMovements.get(PieceBuilder.NORTHEAST) == -1;
		case PieceBuilder.SOUTHEAST: // SouthEast
			if (mMovements.containsKey(PieceBuilder.SOUTHEAST))
				return (mCurrentSquare.getCol() - destCol) < mMovements.get(PieceBuilder.SOUTHEAST)
						|| mMovements.get(PieceBuilder.SOUTHEAST) == -1;
		case PieceBuilder.NORTHWEST: // NorthWest
			if (mMovements.containsKey(PieceBuilder.NORTHWEST))
				return (destCol - mCurrentSquare.getCol()) < mMovements.get(PieceBuilder.NORTHWEST)
						|| mMovements.get(PieceBuilder.NORTHWEST) == -1;
		case PieceBuilder.SOUTHWEST: // SouthWest
			if (mMovements.containsKey(PieceBuilder.SOUTHWEST))
				return (destCol - mCurrentSquare.getCol()) < mMovements.get(PieceBuilder.SOUTHWEST)
						|| mMovements.get(PieceBuilder.SOUTHWEST) == -1;
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
		return mMoveCount;
	}

	public String getName()
	{
		return mName;
	}

	public Square getOriginalSquare()
	{
		return mOriginalSquare;
	}

	public Piece getPinnedBy()
	{
		return mPinnedBy;
	}

	public Square getSquare()
	{
		return mCurrentSquare;
	}

	public boolean isBlack()
	{
		return (mIsBlack);
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

		if (mBoard.getGame().isStaleLegalDests())
			mBoard.getGame().genLegalDests();

		lineOfSight = getLineOfSight(toBlock, false);
		int i = 0;
		while (!blockable && lineOfSight != null && i < lineOfSight.length)
			blockable = (toSave.equals(lineOfSight[i++]));

		return blockable;
	}

	public boolean isCaptured()
	{
		return mCaptured;
	}

	public boolean isGuarding(Square sq)
	{
		if (!mCaptured)
		{
			if (mBoard.getGame().isStaleLegalDests())
				mBoard.getGame().genLegalDests();
			return getGuardSquares().contains(sq);
		}
		else
		{
			return false;
		}
	}

	public boolean isInCheck()
	{
		return mBoard.getGame().isThreatened(this);
	}

	/**
	 * Check if the given Square is a legal attack for this Piece
	 * 
	 * @param threatened The Square to attack
	 * @return If this Square is a legal attack
	 */
	public boolean isLegalAttack(Square threatened)
	{
		if (mName.equals(Messages.getString("pawn"))) //$NON-NLS-1$
		{
			if (mBoard.getGame().isStaleLegalDests())
				mBoard.getGame().genLegalDests();

			if (mCaptured)
				return false;

			if (threatened.getCol() == mCurrentSquare.getCol())
				return false;
			else
				return (isLegalDest(threatened) || (threatened.getRow() - mCurrentSquare.getRow() == ((isBlack()) ? -1 : 1) && Math
						.abs(threatened.getCol() - mCurrentSquare.getCol()) == 1));
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
		if (!mCaptured)
		{
			if (mBoard.getGame().isStaleLegalDests())
				mBoard.getGame().genLegalDests();
			return getLegalDests().contains(dest);
		}
		else
		{
			return false;
		}
	}

	public void setBlack(boolean isBlack)
	{
		mIsBlack = isBlack;
	}

	public void setIsCaptured(boolean t)
	{
		// Clear both Lists so the Piece can't move anymore
		getLegalDests().clear();
		getGuardSquares().clear();
		setPinnedBy(null);
		mCaptured = t;
	}

	public void setGuardSquares(List<Square> guardSquares)
	{
		mGuardSquares = guardSquares;
	}

	public void setLegalDests(List<Square> legalDests)
	{
		mLegalDests = legalDests;
	}

	public void setMoveCount(int moveCount)
	{
		mMoveCount = moveCount;
	}

	public void setOriginalSquare(Square originalSquare)
	{
		mOriginalSquare = originalSquare;
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
		mPinnedBy = pinnedBy;
	}

	public void setSquare(Square curSquare)
	{
		mCurrentSquare = curSquare;
	}

	public void setIsLeaper()
	{
		mIsLeaper = true;
	}

	public List<String> getPromotesTo()
	{
		return mPromotesTo;
	}

	public void setPromotesTo(List<String> promotesTo)
	{
		mPromotesTo = promotesTo;
	}

	private static final long serialVersionUID = -6571501595221097922L;

	private boolean mIsBlack;
	protected boolean mCaptured;
	protected List<Square> mGuardSquares;
	protected List<Square> mLegalDests;
	protected Square mCurrentSquare;
	protected Board mBoard;
	protected String mName;
	protected Map<Character, Integer> mMovements;
	protected ImageIcon mLightIcon;
	protected ImageIcon mDarkIcon;
	protected boolean mIsLeaper = false;
	protected int mMoveCount;
	protected Piece mPinnedBy;
	protected Square mOriginalSquare;
	protected List<String> mPromotesTo = Lists.newArrayList();
}
