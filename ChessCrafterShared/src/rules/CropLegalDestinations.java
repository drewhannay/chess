package rules;

import java.util.List;

import logic.Piece;

public enum CropLegalDestinations
{
	CLASSIC, STATIONARY_OBJECTIVE;

	public void cropLegalDestinations(Piece movingObjectivePiece, Piece pieceToAdjust, List<Piece> enemyTeam)
	{
		switch (this)
		{
		case CLASSIC:
			classicCropLegalDestinations(movingObjectivePiece, pieceToAdjust, enemyTeam);
			break;
		case STATIONARY_OBJECTIVE:
			stationaryObjectiveCropLegalDestinations(movingObjectivePiece, pieceToAdjust, enemyTeam);
			break;
		default:
			break;
		}
	}

	private void classicCropLegalDestinations(Piece movingObjective, Piece pieceToAdjust, List<Piece> enemyTeam)
	{
		pieceToAdjust.adjustPinsLegalDests(movingObjective, enemyTeam);
	}

	private void stationaryObjectiveCropLegalDestinations(Piece movingObjective, Piece pieceToAdjust, List<Piece> enemyTeam)
	{
		if (pieceToAdjust == movingObjective)
			pieceToAdjust.getLegalDests().clear();
		else
			pieceToAdjust.adjustPinsLegalDests(movingObjective, enemyTeam);
		return;
	}
}
