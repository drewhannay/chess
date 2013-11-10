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
		mWhitePieceList = Lists.newArrayList();
		mBlackPieceList = Lists.newArrayList();
		mOriginPiece = new Piece(0, PieceBuilder.getPawnPieceType(), mOrigin);
		mDestinationPiece = new Piece(1, PieceBuilder.getPawnPieceType(), mDestination);
		mWhitePieceList.add(mOriginPiece);
		mBlackPieceList.add(mDestinationPiece);
		mTeams = new Team[] { new Team(null, mWhitePieceList), new Team(null, mBlackPieceList) };
		mBoard = new Board(8, 8, false);
		GameController.setGame(new Game("Classic", new Board[] { mBoard }, mTeams, mTurnKeeper)); //$NON-NLS-1$
		mMove = new Move(mOrigin, mDestination);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
		mOriginPiece.setCoordinates(mOrigin);
		mOriginPiece.setIsCaptured(false);
		mDestinationPiece.setCoordinates(mDestination);
		mDestinationPiece.setIsCaptured(false);
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
		mDestinationPiece.setCoordinates(mStorageCoordinates);

		if (!mOriginPiece.getCoordinates().equals(mOrigin))
			fail("Test failed to set up piece properly.");

		MoveController.execute(mMove);

		if (!mOriginPiece.getCoordinates().equals(mDestination))
			fail("MoveController.execute() failed to update the moved piece's Coordinates properly.");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMovementUndoUpdatesPieceCoordinates()
	{
		mDestinationPiece.setCoordinates(mStorageCoordinates);
		mOriginPiece.setCoordinates(mDestination);

		if (!mOriginPiece.getCoordinates().equals(mDestination))
			fail("Test failed to set up piece properly.");

		MoveController.undo(mMove);

		if (!mOriginPiece.getCoordinates().equals(mOrigin))
			fail("MoveController.undo() failed to update the moved piece's Coordinates properly.");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMoveExecuteThenUndoKeepsPieceCoordinates()
	{
		mDestinationPiece.setCoordinates(mStorageCoordinates);
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
		if (!mDestinationPiece.getCoordinates().equals(mDestination))
			fail("Test failed to set up captured piece properly.");

		MoveController.execute(mMove);

		if (!mOriginPiece.getCoordinates().equals(mDestination))
			fail("Capturing piece failed to change Coordinates");
		if (!mDestinationPiece.isCaptured())
			fail("Captured piece not marked as captured");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testUndoCapture()
	{
		mDestinationPiece.setIsCaptured(true);
		mOriginPiece.setCoordinates(mDestination);

		if (!mOriginPiece.getCoordinates().equals(mDestination))
			fail("Test failed to set up uncapturing piece coordinates properly.");
		if (!mDestinationPiece.getCoordinates().equals(mDestination))
			fail("Test failed to set up uncaptured piece coordinates properly.");
		if (!mDestinationPiece.isCaptured())
			fail("Test failed to set up uncaptured piece captured state properly.");

		MoveController.undo(mMove);

		if (!mOriginPiece.getCoordinates().equals(mOrigin))
			fail("Uncapturing piece failed to change Coordinates");
		if (mDestinationPiece.isCaptured())
			fail("Uncaptured piece still marked as captured");
		if (!mDestinationPiece.getCoordinates().equals(mDestination))
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
		if (mDestinationPiece.isCaptured())
			fail("Uncaptured piece still marked as captured");
		if (!mDestinationPiece.getCoordinates().equals(mDestination))
			fail("Captured piece failed to restore Coordinates properly");
	}

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
	private final static ChessCoordinates mDestination = new ChessCoordinates(2, 2, 0);
	private final static ChessCoordinates mStorageCoordinates = new ChessCoordinates(3, 3, 0);
	private static List<Piece> mWhitePieceList;
	private static List<Piece> mBlackPieceList;
	private static Piece mOriginPiece;
	private static Piece mDestinationPiece;
	private static Board mBoard;
	private static Team[] mTeams;
	private static Move mMove;
}
