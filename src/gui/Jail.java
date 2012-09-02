package gui;

import java.io.Serializable;

public class Jail implements Serializable
{
	public Jail(int rows, int columns)
	{
		m_maxNumberOfRow = rows;
		m_maxNumberOfColumns = columns;
		m_squares = new JailSquare[rows][columns];

		for (int row = 0, col = 0; row < rows; row++)
		{
			// initialize the Squares, adding 1 to ignore counting from 0
			for (col = 0; col < columns; col++)
				m_squares[row][col] = new JailSquare((row + 1), (col + 1));
		}
	}

	public int getMaxRow()
	{
		return m_maxNumberOfRow;
	}

	public JailSquare getJailSquare(int row, int column)
	{
		// subtract one to ignore counting from 0
		return m_squares[row - 1][column - 1];
	}

	public int getMaxColumn()
	{
		return m_maxNumberOfColumns;
	}

	private static final long serialVersionUID = 5949793107459893392L;

	public int m_maxNumberOfColumns;
	protected int m_maxNumberOfRow;
	protected JailSquare m_squares[][];
}
