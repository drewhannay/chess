package com.drewhannay.chesscrafter.action;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ChessAction extends AbstractAction {
    private final boolean mAllowActionListeners;

    private ActionListener mActionListener;

    ChessAction(boolean allowActionListeners) {
        mAllowActionListeners = allowActionListeners;

        setEnabled(!mAllowActionListeners);
    }

    public void setActionListener(@NotNull ActionListener actionListener) {
        Preconditions.checkState(mAllowActionListeners, "ActionListeners not allowed for " + getClass().getName());
        Preconditions.checkNotNull(actionListener);

        mActionListener = actionListener;
        setEnabled(true);
    }

    public void removeActionListener() {
        Preconditions.checkState(mAllowActionListeners, "ActionListeners not allowed for " + getClass().getName());

        mActionListener = null;
        setEnabled(false);
    }

    @Nullable
    ActionListener getActionListener() {
        Preconditions.checkState(mAllowActionListeners, "ActionListeners not allowed for " + getClass().getName());

        return mActionListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (mAllowActionListeners && getActionListener() != null) {
            getActionListener().actionPerformed(e);
        }
    }
}
