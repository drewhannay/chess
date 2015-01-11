package com.drewhannay.chesscrafter.controllers;


import com.drewhannay.chesscrafter.models.*;
import com.drewhannay.chesscrafter.rules.Rules;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.drewhannay.chesscrafter.models.PieceMovements.MovementDirection;
import static com.drewhannay.chesscrafter.models.PieceMovements.UNLIMITED;

public class ComputedPieceData {
    public ComputedPieceData(int teamIndex) {
        mTeamIndex = teamIndex;
        mGuardCoordinatesMap = Maps.newConcurrentMap();
        mLegalDestinationMap = Maps.newConcurrentMap();
        mPinMap = Maps.newHashMap();
        mOccupiedSquares = Lists.newArrayList();
    }

    public void computeLegalDestinations(Piece piece, int targetBoardIndex) {
        // clear any stale data
        mLegalDestinationMap.remove(piece.getId());
        mGuardCoordinatesMap.remove(piece.getId());
        mPinMap.remove(piece.getId());

        computeOccupiedSquares();

        // special case for Pawns, to incorporate enPassant, special
        // initial movement, and diagonal capturing
        if (piece.getPieceType().getName().equals(PieceType.PAWN_NAME)) {
            computePawnLegalDestinations(piece, targetBoardIndex);
            return;
        }

        // declare some convenience variables
        Board board = GameController.getGame().getBoards()[targetBoardIndex];
        PieceType pieceType = piece.getPieceType();
        int pieceRow = piece.getCoordinates().row;
        int pieceColumn = piece.getCoordinates().column;

        Set<ChessCoordinate> guardedCoordinates = Sets.newHashSet();
        Set<ChessCoordinate> legalDestinations = Sets.newHashSet();

        int distance;
        ChessCoordinate destination;
        boolean wraparound = board.isWrapAroundBoard();

        // east
        distance = pieceType.getPieceMovements().getDistance(MovementDirection.EAST);
        int eastMax = distance + pieceColumn;
        if ((eastMax > board.getColumnCount() || distance == UNLIMITED) && !wraparound)
            eastMax = board.getColumnCount();

        for (int c = pieceColumn + 1; (distance == UNLIMITED && wraparound) ? true : c <= eastMax; c++) {
            int j = c;
            if (wraparound && j > board.getColumnCount())
                j = j % board.getColumnCount();

            if (j == 0)
                break;

            destination = new ChessCoordinate(pieceRow, j, targetBoardIndex);

            boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
            if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
                legalDestinations.add(destination);

            if (isGuardingSquare(destination)) {
                shouldBreak = true;
                guardedCoordinates.add(destination);
            }

            shouldBreak = pieceType.isLeaper() ? false : shouldBreak || mOccupiedSquares.contains(destination);

            if (shouldBreak)
                break;
        }

        // west
        distance = pieceType.getPieceMovements().getDistance(MovementDirection.WEST);
        int westMax = pieceColumn - distance;
        if ((westMax < 1 || distance == UNLIMITED) && !wraparound)
            westMax = 1;

        for (int c = pieceColumn - 1; (distance == UNLIMITED && wraparound) ? true : c >= westMax; c--) {
            int j = c;
            if (wraparound && j < 1)
                j = board.getColumnCount() + j;

            destination = new ChessCoordinate(pieceRow, j, targetBoardIndex);

            boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
            if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
                legalDestinations.add(destination);

            if (isGuardingSquare(destination)) {
                shouldBreak = true;
                guardedCoordinates.add(destination);
            }

            shouldBreak = pieceType.isLeaper() ? false : shouldBreak || mOccupiedSquares.contains(destination);

            if (shouldBreak)
                break;
        }

        // north
        distance = pieceType.getPieceMovements().getDistance(MovementDirection.NORTH);
        int northMax = distance + pieceRow;
        if (northMax >= board.getRowCount() || distance == UNLIMITED)
            northMax = board.getRowCount();

        for (int r = pieceRow + 1; r <= northMax; r++) {
            int j = r;

            destination = new ChessCoordinate(j, pieceColumn, targetBoardIndex);

            boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
            if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
                legalDestinations.add(destination);

            if (isGuardingSquare(destination)) {
                shouldBreak = true;
                guardedCoordinates.add(destination);
            }

            shouldBreak = pieceType.isLeaper() ? false : shouldBreak || mOccupiedSquares.contains(destination);

            if (shouldBreak)
                break;
        }

        // south
        distance = pieceType.getPieceMovements().getDistance(MovementDirection.SOUTH);
        int southMax = pieceRow - distance;
        if (southMax < 1 || distance == UNLIMITED)
            southMax = 1;

        for (int r = pieceRow - 1; (r >= southMax); r--) {
            int j = r;

            destination = new ChessCoordinate(j, pieceColumn, targetBoardIndex);

            boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
            if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
                legalDestinations.add(destination);

            if (isGuardingSquare(destination)) {
                shouldBreak = true;
                guardedCoordinates.add(destination);
            }

            shouldBreak = pieceType.isLeaper() ? false : shouldBreak || mOccupiedSquares.contains(destination);

            if (shouldBreak)
                break;
        }

        // northeast
        distance = pieceType.getPieceMovements().getDistance(MovementDirection.NORTHEAST);
        int northeastMax = ((pieceRow >= pieceColumn) ? pieceRow : piece.getCoordinates().column) + distance;

        if (northeastMax >= board.getColumnCount() || distance == UNLIMITED)
            northeastMax = board.getColumnCount();
        if (northeastMax >= board.getRowCount() || distance == UNLIMITED)
            northeastMax = board.getRowCount();

        for (int r = pieceRow + 1, c = pieceColumn + 1; r <= northeastMax && c <= northeastMax; r++, c++) {
            destination = new ChessCoordinate(r, c, targetBoardIndex);

            boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
            if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
                legalDestinations.add(destination);

            if (isGuardingSquare(destination)) {
                shouldBreak = true;
                guardedCoordinates.add(destination);
            }

            shouldBreak = pieceType.isLeaper() ? false : shouldBreak || mOccupiedSquares.contains(destination);

            if (shouldBreak)
                break;
        }

        // southeast
        distance = pieceType.getPieceMovements().getDistance(MovementDirection.SOUTHEAST);
        eastMax = pieceColumn + distance;

        if (eastMax >= board.getColumnCount() || distance == UNLIMITED)
            eastMax = board.getColumnCount();

        int southMin = pieceRow - distance;

        if (southMin <= 1 || distance == UNLIMITED)
            southMin = 1;

        for (int r = pieceRow - 1, c = pieceColumn + 1; r >= southMin && c <= eastMax; r--, c++) {
            destination = new ChessCoordinate(r, c, targetBoardIndex);

            boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
            if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
                legalDestinations.add(destination);

            if (isGuardingSquare(destination)) {
                shouldBreak = true;
                guardedCoordinates.add(destination);
            }

            shouldBreak = pieceType.isLeaper() ? false : shouldBreak || mOccupiedSquares.contains(destination);

            if (shouldBreak)
                break;
        }

        // northwest
        distance = pieceType.getPieceMovements().getDistance(MovementDirection.NORTHWEST);
        int westMin = pieceColumn - distance;
        if (westMin <= 1 || distance == UNLIMITED)
            westMin = 1;

        northMax = pieceRow + distance;
        if (northMax >= board.getRowCount() || distance == UNLIMITED)
            northMax = board.getRowCount();

        for (int r = pieceRow + 1, c = pieceColumn - 1; r <= northMax && c >= westMin; r++, c--) {
            destination = new ChessCoordinate(r, c, targetBoardIndex);

            boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
            if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
                legalDestinations.add(destination);

            if (isGuardingSquare(destination)) {
                shouldBreak = true;
                guardedCoordinates.add(destination);
            }

            shouldBreak = pieceType.isLeaper() ? false : shouldBreak || mOccupiedSquares.contains(destination);

            if (shouldBreak)
                break;
        }

        // southwest
        distance = pieceType.getPieceMovements().getDistance(MovementDirection.SOUTHWEST);
        westMin = pieceColumn - distance;
        if (westMin <= 1 || distance == UNLIMITED)
            westMin = 1;

        southMin = pieceRow - distance;
        if (southMin <= 1 || distance == UNLIMITED)
            southMin = 1;

        for (int r = pieceRow - 1, c = pieceColumn - 1; r >= southMin && c >= westMin; r--, c--) {
            destination = new ChessCoordinate(r, c, targetBoardIndex);

            boolean shouldBreak = sameRowAndColumn(destination, piece.getCoordinates());
            if (!shouldBreak && board.getSquare(destination.row, destination.column).isHabitable())
                legalDestinations.add(destination);

            if (isGuardingSquare(destination)) {
                shouldBreak = true;
                guardedCoordinates.add(destination);
            }

            shouldBreak = pieceType.isLeaper() ? false : shouldBreak || mOccupiedSquares.contains(destination);

            if (shouldBreak)
                break;
        }

		/*
         * Bidirectional Movements
		 * Store of Bidirectional Movements are as followed:
		 * A Piece can move x File by y Rank squares at a time.
		 * IE: A knight can move 1 by 2 or 2 by 1, but not 1 by 1 or 2 by 2
		 */
        for (BidirectionalMovement movement : pieceType.getPieceMovements().getBidirectionalMovements()) {
            int f, r;
            int rank = movement.getRowDistance();
            int file = movement.getColumnDistance();

            // one o'clock
            f = (pieceRow + file);
            r = (pieceColumn + rank);
            if (wraparound) {
                if (r > board.getColumnCount() + 1)
                    r = r % board.getColumnCount();
            }

            if (board.isRowValid(f) && board.isColumnValid(r))
                legalDestinations.add(new ChessCoordinate(f, r, targetBoardIndex));

            // two o'clock
            f = (pieceRow + rank);
            r = (pieceColumn + file);
            if (wraparound) {
                if (r > board.getColumnCount() + 1)
                    r = r % board.getColumnCount();
            }

            if (board.isRowValid(f) && board.isColumnValid(r))
                legalDestinations.add(new ChessCoordinate(f, r, targetBoardIndex));

            // four o'clock
            f = (pieceRow + file);
            r = (pieceColumn - rank);
            if (wraparound) {
                if (r < 1)
                    r = board.getColumnCount() + r;
            }

            if (board.isRowValid(f) && board.isColumnValid(r))
                legalDestinations.add(new ChessCoordinate(f, r, targetBoardIndex));

            // five o'clock
            f = (pieceRow + rank);
            r = (pieceColumn - file);
            if (wraparound) {
                if (r < 1)
                    r = board.getColumnCount() + r;
            }

            if (board.isRowValid(f) && board.isColumnValid(r))
                legalDestinations.add(new ChessCoordinate(f, r, targetBoardIndex));

            // seven o'clock
            f = (pieceRow - file);
            r = (pieceColumn - rank);
            if (wraparound) {
                if (r < 1)
                    r = board.getColumnCount() + r;
            }

            if (board.isRowValid(f) && board.isColumnValid(r))
                legalDestinations.add(new ChessCoordinate(f, r, targetBoardIndex));

            // eight o'clock
            f = (pieceRow - rank);
            r = (pieceColumn - file);
            if (wraparound) {
                if (r < 1)
                    r = board.getColumnCount() + r;
            }

            if (board.isRowValid(f) && board.isColumnValid(r))
                legalDestinations.add(new ChessCoordinate(f, r, targetBoardIndex));

            // ten o'clock
            f = (pieceRow - file);
            r = (pieceColumn + rank);
            if (wraparound) {
                if (r > board.getColumnCount() + 1)
                    r = r % board.getColumnCount();
            }

            if (board.isRowValid(f) && board.isColumnValid(r))
                legalDestinations.add(new ChessCoordinate(f, r, targetBoardIndex));

            // eleven o'clock
            f = (pieceRow - rank);
            r = (pieceColumn + file);
            if (wraparound) {
                if (r > board.getColumnCount() + 1)
                    r = r % board.getColumnCount();
            }

            if (board.isRowValid(f) && board.isColumnValid(r))
                legalDestinations.add(new ChessCoordinate(f, r, targetBoardIndex));
        }

        // Remove any destinations occupied by the piece's team
        Game game = GameController.getGame();
        int teamId = piece.getTeamId(game);
        ChessCoordinate[] copyOfLegalDestinations = new ChessCoordinate[legalDestinations.size()];
        copyOfLegalDestinations = legalDestinations.toArray(copyOfLegalDestinations);
        for (int i = 0; i < copyOfLegalDestinations.length; i++) {
            Piece pieceOnDestination = game.getPieceOnSquare(copyOfLegalDestinations[i]);
            if (pieceOnDestination != null && pieceOnDestination.getTeamId(game) == teamId)
                legalDestinations.remove(copyOfLegalDestinations[i]);
        }

        mLegalDestinationMap.put(piece.getId(), legalDestinations);
        mGuardCoordinatesMap.put(piece.getId(), guardedCoordinates);
    }

