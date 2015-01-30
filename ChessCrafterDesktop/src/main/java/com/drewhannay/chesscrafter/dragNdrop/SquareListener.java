package com.drewhannay.chesscrafter.dragNdrop;

import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Function;

public class SquareListener extends DropAdapter implements MouseListener, PreferenceUtility.PieceToolTipPreferenceChangedListener {
    private final DropManager mDropManager;
    private final Function<BoardCoordinate, List<SquareJLabel>> mHighlightCallback;

    public SquareListener(DropManager dropManager, GlassPane glassPane,
                          Function<BoardCoordinate, List<SquareJLabel>> highlightCallback) {
        super(glassPane);

        mDropManager = dropManager;
        mHighlightCallback = highlightCallback;

        addDropListener(mDropManager);
        PreferenceUtility.addPieceToolTipListener(this);
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
        SquareJLabel squareLabel = (SquareJLabel) event.getSource();
        BoardCoordinate origin = squareLabel.getCoordinates();
        List<SquareJLabel> destinations = mHighlightCallback.apply(origin);
        if (destinations.isEmpty()) {
            return;
        }

        mDropManager.setComponentList(destinations);
        squareLabel.hideIcon();

        Point point = (Point) event.getPoint().clone();
        SwingUtilities.convertPointToScreen(point, event.getComponent());
        SwingUtilities.convertPointFromScreen(point, mGlassPane);

        ImageIcon imageIcon = squareLabel.getPieceIcon();
        BufferedImage image = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        imageIcon.paintIcon(null, graphics, 0, 0);
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

    @Override
    public void onPieceToolTipPreferenceChanged() {
//        mSquareLabel.refresh();
    }
}
