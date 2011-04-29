package ai;

import gui.PlayGame;
import logic.Result;
import ai.AIAdapter.AIBoard;

public interface AIPlugin {
	
	public abstract FakeMove getMove(AIBoard[] boards);
}
