package timer;

public class WordTimer extends ChessTimer
{
	public WordTimer(long startTime)
	{
		m_currentTime = startTime;
		m_initialStartTime = startTime;
		init(null);
	}

	@Override
	public void startTimer()
	{
		m_clockLastUpdatedTime = System.currentTimeMillis();
		m_timer.start();
	}

	@Override
	public void stopTimer()
	{
		m_clockLastUpdatedTime = System.currentTimeMillis();
		updateDisplay();
		m_timer.stop();
	}

	@Override
	public void timeElapsed()
	{
		m_clockDirection = -1;
	}

	private static final long serialVersionUID = -3488243754798571897L;
}
