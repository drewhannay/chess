package com.drewhannay.chesscrafter.timer;

class HourGlassTimer extends ChessTimer {
    public HourGlassTimer(long startTime, boolean isBlackTeamTimer) {
        mCurrentTime = startTime;
        mIsBlackTeamTimer = isBlackTeamTimer;
        mClockLastUpdatedTime = System.currentTimeMillis();
        mInitialStartTime = startTime;
    }

    /**
     * Change direction; easy way to check and see whether you've gone past the
     * alloted time difference between the timers.
     */
    public void reverseFlow() {
        mClockDirection *= -1;
    }

    @Override
    public void startTimer() {
        mClockDirection = 1;
        mClockLastUpdatedTime = System.currentTimeMillis();
        if (mListener != null)
            mListener.onTimerStart();
    }

    @Override
    public void stopTimer() {
        mClockDirection = -1;
        mClockLastUpdatedTime = System.currentTimeMillis();
        if (mListener != null)
            mListener.onTimerStart();
    }
}
