package timer;

class WordTimer extends ChessTimer
{
	public WordTimer(long startTime)
	{
		mCurrentTime = startTime;
		mInitialStartTime = startTime;
		init(null);
	}

	@Override
	public void startTimer()
	{
		mClockLastUpdatedTime = System.currentTimeMillis();
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

	@Override
	public void timeElapsed()
	{
		mClockDirection = -1;
	}

	private static final long serialVersionUID = -3488243754798571897L;
}
