package com.drewhannay.chesscrafter;

import com.apple.eawt.Application;
import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.frame.FrameManager;
import com.drewhannay.chesscrafter.utility.FileUtility;

import javax.swing.*;
import java.awt.*;

public final class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // make the app match the look and feel of the user's system
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            try {
                UIManager.setLookAndFeel(lookAndFeel);
            } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
                System.err.println("Couldn't get the specified look and feel " + lookAndFeel + " for some reason");
                System.err.println("Using default look and feel");
                e.printStackTrace();
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
            application.setDefaultMenuBar(ChessActions.createJMenuBar());
        }
    }

    private static void createTrayIcon() {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            TrayIcon tray = new TrayIcon(FileUtility.getFrontPageImage().getScaledInstance(25, 18, Image.SCALE_SMOOTH));
            try {
                systemTray.add(tray);
            } catch (AWTException e) {
                System.err.println("Couldn't create a tray icon");
                e.printStackTrace();
            }
        }
    }
}
