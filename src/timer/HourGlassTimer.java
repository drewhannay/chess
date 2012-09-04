package timer;

import utility.RunnableOfT;

public class HourGlassTimer extends ChessTimer
{
	public HourGlassTimer(RunnableOfT<Boolean> timeElapsedCallback, long startTime, boolean isBlackTeamTimer)
	{
		m_currentTime = startTime;
		m_isBlackTeamTimer = isBlackTeamTimer;
		m_clockLastUpdatedTime = System.currentTimeMillis();
		m_initialStartTime = startTime;
		init(timeElapsedCallback);
	}

	/**
	 * Change direction; easy way to check and see whether you've gone past the
	 * alloted time difference between the timers.
	 */
	public void reverseFlow()
	{
		m_clockDirection *= -1;
	}

	@Override
	public void startTimer()
	{
		m_clockDirection = 1;
		m_clockLastUpdatedTime = System.currentTimeMillis();
		m_timer.start();
	}

	@Override
	public void stopTimer()
	{
		m_clockDirection = -1;
		m_clockLastUpdatedTime = System.currentTimeMillis();
		m_timer.start();
	}

	private static final long serialVersionUID = 1345233312932413270L;
}
