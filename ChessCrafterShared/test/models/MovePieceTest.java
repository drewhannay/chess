package models;

import static org.junit.Assert.*;

import java.util.List;

import logic.PieceBuilder;
import models.turnkeeper.TurnKeeper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;

import controllers.GameController;
import controllers.MoveController;

public class MovePieceTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMovementUpdatesPieceCoordinates()
	{
		ChessCoordinates origin = new ChessCoordinates(1, 1, 0);
		ChessCoordinates destination = new ChessCoordinates(2, 2, 0);

		// place a white pawn on the origin
		List<Piece> whitePieceList = Lists.newArrayList();
		Piece whitePawn = new Piece(0, PieceBuilder.getPawnPieceType(), origin);
		whitePieceList.add(whitePawn);
		Team whiteTeam = new Team(null, whitePieceList);

		List<Piece> blackPieceList = Lists.newArrayList();
		Team blackTeam = new Team(null, blackPieceList);

		Board board = new Board(8, 8, false);
		Game game = new Game("Classic", new Board[] { board }, new Team[] { whiteTeam, blackTeam }, new TurnKeeper() //$NON-NLS-1$
				{
					@Override
					public int undo()
					{
						return 0;
					}

					@Override
					public int getTeamIndexForNextTurn()
					{
						return 0;
					}
				});

		GameController.setGame(game);

		// the pawn is on the origin
		if (!whitePawn.getCoordinates().equals(origin))
			fail("Test failed to set up piece properly.");

		// move the pawn
		MoveController.execute(new Move(origin, destination));

		// the pawn is on the destination

		if (!whitePawn.getCoordinates().equals(destination))
			fail("MoveController.execute() failed to update the moved piece's Coordinates properly.");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMovementUndoUpdatesPieceCoordinates()
	{
		ChessCoordinates origin = new ChessCoordinates(1, 1, 0);
		ChessCoordinates destination = new ChessCoordinates(2, 2, 0);

		// place a white pawn on the origin
		List<Piece> whitePieceList = Lists.newArrayList();
		Piece whitePawn = new Piece(0, PieceBuilder.getPawnPieceType(), destination);
		whitePieceList.add(whitePawn);
		Team whiteTeam = new Team(null, whitePieceList);

		List<Piece> blackPieceList = Lists.newArrayList();
		Team blackTeam = new Team(null, blackPieceList);

		Board board = new Board(8, 8, false);
		Game game = new Game("Classic", new Board[] { board }, new Team[] { whiteTeam, blackTeam }, new TurnKeeper() //$NON-NLS-1$
				{
					@Override
					public int undo()
					{
						return 0;
					}

					@Override
					public int getTeamIndexForNextTurn()
					{
						return 0;
					}
				});

		GameController.setGame(game);

		// the pawn is on the destination
		if (!whitePawn.getCoordinates().equals(destination))
			fail("Test failed to set up piece properly.");

		// unmove the pawn
		MoveController.undo(new Move(origin, destination));

		// the pawn is on the destination

		if (!whitePawn.getCoordinates().equals(origin))
			fail("MoveController.undo() failed to update the moved piece's Coordinates properly.");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMoveExecuteThenUndoKeepsPieceCoordinates()
	{
		ChessCoordinates origin = new ChessCoordinates(1, 1, 0);
		ChessCoordinates destination = new ChessCoordinates(2, 2, 0);

		// place a white pawn on the origin
		List<Piece> whitePieceList = Lists.newArrayList();
		Piece whitePawn = new Piece(0, PieceBuilder.getPawnPieceType(), origin);
		whitePieceList.add(whitePawn);
		Team whiteTeam = new Team(null, whitePieceList);

		List<Piece> blackPieceList = Lists.newArrayList();
		Team blackTeam = new Team(null, blackPieceList);

		Board board = new Board(8, 8, false);
		Game game = new Game("Classic", new Board[] { board }, new Team[] { whiteTeam, blackTeam }, new TurnKeeper() //$NON-NLS-1$
				{
					@Override
					public int undo()
					{
						return 0;
					}

					@Override
					public int getTeamIndexForNextTurn()
					{
						return 0;
					}
				});

		GameController.setGame(game);

		// the pawn is on the origin
		if (!whitePawn.getCoordinates().equals(origin))
			fail("Test failed to set up piece properly.");

		// move then unmove the pawn
		Move move = new Move(origin, destination);
		MoveController.execute(move);
		MoveController.undo(move);

		if (!whitePawn.getCoordinates().equals(origin))
			fail("MoveController.execute() followed by .undo() failed to restore the Piece's Coordinates.");
	}

}
