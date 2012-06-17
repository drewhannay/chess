package timer;

/**
 * Dummy timer to allow the game to NOT be timed. All methods do nothing.
 * 
 * @author alisa.maas
 * 
 */
public class NoTimer extends ChessTimer
{

	/**
	 * For Serialization.
	 */
	private static final long serialVersionUID = -5680712751187652015L;

	/**
	 * Create a NoTimer object.
	 */
	public NoTimer()
	{
		super.init();
	}

	@Override
	public void start()
	{
	}

	@Override
	public void stop()
	{
	}

	@Override
	public void timeElapsed()
	{
	}

	@Override
	public void reset()
	{
	}

}