    private void computePawnLegalDestinations(Piece pawn, int targetBoardIndex) {
        int pawnRow = pawn.getCoordinates().row;
        int pawnColumn = pawn.getCoordinates().column;
        Board board = GameController.getGame().getBoards()[targetBoardIndex];

        int row;
        int col;
        int direction;
        ChessCoordinate destination;

        Set<ChessCoordinate> guardedCoordinates = Sets.newHashSet();
        Set<ChessCoordinate> legalDestinations = Sets.newHashSet();

        direction = mTeamIndex == 1 ? -1 : 1;

        // take one step forward
        if (board.isRowValid(pawnRow + direction)) {
            destination = new ChessCoordinate(pawnRow + direction, pawnColumn, targetBoardIndex);
            if (!mOccupiedSquares.contains(destination) && board.getSquare(pawnRow + direction, pawnColumn).isHabitable())
                legalDestinations.add(destination);
            else if (isGuardingSquare(destination))
                guardedCoordinates.add(destination);
        }

        // take an opposing piece
        if (board.isRowValid(row = (pawnRow + direction))) {
            col = pawnColumn;

            if (board.isColumnValid(col + 1)) {
                destination = new ChessCoordinate(row, col + 1, targetBoardIndex);

                // order is important here: if we're not guarding the square,
                // but it's occupied, it must be occupied by the other team
                // also, if the square is occupied, it must be habitable, so we
                // don't need to check that
                if (isGuardingSquare(destination))
                    guardedCoordinates.add(destination);
                else if (mOccupiedSquares.contains(destination))
                    legalDestinations.add(destination);
            }
            if (board.isColumnValid(col - 1)) {
                destination = new ChessCoordinate(row, col + 1, targetBoardIndex);

                // order is important here: if we're not guarding the square,
                // but it's occupied, it must be occupied by the other team
                // also, if the square is occupied, it must be habitable, so we
                // don't need to check that
                if (isGuardingSquare(destination))
                    guardedCoordinates.add(destination);
                else if (mOccupiedSquares.contains(destination))
                    legalDestinations.add(destination);
            }
        }

        // two step
        if (pawn.getMoveCount() == 0 && board.isRowValid(pawnRow + (2 * direction))) {
            destination = new ChessCoordinate(pawnRow + (2 * direction), pawnColumn, targetBoardIndex);

            if (!mOccupiedSquares.contains(destination)
                    && !mOccupiedSquares.contains(new ChessCoordinate(pawnRow + direction, pawnColumn, targetBoardIndex))
                    && board.getSquare(destination.row, destination.column).isHabitable()) {
                legalDestinations.add(destination);
            }
        }

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

        mLegalDestinationMap.put(pawn.getId(), legalDestinations);
        mGuardCoordinatesMap.put(pawn.getId(), guardedCoordinates);
    }

