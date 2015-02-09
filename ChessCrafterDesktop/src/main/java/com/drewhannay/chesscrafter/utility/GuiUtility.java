package com.drewhannay.chesscrafter.utility;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.Color;

public final class GuiUtility {

    public static boolean isMac() {
        return System.getProperty("os.name").startsWith("Mac");
    }

    public static void requestFocus(final JComponent component) {
        SwingUtilities.invokeLater(component::requestFocus);
    }

    public static JLabel createJLabel(String labelText) {
        JLabel newLabel = new JLabel(labelText);
        newLabel.setForeground(Color.WHITE);
        return newLabel;
    }

    public static JLabel createJLabel(ImageIcon image) {
        JLabel newLabel = new JLabel(image);
        newLabel.setForeground(Color.BLACK);
        return newLabel;
    }

    public static TitledBorder createBorder(String borderText) {
        TitledBorder newBorder = BorderFactory.createTitledBorder(borderText);
        newBorder.setTitleColor(Color.BLACK);
        return newBorder;
    }
}
