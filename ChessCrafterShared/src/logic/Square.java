package logic;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import utility.FileUtility;
import utility.GuiUtility;

public class Square extends JLabel implements Serializable
{
	public static final Color HIGHLIGHT_COLOR = new Color(20, 129, 191);

	static
	{
		try
		{
			s_uninhabitableIcon = GuiUtility.createImageIcon(48, 48, FileUtility.getImagePath("Uninhabitable.png", true)); //$NON-NLS-1$
		}

		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	public Square(int row, int column)
	{
		mRow = row;
		mColumn = column;
		mIsHabitable = true;
	}

	public int getCol()
	{
		return mColumn;
	}

	public Color getColor()
	{
		return getBackground();
	}

	public Piece getPiece()
	{
		return mPiece;
	}

	public int getRow()
	{
		return mRow;
	}

	public boolean isHabitable()
	{
		return mIsHabitable;
	}

	public boolean isOccupied()
	{
		return (mPiece != null);
	}

	public void hideIcon()
	{
		if (mPiece != null)
			setIcon(null);
	}

	/**
	 * Refresh the GUI's view of this Square with the current accurate
	 * information.
	 */
	public void refresh()
	{
		setOpaque(true);
		if (!mIsHabitable)
		{
			setIcon(s_uninhabitableIcon);
			return;
		}

		// if there's a Piece here...
		if (mPiece != null)
		{
			// ...and it has no Icon
			if (mPiece.getIcon() == null)
				setText(mPiece.getName());// Use it's name
			else
				setIcon(mPiece.getIcon());// Otherwise, use it's Icon
		}
		else
		{// If there's no Piece, clear the Icon and Text of the Square.
			setIcon(null);
			setText(""); //$NON-NLS-1$
		}
		resetColor();// Then reset the color too.
	}

	/**
	 * Refresh the GUI's view of this Square with the current accurate
	 * information, but only for Jails
	 */
	public void refreshJail()
	{
		setOpaque(true);
		// if there's a Piece here...
		if (mPiece != null)
		{
			// ...and it has no Icon
			if (mPiece.getIcon() == null)
				setText(mPiece.getName());// Use it's name
			else
				setIcon(new ImageIcon(mPiece.getIcon().getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));// Otherwise,
																													// use
																													// it's
																													// Icon
		}
		else
		{// If there's no Piece, clear the Icon and Text of the Square.
			setIcon(null);
			setText(""); //$NON-NLS-1$
		}
		resetColor();// Then reset the color too.
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
		if ((mRow % 2 != 0 && mColumn % 2 != 0) || (mRow % 2 == 0 && mColumn % 2 == 0))
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

	/**
	 * Sets the color of a square temporarily
	 * 
	 * @param c the color to set the background
	 */
	public void setColor(Color c)
	{
		setBackground(c);
	}

	/**
	 * Sets the background Color of the Square permanently
	 * 
	 * @param c New Color
	 */
	public void setBackgroundColor(Color c)
	{
		setBackground(c);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		// if the Color is the highlight color then change is only temporary,
		// don't store it.
		if (c != Square.HIGHLIGHT_COLOR)
			mBackgroundColor = c;
	}

	public void setIsHabitable(boolean isHabitable)
	{
		mIsHabitable = isHabitable;
		if (!mIsHabitable)
			setIcon(s_uninhabitableIcon);
		else
			setIcon(null);
	}

	/**
	 * Sets the Piece occupying the Square.
	 * 
	 * @param p New occupying Piece.
	 * @return Old occupying Piece.
	 */
	public Piece setPiece(Piece p)
	{
		Piece oldPiece = mPiece;
		mPiece = p;
		if (mPiece != null)
		{
			mPiece.setSquare(this);
		}
		return oldPiece;
	}

	public void setCol(int col)
	{
		// TODO Make sure they're setting a valid coordinate
		mColumn = col;
	}

	public void setRow(int row)
	{
		// TODO Make sure they're setting a valid coordinate
		mRow = row;
	}

	/**
	 * Get a String representation of this Square
	 * 
	 * @param unique If the row and/or column of this square must be shown
	 * @return The String representation of this Square
	 */
	public String toString(boolean[] unique)
	{
		String files = "-abcdefgh"; //$NON-NLS-1$
		String toReturn = ""; //$NON-NLS-1$

		if (!unique[0])
			toReturn += files.charAt(mColumn);
		if (!unique[1])
			toReturn += mRow;

		return toReturn;
	}

	private static final long serialVersionUID = -5408493670737541871L;

	private Piece mPiece;
	private int mRow;// File
	private int mColumn;// Rank
	private Color mBackgroundColor;
	private boolean mIsHabitable;
	private static ImageIcon s_uninhabitableIcon;
}
