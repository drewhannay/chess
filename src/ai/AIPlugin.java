package ai;

import ai.AIAdapter.AIBoard;

public interface AIPlugin {
	
	public FakeMove getMove(AIBoard[] boards);
}
