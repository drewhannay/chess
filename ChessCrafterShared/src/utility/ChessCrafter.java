package utility;

import gui.PlayGameScreen;
import gui.PlayNetGameScreen;
import logic.Game;

public interface ChessCrafter
{

	public PlayGameScreen getPlayGameScreen();

	public PlayNetGameScreen getNetGameScreen();

	public PlayNetGameScreen getNetGameScreen(Game g, boolean isPlayback, boolean isBlack);

	public void setFileMenuVisibility(boolean visibility);

	public void setOptionsMenuVisibility(boolean visibility);

	public void revertToMainPanel();

	public void setPanel(Object newPanel);
}
