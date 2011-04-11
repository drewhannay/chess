package timer;

/**
 * A class to implement the 
 * Simple Delay timer.
 * @author alisa.maas
 *
 */
public class SimpleDelay extends ChessTimer {

	/**
	 * For Serialization
	 */
	private static final long serialVersionUID = 5421690863308194342L;
	/**
	 * How long to delay before counting down.
	 */
	private long delay;

	/**
	 * @param delay How long to delay
	 * @param startTime The start time
	 * @param isBlack Whether the clock is black.
	 */
	public SimpleDelay(long delay, long startTime, boolean isBlack) {
		this.delay = delay;
		this.isBlack = isBlack;
		time = startTime;
		this.startTime = startTime;
		lastUpdated = System.currentTimeMillis();
		super.init();
	}

	@Override
	public void start() {
		super.delay = false;
		lastUpdated = System.currentTimeMillis();
		updateDisplay();
		timer.setInitialDelay((int) delay);
		super.delay = true;
		timer.start();
	}

	@Override
	public void stop() {
		lastUpdated = System.currentTimeMillis();
		updateDisplay();
		timer.stop();
	}

}