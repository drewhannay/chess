package rules;

import java.util.List;
import javax.swing.JOptionPane;
import utility.GuiUtility;
import logic.Board;
import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Square;
import com.google.common.collect.Lists;

public enum AfterMove
{
	CLASSIC,
	SWAP_COLOR_OF_CAPTURER,
	CAPTURED_PIECE_TO_ORIGIN,
	CAPTURER_STEALS_CAPTURED,
	CAPTURER_PLACES_CAPTURED,
	ATOMIC_CAPTURE;

	public void performAfterMoveAction(Move move)
	{
		switch (this)
		{
		case SWAP_COLOR_OF_CAPTURER:
			swapColorOfCapturingPiece(move);
			break;
		case CAPTURED_PIECE_TO_ORIGIN:
			moveCapturedPieceToOrigin(move);
			break;
		case CAPTURER_STEALS_CAPTURED:
			stealCapturedPiece(move);
			break;
		case CAPTURER_PLACES_CAPTURED:
			placeCapturedPiece(move);
			break;
		case ATOMIC_CAPTURE:
			atomicCapture(move);
			break;
		case CLASSIC:
		default:
			break;
		}
	}

	public void undo(Move move)
	{
		switch (this)
		{
		case SWAP_COLOR_OF_CAPTURER:
			swapColorOfCapturingPiece(move);
			break;
		case CAPTURED_PIECE_TO_ORIGIN:
			undoMoveCapturedPieceToOrigin(move);
			break;
		case CAPTURER_STEALS_CAPTURED:
			undoStealCapturedPiece(move);
			break;
		case CAPTURER_PLACES_CAPTURED:
			undoPlaceCapturedPiece(move);
			break;
		case ATOMIC_CAPTURE:
			undoAtomicCapture(move);
			break;
		case CLASSIC:
		default:
			break;
		}
	}

	public void setGame(Game game)
	{
		mGame = game;
	}

	private void swapColorOfCapturingPiece(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece toSwap = move.getPiece();
		toSwap.getLegalDests().clear();
		toSwap.getGuardSquares().clear();
		toSwap.setPinnedBy(null);
		(toSwap.isBlack() ? mGame.getBlackTeam() : mGame.getWhiteTeam()).remove(toSwap);
		(!toSwap.isBlack() ? mGame.getBlackTeam() : mGame.getWhiteTeam()).add(toSwap);
		toSwap.setBlack(!toSwap.isBlack());
	}

	private void moveCapturedPieceToOrigin(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece toHome = move.getCaptured();
		// if you captured a piece on it's original square, let capturing work
		// as normal
		if (toHome.getOriginalSquare() == toHome.getSquare())
			return;
		// otherwise, move the captured piece to it's original square
		toHome.getLegalDests().clear();
		toHome.getGuardSquares().clear();
		toHome.setPinnedBy(null);

		// if there was a piece on the original square, save it so we can put it
		// back if we undo
		move.setRemoved(toHome.getOriginalSquare().getPiece());
		if (move.getRemoved() != null)
			(move.getRemoved().isBlack() ? mGame.getBlackTeam() : mGame.getWhiteTeam()).remove(move.getRemoved());

		// actually set the captured piece on it's home square
		toHome.getOriginalSquare().setPiece(toHome);
		toHome.setIsCaptured(false);
	}

	private void undoMoveCapturedPieceToOrigin(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece restore = move.getCaptured();
		restore.setIsCaptured(false);
		if (move.getRemoved() != null)
		{
			(move.getRemoved().isBlack() ? mGame.getBlackTeam() : mGame.getWhiteTeam()).add(move.getRemoved());
			move.getRemoved().setIsCaptured(false);
		}
		move.getCaptured().getSquare().setPiece(move.getRemoved());
		move.getDest().setPiece(restore);
	}

	private void stealCapturedPiece(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece toPlace = move.getCaptured();
		toPlace.getLegalDests().clear();
		toPlace.getGuardSquares().clear();
		toPlace.setPinnedBy(null);
		(toPlace.isBlack() ? mGame.getBlackTeam() : mGame.getWhiteTeam()).remove(toPlace);
		(!toPlace.isBlack() ? mGame.getBlackTeam() : mGame.getWhiteTeam()).add(toPlace);
		toPlace.setBlack(!toPlace.isBlack());
		toPlace.setIsCaptured(false);
		move.setOldPos(toPlace.getSquare());
		if (move.isVerified())
		{
			JOptionPane.showMessageDialog(null, Messages.getString("pieceIsOnYourSide")); //$NON-NLS-1$
			GuiUtility.getChessCrafter().getPlayGameScreen().setNextMoveMustPlacePiece(true);
			GuiUtility.getChessCrafter().getPlayGameScreen().setPieceToPlace(toPlace);
		}

	}

