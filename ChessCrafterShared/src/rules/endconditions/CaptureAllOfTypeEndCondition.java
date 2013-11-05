
package rules.endconditions;

public final class CaptureAllOfTypeEndCondition extends EndCondition
{
	@Override
	public void checkEndCondition()
	{
		// TODO Auto-generated method stub
		// List<PieceController> team = (!mIsBlackRuleSet ? mGame.getBlackTeam()
		// : mGame.getWhiteTeam());
		// for (PieceController piece : team)
		// {
		// if (piece.getName().equals(mPieceName) && !piece.isCaptured())
		// return;
		// }
		// Result result = mIsBlackRuleSet ? Result.BLACK_WIN :
		// Result.WHITE_WIN;
		//		result.setGuiText(Messages.getString("gameOverExcSpace") + result.winText() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		// GuiUtility.getChessCrafter().getPlayGameScreen(mGame).endOfGame(result);
	}

	@Override
	public void undo()
	{
	}
}
