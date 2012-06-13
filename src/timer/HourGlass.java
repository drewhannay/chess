package timer;

/**
 * A class to model the Hour Glass timer.
 * 
 * @author alisa.maas
 * 
 */
public class HourGlass extends ChessTimer
{
	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 1345233312932413270L;

	/**
	 * Create an HourGlass timer.
	 * 
	 * @param startTime When to start
	 * @param isBlack Whether the clock is black.
	 */
	public HourGlass(long startTime, boolean isBlack)
	{
		time = startTime;
		this.isBlack = isBlack;
		lastUpdated = System.currentTimeMillis();
		this.startTime = startTime;
		super.init();
	}

	/**
	 * Change direction; easy way to check and see whether you've gone past the
	 * alloted time difference between the timers.
	 */
	public void reverseFlow()
	{
		direction *= -1;

	}

	@Override
	public void start()
	{

		direction = 1;
		lastUpdated = System.currentTimeMillis();
		timer.start();
	}

	@Override
	public void stop()
	{
		direction = -1;
		lastUpdated = System.currentTimeMillis();
		timer.start();
	}
}
