package com.drewhannay.chesscrafter.timer;

class SimpleDelayTimer extends ChessTimer {
    public SimpleDelayTimer(long delayTime, long startTime, boolean isBlackTeamTimer) {
        mDelayTime = delayTime;
        mIsBlackTeamTimer = isBlackTeamTimer;
        mCurrentTime = startTime;
        mInitialStartTime = startTime;
        mClockLastUpdatedTime = System.currentTimeMillis();
    }

    @Override
    public void startTimer() {
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
    public void stopTimer() {
        mClockLastUpdatedTime = System.currentTimeMillis();
        updateDisplay();
        if (mListener != null)
            mListener.onTimerStop();
    }

    private long mDelayTime;
}
