package gui;

import javax.swing.JMenu;

import logic.Result;
import models.Board;
import models.Piece;

public interface PlayGameScreen
{
	public void saveGame();

	public JMenu createMenuBar();

	public void turn(boolean isBlackTurn);

	public void setNextMoveMustPlacePiece(boolean nextMoveMustPlacePiece);

	public boolean getNextMoveMustPlacePiece();

	public void boardRefresh(Board[] boards);

	public void setPieceToPlace(Piece piece);

	public void endOfGame(Result result);

	public void resetTimers();
}
