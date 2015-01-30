package com.drewhannay.chesscrafter.dragNdrop;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MotionAdapter extends MouseMotionAdapter {
    private final GlassPane mGlassPane;

    public MotionAdapter(@NotNull GlassPane glassPane) {
        mGlassPane = glassPane;
    }

    public void mouseDragged(MouseEvent event) {
        Component component = event.getComponent();

        Point point = (Point) event.getPoint().clone();
        SwingUtilities.convertPointToScreen(point, component);
        SwingUtilities.convertPointFromScreen(point, mGlassPane);

        mGlassPane.setPoint(point);
        mGlassPane.repaint();
    }
}
