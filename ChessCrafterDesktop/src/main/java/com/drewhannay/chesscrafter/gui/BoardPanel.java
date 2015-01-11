package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.controllers.GameController;
import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.MotionAdapter;
import com.drewhannay.chesscrafter.models.ChessCoordinate;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class BoardPanel extends JPanel {

    /**
     * Constructor for a normal game board
     *
     * @param rows       total rows in the board
     * @param columns    total columns in the board
     * @param boardIndex keeps track of which board this is if > 1
     */
    public BoardPanel(int rows, int columns, int boardIndex, GlassPane globalGlassPane, DropManager dropManager, boolean isJail) {
        setOpaque(false);
        setLayout(new GridLayout(rows + 1, columns));
        setPreferredSize(new Dimension((columns + 1) * 48, (rows + 1) * 48));
        mSquareLabels = new SquareJLabel[rows][columns];
        mGlassPane = globalGlassPane;
        mDropManager = dropManager;
        mBoardIndex = boardIndex;
        if (isJail)
            createJailGrid(rows, columns);
        else
            createGrid(rows, columns);
    }

    /**
     * Creates the grid for the a game BoardPanel
     *
     * @param rows    the total rows of the board
     * @param columns the total columns of the board
     */
    private void createGrid(int rows, int columns) {
        for (int i = rows; i > 0; i--) {
            JLabel label = GuiUtility.createJLabel("" + i); //$NON-NLS-1$
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label);
            for (int k = 1; k <= columns; k++) {
                SquareJLabel square = new SquareJLabel(new ChessCoordinate(i, k, mBoardIndex), true, 48);
                square.addMouseMotionListener(new MotionAdapter(mGlassPane));
                square.addMouseListener(new SquareListener(square, mDropManager, mGlassPane));
                add(square);
                mSquareLabels[i - 1][k - 1] = square;
                square.refresh();
            }
        }
        for (int j = 0; j <= columns; j++) {
            if (j != 0) {
                JLabel label = GuiUtility.createJLabel("" + (char) (j - 1 + 'A')); //$NON-NLS-1$
                label.setHorizontalAlignment(SwingConstants.CENTER);
                add(label);
            } else {
                add(GuiUtility.createJLabel("")); //$NON-NLS-1$
            }
        }
    }

    public void refreshSquares() {
        for (SquareJLabel[] labelArray : mSquareLabels)
            for (SquareJLabel label : labelArray)
                label.refresh();
    }

    /**
     * Creates the grid for the a jail BoardPanel
     *
     * @param rows    the total rows of the board
     * @param columns the total columns of the board
     */
    private void createJailGrid(int rows, int columns) {
        for (int i = 1; i <= rows; i++) {
            for (int k = 1; k <= columns; k++) {
                SquareJLabel square = new SquareJLabel(new ChessCoordinate(i, k, mBoardIndex + GameController.getGame().getBoards().length), true, 25);
                add(square);
                mSquareLabels[i - 1][k - 1] = square;
            }
        }
    }

    public List<SquareJLabel> highlightSquares(Set<ChessCoordinate> coordinates) {
        List<SquareJLabel> toHighlight = Lists.newArrayList();
        for (ChessCoordinate coordinate : coordinates) {
            SquareJLabel label = mSquareLabels[coordinate.row - 1][coordinate.column - 1];
            label.highlight();
            toHighlight.add(label);
        }
        return toHighlight;
    }

    public Set<SquareJLabel> getSquareLabels() {
        Set<SquareJLabel> labels = Sets.newConcurrentHashSet();
        for (SquareJLabel[] labelArray : mSquareLabels)
            for (SquareJLabel label : labelArray)
                labels.add(label);
        return labels;
    }

    private static final long serialVersionUID = 9042633590279303353L;

    private SquareJLabel[][] mSquareLabels;

    private int mBoardIndex;
    private GlassPane mGlassPane;
    private DropManager mDropManager;
}