	private void undoStealCapturedPiece(Move move)
	{
		if (move.getOldPos() == null)
		{
			return;
		}
		else
		{
			Piece toPlace = move.getCaptured();
			toPlace.getLegalDests().clear();
			toPlace.getGuardSquares().clear();
			toPlace.setPinnedBy(null);
			(toPlace.isBlack() ? mGame.getBlackTeam() : mGame.getWhiteTeam()).remove(toPlace);
			(!toPlace.isBlack() ? mGame.getBlackTeam() : mGame.getWhiteTeam()).add(toPlace);
			toPlace.setBlack(!toPlace.isBlack());
			toPlace.setIsCaptured(false);
			toPlace.getSquare().setPiece(null);
			toPlace.setSquare(move.getOldPos());
			move.getOldPos().setPiece(toPlace);
			toPlace.setIsCaptured(false);
			GuiUtility.getChessCrafter().getPlayGameScreen().setNextMoveMustPlacePiece(false);
			GuiUtility.getChessCrafter().getPlayGameScreen().setPieceToPlace(null);
			move.setOldPos(null);
		}
	}

	private void placeCapturedPiece(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece toPlace = move.getCaptured();
		toPlace.getLegalDests().clear();
		toPlace.getGuardSquares().clear();
		toPlace.setPinnedBy(null);

		move.setOldPos(toPlace.getSquare());
		Piece objectivePiece = toPlace.isBlack() ? mGame.getBlackRules().objectivePiece(toPlace.isBlack()) : mGame.getWhiteRules()
				.objectivePiece(toPlace.isBlack());
		if (move.isVerified() && !(objectivePiece == toPlace))
		{
			toPlace.setIsCaptured(false);
			JOptionPane.showMessageDialog(null, Messages.getString("youCapturedPiece")); //$NON-NLS-1$
			GuiUtility.getChessCrafter().getPlayGameScreen().setNextMoveMustPlacePiece(true);
			GuiUtility.getChessCrafter().getPlayGameScreen().setPieceToPlace(toPlace);
		}
	}

	private void undoPlaceCapturedPiece(Move move)
	{
		if (move.getOldPos() == null)
		{
			return;
		}
		else
		{
			Piece toPlace = move.getCaptured();
			toPlace.getLegalDests().clear();
			toPlace.getGuardSquares().clear();
			toPlace.setPinnedBy(null);
			toPlace.setIsCaptured(false);
			toPlace.getSquare().setPiece(null);
			toPlace.setSquare(move.getOldPos());
			move.getOldPos().setPiece(toPlace);
			toPlace.setIsCaptured(false);
			GuiUtility.getChessCrafter().getPlayGameScreen().setNextMoveMustPlacePiece(false);
			GuiUtility.getChessCrafter().getPlayGameScreen().setPieceToPlace(null);
			move.setOldPos(null);
		}
	}

	/**
	 * Capture removes pieces from 8 surrounding squares, including the
	 * capturing piece - with the exception of pawns, unless the pawn is either
	 * the captured piece or the capturer.
	 * 
	 * @param move The move performed.
	 */
	private void atomicCapture(Move move)
	{
		if (move.getCaptured() == null)
			return;
		Piece captured = move.getCaptured();
		Piece suicide = move.getPiece();
		Square curSquare = captured.getSquare();
		Board board = captured.getBoard();
		Square[] squares = new Square[9];
		int n = 0;
		boolean wraparound = board.isWrapAround();
		int upperCol = curSquare.getCol() + 1;
		for (int i = curSquare.getRow() - 1; i <= 1 + curSquare.getRow(); i++)
		{
			for (int j = curSquare.getCol() - 1; j <= upperCol; j++)
			{
				upperCol = curSquare.getCol() + 1;
				if (board.isRowValid(i))
				{
					if (!wraparound && !board.isColValid(j))
						continue;

					int k = j;
					if (k < 1)
					{
						k = board.getMaxCol();
						upperCol = board.getMaxCol() + 1;
					}
					else if (k > board.getMaxCol())
					{
						k = 1;
					}
					squares[n++] = board.getSquare(i, k);
				}
			}
		}
		List<Piece> exploded = Lists.newArrayList();
		for (Square s : squares)
		{
			if (s == null)
				continue;
			Piece p = s.getPiece();
			if (p != null && (!(p.getName().equals(Messages.getString("pawn"))) && p != suicide) && p != captured) //$NON-NLS-1$
			{
				exploded.add(p);
				p.setIsCaptured(true);
				p.getSquare().setPiece(null);
			}
		}
		exploded.add(suicide);
		suicide.setIsCaptured(true);
		suicide.getSquare().setPiece(null);
		Piece[] toReturn = new Piece[exploded.size()];
		move.setExploded(exploded.toArray(toReturn));
	}

	private void undoAtomicCapture(Move move)
	{
		if (move.getCaptured() == null)
		{
			return;
		}
		else
		{
			Piece[] exploded = move.getExploded();
			for (Piece piece : exploded)
			{
				piece.setIsCaptured(false);
				piece.getSquare().setPiece(piece);
			}
			exploded[exploded.length - 1].setMoveCount(exploded[exploded.length - 1].getMoveCount() - 1);
			move.setExploded(null);
		}
	}

	private Game mGame;
}
