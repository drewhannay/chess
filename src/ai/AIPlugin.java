package ai;

import gui.PlayGame;
import logic.Result;
import ai.AIAdapter.AIBoard;

/**
 * AIPlugin.java
 * 
 * Class providing interface for creating an AI to play against.
 * 
 * @author Drew Hannay
 *
 */
public interface AIPlugin {
	
	/**
	 * Method to determine and return the next move for the AI to make.
	 * @param boards The array of AIBoards which the AI may use to determine moves.
	 * @return FakeMove The FakeMove to be made by the AI player.
	 */
	public abstract FakeMove getMove(AIBoard[] boards);
}
