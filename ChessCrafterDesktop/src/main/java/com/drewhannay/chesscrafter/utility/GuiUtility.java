package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.models.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class GuiUtility {
    public static void requestFocus(final JComponent component) {
        SwingUtilities.invokeLater(component::requestFocus);
    }

    public static JLabel createJLabel(String labelText) {
        JLabel newLabel = new JLabel(labelText);
        newLabel.setForeground(Color.white);
        return newLabel;
    }

    public static JLabel createJLabel(ImageIcon image) {
        JLabel newLabel = new JLabel(image);
        newLabel.setForeground(Color.white);
        return newLabel;
    }

    public static TitledBorder createBorder(String borderText) {
        TitledBorder newBorder = BorderFactory.createTitledBorder(borderText);
        newBorder.setTitleColor(Color.black);
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

    public static String getPieceToolTipText(Piece piece) {
        String name = piece.getName();
        //PieceMovements movements = piece.getPieceType().getPieceMovements();

        StringBuilder builder = new StringBuilder("<html><b>"); //$NON-NLS-1$
        builder.append(name);
        builder.append("</b><br/>"); //$NON-NLS-1$
        /*
        builder.append("<table><tr>"); //$NON-NLS-1$
        builder.append("<td>"); //$NON-NLS-1$
        builder.append("<table border=\"1\"> <tr> <td align=\"center\">"); //$NON-NLS-1$
        builder.append(directionToTooltip(movements.getDistance(Direction.NORTHWEST)));
        builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
        builder.append(directionToTooltip(movements.getDistance(Direction.NORTH)));
        builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
        builder.append(directionToTooltip(movements.getDistance(Direction.NORTHEAST)));
        builder.append("</td></tr>"); //$NON-NLS-1$

        builder.append("<tr> <td align=\"center\">"); //$NON-NLS-1$
        builder.append(directionToTooltip(movements.getDistance(Direction.WEST)));
        builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
        builder.append(name.equals(Messages.getString("Piece.knight")) ? Messages.getString("Piece.knightChar") : name.charAt(0)); //$NON-NLS-1$ //$NON-NLS-2$
        builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
        builder.append(directionToTooltip(movements.getDistance(Direction.EAST)));
        builder.append("</td></tr>"); //$NON-NLS-1$

        builder.append("<tr> <td align=\"center\">"); //$NON-NLS-1$
        builder.append(directionToTooltip(movements.getDistance(Direction.SOUTHWEST)));
        builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
        builder.append(directionToTooltip(movements.getDistance(Direction.SOUTH)));
        builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
        builder.append(directionToTooltip(movements.getDistance(Direction.SOUTHEAST)));
        builder.append("</td></tr>"); //$NON-NLS-1$

        builder.append("</table>"); //$NON-NLS-1$

        builder.append("</td>"); //$NON-NLS-1$

        builder.append("<td>"); //$NON-NLS-1$
        if (piece.getPieceType().isLeaper())
            builder.append(Messages.getString("Piece.ableToLeapBr")); //$NON-NLS-1$
        else
            builder.append(Messages.getString("Piece.notAbleToLeapBr")); //$NON-NLS-1$

        for (TwoHopMovement movement : movements.getTwoHopMovements()) {
            builder.append("- "); //$NON-NLS-1$
            builder.append(movement.getRowDistance());
            builder.append(" x "); //$NON-NLS-1$
            builder.append(movement.getColumnDistance());
            builder.append("<br/>"); //$NON-NLS-1$
        }

        builder.append("</td>"); //$NON-NLS-1$

        builder.append("</html>"); //$NON-NLS-1$
        */
        return builder.toString();
    }

    private static String directionToTooltip(Integer direction) {
        if (direction == UNLIMITED)
            return "&infin;"; //$NON-NLS-1$
        else
            return direction.toString();
    }

    private static final int UNLIMITED = Integer.MAX_VALUE;
}
