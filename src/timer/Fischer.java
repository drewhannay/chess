package timer;

/**
 * A class to model the Fisher Timer.
 * 
 * @author alisa.maas
 * 
 */
public class Fischer extends ChessTimer
{
	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 6129683219865263879L;
	/**
	 * How much to increment the timer by.
	 */
	private long increment;
	/**
	 * Whether the timer is "Fisher After", which adds the increment at the end
	 * of each turn rather than at the beginning.
	 */
	private boolean fisherAfter;
	/**
	 * Whether it is the first time.
	 */
	private boolean firstTime;

	/**
	 * Create a Fisher timer.
	 * 
	 * @param increment How much to increment by
	 * @param startTime When to start the timer
	 * @param fischerAfter Whether it is Fisher after
	 * @param isBlack Whether it is black.
	 */
	public Fischer(long increment, long startTime, boolean fischerAfter,
			boolean isBlack)
	{
		this.increment = increment;
		time = startTime;
		fisherAfter = fischerAfter;
		firstTime = true;
		this.isBlack = isBlack;
		this.startTime = startTime;
		super.init();
	}

	@Override
	public void reset()
	{
		lastUpdated = System.currentTimeMillis();
		time = startTime;
		updateDisplay();
		firstTime = true;
	}

	@Override
	public void start()
	{
		if (!fisherAfter && !firstTime && !timeSet)
		{
			time += increment;
		}
		timeSet = false;
		firstTime = false;

		lastUpdated = System.currentTimeMillis();
		timer.start();
	}

	@Override
	public void stop()
	{
		if (fisherAfter && !timeSet)
		{
			time += increment;
		}
		timeSet = false;
		lastUpdated = System.currentTimeMillis();
		updateDisplay();
		timer.stop();
	}

}
