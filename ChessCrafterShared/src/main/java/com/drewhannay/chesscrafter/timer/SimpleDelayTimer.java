
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
		if (mListener != null)
			mListener.setInitialDelay((int) mDelayTime);
		mIsDelayedTimer = true;
		if (mListener != null)
			mListener.onTimerStart();
	}

	@Override
	public void stopTimer()
	{
		mClockLastUpdatedTime = System.currentTimeMillis();
		updateDisplay();
		if (mListener != null)
			mListener.onTimerStop();
	}

	private long mDelayTime;
}
