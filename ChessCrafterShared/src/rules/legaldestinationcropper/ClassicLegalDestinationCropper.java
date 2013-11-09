
package rules.legaldestinationcropper;

public final class ClassicLegalDestinationCropper extends LegalDestinationCropper
{
	@Override
	public void cropLegalDestinations()
	{
		// TODO Auto-generated method stub
		// private void classicCropLegalDestinations(PieceController
		// movingObjective, PieceController pieceToAdjust,
		// List<PieceController> enemyTeam)
		// {
		// pieceToAdjust.adjustPinsLegalDests(movingObjective, enemyTeam);
		// }
	}

	@Override
	public boolean equals(Object other)
	{
		return other instanceof ClassicLegalDestinationCropper;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
}
