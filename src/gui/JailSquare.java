package gui;

import java.awt.Color;
import java.awt.Image;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import logic.Piece;
import logic.Square;

/**
 * @author cheney.hester The squares that populate the Jail.java objects. They
 * are buttons that can hold pieces and move them for some varients.
 */
public class JailSquare implements Serializable
{
	/**
	 * Because it told me to.
	 */
	private static final long serialVersionUID = -6328168977600352667L;
	/**
	 * The Piece occupying this Square
	 */
	private Piece piece;
	/**
	 * Customizable Color for this Square
	 */
	private Color background;
	/**
	 * JButton represents this Square in the GUI
	 */
	private JButton jb;
	/**
	 * Row Index of Square
	 */
	private int row;// File
	/**
	 * Column Index of Square
	 */
	private int col;// Rank

	/**
	 * @param row the row of the Jail objects
	 * @param column the column of the Jail object
	 */
	public JailSquare(int row, int column)
	{
		this.row = row;
		col = column;
	}

	/**
	 * Getter method for Color of the Square
	 * 
	 * @return Square Color
	 */
	public Color getColor()
	{
		return jb.getBackground();
	}

	/**
	 * Getter method for Piece occupying the Square
	 * 
	 * @return Piece occupying the Square.
	 */
	public Piece getPiece()
	{
		return piece;
	}

	/**
	 * Refresh the GUI's view of this Square with the current accurate
	 * information.
	 */
	public void refresh()
	{
		if (jb != null)
		{
			jb.setContentAreaFilled(false);
			jb.setOpaque(true);
			if (piece != null)
			{// If there's a Piece here
				if (piece.getIcon() == null)
				{// And it has no Icon
					jb.setText(piece.getName());// Use it's name
				}
				else
				{
					Image temp = piece.getIcon().getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
					jb.setIcon(new ImageIcon(temp));// Otherwise, use it's Icon

				}
			}
			else
			{// If there's no Piece, clear the Icon and Text of the Square.
				jb.setIcon(null);
				jb.setText("");
			}
			resetColor();// Then reset the color too.
		}
	}

	/**
	 * Reset temporary changes to the Color of the Square
	 */
	public void resetColor()
	{
		if (background != null)
		{
			// If a custom background color has been saved, use that.
			jb.setBackground(background);
			return;
		}
		jb.setBorder(null);
		// Otherwise make our normal light/dark pattern.
		if ((row % 2 != 0 && col % 2 != 0) || (row % 2 == 0 && col % 2 == 0))
		{
			// jb.setBackground(Color.GRAY);
			// jb.setForeground(Color.LIGHT_GRAY);
		}
		else
		{
			// jb.setBackground(Color.LIGHT_GRAY);
			// jb.setForeground(Color.GRAY);
		}
	}

	/**
	 * Sets the JButton representing the Square.
	 * 
	 * @param jb Modifying JButton.
	 */
	public void setButton(JButton jb)
	{
		this.jb = jb;
		refresh();// Refresh the view of the Square
	}

	/**
	 * Sets the Color of the Square
	 * 
	 * @param c New Color
	 */
	public void setColor(Color c)
	{
		jb.setBackground(c);// Use the given Color for the background
		jb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		if (c != Square.HIGHLIGHT_COLOR)
		{
			// If the Color is the highlight color, then change is only
			// temporary. Don't store it.
			background = c;
		}
	}

	/**
	 * Sets the Piece occupying the Square.
	 * 
	 * @param p New occupying Piece.
	 */
	public void setPiece(Piece p)
	{
		piece = p;
	}

	/**
	 * @param unique A boolean for what piece this is and its move sets.
	 * @return The name of the piece
	 */
	public String toString(boolean[] unique)
	{
		String cols = "-abcdefgh";
		String toReturn = "";
		if (unique[0])
		{
			toReturn += row;
		}
		if (unique[1])
		{
			toReturn += cols.charAt(col);
		}
		return toReturn;
	}

}
