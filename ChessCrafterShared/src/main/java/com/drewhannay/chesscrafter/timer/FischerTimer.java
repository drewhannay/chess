package com.drewhannay.chesscrafter.timer;

class FischerTimer extends ChessTimer {
    public FischerTimer(long incrementAmount, long startTime, boolean isFischerAfterTimer, boolean isBlack) {
        mIncrementAmount = incrementAmount;
        mCurrentTime = startTime;
        mIsFisherAfterTimer = isFischerAfterTimer;
        mIsFirstTime = true;
        mIsBlackTeamTimer = isBlack;
        mInitialStartTime = startTime;
    }

    @Override
    public void reset() {
        mClockLastUpdatedTime = System.currentTimeMillis();
        mCurrentTime = mInitialStartTime;
        updateDisplay();
        mIsFirstTime = true;
    }

    @Override
    public void startTimer() {
        if (!mIsFisherAfterTimer && !mIsFirstTime && !mTimeWasRecentlyReset)
            mCurrentTime += mIncrementAmount;

        mTimeWasRecentlyReset = false;
        mIsFirstTime = false;

        mClockLastUpdatedTime = System.currentTimeMillis();
        if (mListener != null)
            mListener.onTimerStart();
    }

    @Override
    public void stopTimer() {
        if (mIsFisherAfterTimer && !mTimeWasRecentlyReset)
            mCurrentTime += mIncrementAmount;

        mTimeWasRecentlyReset = false;
        mClockLastUpdatedTime = System.currentTimeMillis();
        updateDisplay();
        if (mListener != null)
            mListener.onTimerStop();
    }

    private long mIncrementAmount;
    private boolean mIsFisherAfterTimer;
    private boolean mIsFirstTime;
}
