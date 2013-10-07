package timer;

import utility.RunnableOfT;

class BronsteinDelayTimer extends ChessTimer
{
	public BronsteinDelayTimer(RunnableOfT<Boolean> callback, long incrementAmount, long currentTime, boolean isBlackTeamTimer)
	{
		mIncrementAmount = incrementAmount;
		mIsBlackTeamTimer = isBlackTeamTimer;
		mCurrentTime = currentTime;
		mInitialStartTime = mCurrentTime;
		mClockLastUpdatedTime = System.currentTimeMillis();
		init(callback);
	}

	@Override
	public void startTimer()
	{
		mClockLastUpdatedTime = System.currentTimeMillis();
		mLagTime = System.currentTimeMillis();
		mTimer.start();
	}

	@Override
	public void reset()
	{
		mClockLastUpdatedTime = System.currentTimeMillis();
		mCurrentTime = mInitialStartTime;
		updateDisplay();
		mIsFirstRun = true;
	}

	@Override
	public void stopTimer()
	{
		mClockLastUpdatedTime = System.currentTimeMillis();
		long delay = System.currentTimeMillis() - mLagTime;

		if (!mIsFirstRun && !mTimeWasRecentlyReset)
			mCurrentTime += (delay >= mIncrementAmount ? mIncrementAmount : delay);
		else
			mIsFirstRun = false;

		mTimeWasRecentlyReset = false;
		updateDisplay();
		mTimer.stop();
	}

	private static final long serialVersionUID = 5421690863308194342L;

	private long mIncrementAmount;
	private boolean mIsFirstRun = true;
	private long mLagTime;
}
