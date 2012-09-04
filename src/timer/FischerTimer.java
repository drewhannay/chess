package timer;

import utility.RunnableOfT;

public class FischerTimer extends ChessTimer
{
	public FischerTimer(RunnableOfT<Boolean> timeElapsedCallback, long incrementAmount, long startTime, boolean isFischerAfterTimer, boolean isBlack)
	{
		m_incrementAmount = incrementAmount;
		m_currentTime = startTime;
		m_isFisherAfterTimer = isFischerAfterTimer;
		m_isFirstTime = true;
		m_isBlackTeamTimer = isBlack;
		m_initialStartTime = startTime;
		init(timeElapsedCallback);
	}

	@Override
	public void reset()
	{
		m_clockLastUpdatedTime = System.currentTimeMillis();
		m_currentTime = m_initialStartTime;
		updateDisplay();
		m_isFirstTime = true;
	}

	@Override
	public void startTimer()
	{
		if (!m_isFisherAfterTimer && !m_isFirstTime && !m_timeWasRecentlyReset)
			m_currentTime += m_incrementAmount;

		m_timeWasRecentlyReset = false;
		m_isFirstTime = false;

		m_clockLastUpdatedTime = System.currentTimeMillis();
		m_timer.start();
	}

	@Override
	public void stopTimer()
	{
		if (m_isFisherAfterTimer && !m_timeWasRecentlyReset)
			m_currentTime += m_incrementAmount;

		m_timeWasRecentlyReset = false;
		m_clockLastUpdatedTime = System.currentTimeMillis();
		updateDisplay();
		m_timer.stop();
	}

	private static final long serialVersionUID = 6129683219865263879L;

	private long m_incrementAmount;
	private boolean m_isFisherAfterTimer;
	private boolean m_isFirstTime;
}
