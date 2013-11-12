
package controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import models.BidirectionalMovement;
import models.Board;
import models.ChessCoordinates;
import models.Piece;
import models.PieceMovements;
import models.PieceMovements.MovementDirection;
import models.PieceType;
import models.Team;
import rules.Rules;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class ComputedPieceData
{
	public static Set<ChessCoordinates> getLegalDestinations(Piece piece)
	{
		int targetBoardIndex = piece.getCoordinates().boardIndex;
		Set<ChessCoordinates> occupiedSquares = getOccupiedSquares();
		Set<ChessCoordinates> guardedCoordinates = Sets.newHashSet();
		Set<ChessCoordinates> legalDestinations = Sets.newHashSet();
		Board board = GameController.getGame().getBoards()[targetBoardIndex];
		PieceType pieceType = piece.getPieceType();
		int pieceRow = piece.getCoordinates().row;
		int pieceColumn = piece.getCoordinates().column;
		int teamId = getTeamId(piece);
		
		// special case for Pawns, to incorporate enPassant, special
		// initial movement, and diagonal capturing
		if (pieceType.getName().equals(PieceType.PAWN_NAME))
		{
			return getPawnLegalDestinations(piece);
		}

		int distance;
		ChessCoordinates destination;
		boolean wraparound = board.isWrapAroundBoard();

		// east
		distance = pieceType.getPieceMovements().getDistance(MovementDirection.EAST);
		int northMax = distance + pieceColumn;
		if ((northMax > board.getColumnCount() || distance == PieceMovements.UNLIMITED) && !wraparound)
			northMax = board.getColumnCount();

		for (int c = pieceColumn + 1; (distance == PieceMovements.UNLIMITED && wraparound) ? true : c <= northMax; c++)
		{
			int j = c;
			if (wraparound && j > board.getColumnCount())
				j = j % board.getColumnCount();

			if (j == 0)
				break;

			destination = new ChessCoordinates(pieceRow, j, targetBoardIndex);

			boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
			if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
				legalDestinations.add(destination);

			if (isGuardingSquare(destination, teamId))
			{
				shouldBreak = true;
				guardedCoordinates.add(destination);
			}

			shouldBreak = pieceType.isLeaper() ? false : shouldBreak || occupiedSquares.contains(destination);

			if (shouldBreak)
				break;
		}

		// west
		distance = pieceType.getPieceMovements().getDistance(MovementDirection.WEST);
		int southMax = pieceColumn - distance;
		if ((southMax < 1 || distance == PieceMovements.UNLIMITED) && !wraparound)
			southMax = 1;

		for (int c = pieceColumn - 1; (distance == PieceMovements.UNLIMITED && wraparound) ? true : c >= southMax; c--)
		{
			int j = c;
			if (wraparound && j < 1)
				j = board.getColumnCount() + j;

			destination = new ChessCoordinates(pieceRow, j, targetBoardIndex);

			boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
			if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
				legalDestinations.add(destination);

			if (isGuardingSquare(destination, teamId))
			{
				shouldBreak = true;
				guardedCoordinates.add(destination);
			}

			shouldBreak = pieceType.isLeaper() ? false : shouldBreak || occupiedSquares.contains(destination);

			if (shouldBreak)
				break;
		}

		// north
		distance = pieceType.getPieceMovements().getDistance(MovementDirection.NORTH);
		int eastMax = distance + pieceRow;
		if (eastMax >= board.getRowCount() || distance == PieceMovements.UNLIMITED)
			eastMax = board.getRowCount();

		for (int r = pieceRow + 1; r <= eastMax; r++)
		{
			int j = r;

			destination = new ChessCoordinates(j, pieceColumn, targetBoardIndex);

			boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
			if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
				legalDestinations.add(destination);

			if (isGuardingSquare(destination, teamId))
			{
				shouldBreak = true;
				guardedCoordinates.add(destination);
			}

			shouldBreak = pieceType.isLeaper() ? false : shouldBreak || occupiedSquares.contains(destination);

			if (shouldBreak)
				break;
		}

		// south
		distance = pieceType.getPieceMovements().getDistance(MovementDirection.SOUTH);
		int westMax = pieceRow - distance;
		if (westMax < 1 || distance == PieceMovements.UNLIMITED)
			westMax = 1;

		for (int r = pieceRow - 1; (r >= westMax); r--)
		{
			int j = r;

			destination = new ChessCoordinates(j, pieceColumn, targetBoardIndex);

			boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
			if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
				legalDestinations.add(destination);

			if (isGuardingSquare(destination, teamId))
			{
				shouldBreak = true;
				guardedCoordinates.add(destination);
			}

			shouldBreak = pieceType.isLeaper() ? false : shouldBreak || occupiedSquares.contains(destination);

			if (shouldBreak)
				break;
		}

		// northeast
		distance = pieceType.getPieceMovements().getDistance(MovementDirection.NORTHEAST);
		int neMax = ((pieceRow >= pieceColumn) ? pieceRow : piece.getCoordinates().column) + distance;

		if (neMax >= board.getColumnCount() || distance == PieceMovements.UNLIMITED)
			neMax = board.getColumnCount();
		if (neMax >= board.getRowCount() || distance == PieceMovements.UNLIMITED)
			neMax = board.getRowCount();

		for (int r = pieceRow + 1, c = pieceColumn + 1; r <= neMax && c <= neMax; r++, c++)
		{
			destination = new ChessCoordinates(r, c, targetBoardIndex);

			boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
			if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
				legalDestinations.add(destination);

			if (isGuardingSquare(destination, teamId))
			{
				shouldBreak = true;
				guardedCoordinates.add(destination);
			}

			shouldBreak = pieceType.isLeaper() ? false : shouldBreak || occupiedSquares.contains(destination);

			if (shouldBreak)
				break;
		}

		// southeast
		distance = pieceType.getPieceMovements().getDistance(MovementDirection.SOUTHEAST);
		int eastMaximum = pieceColumn + distance;

		if (eastMaximum >= board.getColumnCount() || distance == PieceMovements.UNLIMITED)
			eastMaximum = board.getColumnCount();

		int southMin = pieceRow - distance;

		if (southMin <= 1 || distance == PieceMovements.UNLIMITED)
			southMin = 1;

		for (int r = pieceRow - 1, c = pieceColumn + 1; r >= southMin && c <= eastMaximum; r--, c++)
		{
			destination = new ChessCoordinates(r, c, targetBoardIndex);

			boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
			if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
				legalDestinations.add(destination);

			if (isGuardingSquare(destination, teamId))
			{
				shouldBreak = true;
				guardedCoordinates.add(destination);
			}

			shouldBreak = pieceType.isLeaper() ? false : shouldBreak || occupiedSquares.contains(destination);

			if (shouldBreak)
				break;
		}

		// northwest
		distance = pieceType.getPieceMovements().getDistance(MovementDirection.NORTHWEST);
		int westMin = pieceColumn - distance;
		if (westMin <= 1 || distance == PieceMovements.UNLIMITED)
			westMin = 1;

		northMax = pieceRow + distance;
		if (northMax >= board.getRowCount() || distance == PieceMovements.UNLIMITED)
			northMax = board.getRowCount();

		for (int r = pieceRow + 1, c = pieceColumn - 1; r <= northMax && c >= westMin; r++, c--)
		{
			destination = new ChessCoordinates(r, c, targetBoardIndex);

			boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
			if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
				legalDestinations.add(destination);

			if (isGuardingSquare(destination, teamId))
			{
				shouldBreak = true;
				guardedCoordinates.add(destination);
			}

			shouldBreak = pieceType.isLeaper() ? false : shouldBreak || occupiedSquares.contains(destination);

			if (shouldBreak)
				break;
		}

		// southwest
		distance = pieceType.getPieceMovements().getDistance(MovementDirection.SOUTHWEST);
		westMin = pieceColumn - distance;
		if (westMin <= 1 || distance == PieceMovements.UNLIMITED)
			westMin = 1;

		southMin = pieceRow - distance;
		if (southMin <= 1 || distance == PieceMovements.UNLIMITED)
			southMin = 1;

		for (int r = pieceRow - 1, c = pieceColumn - 1; r >= southMin && c >= westMin; r--, c--)
		{
			destination = new ChessCoordinates(r, c, targetBoardIndex);

			boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
			if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
				legalDestinations.add(destination);

			if (isGuardingSquare(destination, teamId))
			{
				shouldBreak = true;
				guardedCoordinates.add(destination);
			}

			shouldBreak = pieceType.isLeaper() ? false : shouldBreak || occupiedSquares.contains(destination);

			if (shouldBreak)
				break;
		}

		/*
		 * Bidirectional Movements
		 * Store of Bidirectional Movements are as followed:
		 * A Piece can move x File by y Rank squares at a time.
		 * IE: A knight can move 1 by 2 or 2 by 1, but not 1 by 1 or 2 by 2
		 */
		for (BidirectionalMovement movement : pieceType.getPieceMovements().getBidirectionalMovements())
		{
			int f, r;
			int rank = movement.getRowDistance();
			int file = movement.getColumnDistance();

			// one o'clock
			f = (pieceRow + file);
			r = (pieceColumn + rank);
			if (wraparound)
			{
				if (r > board.getColumnCount() + 1)
					r = r % board.getColumnCount();
			}

			if (board.isRowValid(f) && board.isColumnValid(r))
				legalDestinations.add(new ChessCoordinates(f, r, targetBoardIndex));

			// two o'clock
			f = (pieceRow + rank);
			r = (pieceColumn + file);
			if (wraparound)
			{
				if (r > board.getColumnCount() + 1)
					r = r % board.getColumnCount();
			}

			if (board.isRowValid(f) && board.isColumnValid(r))
				legalDestinations.add(new ChessCoordinates(f, r, targetBoardIndex));

			// four o'clock
			f = (pieceRow + file);
			r = (pieceColumn - rank);
			if (wraparound)
			{
				if (r < 1)
					r = board.getColumnCount() + r;
			}

			if (board.isRowValid(f) && board.isColumnValid(r))
				legalDestinations.add(new ChessCoordinates(f, r, targetBoardIndex));

			// five o'clock
			f = (pieceRow + rank);
			r = (pieceColumn - file);
			if (wraparound)
			{
				if (r < 1)
					r = board.getColumnCount() + r;
			}

			if (board.isRowValid(f) && board.isColumnValid(r))
				legalDestinations.add(new ChessCoordinates(f, r, targetBoardIndex));

			// seven o'clock
			f = (pieceRow - file);
			r = (pieceColumn - rank);
			if (wraparound)
			{
				if (r < 1)
					r = board.getColumnCount() + r;
			}

			if (board.isRowValid(f) && board.isColumnValid(r))
				legalDestinations.add(new ChessCoordinates(f, r, targetBoardIndex));

			// eight o'clock
			f = (pieceRow - rank);
			r = (pieceColumn - file);
			if (wraparound)
			{
				if (r < 1)
					r = board.getColumnCount() + r;
			}

			if (board.isRowValid(f) && board.isColumnValid(r))
				legalDestinations.add(new ChessCoordinates(f, r, targetBoardIndex));

			// ten o'clock
			f = (pieceRow - file);
			r = (pieceColumn + rank);
			if (wraparound)
			{
				if (r > board.getColumnCount() + 1)
					r = r % board.getColumnCount();
			}

			if (board.isRowValid(f) && board.isColumnValid(r))
				legalDestinations.add(new ChessCoordinates(f, r, targetBoardIndex));

			// eleven o'clock
			f = (pieceRow - rank);
			r = (pieceColumn + file);
			if (wraparound)
			{
				if (r > board.getColumnCount() + 1)
					r = r % board.getColumnCount();
			}

			if (board.isRowValid(f) && board.isColumnValid(r))
				legalDestinations.add(new ChessCoordinates(f, r, targetBoardIndex));
		}

		return legalDestinations;
	}

	private static Set<ChessCoordinates> getPawnLegalDestinations(Piece pawn)
	{
		int targetBoardIndex = pawn.getCoordinates().boardIndex;
		int pawnRow = pawn.getCoordinates().row;
		int pawnColumn = pawn.getCoordinates().column;
		int row;
		int col;
		ChessCoordinates destination;

		Set<ChessCoordinates> guardedCoordinates = Sets.newHashSet();
		Set<ChessCoordinates> legalDestinations = Sets.newHashSet();
		Set<ChessCoordinates> occupiedSquares = getOccupiedSquares();
		int teamIndex = getTeamId(pawn);
		int direction = teamIndex == 1 ? -1 : 1;
		
		Board board = GameController.getGame().getBoards()[targetBoardIndex];

		// take one step forward
		if (board.isRowValid(pawnRow + direction))
		{
			destination = new ChessCoordinates(pawnRow + direction, pawnColumn, targetBoardIndex);
			if (!occupiedSquares.contains(destination) && board.getSquare(pawnRow + direction, pawnColumn).isHabitable())
				legalDestinations.add(destination);
			else if (isGuardingSquare(destination, teamIndex))
				guardedCoordinates.add(destination);
		}

		// take an opposing piece
		if (board.isRowValid(row = (pawnRow + direction)))
		{
			col = pawnColumn;

			if (board.isColumnValid(col + 1))
			{
				destination = new ChessCoordinates(row, col + 1, targetBoardIndex);

				// order is important here: if we're not guarding the square,
				// but it's occupied, it must be occupied by the other team
				// also, if the square is occupied, it must be habitable, so we
				// don't need to check that
				if (isGuardingSquare(destination, teamIndex))
					guardedCoordinates.add(destination);
				else if (occupiedSquares.contains(destination))
					legalDestinations.add(destination);
			}
			if (board.isColumnValid(col - 1))
			{
				destination = new ChessCoordinates(row, col + 1, targetBoardIndex);

				// order is important here: if we're not guarding the square,
				// but it's occupied, it must be occupied by the other team
				// also, if the square is occupied, it must be habitable, so we
				// don't need to check that
				if (isGuardingSquare(destination, teamIndex))
					guardedCoordinates.add(destination);
				else if (occupiedSquares.contains(destination))
					legalDestinations.add(destination);
			}
		}

		// two step
		if (pawn.getMoveCount() == 0 && board.isRowValid(pawnRow + (2 * direction)))
		{
			destination = new ChessCoordinates(pawnRow + (2 * direction), pawnColumn, targetBoardIndex);

			if (!occupiedSquares.contains(destination)
					&& !occupiedSquares.contains(new ChessCoordinates(pawnRow + direction, pawnColumn, targetBoardIndex))
					&& board.getSquare(destination.row, destination.column).isHabitable())
			{
				legalDestinations.add(destination);
			}
		}

		return legalDestinations;
		// TODO: re-enable enPassant
		// if (board.getGame().isClassicChess())
		// {
		// // enPassant
		// if (isBlack() == board.isBlackTurn() && ((!isBlack() && pawnRow == 5)
		// || (isBlack() && pawnRow == 4)))
		// {
		// col = pawnColumn;
		// row = isBlack() ? pawnRow - 1 : pawnRow + 1;
		// if (board.isColValid(col + 1) && board.getEnpassantCol() == (col +
		// 1))
		// addLegalDest(board.getSquare(row, (col + 1)));
		//
		// if (board.isColValid(col - 1) && board.getEnpassantCol() == (col -
		// 1))
		// addLegalDest(board.getSquare(row, (col - 1)));
		// }
		// }
	}

	private static Set<ChessCoordinates> getOccupiedSquares()
	{
		Set<ChessCoordinates> occupiedSquares = Sets.newHashSet();

		for (Team team : GameController.getGame().getTeams())
		{
			for (Piece piece : team.getPieces())
				occupiedSquares.add(piece.getCoordinates());
		}
		
		return occupiedSquares;
	}

	private static Piece getPieceAtCoordinates(ChessCoordinates coordinates)
	{
		for (Team team : GameController.getGame().getTeams())
		{
			for (Piece piece : team.getPieces())
			{
				if (piece.getCoordinates().equals(coordinates))
					return piece;
			}
		}

		return null;
	}

	private static boolean sameRowAndColumn(ChessCoordinates destination, ChessCoordinates pieceCoordinates)
	{
		return destination.row == pieceCoordinates.row && destination.column == pieceCoordinates.column;
	}

	private static boolean isGuardingSquare(ChessCoordinates coordinates, int teamIndex)
	{
		for (Piece piece : GameController.getGame().getTeams()[teamIndex].getPieces())
		{
			if (piece.getCoordinates().equals(coordinates))
				return true;
		}

		return false;
	}

	public static void adjustPinsLegalDests(Piece piece, Piece pieceToProtect, int teamIndex)
	{
		Rules rules = GameController.getGame().getTeams()[teamIndex].getRules();
		if (rules.getObjectivePieceType().equals(piece.getPieceType()))
		{
			Set<ChessCoordinates> oldLegalDestinations = getLegalDestinations(piece);
			Set<ChessCoordinates> newLegalDestinations = Sets.newHashSet();

			// make sure the you don't move into check
			for (ChessCoordinates coordinates : oldLegalDestinations)
			{
				if (!GameController.isThreatened(coordinates, teamIndex) && !GameController.isGuarded(coordinates, teamIndex))
					newLegalDestinations.add(coordinates);
			}

			// TODO: re-enable castling
			// if (mBoard.getGame().isClassicChess())
			// {
			// // castling
			// if (getMoveCount() == 0)
			// {
			// boolean blocked = false;
			// // castle Queen side
			// PieceController rook = mBoard.getSquare(mCurrentSquare.getRow(),
			// 1).getPiece();
			// if (rook != null && rook.getMoveCount() == 0)
			// {
			// blocked = false;
			//
			// for (int c = (rook.getSquare().getCol() + 1); c <=
			// piece.getCoordinates().column && !blocked; c++)
			// {
			// if (c < piece.getCoordinates().column)
			// blocked = mBoard.getSquare(mCurrentSquare.getRow(),
			// c).isOccupied();
			//
			// if (!blocked)
			// blocked =
			// mBoard.getGame().isThreatened(mBoard.getSquare(mCurrentSquare.getRow(),
			// c), !isBlack());
			// }
			//
			// if (!blocked)
			// addLegalDest(mBoard.getSquare(((isBlack()) ? 8 : 1), 3));
			// }
			//
			// // castle King side
			// rook = mBoard.getSquare(mCurrentSquare.getRow(),
			// mBoard.getColumnCount()).getPiece();
			// if (rook != null && rook.getMoveCount() == 0)
			// {
			// blocked = false;
			//
			// for (int c = (rook.getSquare().getCol() - 1); c >=
			// piece.getCoordinates().column && !blocked; c--)
			// {
			// if (c > piece.getCoordinates().column)
			// blocked = mBoard.getSquare(mCurrentSquare.getRow(),
			// c).isOccupied();
			//
			// if (!blocked)
			// blocked =
			// mBoard.getGame().isThreatened(mBoard.getSquare(mCurrentSquare.getRow(),
			// c), !isBlack());
			// }
			//
			// if (!blocked)
			// addLegalDest(mBoard.getSquare(((isBlack()) ? 8 : 1), 7));
			// }
			// }
			// }

			return;
		}

		Piece pinned = null;
		Piece linePiece;

		ChessCoordinates[] line = getLineOfSight(piece, pieceToProtect, false);
		Set<ChessCoordinates> legalDestinations = getLegalDestinations(piece);
		Set<ChessCoordinates> occupiedSquares = getOccupiedSquares();
		
		
		if (line != null)
		{
			List<ChessCoordinates> lineList = Lists.newArrayList();
			for (ChessCoordinates coordinates : line)
			{
				if (legalDestinations.contains(coordinates) || coordinates.equals(piece.getCoordinates()))
					lineList.add(coordinates);
			}
			line = new ChessCoordinates[lineList.size()];
			lineList.toArray(line);

			// start i at 1 since 0 is this piece
			for (int i = 1; i < line.length; i++)
			{
				if (occupiedSquares.contains(line[i]))
				{
					linePiece = getPieceAtCoordinates(line[i]);

					// two pieces blocking the attack is not a pin
					if (pinned != null)
					{
						pinned = null;
						break;
					}
					// friend in the way
					else if (GameController.getGame().getTeams()[teamIndex].getPieces().contains(linePiece))
					{
						break;
					}
					else
					{
						pinned = linePiece;
					}
				}
			}

			if (pinned != null)
			{
				// need to AND moves with line (includes this square)
				List<ChessCoordinates> maintainPins = Arrays.asList(line);
				setPinned(pinned, piece, maintainPins);
			}
		}
	}

	public static Set<ChessCoordinates> getLegalDestsToSaveObjective(Piece piece, Piece objectivePiece, Piece threat, int teamIndex)
	{
		// the objective piece can't save itself
		if (GameController.getGame().getTeams()[teamIndex].getRules().getObjectivePieceType().equals(piece.getPieceType()))
			return null;

		Set<ChessCoordinates> oldLegalDestinations = getLegalDestinations(piece);
		Set<ChessCoordinates> newLegalDestinations = Sets.newHashSet();

		for (ChessCoordinates coordinates : oldLegalDestinations)
		{
			if (isBlockable(piece, coordinates, threat))
				newLegalDestinations.add(coordinates);
			else if (coordinates.equals(threat.getCoordinates()))
				newLegalDestinations.add(coordinates);
		}

		return newLegalDestinations;
	}

	public static Set<ChessCoordinates> getGuardedSquares(Piece piece)
	{
		//TODO fill this out
		return null;
	}

	/**
	 * Get the Squares between this piece and the target piece
	 * 
	 * @param targetRow
	 *            The row of the target
	 * @param targetColumn
	 *            The column of the target
	 * @param inclusive
	 *            Whether or not to include the target piece square
	 * @return The Squares between this Piece and the target piece
	 */
	public static ChessCoordinates[] getLineOfSight(Piece piece, int targetRow, int targetColumn, boolean inclusive)
	{
		if (piece.getPieceType().getName().equals(PieceType.PAWN_NAME))
			return null;

		ChessCoordinates[] returnSet = null;
		List<ChessCoordinates> returnTemp = Lists.newArrayList();

		// TODO: is this right or do we need to pass in the board index?
		int originBoardIndex = piece.getCoordinates().boardIndex;

		int originColumn = piece.getCoordinates().column;
		int originRow = piece.getCoordinates().row;
		int r = 0;// Row
		int c = 0;// Column
		int i = 0;// Return counter

		// Same column
		if (originColumn == targetColumn)
		{
			// North
			if (originRow < targetRow && canAttack(piece, targetRow, targetColumn, MovementDirection.NORTH))
			{
				for (r = (originRow + 1); r <= targetRow; r++)
				{
					if (r != targetRow || inclusive)
						returnTemp.add(i++, new ChessCoordinates(r, originColumn, originBoardIndex));
				}
			}
			// South
			else
			{
				if (canAttack(piece, targetRow, targetColumn, MovementDirection.SOUTH))
				{
					for (r = (originRow - 1); r >= targetRow; r--)
					{
						if (r != targetRow || inclusive)
							returnTemp.add(i++, new ChessCoordinates(r, originColumn, originBoardIndex));
					}
				}
			}
		}

		// Same Row
		else if (originRow == targetRow)
		{
			// East
			if (originColumn < targetColumn && canAttack(piece, targetRow, targetColumn, MovementDirection.EAST))
			{
				for (c = (originColumn + 1); c <= targetColumn; c++)
				{
					if (c != targetColumn || inclusive)
						returnTemp.add(i++, new ChessCoordinates(originRow, c, originBoardIndex));
				}
			}
			// West
			else
			{
				if (canAttack(piece, targetRow, targetColumn, MovementDirection.WEST))
				{
					for (c = (originColumn - 1); c >= targetColumn; c--)
					{
						if (c != targetColumn || inclusive)
							returnTemp.add(i++, new ChessCoordinates(originRow, c, originBoardIndex));
					}
				}
			}
		}

		// First diagonal
		else if ((originColumn - targetColumn) == (originRow - targetRow))
		{
			// Northeast
			if (originRow < targetRow && canAttack(piece, targetRow, targetColumn, MovementDirection.NORTHEAST))
			{
				for (c = (originColumn + 1), r = (originRow + 1); r <= targetRow; c++, r++)
				{
					if (r != targetRow || inclusive)
						returnTemp.add(i++, new ChessCoordinates(r, c, originBoardIndex));
				}
			}
			// Southwest
			else
			{
				if (canAttack(piece, targetRow, targetColumn, MovementDirection.SOUTHWEST))
				{
					for (c = (originColumn - 1), r = (originRow - 1); r >= targetRow; c--, r--)
					{
						if (r != targetRow || inclusive)
							returnTemp.add(i++, new ChessCoordinates(r, c, originBoardIndex));
					}
				}
			}
		}
		// Second diagonal
		else if ((originColumn - targetColumn) == ((originRow - targetRow) * -1))
		{
			// Northwest
			if ((originRow - targetRow) < 0 && canAttack(piece, targetRow, targetColumn, MovementDirection.NORTHWEST))
			{
				for (c = (originColumn - 1), r = (originRow + 1); r <= targetRow; c--, r++)
				{
					if (r != targetRow || inclusive)
						returnTemp.add(i++, new ChessCoordinates(r, c, originBoardIndex));
				}
			}
			// Southeast
			else
			{
				if (canAttack(piece, targetRow, targetColumn, MovementDirection.SOUTHEAST))
				{
					for (c = (originColumn + 1), r = (originRow - 1); r >= targetRow; c++, r--)
					{
						if (r != targetRow || inclusive)
							returnTemp.add(i++, new ChessCoordinates(r, c, originBoardIndex));
					}
				}
			}
		}

		if (i != 0)
		{
			// if i is zero, they weren't in line so return the null array
			returnSet = new ChessCoordinates[i + 1];
			returnSet[0] = piece.getCoordinates();

			int j = 1;
			for (ChessCoordinates coordinates : returnTemp)
				returnSet[j++] = coordinates;
		}

		return returnSet;
	}

	/**
	 * @param destRow
	 *            The row you wish to move to
	 * @param destCol
	 *            The column you wish to move to
	 * @param direction
	 *            The direction that space is from you.
	 * @return true if you are allowed to take that space and/or the piece on
	 *         it, false otherwise
	 */
	public static boolean canAttack(Piece piece, int destRow, int destCol, MovementDirection direction)
	{
		int distance = piece.getPieceType().getPieceMovements().getDistance(direction);
		int pieceRow = piece.getCoordinates().row;
		int pieceColumn = piece.getCoordinates().column;

		if (distance == PieceMovements.UNLIMITED)
			return true;

		switch (direction)
		{
		case SOUTH:
			return (destRow - pieceRow) < distance;
		case NORTH:
			return (pieceRow - destRow) < distance;
		case EAST:
			return (destCol - pieceColumn) < distance;
		case WEST:
			return (pieceColumn - destCol) < distance;
		case NORTHEAST:
			return (pieceColumn - destCol) < distance;
		case SOUTHEAST:
			return (pieceColumn - destCol) < distance;
		case NORTHWEST:
			return (destCol - pieceColumn) < distance;
		case SOUTHWEST:
			return (destCol - pieceColumn) < distance;
		default:
			throw new IllegalArgumentException("Unknown movement direction character"); //$NON-NLS-1$
		}
	}

	/**
	 * Get the Squares between this piece and the target piece
	 * 
	 * @param target
	 *            The piece in the line of sight
	 * @param inclusive
	 *            Whether or not to include the target piece square
	 * @return The Squares between this Piece and the target piece
	 */
	public static ChessCoordinates[] getLineOfSight(Piece subject, Piece target, boolean inclusive)
	{
		return getLineOfSight(subject, target.getCoordinates().row, target.getCoordinates().column, inclusive);
	}

	/**
	 * Check if the given Square can be saved from the given target by this
	 * piece
	 * 
	 * @param toSave
	 *            The Square to save
	 * @param coordinatesToBlock
	 *            The Piece to block
	 * @return If the given Square can be saved
	 */
	public static boolean isBlockable(Piece blocker, ChessCoordinates coordinatesToBlock, Piece attacker)
	{
		boolean blockable = false;
		ChessCoordinates[] lineOfSight = getLineOfSight(blocker, attacker, false);

		int i = 0;
		while (!blockable && lineOfSight != null && i < lineOfSight.length)
			blockable = coordinatesToBlock.equals(lineOfSight[i++]);

		return blockable;
	}

	public static boolean isGuarding(Piece piece, ChessCoordinates coordinates)
	{
		Set<ChessCoordinates> guardedSquares = getGuardedSquares(piece);
		return guardedSquares.contains(coordinates);
	}

	public static boolean canLegallyAttack(Piece piece, ChessCoordinates threatenedCoordinates, int teamIndex)
	{
		if (piece.getPieceType().getName().equals(PieceType.PAWN_NAME))
		{
			if (threatenedCoordinates.column == piece.getCoordinates().column)
				return false;
			else
				return isLegalDestination(piece, threatenedCoordinates)
						|| (threatenedCoordinates.row - piece.getCoordinates().row == ((teamIndex == 1) ? -1 : 1) && Math
								.abs(threatenedCoordinates.column - piece.getCoordinates().column) == 1);
		}
		return isLegalDestination(piece, threatenedCoordinates);
	}

	public static boolean isLegalDestination(Piece piece, ChessCoordinates destination)
	{
		return getLegalDestinations(piece).contains(destination);
	}

	/**
	 * Limit the legal destinations of this piece if it is pinned by another
	 * piece
	 * 
	 * @param pinner
	 *            The piece pinning this Piece
	 * @param lineOfSight
	 *            The legal destinations to retain
	 */
	public static void setPinned(Piece pinned, Piece pinner, List<ChessCoordinates> lineOfSight)
	{
		//TODO fix this/decide if we need it
		
		
		
//		mPinMap.put(pinned.getId(), pinner.getId());
//
//		int boardIndex = lineOfSight.get(0).boardIndex;
//		Set<ChessCoordinates> legalDestinations = getLegalDestinations(pinned, boardIndex);
//		legalDestinations.retainAll(lineOfSight);
//		mLegalDestinationMap.put(pinned.getId(), legalDestinations);
	}

	private static int getTeamId(Piece piece)
	{
		int teamId = 0;
		for (Team team : GameController.getGame().getTeams())
		{
			for (Piece indexPiece : team.getPieces())
			{
				if (indexPiece.equals(piece))
				{
					return teamId;
				}
			}
			teamId++;
		}
		return -1;
	}
}
