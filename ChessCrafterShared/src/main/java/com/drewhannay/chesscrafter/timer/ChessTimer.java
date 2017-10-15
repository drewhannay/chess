package com.drewhannay.chesscrafter.timer;

import com.google.common.base.Preconditions;

import java.text.NumberFormat;

public abstract class ChessTimer {
    public interface ChessTimerListener {
        void onDisplayUpdated(String displayText);

        void onTimerStart();

        void onTimerStop();

        void setInitialDelay(int initialDelay);

        void onTimerRestart();
    }

    public void init() {
        mNumberFormat = NumberFormat.getNumberInstance();
        mNumberFormat.setMinimumIntegerDigits(2);
        mIsStopped = false;
    }

    public void setChessTimerListener(ChessTimerListener listener) {
        mListener = listener;
    }

    // FIXME: this is a terrible method...
    public static boolean isNoTimer(ChessTimer timer) {
        return timer instanceof NoTimer;
    }

    // FIXME: this is a terrible method...
    public static boolean isWordTimer(ChessTimer timer) {
        return timer instanceof WordTimer;
    }

    public static ChessTimer createNoTimer() {
        return new NoTimer();
    }

    public static ChessTimer createTimer(TimerTypes timerType, long incrementAmount,
                                         long startTime, boolean isBlackTeamTimer) {
        switch (timerType) {
            case NO_TIMER:
                return new NoTimer();
//            case BRONSTEIN_DELAY:
//                return new BronsteinDelayTimer(timeElapsedCallback, incrementAmount, startTime, isBlackTeamTimer);
//            case FISCHER:
//                return new FischerTimer(timeElapsedCallback, incrementAmount, startTime, false, isBlackTeamTimer);
//            case FISCHER_AFTER:
//                return new FischerTimer(timeElapsedCallback, incrementAmount, startTime, true, isBlackTeamTimer);
//            case HOUR_GLASS:
//                // time is halved since it is actually the time the player may not
//                // exceed
//                return new HourGlassTimer(timeElapsedCallback, startTime / 2, isBlackTeamTimer);
//            case SIMPLE_DELAY:
//                return new SimpleDelayTimer(timeElapsedCallback, incrementAmount, startTime, isBlackTeamTimer);
//            case WORD:
//                return new WordTimer(startTime);
            default:
                Preconditions.checkArgument(false);
                return null;
        }
    }

    public static void stopTimers() {
        mIsStopped = true;
    }

    public int getClockDirection() {
        return mClockDirection;
    }

    public long getStartTime() {
        return mInitialStartTime;
    }

    public long getRawTime() {
        return mCurrentTime;
    }

    /**
     * Reset the timer to the original settings
     */
    public void reset() {
        mClockLastUpdatedTime = System.currentTimeMillis();
        mCurrentTime = mInitialStartTime;
        updateDisplay();
    }

    /**
     * Restart the timer after resuming a saved game.
     */
    public void restart() {
        if (mListener != null)
            mListener.onTimerRestart();
        mClockLastUpdatedTime = System.currentTimeMillis();
    }

    public void setClockDirection(int newDirection) {
        mClockDirection = newDirection;
    }

    public void setClockTime(long newTime) {
        mCurrentTime = newTime == -1 ? mInitialStartTime : newTime;
        mTimeWasRecentlyReset = true;
    }

    public abstract void startTimer();

    public abstract void stopTimer();

    public void timeElapsed() {
//        mTimeElapsedCallback.run(mIsBlackTeamTimer);
        if (mListener != null)
            mListener.onTimerStop();
    }

    public void updateDisplay() {
        if (mIsStopped) {
            if (mListener != null)
                mListener.onTimerStop();
            return;
        }
        long now = System.currentTimeMillis();
        long elapsed = now - mClockLastUpdatedTime;

        if (!mIsDelayedTimer)
            mCurrentTime -= elapsed * mClockDirection;
        else
            mIsDelayedTimer = false;

        mClockLastUpdatedTime = now;

        int minutes = (int) (mCurrentTime / 60000);
        int seconds = (int) ((mCurrentTime % 60000) / 1000);
        if (mListener != null)
            mListener.onDisplayUpdated(mNumberFormat.format(minutes) + ":" + mNumberFormat.format(seconds)); //$NON-NLS-1$
        if (mCurrentTime <= 0) {
            mCurrentTime = Math.abs(mCurrentTime);
            timeElapsed();
        }
    }

    @Override
    public boolean equals(Object other) {
        // TODO: this shouldn't be defined here, and it's just plain incorrect
        return true;
    }

    @Override
    public int hashCode() {
        // TODO this shouldn't be defined here, and it's just plain incorrect
        return super.hashCode();
    }

    protected long mCurrentTime;
    protected long mClockLastUpdatedTime;
    protected NumberFormat mNumberFormat;
    protected boolean mIsDelayedTimer;
    protected boolean mIsBlackTeamTimer;
    protected boolean mTimeWasRecentlyReset;
    protected long mInitialStartTime;
    // should be 1 or -1 to account for timer counting up instead. -1 to count
    // up since it gets subtracted
    protected int mClockDirection = 1;
    protected static boolean mIsStopped;
    protected ChessTimerListener mListener;
}
