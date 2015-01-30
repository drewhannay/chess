package com.drewhannay.chesscrafter.dragNdrop;

import javax.swing.*;
import java.awt.*;

public final class GlassPane extends JPanel {
    private final AlphaComposite mAlphaComposite;

    private Image mDraggedImage;
    private Point mLocation;

    public GlassPane() {
        setOpaque(false);
        mAlphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        mLocation = new Point(0, 0);
    }

    public void setImage(Image draggedImage) {
        mDraggedImage = draggedImage;
    }

    public void setPoint(Point location) {
        mLocation = location;
    }

    public void paintComponent(Graphics graphics) {
        if (mDraggedImage == null)
            return;

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setComposite(mAlphaComposite);
        graphics2D.drawImage(mDraggedImage, (int) (mLocation.getX() - (mDraggedImage.getWidth(this) / 2)),
                (int) (mLocation.getY() - (mDraggedImage.getHeight(this) / 2)), null);
    }
}
