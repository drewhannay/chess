package com.drewhannay.chesscrafter.dragNdrop;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDropManager implements DropListener {
    public AbstractDropManager() {
        mComponents = ImmutableList.of();
    }

    protected Optional<? extends JComponent> isInTarget(@NotNull Point point) {
        return mComponents.stream()
                .filter(component -> {
                    Rectangle bounds = component.getBounds();
                    Point location = component.getLocation();
                    SwingUtilities.convertPointToScreen(location, component.getParent());
                    bounds.x = location.x;
                    bounds.y = location.y;
                    return bounds.contains(point);
                })
                .limit(1)
                .findFirst();
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
