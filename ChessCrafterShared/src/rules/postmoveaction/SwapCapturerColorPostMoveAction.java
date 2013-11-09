
package rules.postmoveaction;

import models.Move;

public final class SwapCapturerColorPostMoveAction extends PostMoveAction
{
	@Override
	public void perform(Move move)
	{
		// TODO Auto-generated method stub
		swapColorOfCapturingPiece(move);
	}

	@Override
	public void undo(Move move)
	{
		// TODO Auto-generated method stub
		swapColorOfCapturingPiece(move);
	}

	private void swapColorOfCapturingPiece(Move move)
	{
		// if (move.getCaptured() == null)
		// return;
		// PieceController toSwap = move.getPiece();
		// toSwap.getLegalDests().clear();
		// toSwap.getGuardSquares().clear();
		// toSwap.setPinnedBy(null);
		// (toSwap.isBlack() ? mGame.getBlackTeam() :
		// mGame.getWhiteTeam()).remove(toSwap);
		// (!toSwap.isBlack() ? mGame.getBlackTeam() :
		// mGame.getWhiteTeam()).add(toSwap);
		// toSwap.setBlack(!toSwap.isBlack());
	}

	@Override
	public boolean equals(Object other)
	{
		return other instanceof SwapCapturerColorPostMoveAction;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
}