    private void computeOccupiedSquares() {
        mOccupiedSquares.clear();

        for (Team team : GameController.getGame().getTeams()) {
            for (Piece piece : team.getPieces())
                mOccupiedSquares.add(piece.getCoordinates());
        }
    }

    private Piece getPieceAtCoordinates(ChessCoordinate coordinates) {
        for (Team team : GameController.getGame().getTeams()) {
            for (Piece piece : team.getPieces()) {
                if (piece.getCoordinates().equals(coordinates))
                    return piece;
            }
        }

        return null;
    }

    private boolean sameRowAndColumn(ChessCoordinate destination, ChessCoordinate pieceCoordinates) {
        return destination.row == pieceCoordinates.row && destination.column == pieceCoordinates.column;
    }

    private boolean isGuardingSquare(ChessCoordinate coordinates) {
        for (Piece piece : GameController.getGame().getTeams()[mTeamIndex].getPieces()) {
            if (piece.getCoordinates().equals(coordinates))
                return true;
        }

        return false;
    }

    private void adjustPinsLegalDests(Piece piece, Piece pieceToProtect) {
        Rules rules = GameController.getGame().getTeams()[mTeamIndex].getRules();
        if (rules.getObjectivePieceType().equals(piece.getPieceType())) {
            Set<ChessCoordinate> oldLegalDestinations = mLegalDestinationMap.get(piece.getId());
            Set<ChessCoordinate> newLegalDestinations = Sets.newHashSet();

            // make sure the you don't move into check
            for (ChessCoordinate coordinates : oldLegalDestinations) {
                if (!GameController.isThreatened(coordinates, mTeamIndex) && !GameController.isGuarded(coordinates, mTeamIndex))
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

        ChessCoordinate[] line = getLineOfSight(piece, pieceToProtect, false);
        Set<ChessCoordinate> legalDestinations = mLegalDestinationMap.get(piece.getId());

        if (line != null) {
            List<ChessCoordinate> lineList = Lists.newArrayList();
            for (ChessCoordinate coordinates : line) {
                if (legalDestinations.contains(coordinates) || coordinates.equals(piece.getCoordinates()))
                    lineList.add(coordinates);
            }
            line = new ChessCoordinate[lineList.size()];
            lineList.toArray(line);

            // start i at 1 since 0 is this piece
            for (int i = 1; i < line.length; i++) {
                if (mOccupiedSquares.contains(line[i])) {
                    linePiece = getPieceAtCoordinates(line[i]);

                    // two pieces blocking the attack is not a pin
                    if (pinned != null) {
                        pinned = null;
                        break;
                    }
                    // friend in the way
                    else if (GameController.getGame().getTeams()[mTeamIndex].getPieces().contains(linePiece)) {
                        break;
                    } else {
                        pinned = linePiece;
                    }
                }
            }

            if (pinned != null) {
                // need to AND moves with line (includes this square)
                List<ChessCoordinate> maintainPins = Arrays.asList(line);
                setPinned(pinned, piece, maintainPins);
            }
        }
    }

    public void computeLegalDestsToSaveObjective(Piece piece, Piece objectivePiece, Piece threat) {
        // the objective piece can't save itself
        if (GameController.getGame().getTeams()[mTeamIndex].getRules().getObjectivePieceType().equals(piece.getPieceType()))
            return;

        Set<ChessCoordinate> oldLegalDestinations = mLegalDestinationMap.get(piece.getId());
        Set<ChessCoordinate> newLegalDestinations = Sets.newHashSet();

        for (ChessCoordinate coordinates : oldLegalDestinations) {
            if (isBlockable(piece, coordinates, threat))
                newLegalDestinations.add(coordinates);
            else if (coordinates.equals(threat.getCoordinates()))
                newLegalDestinations.add(coordinates);
        }

        mLegalDestinationMap.put(piece.getId(), newLegalDestinations);
    }

    public Set<ChessCoordinate> getGuardSquares(Piece piece) {
        return mGuardCoordinatesMap.get(piece.getId());
    }

    public Set<ChessCoordinate> getLegalDests(Piece piece) {
        return mLegalDestinationMap.get(piece.getId());
    }

    /**
     * Get the Squares between this piece and the target piece
     *
     * @param targetRow    The row of the target
     * @param targetColumn The column of the target
     * @param inclusive    Whether or not to include the target piece square
     * @return The Squares between this Piece and the target piece
     */
    public ChessCoordinate[] getLineOfSight(Piece piece, int targetRow, int targetColumn, boolean inclusive) {
        if (piece.getPieceType().getName().equals(PieceType.PAWN_NAME))
            return null;

        ChessCoordinate[] returnSet = null;
        List<ChessCoordinate> returnTemp = Lists.newArrayList();

        // TODO: is this right or do we need to pass in the board index?
        int originBoardIndex = piece.getCoordinates().boardIndex;

        int originColumn = piece.getCoordinates().column;
        int originRow = piece.getCoordinates().row;
        int r = 0;// Row
        int c = 0;// Column
        int i = 0;// Return counter

        // Same column
        if (originColumn == targetColumn) {
            // North
            if (originRow < targetRow && canAttack(piece, targetRow, targetColumn, MovementDirection.NORTH)) {
                for (r = (originRow + 1); r <= targetRow; r++) {
                    ChessCoordinate indexCoordinates = new ChessCoordinate(r, originColumn, originBoardIndex);
                    if (mOccupiedSquares.contains(indexCoordinates) && !inclusive)
                        break;
                    if (r != targetRow || inclusive)
                        returnTemp.add(i++, indexCoordinates);
                }
            }
            // South
            else {
                if (canAttack(piece, targetRow, targetColumn, MovementDirection.SOUTH)) {
                    for (r = (originRow - 1); r >= targetRow; r--) {
                        ChessCoordinate indexCoordinates = new ChessCoordinate(r, originColumn, originBoardIndex);
                        if (mOccupiedSquares.contains(indexCoordinates) && !inclusive)
                            break;
                        if (r != targetRow || inclusive)
                            returnTemp.add(i++, indexCoordinates);
                    }
                }
            }
        }

        // Same Row
        else if (originRow == targetRow) {
            // East
            if (originColumn < targetColumn && canAttack(piece, targetRow, targetColumn, MovementDirection.EAST)) {
                for (c = (originColumn + 1); c <= targetColumn; c++) {
                    ChessCoordinate indexCoordinates = new ChessCoordinate(originRow, c, originBoardIndex);
                    if (mOccupiedSquares.contains(indexCoordinates) && !inclusive)
                        break;
                    if (c != targetColumn || inclusive)
                        returnTemp.add(i++, indexCoordinates);
                }
            }
            // West
            else {
                if (canAttack(piece, targetRow, targetColumn, MovementDirection.WEST)) {
                    for (c = (originColumn - 1); c >= targetColumn; c--) {
                        ChessCoordinate indexCoordinates = new ChessCoordinate(originRow, c, originBoardIndex);
                        if (mOccupiedSquares.contains(indexCoordinates) && !inclusive)
                            break;
                        if (c != targetColumn || inclusive)
                            returnTemp.add(i++, indexCoordinates);
                    }
                }
            }
        }

        // First diagonal
        else if ((originColumn - targetColumn) == (originRow - targetRow)) {
            // Northeast
            if (originRow < targetRow && canAttack(piece, targetRow, targetColumn, MovementDirection.NORTHEAST)) {
                for (c = (originColumn + 1), r = (originRow + 1); r <= targetRow; c++, r++) {
                    ChessCoordinate indexCoordinates = new ChessCoordinate(r, c, originBoardIndex);
                    if (mOccupiedSquares.contains(indexCoordinates) && !inclusive)
                        break;
                    if (r != targetRow || inclusive)
                        returnTemp.add(i++, indexCoordinates);
                }
            }
            // Southwest
            else {
                if (canAttack(piece, targetRow, targetColumn, MovementDirection.SOUTHWEST)) {
                    for (c = (originColumn - 1), r = (originRow - 1); r >= targetRow; c--, r--) {
                        ChessCoordinate indexCoordinates = new ChessCoordinate(r, c, originBoardIndex);
                        if (mOccupiedSquares.contains(indexCoordinates) && !inclusive)
                            break;
                        if (r != targetRow || inclusive)
                            returnTemp.add(i++, indexCoordinates);
                    }
                }
            }
        }
        // Second diagonal
        else if ((originColumn - targetColumn) == ((originRow - targetRow) * -1)) {
            // Northwest
            if ((originRow - targetRow) < 0 && canAttack(piece, targetRow, targetColumn, MovementDirection.NORTHWEST)) {
                for (c = (originColumn - 1), r = (originRow + 1); r <= targetRow; c--, r++) {
                    ChessCoordinate indexCoordinates = new ChessCoordinate(r, c, originBoardIndex);
                    if (mOccupiedSquares.contains(indexCoordinates) && !inclusive)
                        break;
                    if (r != targetRow || inclusive)
                        returnTemp.add(i++, indexCoordinates);
                }
            }
            // Southeast
            else {
                if (canAttack(piece, targetRow, targetColumn, MovementDirection.SOUTHEAST)) {
                    for (c = (originColumn + 1), r = (originRow - 1); r >= targetRow; c++, r--) {
                        ChessCoordinate indexCoordinates = new ChessCoordinate(r, c, originBoardIndex);
                        if (mOccupiedSquares.contains(indexCoordinates) && !inclusive)
                            break;
                        if (r != targetRow || inclusive)
                            returnTemp.add(i++, indexCoordinates);
                    }
                }
            }
        }

        if (i != 0) {
            // if i is zero, they weren't in line so return the null array
            returnSet = new ChessCoordinate[i + 1];
            returnSet[0] = piece.getCoordinates();

            int j = 1;
            for (ChessCoordinate coordinates : returnTemp)
                returnSet[j++] = coordinates;
        }

        return returnSet;
    }

    /**
     * @param destRow   The row you wish to move to
     * @param destCol   The column you wish to move to
     * @param direction The direction that space is from you.
     * @return true if you are allowed to take that space and/or the piece on
     * it, false otherwise
     */
    public boolean canAttack(Piece piece, int destRow, int destCol, MovementDirection direction) {
        int distance = piece.getPieceType().getPieceMovements().getDistance(direction);
        int pieceRow = piece.getCoordinates().row;
        int pieceColumn = piece.getCoordinates().column;

        if (distance == UNLIMITED)
            return true;

        switch (direction) {
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
                throw new IllegalArgumentException("Unknown movement direction character");
        }
    }

    /**
     * Get the Squares between this piece and the target piece
     *
     * @param target    The piece in the line of sight
     * @param inclusive Whether or not to include the target piece square
     * @return The Squares between this Piece and the target piece
     */
    public ChessCoordinate[] getLineOfSight(Piece subject, Piece target, boolean inclusive) {
        return getLineOfSight(subject, target.getCoordinates().row, target.getCoordinates().column, inclusive);
    }

    /**
     * Check if the given Square can be saved from the given target by this
     * piece
     *
     * @param toSave             The Square to save
     * @param coordinatesToBlock The Piece to block
     * @return If the given Square can be saved
     */
    public boolean isBlockable(Piece blocker, ChessCoordinate coordinatesToBlock, Piece attacker) {
        boolean blockable = false;
        ChessCoordinate[] lineOfSight = getLineOfSight(blocker, attacker, false);

        int i = 0;
        while (!blockable && lineOfSight != null && i < lineOfSight.length)
            blockable = coordinatesToBlock.equals(lineOfSight[i++]);

        return blockable;
    }

    public boolean isGuarding(Piece piece, ChessCoordinate coordinates) {
        Set<ChessCoordinate> guardedSquares = mGuardCoordinatesMap.get(piece.getId());
        return guardedSquares.contains(coordinates);
    }

    public boolean canLegallyAttack(Piece piece, ChessCoordinate threatenedCoordinates) {
        if (piece.getPieceType().getName().equals(PieceType.PAWN_NAME)) {
            if (threatenedCoordinates.column == piece.getCoordinates().column)
                return false;
            else
                return isLegalDestination(piece, threatenedCoordinates)
                        || (threatenedCoordinates.row - piece.getCoordinates().row == ((mTeamIndex == 1) ? -1 : 1) && Math
                        .abs(threatenedCoordinates.column - piece.getCoordinates().column) == 1);
        }
        return isLegalDestination(piece, threatenedCoordinates);
    }

    public boolean isLegalDestination(Piece piece, ChessCoordinate destination) {
        return mLegalDestinationMap.get(piece.getId()).contains(destination);
    }

    /**
     * Limit the legal destinations of this piece if it is pinned by another
     * piece
     *
     * @param pinner      The piece pinning this Piece
     * @param lineOfSight The legal destinations to retain
     */
    public void setPinned(Piece pinned, Piece pinner, List<ChessCoordinate> lineOfSight) {
        mPinMap.put(pinned.getId(), pinner.getId());

        Set<ChessCoordinate> legalDestinations = mLegalDestinationMap.get(pinned.getId());
        legalDestinations.retainAll(lineOfSight);
        mLegalDestinationMap.put(pinned.getId(), legalDestinations);
    }

    private final int mTeamIndex;
    private final Map<Long, Set<ChessCoordinate>> mGuardCoordinatesMap;
    private final Map<Long, Set<ChessCoordinate>> mLegalDestinationMap;
    // TODO: the PinMap may need to be game-global and not team-specific
    private final Map<Long, Long> mPinMap;

    private final List<ChessCoordinate> mOccupiedSquares;
}
