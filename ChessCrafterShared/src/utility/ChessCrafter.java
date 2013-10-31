package utility;

import gui.PlayGameScreen;
import gui.PlayNetGameScreen;
import gui.WatchGameScreen;
import java.io.File;

import models.Game;

public interface ChessCrafter
{

	public PlayGameScreen getPlayGameScreen(Game game);

	public PlayNetGameScreen getNetGameScreen();

	public PlayNetGameScreen getNetGameScreen(Game g, boolean isPlayback, boolean isBlack);

	public void setFileMenuVisibility(boolean visibility);

	public void setOptionsMenuVisibility(boolean visibility);

	public void revertToMainPanel();

	public void setPanel(Object newPanel);

	public WatchGameScreen getWatchGameScreen(File acnFile);
}
