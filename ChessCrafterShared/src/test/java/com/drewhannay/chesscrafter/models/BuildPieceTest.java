package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.PieceBuilder;
import com.google.common.collect.ImmutableSet;
import org.junit.*;

import static org.junit.Assert.fail;

public class BuildPieceTest {
    @SuppressWarnings("nls")
    @Test
    public final void testBuildingRook() {
        PieceType rook = PieceBuilder.getRookPieceType();

        if (rook.isLeaper())
            fail("PieceBuilder.getRookPieceType() set as Leaper");

        if (!rook.getName().equals("Rook"))
            fail("PieceBuilder Rook type returns incorrect name: " + rook.getName());

        PieceMovements pieceMovements = rook.getPieceMovements();
        if (pieceMovements.getDistance(Direction.NORTH) != -1)
            fail("PieceBuilder Rook type has incorrect North movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.NORTH));
        if (pieceMovements.getDistance(Direction.NORTHEAST) != 0)
            fail("PieceBuilder Rook type has incorrect Northeast movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.NORTHEAST));
        if (pieceMovements.getDistance(Direction.EAST) != -1)
            fail("PieceBuilder Rook type has incorrect East movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.EAST));
        if (pieceMovements.getDistance(Direction.SOUTHEAST) != 0)
            fail("PieceBuilder Rook type has incorrect Southeast movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.SOUTHEAST));
        if (pieceMovements.getDistance(Direction.SOUTH) != -1)
            fail("PieceBuilder Rook type has incorrect South movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.SOUTH));
        if (pieceMovements.getDistance(Direction.SOUTHWEST) != 0)
            fail("PieceBuilder Rook type has incorrect Southwest movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.SOUTHWEST));
        if (pieceMovements.getDistance(Direction.WEST) != -1)
            fail("PieceBuilder Rook type has incorrect West movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.WEST));
        if (pieceMovements.getDistance(Direction.NORTHWEST) != 0)
            fail("PieceBuilder Rook type has incorrect Northwest movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.NORTHWEST));

        if (!pieceMovements.getBidirectionalMovements().isEmpty())
            fail("PieceBuilder Rook type has non-empty Bidirectional Movement set.");
    }

    @SuppressWarnings("nls")
    @Test
    public final void testBuildingBishop() {
        PieceType bishop = PieceBuilder.getBishopPieceType();

        if (bishop.isLeaper())
            fail("PieceBuilder.getBishopPieceType() set as Leaper");

        if (!bishop.getName().equals("Bishop"))
            fail("PieceBuilder Bishop type returns incorrect name: " + bishop.getName());

        PieceMovements pieceMovements = bishop.getPieceMovements();
        if (pieceMovements.getDistance(Direction.NORTH) != 0)
            fail("PieceBuilder Bishop type has incorrect North movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.NORTH));
        if (pieceMovements.getDistance(Direction.NORTHEAST) != -1)
            fail("PieceBuilder Bishop type has incorrect Northeast movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.NORTHEAST));
        if (pieceMovements.getDistance(Direction.EAST) != 0)
            fail("PieceBuilder Bishop type has incorrect East movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.EAST));
        if (pieceMovements.getDistance(Direction.SOUTHEAST) != -1)
            fail("PieceBuilder Bishop type has incorrect Southeast movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.SOUTHEAST));
        if (pieceMovements.getDistance(Direction.SOUTH) != 0)
            fail("PieceBuilder Bishop type has incorrect South movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.SOUTH));
        if (pieceMovements.getDistance(Direction.SOUTHWEST) != -1)
            fail("PieceBuilder Bishop type has incorrect Southwest movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.SOUTHWEST));
        if (pieceMovements.getDistance(Direction.WEST) != 0)
            fail("PieceBuilder Bishop type has incorrect West movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.WEST));
        if (pieceMovements.getDistance(Direction.NORTHWEST) != -1)
            fail("PieceBuilder Bishop type has incorrect Northwest movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.NORTHWEST));

        if (!pieceMovements.getBidirectionalMovements().isEmpty())
            fail("PieceBuilder Bishop type has non-empty Bidirectional Movement set.");
    }

    @SuppressWarnings("nls")
    @Test
    public final void testBuildingKnight() {
        PieceType knight = PieceBuilder.getKnightPieceType();

//        if (!knight.isLeaper())
//            fail("PieceBuilder.getKnightPieceType() not set as Leaper");

        if (!knight.getName().equals("Knight"))
            fail("PieceBuilder Knight type returns incorrect name: " + knight.getName());

        PieceMovements pieceMovements = knight.getPieceMovements();
        if (pieceMovements.getDistance(Direction.NORTH) != 0)
            fail("PieceBuilder Knight type has incorrect North movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.NORTH));
        if (pieceMovements.getDistance(Direction.NORTHEAST) != 0)
            fail("PieceBuilder Knight type has incorrect Northeast movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.NORTHEAST));
        if (pieceMovements.getDistance(Direction.EAST) != 0)
            fail("PieceBuilder Knight type has incorrect East movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.EAST));
        if (pieceMovements.getDistance(Direction.SOUTHEAST) != 0)
            fail("PieceBuilder Knight type has incorrect Southeast movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.SOUTHEAST));
        if (pieceMovements.getDistance(Direction.SOUTH) != 0)
            fail("PieceBuilder Knight type has incorrect South movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.SOUTH));
        if (pieceMovements.getDistance(Direction.SOUTHWEST) != 0)
            fail("PieceBuilder Knight type has incorrect Southwest movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.SOUTHWEST));
        if (pieceMovements.getDistance(Direction.WEST) != 0)
            fail("PieceBuilder Knight type has incorrect West movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.WEST));
        if (pieceMovements.getDistance(Direction.NORTHWEST) != 0)
            fail("PieceBuilder Knight type has incorrect Northwest movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.NORTHWEST));

        ImmutableSet<BidirectionalMovement> bidirectionalMovements = pieceMovements.getBidirectionalMovements();
        if (!bidirectionalMovements.contains(new BidirectionalMovement(1, 2)))
            fail("PieceBuilder Knight type doesn't have 1 x 2 BidirectionalMovement.");
        if (bidirectionalMovements.size() != 1)
            fail("PieceBuilder Knight type has an unexcpected number of BidirectionalMovements. Expected: 1. Returned: " + bidirectionalMovements.size());
    }

    @SuppressWarnings("nls")
    @Test
    public final void testBuildingQueen() {
        PieceType queen = PieceBuilder.getQueenPieceType();

        if (queen.isLeaper())
            fail("PieceBuilder.getQueenPieceType() set as Leaper");

        if (!queen.getName().equals("Queen"))
            fail("PieceBuilder Bishop type returns incorrect name: " + queen.getName());

        PieceMovements pieceMovements = queen.getPieceMovements();
        if (pieceMovements.getDistance(Direction.NORTH) != -1)
            fail("PieceBuilder Queen type has incorrect North movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.NORTH));
        if (pieceMovements.getDistance(Direction.NORTHEAST) != -1)
            fail("PieceBuilder Queen type has incorrect Northeast movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.NORTHEAST));
        if (pieceMovements.getDistance(Direction.EAST) != -1)
            fail("PieceBuilder Queen type has incorrect East movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.EAST));
        if (pieceMovements.getDistance(Direction.SOUTHEAST) != -1)
            fail("PieceBuilder Queen type has incorrect Southeast movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.SOUTHEAST));
        if (pieceMovements.getDistance(Direction.SOUTH) != -1)
            fail("PieceBuilder Queen type has incorrect South movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.SOUTH));
        if (pieceMovements.getDistance(Direction.SOUTHWEST) != -1)
            fail("PieceBuilder Queen type has incorrect Southwest movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.SOUTHWEST));
        if (pieceMovements.getDistance(Direction.WEST) != -1)
            fail("PieceBuilder Queen type has incorrect West movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.WEST));
        if (pieceMovements.getDistance(Direction.NORTHWEST) != -1)
            fail("PieceBuilder Queen type has incorrect Northwest movement. Expected: -1. Returned: " + pieceMovements.getDistance(Direction.NORTHWEST));

        if (!pieceMovements.getBidirectionalMovements().isEmpty())
            fail("PieceBuilder Queen type has non-empty Bidirectional Movement set.");
    }

    @SuppressWarnings("nls")
    @Test
    public final void testBuildingKing() {
        PieceType king = PieceBuilder.getKingPieceType();

        if (king.isLeaper())
            fail("PieceBuilder.getKingPieceType() set as Leaper");

        if (!king.getName().equals("King"))
            fail("PieceBuilder King type returns incorrect name: " + king.getName());

        PieceMovements pieceMovements = king.getPieceMovements();
        if (pieceMovements.getDistance(Direction.NORTH) != 1)
            fail("PieceBuilder King type has incorrect North movement. Expected: 1. Returned: " + pieceMovements.getDistance(Direction.NORTH));
        if (pieceMovements.getDistance(Direction.NORTHEAST) != 1)
            fail("PieceBuilder King type has incorrect Northeast movement. Expected: 1. Returned: " + pieceMovements.getDistance(Direction.NORTHEAST));
        if (pieceMovements.getDistance(Direction.EAST) != 1)
            fail("PieceBuilder King type has incorrect East movement. Expected: 1. Returned: " + pieceMovements.getDistance(Direction.EAST));
        if (pieceMovements.getDistance(Direction.SOUTHEAST) != 1)
            fail("PieceBuilder King type has incorrect Southeast movement. Expected: 1. Returned: " + pieceMovements.getDistance(Direction.SOUTHEAST));
        if (pieceMovements.getDistance(Direction.SOUTH) != 1)
            fail("PieceBuilder King type has incorrect South movement. Expected: 1. Returned: " + pieceMovements.getDistance(Direction.SOUTH));
        if (pieceMovements.getDistance(Direction.SOUTHWEST) != 1)
            fail("PieceBuilder King type has incorrect Southwest movement. Expected: 1. Returned: " + pieceMovements.getDistance(Direction.SOUTHWEST));
        if (pieceMovements.getDistance(Direction.WEST) != 1)
            fail("PieceBuilder King type has incorrect West movement. Expected: 1. Returned: " + pieceMovements.getDistance(Direction.WEST));
        if (pieceMovements.getDistance(Direction.NORTHWEST) != 1)
            fail("PieceBuilder King type has incorrect Northwest movement. Expected: 1. Returned: " + pieceMovements.getDistance(Direction.NORTHWEST));

        if (!pieceMovements.getBidirectionalMovements().isEmpty())
            fail("PieceBuilder King type has non-empty Bidirectional Movement set.");
    }

    @SuppressWarnings("nls")
    @Test
    public final void testBuildingPawn() {
        PieceType pawn = PieceBuilder.getPawnPieceType();

        if (pawn.isLeaper())
            fail("PieceBuilder.getPawnPieceType() set as Leaper");

        if (!pawn.getName().equals("Pawn"))
            fail("PieceBuilder Pawn type returns incorrect name: " + pawn.getName());

        PieceMovements pieceMovements = pawn.getPieceMovements();
        //TODO: Once we set up a more robust pawn-type creation, this will probably be different
//		if (pieceMovements.getDistance(Direction.NORTH) != 1)
//			fail("PieceBuilder Pawn type has incorrect North movement. Expected: 1. Returned: " + pieceMovements.getDistance(Direction.NORTH));
        if (pieceMovements.getDistance(Direction.NORTHEAST) != 0)
            fail("PieceBuilder Pawn type has incorrect Northeast movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.NORTHEAST));
        if (pieceMovements.getDistance(Direction.EAST) != 0)
            fail("PieceBuilder Pawn type has incorrect East movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.EAST));
        if (pieceMovements.getDistance(Direction.SOUTHEAST) != 0)
            fail("PieceBuilder Pawn type has incorrect Southeast movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.SOUTHEAST));
//		if (pieceMovements.getDistance(Direction.SOUTH) != 0)
//			fail("PieceBuilder Pawn type has incorrect South movement. Expected: 1. Returned: " + pieceMovements.getDistance(Direction.SOUTH));
        if (pieceMovements.getDistance(Direction.SOUTHWEST) != 0)
            fail("PieceBuilder Pawn type has incorrect Southwest movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.SOUTHWEST));
        if (pieceMovements.getDistance(Direction.WEST) != 0)
            fail("PieceBuilder Pawn type has incorrect West movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.WEST));
        if (pieceMovements.getDistance(Direction.NORTHWEST) != 0)
            fail("PieceBuilder Pawn type has incorrect Northwest movement. Expected: 0. Returned: " + pieceMovements.getDistance(Direction.NORTHWEST));

        if (!pieceMovements.getBidirectionalMovements().isEmpty())
            fail("PieceBuilder Pawn type has non-empty Bidirectional Movement set.");
    }
}
