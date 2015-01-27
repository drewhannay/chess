package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.utility.AppConstants;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.google.common.base.Preconditions;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

public class ChessFrame extends JFrame {
    public ChessFrame() {
        enableOSXFullscreen(this);

        setTitle(AppConstants.APP_NAME);
        setLayout(new BorderLayout());
        setResizable(true);

        // TODO: this is wrong
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // TODO: always use this image?
        setIconImage(FileUtility.getFrontPageImage());

        // put the window in the center of the screen, regardless of resolution
        setLocationRelativeTo(null);

        setJMenuBar(ChessActions.createJMenuBar());
        setPreferredSize(new Dimension(685, 450));

        initComponents();

        setVisible(true);
    }

    void initComponents() {
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
