package com.drewhannay.chesscrafter.dragNdrop;

import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;
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
    private final SquareJLabel mSquareLabel;
    private final Function<BoardCoordinate, List<SquareJLabel>> mHighlightCallback;


    public SquareListener(SquareJLabel squareLabel, DropManager dropManager, GlassPane glassPane,
                          Function<BoardCoordinate, List<SquareJLabel>> highlightCallback) {
        super(glassPane);

        mSquareLabel = squareLabel;
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
        BoardCoordinate origin = mSquareLabel.getCoordinates();
        List<SquareJLabel> destinations = mHighlightCallback.apply(origin);
        if (destinations.isEmpty()) {
            return;
        }

        mDropManager.setComponentList(destinations);
        mSquareLabel.hideIcon();

        Component component = event.getComponent();

        mGlassPane.setVisible(true);

        Point point = (Point) event.getPoint().clone();
        SwingUtilities.convertPointToScreen(point, component);
        SwingUtilities.convertPointFromScreen(point, mGlassPane);

        mGlassPane.setPoint(point);

        BufferedImage image;

        Piece piece = mSquareLabel.getPiece();
        ImageIcon imageIcon = PieceIconUtility.getPieceIcon(piece.getName(), mSquareLabel.getImageScale(), piece.getTeamId());
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        imageIcon.paintIcon(null, graphics2D, 0, 0);
        graphics2D.dispose();

        mGlassPane.setImage(image);

        mGlassPane.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        Point point = (Point) event.getPoint().clone();
        SwingUtilities.convertPointToScreen(point, event.getComponent());

        mGlassPane.setImage(null);
        mGlassPane.setVisible(false);

        fireDropEvent(new DropEvent(point, mSquareLabel), false);
    }

    @Override
    public void onPieceToolTipPreferenceChanged() {
        mSquareLabel.refresh();
    }
}
