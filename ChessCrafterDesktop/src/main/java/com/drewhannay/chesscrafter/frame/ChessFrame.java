package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.utility.ImageUtility;
import com.drewhannay.chesscrafter.utility.Log;
import com.google.common.base.Preconditions;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
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
        setIconImage(ImageUtility.readSystemImage("chess_logo"));

        setJMenuBar(ChessActions.createJMenuBar());
        setPreferredSize(new Dimension(685, 450));

        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "close");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_DOWN_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "fullscreen");

        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWindowCloseKeyboardRequest();
            }
        });
        actionMap.put("fullscreen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: fix full screen toggle
//                Application application = Application.getApplication();
//                if (application != null) {
//                    application.requestToggleFullScreen(ChessFrame.this);
//                }
            }
        });

        setPreferredSize(new Dimension(685, 450));
        setMinimumSize(new Dimension(685, 450));

        this.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                Dimension d = getSize();
                Dimension minD = getMinimumSize();
                if(d.width<minD.width)
                    d.width=minD.width;
                if(d.height<minD.height)
                    d.height=minD.height;
                setSize(d);
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
