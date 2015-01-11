package com.drewhannay.chesscrafter.controllers;

//TODO: is this class even necessary?
public class SquareController {
    // TODO: future refactorings will hopefully render this interface
    // unnecessary
    public interface SquareStateListener {
        public void onSetIsThreatSquare();

        public void onStateChanged();

        // TODO: this method definitely shouldn't be necessary
        public void onJailStateChanged();
    }

    public void setSquareStateListener(SquareStateListener listener) {
        mListener = listener;
    }

    public void setIsThreatSquare(boolean isThreatSquare) {
        if (mListener != null)
            mListener.onSetIsThreatSquare();
    }

    public void setStateChanged() {
        if (mListener != null)
            mListener.onStateChanged();
    }

    public void setJailStateChanged() {
        if (mListener != null)
            mListener.onJailStateChanged();
    }

    private SquareStateListener mListener;
}
