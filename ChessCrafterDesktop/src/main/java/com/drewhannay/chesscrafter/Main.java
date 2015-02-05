package com.drewhannay.chesscrafter;

import com.apple.eawt.Application;
import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.frame.FrameManager;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.Log;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;

public final class Main {

    private static final String TAG = "Main";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // make the app match the look and feel of the user's system
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            try {
                UIManager.setLookAndFeel(lookAndFeel);
            } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
                Log.e(TAG, "Couldn't get the specified look and feel " + lookAndFeel + " for some reason");
                Log.e(TAG, "Using default look and feel", e);
            }

            setOsXProperties();

            // set a tray icon if the system supports it
            createTrayIcon();

            FrameManager.INSTANCE.openGameFrame();
        });
    }

    private static void setOsXProperties() {
        // put menus in the OS X menu bar if we're on OS X
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        Application application = Application.getApplication();
        if (application != null) {
            application.setDefaultMenuBar(ChessActions.createJMenuBar(false));
        }

        setMacApplicationMenuHandlers();
    }

    private static void setMacApplicationMenuHandlers() {
        Application application = Application.getApplication();
        if (application != null) {
            application.setAboutHandler(aboutEvent -> ChessActions.ABOUT.getAction().actionPerformed(null));
            application.setPreferencesHandler(preferencesEvent -> ChessActions.PREFERENCES.getAction().actionPerformed(null));

            // TODO: this isn't always correct; prompt to save game, etc
            application.setQuitHandler((quitEvent, quitResponse) -> quitResponse.performQuit());
        }
    }

    private static void createTrayIcon() {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            TrayIcon tray = new TrayIcon(FileUtility.getFrontPageImage().getScaledInstance(25, 18, Image.SCALE_SMOOTH));
            try {
                systemTray.add(tray);
            } catch (AWTException e) {
                Log.e(TAG, "Couldn't create a tray icon", e);
            }
        }
    }
}
