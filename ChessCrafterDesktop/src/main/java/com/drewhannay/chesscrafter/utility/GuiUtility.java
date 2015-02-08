package com.drewhannay.chesscrafter.utility;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    public static void setupDoneButton(JButton doneButton, final JFrame popup) {
        doneButton.addActionListener(event -> popup.dispose());
    }

    public static void setupVariantCancelButton(JButton cancelButton, final JPanel displayPanel, final JFrame popup) {
        cancelButton.addActionListener(event -> {
            displayPanel.removeAll();
            popup.setVisible(false);
        });
    }

    /**
     * Should only be called for images that are bundled with the app.
     * Will crash the app if the image cannot be found.
     */
    public static ImageIcon createSystemImageIcon(int imageWidth, int imageHeight, String imageLocation) {
        try {
            return createImageIcon(imageWidth, imageHeight, imageLocation, true);
        } catch (IOException e) {
            throw new RuntimeException("Cannot find image:" + imageLocation);
        }
    }

    public static BufferedImage createBufferedImage(int width, int height, String name) throws IOException {
        return ImageIO.read(new File(FileUtility.getImagePath(name)));
    }

    public static ImageIcon createImageIcon(int width, int height, String path, boolean isSystemFile) throws IOException {
        ImageIcon icon = createImageIcon(path, isSystemFile);
        icon.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return icon;
    }

    public static ImageIcon createImageIcon(String path, boolean isSystemFile) throws IOException {
        BufferedImage bufferedImage;
        if (isSystemFile)
            bufferedImage = ImageIO.read(GuiUtility.class.getResource(path));
        else
            bufferedImage = ImageIO.read(new File(FileUtility.getImagePath(path)));

        return new StretchIcon(bufferedImage, true);
    }
}
