
package gui;

import javax.swing.JMenu;
import logic.Result;
import models.Board;
import controllers.ComputedPieceData;

public interface PlayGameScreen
{
	public void saveGame();

	public JMenu createMenuBar();

	public void turn(boolean isBlackTurn);

	public void setNextMoveMustPlacePiece(boolean nextMoveMustPlacePiece);

	public boolean getNextMoveMustPlacePiece();

	public void boardRefresh(Board[] boards);

	public void setPieceToPlace(ComputedPieceData piece);

	public void endOfGame(Result result);

	public void resetTimers();
}
