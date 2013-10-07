package gui;

import ai.FakeMove;

public interface PlayNetGameScreen {

	public boolean isBlackPlayer();

	public void setIsAIGame(boolean isAIGame);

	public void setIsRunning(boolean b);

	public boolean isRunning();

	public FakeMove getNetMove();

	public boolean drawRequested();

	public void setDrawRequested(boolean b);

	public void setNetMove(FakeMove fakeMove);
}
