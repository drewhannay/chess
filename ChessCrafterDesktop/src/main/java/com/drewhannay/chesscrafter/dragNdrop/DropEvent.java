package com.drewhannay.chesscrafter.dragNdrop;

import javax.swing.*;
import java.awt.*;

public class DropEvent {
    public DropEvent(Point point, JComponent originComponent) {
        mPoint = point;
        mOriginComponent = originComponent;
    }

    public Point getDropLocation() {
        return mPoint;
    }

    public JComponent getOriginComponent() {
        return mOriginComponent;
    }

    private Point mPoint;
    private JComponent mOriginComponent;
}
