
package utility;

import gui.PlayGameScreen;
import gui.WatchGameScreen;
import java.io.File;
import controllers.GameController;

public interface ChessCrafter
{
	public PlayGameScreen getPlayGameScreen(GameController game);

	public void setFileMenuVisibility(boolean visibility);

	public void setOptionsMenuVisibility(boolean visibility);

	public void revertToMainPanel();

	public void setPanel(Object newPanel);

	public WatchGameScreen getWatchGameScreen(File acnFile);
}
