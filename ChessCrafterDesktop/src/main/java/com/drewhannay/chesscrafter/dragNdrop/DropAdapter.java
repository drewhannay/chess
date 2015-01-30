package com.drewhannay.chesscrafter.dragNdrop;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseAdapter;
import java.util.List;

public class DropAdapter extends MouseAdapter {
    protected final GlassPane mGlassPane;

    private final List<DropListener> mListeners;

    public DropAdapter(@NotNull GlassPane glassPane) {
        mGlassPane = glassPane;
        mListeners = Lists.newArrayList();
    }

    public void addDropListener(@Nullable DropListener listener) {
        if (listener != null) {
            mListeners.add(listener);
        }
    }

    public void removeDropListener(@Nullable DropListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
    }

    protected final void fireDropEvent(DropEvent event, boolean fromDisplayBoard) {
        mListeners.forEach(listener -> listener.dropped(event, fromDisplayBoard));
    }
}
