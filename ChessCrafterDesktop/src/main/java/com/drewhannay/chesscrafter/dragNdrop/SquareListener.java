package com.drewhannay.chesscrafter.dragNdrop;

import com.drewhannay.chesscrafter.label.SquareJLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Supplier;

public class SquareListener extends DropAdapter implements MouseListener {
    private final DropManager mDropManager;
    private final Supplier<List<SquareJLabel>> mHighlightCallback;

    public SquareListener(DropManager dropManager, GlassPane glassPane,
                          Supplier<List<SquareJLabel>> highlightCallback) {
        super(glassPane);

        mDropManager = dropManager;
        mHighlightCallback = highlightCallback;

        addDropListener(mDropManager);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
        List<SquareJLabel> destinations = mHighlightCallback.get();
        if (destinations.isEmpty()) {
            return;
        }

        mDropManager.setComponentList(destinations);

        SquareJLabel squareLabel = (SquareJLabel) event.getSource();
        Icon icon = squareLabel.getIcon();
        if (icon == null) {
            return;
        }
        squareLabel.hideIcon();

        Point point = (Point) event.getPoint().clone();
        SwingUtilities.convertPointToScreen(point, event.getComponent());
        SwingUtilities.convertPointFromScreen(point, mGlassPane);

        BufferedImage image = new BufferedImage(squareLabel.getWidth(), squareLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        icon.paintIcon(squareLabel, graphics, 0, 0);
        graphics.dispose();

        mGlassPane.setVisible(true);
        mGlassPane.setPoint(point);
        mGlassPane.setImage(image);
        mGlassPane.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        Point point = (Point) event.getPoint().clone();
        SwingUtilities.convertPointToScreen(point, event.getComponent());

        mGlassPane.setImage(null);
        mGlassPane.setVisible(false);

        fireDropEvent(new DropEvent(point, (JComponent) event.getComponent()), false);
    }
}
