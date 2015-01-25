package com.drewhannay.chesscrafter.dragNdrop;

import com.google.common.collect.ImmutableList;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class AbstractDropManager implements DropListener {
    public AbstractDropManager() {
        mComponents = ImmutableList.of();
    }

    protected JComponent isInTarget(Point point) {
        for (JComponent component : mComponents) {
            Rectangle bounds = component.getBounds();
            Point location = component.getLocation();
            SwingUtilities.convertPointToScreen(location, component.getParent());
            bounds.x = location.x;
            bounds.y = location.y;
            if (bounds.contains(point))
                return component;
        }

        return null;
    }

    public void setComponentList(List<? extends JComponent> components) {
        mComponents = components;
    }

    public void clearComponentList() {
        mComponents = ImmutableList.of();
    }

    public abstract void dropped(DropEvent event, boolean fromDisplayBoard);

    protected List<? extends JComponent> mComponents;
}
