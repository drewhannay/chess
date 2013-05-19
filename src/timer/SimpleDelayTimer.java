package timer;

import utility.RunnableOfT;

class SimpleDelayTimer extends ChessTimer
{
	public SimpleDelayTimer(RunnableOfT<Boolean> timeElapsedCallback, long delayTime, long startTime, boolean isBlackTeamTimer)
	{
		mDelayTime = delayTime;
		mIsBlackTeamTimer = isBlackTeamTimer;
		mCurrentTime = startTime;
		mInitialStartTime = startTime;
		mClockLastUpdatedTime = System.currentTimeMillis();
		init(timeElapsedCallback);
	}

	@Override
	public void startTimer()
	{
		mIsDelayedTimer = false;
		mClockLastUpdatedTime = System.currentTimeMillis();
		updateDisplay();
		mTimer.setInitialDelay((int) mDelayTime);
		mIsDelayedTimer = true;
		mTimer.start();
	}

	@Override
	public void stopTimer()
	{
		mClockLastUpdatedTime = System.currentTimeMillis();
		updateDisplay();
		mTimer.stop();
	}

	private static final long serialVersionUID = 5421690863308194342L;

	private long mDelayTime;
}
