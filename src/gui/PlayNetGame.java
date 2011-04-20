package gui;

import logic.Game;

public class PlayNetGame extends PlayGame {

	private static final long serialVersionUID = -4220208356045682711L;
	
	private boolean isBlack;

	public PlayNetGame(Game g, boolean isPlayback, boolean isBlack) {
		super(g, isPlayback);
		this.isBlack = isBlack;
	}
	
	/**
	 * Getter method for isBlack boolean
	 * @return If the player of this game controls the white or black team
	 */
	public boolean isBlack(){
		return isBlack;
	}
	
	

}
