
package gui;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import utility.DesktopGuiUtility;
import utility.PieceIconUtility;
import controllers.SquareController;
import controllers.SquareController.SquareStateListener;

public class SquareJLabel extends JLabel
{
	public static final Color HIGHLIGHT_COLOR = new Color(20, 129, 191);

	public SquareJLabel(SquareController square)
	{
		mSquare = square;
		mSquare.setSquareStateListener(mSquareStateListener);
	}

	public SquareController getSquare()
	{
		return mSquare;
	}

	public Color getColor()
	{
		return getBackground();
	}

	/**
	 * Sets the color of a square temporarily
	 * 
	 * @param c
	 *            the color to set the background
	 */
	public void setColor(Color c)
	{
		setBackground(c);
	}

	/**
	 * Sets the background Color of the Square permanently
	 * 
	 * @param c
	 *            New Color
	 */
	public void setBackgroundColor(Color c)
	{
		setBackground(c);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		// if the Color is the highlight color then change is only temporary,
		// don't store it
		if (c != HIGHLIGHT_COLOR)
			mBackgroundColor = c;
	}

	public void hideIcon()
	{
		if (mSquare.getPiece() != null)
			setIcon(null);
	}

	/**
	 * Refresh the GUI's view of this Square with the current accurate
	 * information.
	 */
	public void refresh()
	{
		setOpaque(true);
		if (!mSquare.isHabitable())
		{
			setIcon(s_uninhabitableIcon);
			return;
		}

		PieceController piece = mSquare.getPiece();
		if (piece != null)
		{
			ImageIcon pieceIcon = PieceIconUtility.getPieceIcon(piece.getName(), piece.isBlack());
			if (pieceIcon == null)
				setText(piece.getName());
			else
				setIcon(pieceIcon);

			if (PreferenceUtility.getPreference().showPieceToolTips())
				setToolTipText(piece.getToolTipText());
			else
				setToolTipText(null);
		}
		else
		{
			// if there's no Piece, clear the Icon, tooltip, and Text of the
			// Square
			setIcon(null);
			setText(""); //$NON-NLS-1$
			setToolTipText(null);
		}

		// then reset the color too
		resetColor();
	}

	/**
	 * Refresh the GUI's view of this Square with the current accurate
	 * information, but only for Jails
	 */
	public void refreshJail()
	{
		setOpaque(true);

		PieceController piece = mSquare.getPiece();
		if (piece != null)
		{
			ImageIcon pieceIcon = PieceIconUtility.getPieceIcon(piece.getName(), piece.isBlack());
			if (pieceIcon == null)
				setText(piece.getName());
			else
				setIcon(new ImageIcon(pieceIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
		}
		else
		{
			setIcon(null);
			setText(""); //$NON-NLS-1$
		}
		resetColor();
	}

	/**
	 * Reset temporary changes to the Color of the Square
	 */
	public void resetColor()
	{
		if (mBackgroundColor != null)
		{
			// If a custom background color has been saved, use that.
			setBackground(mBackgroundColor);
			return;
		}
		setBorder(null);
		// Otherwise make our normal light/dark pattern.
		if ((mSquare.getRow() % 2 != 0 && mSquare.getCol() % 2 != 0) || (mSquare.getRow() % 2 == 0 && mSquare.getCol() % 2 == 0))
		{
			setBackground(Color.LIGHT_GRAY);
			setForeground(Color.getHSBColor(30, 70, 70));
		}
		else
		{
			setBackground(Color.getHSBColor(30, 70, 70));
			setForeground(Color.LIGHT_GRAY);
		}
	}

	private final SquareStateListener mSquareStateListener = new SquareStateListener()
	{
		@Override
		public void onStateChanged()
		{
			refresh();
		}

		@Override
		public void onSetIsThreatSquare()
		{
			setColor(Color.RED);
		}

		@Override
		public void onJailStateChanged()
		{
			refreshJail();
		};
	};

	static
	{
		try
		{
			s_uninhabitableIcon = DesktopGuiUtility.createImageIcon(48, 48, "/Uninhabitable.png"); //$NON-NLS-1$
		}

		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	private static final long serialVersionUID = -5060622037769752836L;

	private static ImageIcon s_uninhabitableIcon;

	private SquareController mSquare;
	private Color mBackgroundColor;
}
