
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
		mPieceIndex = 0;
		
		mMovingPiece = new Piece(mPieceIndex++, PieceBuilder.getRookPieceType(), mOrigin);
		mSameTeamPiece = new Piece(mPieceIndex++, PieceBuilder.getRookPieceType(), mStorageCoordinates);
		mOppositeTeamPiece = new Piece(mPieceIndex++, PieceBuilder.getRookPieceType(), mStorageCoordinates);

		final List<Piece> whitePieceList = Lists.newArrayList(mMovingPiece, mSameTeamPiece);
		final List<Piece> blackPieceList = Lists.newArrayList(mOppositeTeamPiece);

		mMyTeam = new Team(null, whitePieceList);
		mOtherTeam = new Team(null, blackPieceList);

		Team[] teams = new Team[] { mMyTeam, mOtherTeam };
		Board board = new Board(8, 8, false);
		GameController.setGame(new Game("Classic", new Board[] { board }, teams, mTurnKeeper)); //$NON-NLS-1$
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
		mMovingPiece.setCoordinates(mOrigin);
	}

	@After
	public void tearDown() throws Exception
	{
		for (Piece piece : mMyTeam.getCapturedPieces())
			mMyTeam.markPieceAsNotCaptured(piece);
		
		for (Piece piece : mMyTeam.getPieces())
			piece.setCoordinates(mStorageCoordinates);

		for (Piece piece : mOtherTeam.getCapturedPieces())
			mOtherTeam.markPieceAsNotCaptured(piece);
		
		for (Piece piece : mOtherTeam.getPieces())
			piece.setCoordinates(mStorageCoordinates);
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMovementUpdatesPieceCoordinates()
	{
		if (!mMovingPiece.getCoordinates().equals(mOrigin))
			fail("Test failed to set up piece properly.");

		ChessCoordinates destination = new ChessCoordinates(1, 2, 0);
		
		if (!MoveController.execute(new Move(mOrigin, destination)))
			fail("MoveController.execute() returned false for a valid move");

		if (!mMovingPiece.getCoordinates().equals(destination))
			fail("MoveController.execute() failed to update the moved piece's Coordinates properly.");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMovementUndoUpdatesPieceCoordinates()
	{
		ChessCoordinates destination = new ChessCoordinates(1, 2, 0);
		mMovingPiece.setCoordinates(destination);

		if (!MoveController.undo(new Move(mOrigin, destination)))
			fail("MoveController.undo() returned false for a valid move");

		if (!mMovingPiece.getCoordinates().equals(mOrigin))
			fail("MoveController.undo() failed to update the unmoved piece's Coordinates properly.");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMoveExecuteThenUndoKeepsPieceCoordinates()
	{
		mOppositeTeamPiece.setCoordinates(mStorageCoordinates);
		if (!mMovingPiece.getCoordinates().equals(mOrigin))
			fail("Test failed to set up piece properly.");

		Move move = new Move(mOrigin, new ChessCoordinates(1, 6, 0));
		
		if (!MoveController.execute(move))
			fail("MoveController.execute() returned false for a valid move");
		if (!MoveController.undo(move))
			fail("MoveController.undo() returned false for a valid move");

		if (!mMovingPiece.getCoordinates().equals(mOrigin))
			fail("MoveController.execute() followed by .undo() failed to restore the Piece's Coordinates.");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testCapture()
	{
		ChessCoordinates destinationWithOppositePiece = new ChessCoordinates(1, 3, 0);
		mOppositeTeamPiece.setCoordinates(destinationWithOppositePiece);

		
		if (!MoveController.execute(new Move(mOrigin, destinationWithOppositePiece)))
			fail("MoveController.execute() returned false for a valid move");

		if (!mMovingPiece.getCoordinates().equals(destinationWithOppositePiece))
			fail("Capturing piece failed to change Coordinates");
		if (!mOtherTeam.getCapturedPieces().contains(mOppositeTeamPiece))
			fail("Captured piece not in captured list");
		if (mOtherTeam.getPieces().contains(mOppositeTeamPiece))
			fail("Captured piece still in uncaptured list");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testUndoCapture()
	{
		ChessCoordinates destinationWithOppositePiece = new ChessCoordinates(1, 3, 0);
		
		mOtherTeam.markPieceAsCaptured(mOppositeTeamPiece);
		mMovingPiece.setCoordinates(destinationWithOppositePiece);
		mOppositeTeamPiece.setCoordinates(destinationWithOppositePiece);

		if (!MoveController.undo(new Move(mOrigin, destinationWithOppositePiece)))
			fail("MoveController.undo() returned false for a valid move");

		if (!mMovingPiece.getCoordinates().equals(mOrigin))
			fail("Uncapturing piece failed to change Coordinates");
		if (mOtherTeam.getCapturedPieces().contains(mOppositeTeamPiece))
			fail("Uncaptured piece still marked as captured");
		if (!mOppositeTeamPiece.getCoordinates().equals(destinationWithOppositePiece))
			fail("Uncaptured piece failed to restore Coordinates properly");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testExecuteThenUndoCapture()
	{
		ChessCoordinates destination = new ChessCoordinates(1, 5, 0);
		mOppositeTeamPiece.setCoordinates(destination);
		Move move = new Move(mOrigin, destination);
		
		if (!MoveController.execute(move))
			fail("MoveController.execute() returned false for a valid move");
		if (!MoveController.undo(move))
			fail("MoveController.undo() returned false for a valid move");

		if (!mMovingPiece.getCoordinates().equals(mOrigin))
			fail("Capturing piece failed to restore Coordinates properly");
		if (mOtherTeam.getCapturedPieces().contains(mOppositeTeamPiece))
			fail("Uncaptured piece still marked as captured");
		if (!mOppositeTeamPiece.getCoordinates().equals(destination))
			fail("Captured piece failed to restore Coordinates properly");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testCaptureSameTeam()
	{
		ChessCoordinates destination = new ChessCoordinates(1, 5, 0);
		if (!mMovingPiece.getCoordinates().equals(mOrigin))
			fail("Failed to set up moving piece coordinates");
		if (!mSameTeamPiece.getCoordinates().equals(destination))
			fail("Failed to set up same-team piece coordinates");

		if (MoveController.execute(new Move(mOrigin, destination)))
			fail("MoveController.execute() returned true for a same-team capture");

		if (!mMovingPiece.getCoordinates().equals(mOrigin))
			fail("Same-team capture changed capturer's Coordinates");
		if (mMyTeam.getCapturedPieces().contains(mSameTeamPiece))
			fail("Same-team capture changed capturee's isCaptured() state");
		if (!mSameTeamPiece.getCoordinates().equals(destination))
			fail("Same-team capture changed capturee's Coordinates");
	}

	@SuppressWarnings("nls")
	@Test
	public final void testMovingThroughPieceWithNonLeaper()
	{
		mOppositeTeamPiece.setCoordinates(new ChessCoordinates(1, 2, 0));
		mSameTeamPiece.setCoordinates(new ChessCoordinates(2, 1, 0));

		if (MoveController.execute(new Move(mOrigin, new ChessCoordinates(1, 3, 0))))
			fail("MoveController.execute() returned true for a rook moving through an opposite-team piece");

		if (MoveController.execute(new Move(mOrigin, new ChessCoordinates(3, 1, 0))))
			fail("MoveController.execute() returned true for a rook moving through a same-team piece");

		mMovingPiece.setCoordinates(mStorageCoordinates);
		ChessCoordinates obstacleCoordinates = new ChessCoordinates(2, 2, 0);

		// place a bishop on the origin to test moving through pieces
		Piece bishop = new Piece(mPieceIndex++, PieceBuilder.getBishopPieceType(), mOrigin);
		mMyTeam.getPieces().add(bishop);
		mSameTeamPiece.setCoordinates(obstacleCoordinates);

		ChessCoordinates bishopDestination = new ChessCoordinates(3, 3, 0);
		Move attemptedBishopJump = new Move(mOrigin, bishopDestination);
		if (MoveController.execute(attemptedBishopJump))
			fail("MoveController.execute() returned true for a bishop moving through a same-team piece");

		mSameTeamPiece.setCoordinates(mStorageCoordinates);
		mOppositeTeamPiece.setCoordinates(obstacleCoordinates);

		if (MoveController.execute(attemptedBishopJump))
			fail("MoveController.execute() returned true for a bishop moving through an opposite-team piece");

		bishop.setCoordinates(mStorageCoordinates);

		// place a bishop on the origin to test moving through pieces
		Piece queen = new Piece(mPieceIndex++, PieceBuilder.getQueenPieceType(), mOrigin);
		mMyTeam.getPieces().add(queen);
		mSameTeamPiece.setCoordinates(obstacleCoordinates);

		Move attemptedQueenJump = new Move(mOrigin, new ChessCoordinates(3, 3, 0));
		if (MoveController.execute(attemptedQueenJump))
			fail("MoveController.execute() returned true for a queen moving diagonally through a same-team piece");

		mSameTeamPiece.setCoordinates(mStorageCoordinates);
		mOppositeTeamPiece.setCoordinates(obstacleCoordinates);

		if (MoveController.execute(attemptedQueenJump))
			fail("MoveController.execute() returned true for a queen moving diagonally through an opposite-team piece");

		obstacleCoordinates = new ChessCoordinates(1, 2, 0);

		attemptedQueenJump = new Move(mOrigin, new ChessCoordinates(1, 5, 0));
		mOppositeTeamPiece.setCoordinates(mStorageCoordinates);
		mSameTeamPiece.setCoordinates(obstacleCoordinates);

		if (MoveController.execute(attemptedQueenJump))
			fail("MoveController.execute() returned true for a queen moving horizontally through a same-team piece");

		mOppositeTeamPiece.setCoordinates(obstacleCoordinates);
		mSameTeamPiece.setCoordinates(mStorageCoordinates);

		if (MoveController.execute(attemptedQueenJump))
			fail("MoveController.execute() returned true for a queen moving horizontally through an opposite-team piece");

		obstacleCoordinates = new ChessCoordinates(2, 1, 0);
		attemptedQueenJump = new Move(mOrigin, new ChessCoordinates(5, 1, 0));
		mSameTeamPiece.setCoordinates(obstacleCoordinates);

		if (MoveController.execute(attemptedQueenJump))
			fail("MoveController.execute() returned true for a queen moving vertically through a same-team piece");

		mSameTeamPiece.setCoordinates(mStorageCoordinates);
		mOppositeTeamPiece.setCoordinates(obstacleCoordinates);

		if (MoveController.execute(attemptedQueenJump))
			fail("MoveController.execute() returned true for a queen moving vertically through an opposite-team piece");

		queen.setCoordinates(mStorageCoordinates);

		mMyTeam.getPieces().add(new Piece(mPieceIndex++, PieceBuilder.getKnightPieceType(), mOrigin));
		mOppositeTeamPiece.setCoordinates(new ChessCoordinates(1, 2, 0));
		mOtherTeam.getPieces().add(new Piece(mPieceIndex++, PieceBuilder.getRookPieceType(), new ChessCoordinates(2, 1, 0)));
		mOtherTeam.getPieces().add(new Piece(mPieceIndex++, PieceBuilder.getRookPieceType(), new ChessCoordinates(2, 2, 0)));

		if (!MoveController.execute(new Move(mOrigin, new ChessCoordinates(2, 3, 0))))
			fail("MoveController.execute() returned false for a knight jumping over a barrier.");
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
	private final static ChessCoordinates mStorageCoordinates = new ChessCoordinates(8, 8, 0);
	private static Piece mMovingPiece;
	private static Piece mSameTeamPiece;
	private static Piece mOppositeTeamPiece;
	private static Team mMyTeam;
	private static Team mOtherTeam;
	private static int mPieceIndex;
}
