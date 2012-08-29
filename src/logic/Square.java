package logic;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Square extends JLabel implements Serializable
{
	public static final Color HIGHLIGHT_COLOR = new Color(20, 129, 191);

	public Square(int row, int column)
	{
		m_row = row;
		m_column = column;
		m_isHabitable = true;
	}

	public int getCol()
	{
		return m_column;
	}

	public Color getColor()
	{
		return getBackground();
	}

	public Piece getPiece()
	{
		return m_piece;
	}

	public int getRow()
	{
		return m_row;
	}

	public boolean isHabitable()
	{
		return m_isHabitable;
	}

	public boolean isOccupied()
	{
		return (m_piece != null);
	}

	/**
	 * Refresh the GUI's view of this Square with the current accurate
	 * information.
	 */
	public void refresh()
	{
		setOpaque(true);
		// if there's a Piece here...
		if (m_piece != null)
		{
			// ...and it has no Icon
			if (m_piece.getIcon() == null)
				setText(m_piece.getName());// Use it's name
			else
				setIcon(m_piece.getIcon());// Otherwise, use it's Icon
		}
		else
		{// If there's no Piece, clear the Icon and Text of the Square.
			setIcon(null);
			setText("");
		}
		resetColor();// Then reset the color too.
	}

	/**
	 * Reset temporary changes to the Color of the Square
	 */
	public void resetColor()
	{
		if (m_backgroundColor != null)
		{
			// If a custom background color has been saved, use that.
			setBackground(m_backgroundColor);
			return;
		}
		setBorder(null);
		// Otherwise make our normal light/dark pattern.
		if ((m_row % 2 != 0 && m_column % 2 != 0) || (m_row % 2 == 0 && m_column % 2 == 0))
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
		// if the Color is the highlight color then change is only temporary, don't store it.
		if (c != Square.HIGHLIGHT_COLOR)
			m_backgroundColor = c;
	}

	public void setIsHabitable(boolean isHabitable)
	{
		m_isHabitable = isHabitable;
	}

	/**
	 * Sets the Piece occupying the Square.
	 * 
	 * @param p New occupying Piece.
	 * @return Old occupying Piece.
	 */
	public Piece setPiece(Piece p)
	{
		Piece oldPiece = m_piece;
		m_piece = p;
		if (m_piece != null)
		{
			m_piece.setSquare(this);
		}
		return oldPiece;
	}

	public void setCol(int col)
	{
		// TODO Make sure they're setting a valid coordinate
		m_column = col;
	}

	public void setRow(int row)
	{
		// TODO Make sure they're setting a valid coordinate
		m_row = row;
	}

	/**
	 * Get a String representation of this Square
	 * 
	 * @param unique If the row and/or column of this square must be shown
	 * @return The String representation of this Square
	 */
	public String toString(boolean[] unique)
	{
		String files = "-abcdefgh";
		String toReturn = "";

		if (!unique[0])
			toReturn += files.charAt(m_column);
		if (!unique[1])
			toReturn += m_row;

		return toReturn;
	}

	private static final long serialVersionUID = -5408493670737541871L;

	private Piece m_piece;
	private int m_row;// File
	private int m_column;// Rank
	private Color m_backgroundColor;
	private boolean m_isHabitable;	
}
