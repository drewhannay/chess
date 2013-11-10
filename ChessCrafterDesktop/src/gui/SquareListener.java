package gui;

import gui.PreferenceUtility.PieceToolTipPreferenceChangedListener;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import utility.Preference;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import controllers.SquareController;
import dragNdrop.AbstractDropManager;
import dragNdrop.DropAdapter;
import dragNdrop.DropEvent;
import dragNdrop.DropManager;
import dragNdrop.GlassPane;

public class SquareListener extends DropAdapter implements MouseListener, PieceToolTipPreferenceChangedListener
{
	public SquareListener(SquareJLabel squareLabel, DropManager dropManager)
	{
		super(mGlobalGlassPane);
		mSquareLabel = squareLabel;
		mSquareLabel.refresh();
		// mBoard = board;
		mDropManager = dropManager;
		addDropListener(mDropManager);
		PreferenceUtility.addPieceToolTipListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
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

		/*if (mSquareLabel.getPiece() == null || mSquareLabel.getPiece().isBlack() != getGame().isBlackMove())
		{
			return;
		}*/

		List<SquareController> destinations = mSquareLabel.getSquare().getPiece().getLegalDests();
		List<SquareJLabel> destinationLabels = Lists.newArrayList();
		if (destinations.size() > 0)
		{
			mPreference = PreferenceUtility.getPreference();
			if (mPreference.isHighlightMoves())
			{
				for (SquareJLabel squareLabel : mSquareLabels)
				{
					if (destinations.contains(squareLabel.getSquare()))
					{
						squareLabel.setColor(SquareJLabel.HIGHLIGHT_COLOR);
						destinationLabels.add(squareLabel);
					}
				}
			}
		}
		mDropManager.setComponentList(destinationLabels);
		// mDropManager.setBoard(mBoard);

		// if (mSquareLabel.getSquare().getPiece() == null)
		// return;
		// else
		mSquareLabel.hideIcon();

		Driver.getInstance().setGlassPane(mGlassPane);
		Component component = event.getComponent();

		mGlassPane.setVisible(true);

		Point point = (Point) event.getPoint().clone();
		SwingUtilities.convertPointToScreen(point, component);
		SwingUtilities.convertPointFromScreen(point, mGlassPane);

		mGlassPane.setPoint(point);

		BufferedImage image = null;
		/*
		 * PieceController piece = mSquareLabel.getSquare().getPiece();
		 * ImageIcon imageIcon = PieceIconUtility.getPieceIcon(piece.getName(),
		 * piece.isBlack()); int width = imageIcon.getIconWidth(); int height =
		 * imageIcon.getIconHeight(); image = new BufferedImage(width, height,
		 * BufferedImage.TYPE_INT_ARGB); Graphics2D graphics2D = (Graphics2D)
		 * image.getGraphics(); imageIcon.paintIcon(null, graphics2D, 0, 0);
		 * graphics2D.dispose();
		 * 
		 * mGlassPane.setImage(image);
		 */
		mGlassPane.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		Point point = (Point) event.getPoint().clone();
		SwingUtilities.convertPointToScreen(point, event.getComponent());

		mGlassPane.setImage(null);
		mGlassPane.setVisible(false);

		fireDropEvent(new DropEvent(point, mSquareLabel), false);
	}

	private SquareJLabel mSquareLabel;

	@Override
	public void onPieceToolTipPreferenceChanged()
	{
		mSquareLabel.refresh();
	}

	private static GlassPane mGlobalGlassPane;
	private Preference mPreference;
	private final DropManager mDropManager;
}
