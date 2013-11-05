
package timer;

import utility.RunnableOfT;

class HourGlassTimer extends ChessTimer
{
	public HourGlassTimer(RunnableOfT<Boolean> timeElapsedCallback, long startTime, boolean isBlackTeamTimer)
	{
		mCurrentTime = startTime;
		mIsBlackTeamTimer = isBlackTeamTimer;
		mClockLastUpdatedTime = System.currentTimeMillis();
		mInitialStartTime = startTime;
		init(timeElapsedCallback);
	}

	/**
	 * Change direction; easy way to check and see whether you've gone past the
	 * alloted time difference between the timers.
	 */
	public void reverseFlow()
	{
		mClockDirection *= -1;
	}

	@Override
	public void startTimer()
	{
		mClockDirection = 1;
		mClockLastUpdatedTime = System.currentTimeMillis();
		if (mListener != null)
			mListener.onTimerStart();
	}

	@Override
	public void stopTimer()
	{
		mClockDirection = -1;
		mClockLastUpdatedTime = System.currentTimeMillis();
		if (mListener != null)
			mListener.onTimerStart();
	}
}
