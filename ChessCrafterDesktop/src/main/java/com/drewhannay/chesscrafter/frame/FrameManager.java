package com.drewhannay.chesscrafter.frame;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum FrameManager {
    INSTANCE;

    private final List<ChessFrame> mFrames;

    private FrameManager() {
        mFrames = new ArrayList<>();
    }

    public void openGameFrame() {
        verifyNotCreated(GameFrame.class);
        openFrame(new GameFrame());
    }

    public void openPieceCrafterFrame() {
        verifyNotCreated(PieceCrafterFrame.class);
        openFrame(new PieceCrafterFrame());
    }

    private void openFrame(@NotNull ChessFrame frame) {
        frame.initComponents();
        frame.pack();
        Preconditions.checkState(!frame.isVisible(), "Do not manually call Frame#setVisible!");
        frame.setVisible(true);

        mFrames.add(frame);
    }

    private void verifyNotCreated(@NotNull Class<? extends ChessFrame> klazz) {
        for (ChessFrame frame : mFrames) {
            Preconditions.checkState(frame.getClass() != klazz);
        }
    }
}
