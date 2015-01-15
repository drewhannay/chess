package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.controllers.GameController;
import com.drewhannay.chesscrafter.dragNdrop.DropAdapter;
import com.drewhannay.chesscrafter.dragNdrop.DropEvent;
import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.models.ChessCoordinate;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class SquareListener extends DropAdapter implements MouseListener, PreferenceUtility.PieceToolTipPreferenceChangedListener {
    public SquareListener(SquareJLabel squareLabel, DropManager dropManager, GlassPane glassPane) {
        super(glassPane);
        mSquareLabel = squareLabel;
        mDropManager = dropManager;
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
        // TODO: dropping from a jail currently doesn't work
        // if (m_nextMoveMustPlacePiece)
        // {
        // m_nextMoveMustPlacePiece = false;
        // getGame().nextTurn();
        // if (!m_clickedSquare.isOccupied() &&
        // m_clickedSquare.isHabitable() && m_pieceToPlace != null)
        // {
        // m_pieceToPlace.setSquare(m_clickedSquare);
        // m_clickedSquare.setPiece(m_pieceToPlace);
        // m_pieceToPlace = null;
        // m_nextMoveMustPlacePiece = false;
        // boardRefresh(getGame().getBoards());
        // getGame().genLegalDests();
        // }
        //
        // return;
        // }

		/*
         * if (mSquareLabel.getPiece() == null || mSquareLabel.getPiece().isBlack() != getGame().isBlackMove())
		 * {
		 * return;
		 * }
		 */

        Game game = GameController.getGame();
        ChessCoordinate coordinates = mSquareLabel.getCoordinates();
        if (game.getPiece(coordinates.boardIndex, coordinates) == null || game.getPiece(coordinates.boardIndex, coordinates).getTeamId() != game.getTurnKeeper().getActiveTeamId())
            return;

        List<SquareJLabel> destinationLabels = PlayGamePanel.highlightLegalDestinations(coordinates.boardIndex, coordinates);

        mDropManager.setComponentList(destinationLabels);
        mSquareLabel.hideIcon();

        Driver.getInstance().setGlassPane(mGlassPane);
        Component component = event.getComponent();

        mGlassPane.setVisible(true);

        Point point = (Point) event.getPoint().clone();
        SwingUtilities.convertPointToScreen(point, component);
        SwingUtilities.convertPointFromScreen(point, mGlassPane);

        mGlassPane.setPoint(point);

        BufferedImage image;

        Piece piece = mSquareLabel.getPiece();
        ImageIcon imageIcon = PieceIconUtility.getPieceIcon(piece.getName(), 48, piece.getTeamId());
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

    private SquareJLabel mSquareLabel;
    private final DropManager mDropManager;
}
