package timer;

import utility.RunnableOfT;

public class BronsteinDelayTimer extends ChessTimer
{
	public BronsteinDelayTimer(RunnableOfT<Boolean> callback, long incrementAmount, long currentTime, boolean isBlackTeamTimer)
	{
		m_incrementAmount = incrementAmount;
		m_isBlackTeamTimer = isBlackTeamTimer;
		m_currentTime = currentTime;
		m_initialStartTime = m_currentTime;
		m_clockLastUpdatedTime = System.currentTimeMillis();
		init(callback);
	}

	@Override
	public void startTimer()
	{
		m_clockLastUpdatedTime = System.currentTimeMillis();
		m_lagTime = System.currentTimeMillis();
		m_timer.start();
	}

	@Override
	public void reset()
	{
		m_clockLastUpdatedTime = System.currentTimeMillis();
		m_currentTime = m_initialStartTime;
		updateDisplay();
		m_isFirstRun = true;
	}

	@Override
	public void stopTimer()
	{
		m_clockLastUpdatedTime = System.currentTimeMillis();
		long delay = System.currentTimeMillis() - m_lagTime;

		if (!m_isFirstRun && !m_timeWasRecentlyReset)
			m_currentTime += (delay >= m_incrementAmount ? m_incrementAmount : delay);
		else
			m_isFirstRun = false;

		m_timeWasRecentlyReset = false;
		updateDisplay();
		m_timer.stop();
	}

	private static final long serialVersionUID = 5421690863308194342L;

	private long m_incrementAmount;
	private boolean m_isFirstRun = true;
	private long m_lagTime;
}
