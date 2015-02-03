package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.Log;
import com.google.common.base.Preconditions;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;

class ChessFrame extends JFrame {

    private static final String TAG = "ChessFrame";

    private final GlassPane mGlassPane;

    ChessFrame() {
        mGlassPane = new GlassPane();
        mGlassPane.setOpaque(false);

        setGlassPane(mGlassPane);
    }

    @Override
    public GlassPane getGlassPane() {
        return mGlassPane;
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

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWindowCloseKeyboardRequest();
            }
        });

        doInitComponents();
    }

    void doInitComponents() {
    }

    void onWindowCloseKeyboardRequest() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
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
            Log.e(TAG, "OS X Fullscreen FAIL", e);
        }
    }
}
