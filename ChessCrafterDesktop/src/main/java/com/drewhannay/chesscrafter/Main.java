package com.drewhannay.chesscrafter;

import com.drewhannay.chesscrafter.frame.GameFrame;
import com.drewhannay.chesscrafter.utility.FileUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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

            // put menus in the OS X menu bar if we're on OS X
            System.setProperty("apple.laf.useScreenMenuBar", "true");

            // set a tray icon if we're on Windows
            createWindowsTrayIcon();

            new GameFrame();
        });
    }

    private static void createWindowsTrayIcon() {
        BufferedImage frontPageImage = FileUtility.getFrontPageImage();
        if (System.getProperty("os.name").startsWith("Windows")) {
            SystemTray sysTray = SystemTray.getSystemTray();
            TrayIcon tray = new TrayIcon(frontPageImage.getScaledInstance(25, 18, Image.SCALE_SMOOTH));
            try {
                sysTray.add(tray);
            } catch (AWTException e) {
                System.err.println("Couldn't create a tray icon");
                e.printStackTrace();
            }
        }
    }
}
