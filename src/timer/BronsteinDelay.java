package timer;

/**
 * A concrete class to model the
 * Bronstein Delay timer.
 * @author alisa.maas
 *
 */
public class BronsteinDelay extends ChessTimer {
	/**
	 * For Serialization
	 */
	private static final long serialVersionUID = 5421690863308194342L;
	/**
	 * How much to increment the timer by each turn.
	 */
	private long increment;
	/**
	 * Whether it is the first time this timer
	 * has been run.
	 */
	private boolean firstTime = true;
	/**
	 * The lag time - used for computing
	 * how much time to add to the clock.
	 */
	private long lag;

	/**
	 * Create a new BronsteinDelay timer.
	 * @param increment How much to increment by
	 * @param startTime The start time.
	 * @param isBlack Whether the timer is black.
	 */
	public BronsteinDelay(long increment, long startTime, boolean isBlack) {
		this.increment = increment;
		this.isBlack = isBlack;
		time = startTime;
		this.startTime = time;
		lastUpdated = System.currentTimeMillis();
		super.init();
	}

	@Override
	public void start() {
		lastUpdated = System.currentTimeMillis();
		lag = System.currentTimeMillis();
		timer.start();
	}

	@Override
	public void stop() {
		lastUpdated = System.currentTimeMillis();
		long delay = System.currentTimeMillis() - lag;
		if (!firstTime && !timeSet) {
			time += (delay >= increment ? increment : delay);
		} else {
			firstTime = false;
		}
		timeSet = false;
		updateDisplay();
		timer.stop();
	}

}
