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
	public JailSquare(int row, int column)
	{
		m_row = row;
		m_column = column;
	}

	public Color getColor()
	{
		return m_button.getBackground();
	}

	public Piece getOccupyingPiece()
	{
		return m_piece;
	}

	public void refreshSquareGUI()
	{
		if (m_button != null)
		{
			m_button.setContentAreaFilled(false);
			m_button.setOpaque(true);
			if (m_piece != null)
			{
				if (m_piece.getIcon() == null)
					m_button.setText(m_piece.getName());
				else
					m_button.setIcon(new ImageIcon(m_piece.getIcon().getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
			}
			else
			{
				m_button.setIcon(null);
				m_button.setText("");
			}

			resetColor();
		}
	}

	public void resetColor()
	{
		if (m_backgroundColor != null)
		{
			m_button.setBackground(m_backgroundColor);
			return;
		}
		m_button.setBorder(null);
	}

	public void setButton(JButton button)
	{
		m_button = button;
		refreshSquareGUI();
	}

	public void setColor(Color color)
	{
		m_button.setBackground(color);
		m_button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		if (color != Square.HIGHLIGHT_COLOR)
		{
			m_backgroundColor = color;
		}
	}

	public void setOccupyingPiece(Piece piece)
	{
		m_piece = piece;
	}

	// TODO: this method doesn't seem right...it probably shouldn't be in
	// JailSquare
	public String toACNString(boolean[] shouldPrintRowAndColumn)
	{
		StringBuilder builder = new StringBuilder();

		if (shouldPrintRowAndColumn[0])
			builder.append(m_row);
		if (shouldPrintRowAndColumn[1])
			builder.append(COLUMNS.charAt(m_column));

		return builder.toString();
	}

	private static final long serialVersionUID = -6328168977600352667L;
	private static final String COLUMNS = "-abcdefgh";

	private Piece m_piece;
	private Color m_backgroundColor;
	private JButton m_button;
	private int m_row;
	private int m_column;

}
