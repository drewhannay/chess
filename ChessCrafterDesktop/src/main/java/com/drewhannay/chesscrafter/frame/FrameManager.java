package com.drewhannay.chesscrafter.frame;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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

    public void openGameCrafterFrame() {
        verifyNotCreated(GameCrafterFrame.class);
        openFrame(new GameCrafterFrame());
    }

    public void openPieceCrafterFrame() {
        verifyNotCreated(PieceCrafterFrame.class);
        openFrame(new PieceCrafterFrame());
    }

    public void openAboutFrame() {
        verifyNotCreated(AboutFrame.class);
        openFrame(new AboutFrame());
    }

    public void openHelpFrame() {
        verifyNotCreated(HelpFrame.class);
        openFrame(new HelpFrame());
    }

    private void openFrame(@NotNull ChessFrame frame) {
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(mWindowListener);

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

    private final WindowListener mWindowListener = new WindowListener() {
        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            ChessFrame frame = (ChessFrame) e.getSource();
            frame.dispose();

            mFrames.remove(frame);
            if (mFrames.isEmpty()) {
                System.exit(0);
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    };
}
