
package controllers;

import models.ChessCoordinates;
import models.Move;
import models.Piece;
import models.Team;

public class MoveController
{
	/**
	 * Execute the constructed Move.
	 */
	public static boolean execute(Move move)
	{
		ChessCoordinates origin = move.getOrigin();
		ChessCoordinates destination = move.getDestination();

		Piece movingPiece = null;
		Piece capturedPiece = null;

		Team movingTeam = null;
		Team capturedTeam = null;

		for (Team team : GameController.getGame().getTeams())
		{
			for (Piece piece : team.getPieces())
			{
				if (team.getCapturedPieces().contains(piece))
					continue;
				if (piece.getCoordinates().equals(origin))
				{
					movingPiece = piece;
					movingTeam = team;
				}
				else if (piece.getCoordinates().equals(destination))
				{
					capturedPiece = piece;
					capturedTeam = team;
				}
			}
		}

		if (movingPiece == null)
			return false;

		if (movingTeam.equals(capturedTeam))
			return false;

		if (!GameController.isLegalMove(movingPiece, destination))
			return false;

		/**
		 * All code past this point changes Piece state data. If a move is invalid, it would be
		 * best to return false before here.
		 */

		if (capturedPiece != null)
		{
			capturedTeam.markPieceAsCaptured(capturedPiece);
		}

		movingPiece.setCoordinates(destination);

		// prevEnpassantCol = board.getEnpassantCol();
		//
		// if (board.getGame().isClassicChess())
		// {
		// if (castleKingside)
		// {
		// if (board.isBlackTurn())
		// {
		// origin = board.getSquare(8, 5);
		// setDest(board.getSquare(8, 7));
		// }
		// else
		// {
		// origin = board.getSquare(1, 5);
		// setDest(board.getSquare(1, 7));
		// }
		// }
		// else if (castleQueenside)
		// {
		// if (board.isBlackTurn())
		// {
		// origin = board.getSquare(8, 5);
		// setDest(board.getSquare(8, 3));
		// }
		// else
		// {
		// origin = board.getSquare(1, 5);
		// setDest(board.getSquare(1, 3));
		// }
		// }
		// }
		//
		// setPiece(origin.getPiece());
		//
		// if (!origin.isOccupied())
		// return false; // If there is no Piece to move, return unsuccessful
		//
		// if (getPiece().isBlack() != board.isBlackTurn())
		// return false;
		//
		// setCaptured(getDest().getPiece());
		//
		// if (board.getGame().isClassicChess())
		// {
		//			if (origin.getPiece().getName().equals(Messages.getString("pawn")) && getCaptured() == null && origin.getCol() != getDest().getCol()) //$NON-NLS-1$
		// {
		// setCaptured(board.getSquare(origin.getRow(),
		// getDest().getCol()).getPiece());
		// }
		// else
		// {
		// board.setEnpassantCol(BoardController.NO_ENPASSANT);
		// }
		// }
		//
		// // Take the captured Piece off the board
		// if (getCaptured() != null)
		// {
		// getCaptured().setIsCaptured(true);
		// SquareController capSquare = getCaptured().getSquare();
		// capSquare.setPiece(null);
		// }
		//
		// // Only do enpassant and castling for Classic chess.
		// if (board.getGame().isClassicChess())
		// {
		// // Mark enpassant on the board
		//			if (origin.getPiece().getName().equals(Messages.getString("pawn")) && Math.abs(origin.getRow() - getDest().getRow()) == 2) //$NON-NLS-1$
		// {
		// board.setEnpassantCol(origin.getCol());
		// }
		//
		// // Castling
		//			if (origin.getPiece().getName().equals(Messages.getString("king")) && origin.getPiece().getMoveCount() == 0) //$NON-NLS-1$
		// {
		//
		// SquareController rookOrigin;
		// SquareController rookDest;
		// // Long
		// if (getDest().getCol() == 3)
		// { // c
		// rookOrigin = board.getSquare(origin.getRow(), 1); // a
		// rookDest = board.getSquare(origin.getRow(), 4); // d
		// rookDest.setPiece(rookOrigin.setPiece(null));
		// rookDest.getPiece().setMoveCount(rookDest.getPiece().getMoveCount() +
		// 1);
		// castleQueenside = true;
		// }
		// // Short
		// else if (getDest().getCol() == 7)
		// { // g
		// rookOrigin = board.getSquare(origin.getRow(), 8); // h
		// rookDest = board.getSquare(origin.getRow(), 6); // f
		// rookDest.setPiece(rookOrigin.setPiece(null));
		// rookDest.getPiece().setMoveCount(rookDest.getPiece().getMoveCount() +
		// 1);
		// castleKingside = true;
		// }
		// }
		// }
		//
		// unique = board.isDestUniqueForClass(getDest(), getPiece());
		//
		// getDest().setPiece(origin.setPiece(null));// Otherwise, move the
		// Piece.
		// // Clear the list of legal destinations for the piece.
		// ComputedPieceData p = getDest().getPiece();
		// p.getLegalDests().clear();
		// p.getGuardSquares().clear();
		// p.setPinnedBy(null);
		// p.setMoveCount(p.getMoveCount() + 1);
		//
		// List<SquareController> promoSquares = (board.isBlackTurn() ?
		// board.getGame().getBlackRules() : board.getGame().getWhiteRules())
		// .getPromotionSquares(p);
		// if (promoSquares != null && promoSquares.contains(getDest()))
		// {
		// promotion = (board.isBlackTurn() ? board.getGame().getBlackRules() :
		// board.getGame().getWhiteRules()).promote(p,
		// isVerified(), promo);
		// }
		//
		// board.getGame().afterMove(this);
		//
		// if (isVerified() && prev == null)
		// {
		// oldWhiteTime = oldBlackTime = -1;
		// }
		// else
		// {
		// oldWhiteTime = board.getGame().getWhiteTimer().getRawTime();
		// oldBlackTime = board.getGame().getBlackTimer().getRawTime();
		// }
		// if (ChessTimer.isWordTimer(board.getGame().getWhiteTimer()))
		// {
		// oldWhiteDirection =
		// board.getGame().getWhiteTimer().getClockDirection();
		// oldBlackDirection =
		// board.getGame().getBlackTimer().getClockDirection();
		// }
		//
		// prev = board.getGame().getLastMove();
		// board.getGame().setLastMove(this);
		//
		// board.getGame().setStaleLegalDests(true);
		//
		// if (!isVerified())
		// {
		// board.getGame().genLegalDests();
		// }
		//
		// setVerified(true);
		//
		// if (!board.getGame().isPlayback())
		// {
		// GuiUtility.getChessCrafter().getPlayGameScreen(board.getGame()).boardRefresh(board.getGame().getBoards());
		// board.getGame().nextTurn();
		// }
		return true;
	}

