package timer;

import utility.RunnableOfT;

class SimpleDelayTimer extends ChessTimer
{
	public SimpleDelayTimer(RunnableOfT<Boolean> timeElapsedCallback, long delayTime, long startTime, boolean isBlackTeamTimer)
	{
		m_delayTime = delayTime;
		m_isBlackTeamTimer = isBlackTeamTimer;
		m_currentTime = startTime;
		m_initialStartTime = startTime;
		m_clockLastUpdatedTime = System.currentTimeMillis();
		init(timeElapsedCallback);
	}

	@Override
	public void startTimer()
	{
		m_isDelayedTimer = false;
		m_clockLastUpdatedTime = System.currentTimeMillis();
		updateDisplay();
		m_timer.setInitialDelay((int) m_delayTime);
		m_isDelayedTimer = true;
		m_timer.start();
	}

	@Override
	public void stopTimer()
	{
		m_clockLastUpdatedTime = System.currentTimeMillis();
		updateDisplay();
		m_timer.stop();
	}

	private static final long serialVersionUID = 5421690863308194342L;

	private long m_delayTime;
}
