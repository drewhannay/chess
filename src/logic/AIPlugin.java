package logic;

import logic.AIAdapter.AIBoard;
import net.FakeMove;

public interface AIPlugin {
	
	public FakeMove getMove(AIBoard[] boards);
}
