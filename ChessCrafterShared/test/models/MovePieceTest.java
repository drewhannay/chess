
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

		mOriginPiece = new Piece(0, PieceBuilder.getPawnPieceType(), mOrigin);
		mSameTeamDestinationPiece = new Piece(1, PieceBuilder.getPawnPieceType(), mSameTeamDestination);
		mOppositeTeamDestinationPiece = new Piece(1, PieceBuilder.getPawnPieceType(), mOppositeTeamDestination);

		final List<Piece> whitePieceList = Lists.newArrayList(mOriginPiece, mSameTeamDestinationPiece);
		final List<Piece> blackPieceList = Lists.newArrayList(mOppositeTeamDestinationPiece);

		mMyTeam = new Team(null, whitePieceList);
		mOtherTeam = new Team(null, blackPieceList);

		Team[] teams = new Team[] { mMyTeam, mOtherTeam };
		Board board = new Board(8, 8, false);
		GameController.setGame(new Game("Classic", new Board[] { board }, teams, mTurnKeeper)); //$NON-NLS-1$
		mMove = new Move(mOrigin, mOppositeTeamDestination);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
		mOriginPiece.setCoordinates(mOrigin);
		mOppositeTeamDestinationPiece.setCoordinates(mOppositeTeamDestination);
		mOtherTeam.markPieceAsNotCaptured(mOppositeTeamDestinationPiece);
		mSameTeamDestinationPiece.setCoordinates(mSameTeamDestination);
		mMyTeam.markPieceAsNotCaptured(mSameTeamDestinationPiece);
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMovementUpdatesPieceCoordinates()
	{
		// place the destination piece out of the way for this test
		mOppositeTeamDestinationPiece.setCoordinates(mStorageCoordinates);

		if (!mOriginPiece.getCoordinates().equals(mOrigin))
			fail("Test failed to set up piece properly.");

		MoveController.execute(mMove);

		if (!mOriginPiece.getCoordinates().equals(mOppositeTeamDestination))
			fail("MoveController.execute() failed to update the moved piece's Coordinates properly.");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMovementUndoUpdatesPieceCoordinates()
	{
		mOppositeTeamDestinationPiece.setCoordinates(mStorageCoordinates);
		mOriginPiece.setCoordinates(mOppositeTeamDestination);

		if (!mOriginPiece.getCoordinates().equals(mOppositeTeamDestination))
			fail("Test failed to set up piece properly.");

		MoveController.undo(mMove);

		if (!mOriginPiece.getCoordinates().equals(mOrigin))
			fail("MoveController.undo() failed to update the moved piece's Coordinates properly.");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMoveExecuteThenUndoKeepsPieceCoordinates()
	{
		mOppositeTeamDestinationPiece.setCoordinates(mStorageCoordinates);
		if (!mOriginPiece.getCoordinates().equals(mOrigin))
			fail("Test failed to set up piece properly.");

		MoveController.execute(mMove);
		MoveController.undo(mMove);

		if (!mOriginPiece.getCoordinates().equals(mOrigin))
			fail("MoveController.execute() followed by .undo() failed to restore the Piece's Coordinates.");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testCapture()
	{
		if (!mOriginPiece.getCoordinates().equals(mOrigin))
			fail("Test failed to set up capturing piece properly.");
		if (!mOppositeTeamDestinationPiece.getCoordinates().equals(mOppositeTeamDestination))
			fail("Test failed to set up captured piece properly.");

		MoveController.execute(mMove);

		if (!mOriginPiece.getCoordinates().equals(mOppositeTeamDestination))
			fail("Capturing piece failed to change Coordinates");
		if (!mOtherTeam.getCapturedPieces().contains(mOppositeTeamDestinationPiece))
			fail("Captured piece not in captured list");
		if (mOtherTeam.getPieces().contains(mOppositeTeamDestinationPiece))
			fail("Captured piece still in uncaptured list");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testUndoCapture()
	{
		mOtherTeam.markPieceAsCaptured(mOppositeTeamDestinationPiece);
		mOriginPiece.setCoordinates(mOppositeTeamDestination);

		if (!mOriginPiece.getCoordinates().equals(mOppositeTeamDestination))
			fail("Test failed to set up uncapturing piece coordinates properly.");
		if (!mOppositeTeamDestinationPiece.getCoordinates().equals(mOppositeTeamDestination))
			fail("Test failed to set up uncaptured piece coordinates properly.");
		if (!mOtherTeam.getCapturedPieces().contains(mOppositeTeamDestinationPiece))
			fail("Test failed to set up uncaptured piece captured state properly.");

		MoveController.undo(mMove);

		if (!mOriginPiece.getCoordinates().equals(mOrigin))
			fail("Uncapturing piece failed to change Coordinates");
		if (mOtherTeam.getCapturedPieces().contains(mOppositeTeamDestinationPiece))
			fail("Uncaptured piece still marked as captured");
		if (!mOppositeTeamDestinationPiece.getCoordinates().equals(mOppositeTeamDestination))
			fail("Uncaptured piece failed to restore Coordinates properly");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testExecuteThenUndoCapture()
	{
		MoveController.execute(mMove);
		MoveController.undo(mMove);

		if (!mOriginPiece.getCoordinates().equals(mOrigin))
			fail("Capturing piece failed to restore Coordinates properly");
		if (mOtherTeam.getCapturedPieces().contains(mOppositeTeamDestinationPiece))
			fail("Uncaptured piece still marked as captured");
		if (!mOppositeTeamDestinationPiece.getCoordinates().equals(mOppositeTeamDestination))
			fail("Captured piece failed to restore Coordinates properly");
	}

	// @SuppressWarnings("nls")
	// @Test
	// public final void testCaptureSameTeam()
	// {
	// MoveController.execute(new Move(mOrigin, mSameTeamDestination));
	//
	// if (!mOriginPiece.getCoordinates().equals(mOrigin))
	// fail("Same-team capture changed capturer's Coordinates");
	// if (mMyTeam.getCapturedPieces().contains(mSameTeamDestinationPiece))
	// fail("Same-team capture changed capturee's isCaptured() state");
	// if (!mSameTeamDestinationPiece.getCoordinates().equals(mSameTeamDestination))
	// fail("Same-team capture changed capturee's Coordinates");
	// }

	private final static TurnKeeper mTurnKeeper = new TurnKeeper()
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
	};

	private final static ChessCoordinates mOrigin = new ChessCoordinates(1, 1, 0);
	private final static ChessCoordinates mOppositeTeamDestination = new ChessCoordinates(2, 2, 0);
	private final static ChessCoordinates mSameTeamDestination = new ChessCoordinates(3, 3, 0);
	private final static ChessCoordinates mStorageCoordinates = new ChessCoordinates(4, 4, 0);
	private static Piece mOriginPiece;
	private static Piece mSameTeamDestinationPiece;
	private static Piece mOppositeTeamDestinationPiece;
	private static Move mMove;
	private static Team mMyTeam;
	private static Team mOtherTeam;
}