	/**
	 * Undo the execution of this move
	 * 
	 * @throws Exception
	 *             If the undo doesn't work properly
	 */
	public static boolean undo(Move move)
	{
		ChessCoordinates origin = move.getOrigin();
		ChessCoordinates destination = move.getDestination();

		Piece unmovingPiece = null;
		Team movingTeam = null;
		for (Team team : GameController.getGame().getTeams())
		{
			for (Piece piece : team.getPieces())
			{
				if (piece.getCoordinates().equals(destination))
				{
					unmovingPiece = piece;
					movingTeam = team;
				}
			}
		}

		unmovingPiece.setCoordinates(origin);

		Piece uncapturedPiece = null;
		Team uncapturedTeam = null;

		for (Team team : GameController.getGame().getTeams())
		{
			if (team.equals(movingTeam))
				continue;

			for (Piece piece : team.getCapturedPieces())
			{
				if (piece.getCoordinates().equals(destination))
				{
					uncapturedPiece = piece;
					uncapturedTeam = team;
				}
			}
		}

		if (uncapturedPiece != null)
			uncapturedTeam.markPieceAsNotCaptured(uncapturedPiece);

		// board.setEnpassantCol(prevEnpassantCol);
		//
		// if (board.getGame().isClassicChess())
		// {
		// // Castling
		//			if (getPiece().getName().equals(Messages.getString("king")) && getPiece().getMoveCount() == 1) //$NON-NLS-1$
		// {
		// SquareController rookOrigin;
		// SquareController rookDest;
		// // Long
		// if (getDest().getCol() == 3)
		// {// c
		// rookOrigin = board.getSquare(origin.getRow(), 1);// a
		// rookDest = board.getSquare(origin.getRow(), 4);// d
		// rookOrigin.setPiece(rookDest.setPiece(null));
		// rookOrigin.getPiece().setSquare(rookOrigin);
		// rookOrigin.getPiece().setMoveCount(rookOrigin.getPiece().getMoveCount()
		// - 1);
		// }
		// // Short
		// else if (getDest().getCol() == 7)
		// {// g
		// rookOrigin = board.getSquare(origin.getRow(), 8);// h
		// rookDest = board.getSquare(origin.getRow(), 6);// f
		// rookOrigin.setPiece(rookDest.setPiece(null));
		// rookOrigin.getPiece().setSquare(rookOrigin);
		// rookOrigin.getPiece().setMoveCount(rookOrigin.getPiece().getMoveCount()
		// - 1);
		// }
		// }
		// }
		//
		// List<SquareController> promoSquares = (board.isBlackTurn() ?
		// board.getGame().getBlackRules() : board.getGame().getWhiteRules())
		// .getPromotionSquares(getPiece());
		// if (promoSquares != null && promoSquares.contains(getDest()))
		// {
		// (board.isBlackTurn() ? board.getGame().getBlackRules() :
		// board.getGame().getWhiteRules()).undoPromote(promotion);
		// }
		// if (getDest().getPiece() != null)
		// getDest().getPiece().setMoveCount(getDest().getPiece().getMoveCount()
		// - 1);
		//
		// origin.setPiece(getDest().setPiece(null));
		// getPiece().setSquare(origin);
		//
		// // Put any captured Pieces back on the board.
		// if (getCaptured() != null)
		// {
		// getCaptured().setIsCaptured(false);
		// getCaptured().getSquare().setPiece(null);
		// getDest().setPiece(getCaptured());
		// getCaptured().getLegalDests().clear();
		// }
		//
		// board.getGame().undoAfterMove(this);
		// board.getGame().getWhiteTimer().setClockTime(oldWhiteTime);
		// board.getGame().getBlackTimer().setClockTime(oldBlackTime);
		// if (ChessTimer.isWordTimer(board.getGame().getWhiteTimer()))
		// {
		// board.getGame().getWhiteTimer().setClockDirection(oldWhiteDirection);
		// board.getGame().getBlackTimer().setClockDirection(oldBlackDirection);
		// }
		// if (!board.getGame().isPlayback())
		// board.getGame().prevTurn();
		// board.getGame().setLastMove(prev);
		//
		// board.getGame().setStaleLegalDests(true);
		//
		// if (!board.getGame().isPlayback())
		// GuiUtility.getChessCrafter().getPlayGameScreen(board.getGame()).boardRefresh(board.getGame().getBoards());

		return true;
	}
}
