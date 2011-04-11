package timer;

/**
 * A class to implement the Word
 * timer.
 * @author alisa.maas
 *
 */
public class Word extends ChessTimer {

	/**
	 * For Serialization
	 */
	private static final long serialVersionUID = -3488243754798571897L;

	/**
	 * Make a new Word timer.
	 * @param startTime What time to start at.
	 */
	public Word(long startTime) {
		time = startTime;
		this.startTime = startTime;
		init();
	}

	@Override
	public void start() {
		lastUpdated = System.currentTimeMillis();
		timer.start();
	}

	@Override
	public void stop() {
		lastUpdated = System.currentTimeMillis();
		updateDisplay();
		timer.stop();
	}

	@Override
	public void timeElapsed() {
		//start counting up. Maybe set the font to red here?
		direction = -1;

	}

}
