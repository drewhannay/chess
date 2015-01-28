package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.google.common.base.Preconditions;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

class ChessFrame extends JFrame {
    ChessFrame() {
    }

    final void initComponents() {
        enableOSXFullscreen(this);

        setLayout(new BorderLayout());
        setResizable(true);

        // TODO: always use this image?
        setIconImage(FileUtility.getFrontPageImage());

        // put the window in the center of the screen, regardless of resolution
        setLocationRelativeTo(null);

        setJMenuBar(ChessActions.createJMenuBar());
        setPreferredSize(new Dimension(685, 450));

        doInitComponents();
    }

    void doInitComponents() {
    }

    private void enableOSXFullscreen(Window window) {
        Preconditions.checkNotNull(window);
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class<?> params[] = new Class[]{Window.class, Boolean.TYPE};
            @SuppressWarnings("unchecked")
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (Exception e) {
            System.out.println("OS X Fullscreen FAIL" + e.getLocalizedMessage());
        }
    }
}
