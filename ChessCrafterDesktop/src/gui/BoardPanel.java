package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import models.ChessCoordinates;
import models.Piece;

public class BoardPanel extends JPanel{

	public BoardPanel(int rows, int columns){
		setOpaque(false);
		setLayout(new GridLayout(rows + 1, columns));
		setPreferredSize(new Dimension((columns + 1) * 48, (rows + 1) * 48));
		mSquareLabels = new SquareJLabel[rows][columns];
		createGrid(rows, columns);
	}
	
	public BoardPanel(int rows, int columns, boolean isJail){
		setOpaque(false);
		setLayout(new GridLayout(rows + 1, columns));
		setPreferredSize(new Dimension((columns + 1) * 25, (rows + 1) * 25));
		mSquareLabels = new SquareJLabel[rows][columns];
		createGrid(rows, columns);
	}

	/**
	 * Creates the grid for the BoardPanel
	 * 
	 * @param rows the total rows of the board
	 * @param columns the total columns of the board
	 */
	public void createGrid(int rows, int columns)
	{
		for(int i = 0; i < rows; i++){
			for(int k = 1; k <= columns; k++){
				SquareJLabel square = new SquareJLabel(new ChessCoordinates(i, k, 0), null, true);
				add(square);
				mSquareLabels[i][k] = square;
			}
		}
	}
	
	public void updateSquareLabel(ChessCoordinates coordinates, Piece piece){
		mSquareLabels[coordinates.row][coordinates.column].changePiece(piece);
		if(isJail)
			mSquareLabels[coordinates.row][coordinates.column].refresh(true);
		else
			mSquareLabels[coordinates.row][coordinates.column].refresh(false);
	}
	
	private static final long serialVersionUID = 9042633590279303353L;
	
	private SquareJLabel[][] mSquareLabels;
	private boolean isJail;
}
