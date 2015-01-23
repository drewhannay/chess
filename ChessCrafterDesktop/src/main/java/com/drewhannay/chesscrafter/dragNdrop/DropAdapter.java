package com.drewhannay.chesscrafter.dragNdrop;

import com.google.common.collect.Lists;

import java.awt.event.MouseAdapter;
import java.util.List;

public class DropAdapter extends MouseAdapter {
    public DropAdapter(GlassPane glassPane) {
        mGlassPane = glassPane;
        mListeners = Lists.newArrayList();
    }

    public void addDropListener(DropListener listener) {
        if (listener != null)
            mListeners.add(listener);
    }

    public void removeDropListener(DropListener listener) {
        if (listener != null)
            mListeners.remove(listener);
    }

    protected void fireDropEvent(DropEvent event, boolean fromDisplayBoard) {
        for (DropListener mListener : mListeners) mListener.dropped(event, fromDisplayBoard);
    }

    protected GlassPane mGlassPane;

    private List<DropListener> mListeners;
}
