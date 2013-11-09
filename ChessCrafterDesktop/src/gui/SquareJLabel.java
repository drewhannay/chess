
package gui;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import utility.GuiUtility;
import utility.PieceIconUtility;
import models.ChessCoordinates;
import models.Piece;

public class SquareJLabel extends JLabel
{
	public static final Color HIGHLIGHT_COLOR = new Color(20, 129, 191);

	public SquareJLabel(ChessCoordinates coords, Piece piece, boolean isHabitable){
		mChessCoordinates = coords;
		mPiece = piece;
		mIsHabitable = isHabitable;
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
		if (mPiece.getPieceType() != null)
			setIcon(null);
	}
	
	/**
	 * Changes out the current piece located on this SquareJLabel
	 * 
	 * @param piece the piece replacing the current
	 */
	public void changePiece(Piece piece)
	{
		mPiece = piece;
	}

	/**
	 * Refresh the GUI's view of this Square with the current accurate
	 * information.
	 * 
	 * @param isJail provided to distinguish if this SquareJLabel is a part of a jail
	 */
	public void refresh(boolean isJail)
	{
		setOpaque(true);
		if (!mIsHabitable)
		{
			setIcon(s_uninhabitableIcon);
			return;
		}

		if (mPiece != null)
		{
			//TODO figure out how to determine the correct piece image
			//ImageIcon pieceIcon = PieceIconUtility.getPieceIcon(mPiece.getPieceType().getName(), mPiece.isBlack());
			ImageIcon pieceIcon = PieceIconUtility.getPieceIcon(mPiece.getPieceType().getName(), false);
			if (pieceIcon == null)
				setText(mPiece.getPieceType().getName());
			else{
				if(isJail)
					setIcon(new ImageIcon(pieceIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
				else
					setIcon(pieceIcon);
			}

			/*if (PreferenceUtility.getPreference().showPieceToolTips())
				setToolTipText(mPiece.getPieceType().getToolTipText());
			else
				setToolTipText(null);*/
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
		if ((mChessCoordinates.row % 2 != 0 && mChessCoordinates.column % 2 != 0) || (mChessCoordinates.row % 2 == 0 && mChessCoordinates.column % 2 == 0))
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
	
	public void onSetIsThreatSquare()
	{
		setColor(Color.RED);
	}

	static
	{
		try
		{
			s_uninhabitableIcon = GuiUtility.createImageIcon(48, 48, "/Uninhabitable.png"); //$NON-NLS-1$
		}

		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	private static final long serialVersionUID = -5060622037769752836L;

	private static ImageIcon s_uninhabitableIcon;

	private Color mBackgroundColor;
	
	private ChessCoordinates mChessCoordinates;
	private Piece mPiece;
	private boolean mIsHabitable;
}
