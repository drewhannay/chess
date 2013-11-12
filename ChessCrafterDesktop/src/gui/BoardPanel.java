package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.ChessCoordinates;
import models.Piece;
import utility.GuiUtility;
import dragNdrop.DropManager;
import dragNdrop.GlassPane;
import dragNdrop.MotionAdapter;

public class BoardPanel extends JPanel
{

	/**
	 * Constructor for a normal game board
	 * 
	 * @param rows total rows in the board
	 * @param columns total columns in the board
	 * @param boardIndex keeps track of which board this is if > 1
	 */
	public BoardPanel(int rows, int columns, int boardIndex, GlassPane globalGlassPane, DropManager dropManager)
	{
		setOpaque(false);
		setLayout(new GridLayout(rows + 1, columns));
		setPreferredSize(new Dimension((columns + 1) * 48, (rows + 1) * 48));
		mBoardIndex = boardIndex;
		mSquareLabels = new SquareJLabel[rows][columns];
		mGlassPane = globalGlassPane;
		mDropManager = dropManager;
		createGrid(rows, columns);
	}

	/**
	 * Constructor for Jail boards
	 * 
	 * @param rows total rows in jail
	 * @param columns total columns in jail
	 */
	public BoardPanel(int rows, int columns)
	{
		setOpaque(false);
		setLayout(new GridLayout(rows + 1, columns));
		setPreferredSize(new Dimension((columns + 1) * 25, (rows + 1) * 25));
		mSquareLabels = new SquareJLabel[rows][columns];
		mBoardIndex = 0;
		createJailGrid(rows, columns);
	}

	/**
	 * Creates the grid for the a game BoardPanel
	 * 
	 * @param rows the total rows of the board
	 * @param columns the total columns of the board
	 */
	public void createGrid(int rows, int columns)
	{
		for (int i = rows; i > 0; i--)
		{
			JLabel label = GuiUtility.createJLabel("" + i); //$NON-NLS-1$
			label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label);
			for (int k = 1; k <= columns; k++)
			{
				SquareJLabel square = new SquareJLabel(new ChessCoordinates(i, k, mBoardIndex), true, 48);
				square.addMouseMotionListener(new MotionAdapter(mGlassPane));
				square.addMouseListener(new SquareListener(square, mDropManager));
				add(square);
				mSquareLabels[i][k] = square;
			}
		}
		for (int j = 0; j <= columns; j++)
		{
			if (j != 0)
			{
				JLabel label = GuiUtility.createJLabel("" + (char) (j - 1 + 'A')); //$NON-NLS-1$
				label.setHorizontalAlignment(SwingConstants.CENTER);
				add(label);
			}
			else
			{
				add(GuiUtility.createJLabel("")); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Creates the grid for the a jail BoardPanel
	 * 
	 * @param rows the total rows of the board
	 * @param columns the total columns of the board
	 */
	public void createJailGrid(int rows, int columns)
	{
		for (int i = 0; i < rows; i++)
		{
			for (int k = 0; k <= columns; k++)
			{
				SquareJLabel square = new SquareJLabel(new ChessCoordinates(i, k, mBoardIndex), true, 25);
				add(square);
				mSquareLabels[i][k] = square;
			}
		}
	}

	/**
	 * Call to update the the SquareJLabel within this BoardPanel
	 * 
	 * @param coordinates coordinates of the SquareJLabel
	 * @param piece the piece that needs to be placed or null for an empty piece
	 */
	public void updateSquareLabel(ChessCoordinates coordinates, Piece piece)
	{
		mSquareLabels[coordinates.row][coordinates.column].changePiece(piece);
	}

	private static final long serialVersionUID = 9042633590279303353L;

	private SquareJLabel[][] mSquareLabels;

	private int mBoardIndex;
	private GlassPane mGlassPane;
	private DropManager mDropManager;
}
