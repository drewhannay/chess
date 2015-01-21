package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.MotionAdapter;
import com.drewhannay.chesscrafter.models.*;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.google.common.collect.Lists;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BoardPanel extends JPanel {

    public BoardPanel(BoardSize boardSize, int boardIndex, int teamIndex, GlassPane globalGlassPane, DropManager dropManager, boolean isJail) {
        setOpaque(false);
        setLayout(new GridLayout(boardSize.width + 1, boardSize.height));
        setPreferredSize(new Dimension((boardSize.width + 1) * 48, (boardSize.height + 1) * 48));
        mSquareLabels = new SquareJLabel[boardSize.width][boardSize.height];
        mGlassPane = globalGlassPane;
        mDropManager = dropManager;
        mBoardIndex = boardIndex;
        mTeamIndex = teamIndex;
        if (isJail)
            createJailGrid(boardSize);
        else
            createGrid(boardSize);
    }

    private void createGrid(BoardSize boardSize) {
        for (int y = boardSize.height; y > 0; y--) {
            JLabel label = GuiUtility.createJLabel("" + y); //$NON-NLS-1$
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label);
            for (int x = 1; x <= boardSize.width; x++) {
                SquareJLabel square = new SquareJLabel(BoardCoordinate.at(x, y), true, 48);
                square.addMouseMotionListener(new MotionAdapter(mGlassPane));
                square.addMouseListener(new SquareListener(square, mDropManager, mGlassPane));
                add(square);
                mSquareLabels[x - 1][y - 1] = square;
                square.refresh();
            }
        }
        for (int x = 0; x <= boardSize.width; x++) {
            if (x != 0) {
                JLabel label = GuiUtility.createJLabel("" + (char) (x - 1 + 'A')); //$NON-NLS-1$
                label.setHorizontalAlignment(SwingConstants.CENTER);
                add(label);
            } else {
                add(GuiUtility.createJLabel("")); //$NON-NLS-1$
            }
        }
    }

    public void updatePieceLocations(Board[] boards) {
        Board board = boards[mBoardIndex];
        for (int x = 0; x < mSquareLabels.length; x++) {
            for (int y = 0; y < mSquareLabels[x].length; y++) {
                SquareJLabel square = mSquareLabels[x][y];
                square.setPiece(board.getPiece(BoardCoordinate.at(x + 1, y + 1)));
            }
        }
    }

    //TODO update the square of the piece in check when Logic can provide it
    /*public void updateCheckLocation(Board[] boards){

    }*/

    public void updateJailPopulation(Team[] teams) {
        Team team = teams[mTeamIndex];
        Iterator<Piece> pieceIterator = team.getCapturedOpposingPieces().iterator();
        for (int x = 0; x < mSquareLabels.length; x++) {
            for (int y = 0; y < mSquareLabels[x].length; y++) {
                SquareJLabel square = mSquareLabels[x][y];
                if (pieceIterator.hasNext()) {
                    square.setPiece(pieceIterator.next());
                } else {
                    square.setPiece(null);
                }
            }
        }
    }

    public void refreshSquares() {
        for (SquareJLabel[] labelArray : mSquareLabels)
            for (SquareJLabel label : labelArray)
                label.refresh();
    }

    private void createJailGrid(BoardSize boardSize) {
        for (int x = 1; x <= boardSize.width; x++) {
            for (int y = 1; y <= boardSize.height; y++) {
                SquareJLabel square = new SquareJLabel(BoardCoordinate.at(x, y), true, 25);
                add(square);
                mSquareLabels[x - 1][y - 1] = square;
            }
        }
    }

    public List<SquareJLabel> highlightSquares(Set<BoardCoordinate> coordinates) {
        List<SquareJLabel> toHighlight = Lists.newArrayList();
        for (BoardCoordinate coordinate : coordinates) {
            SquareJLabel label = mSquareLabels[coordinate.x - 1][coordinate.y - 1];
            label.highlight();
            toHighlight.add(label);
        }
        return toHighlight;
    }

    private static final long serialVersionUID = 9042633590279303353L;

    private SquareJLabel[][] mSquareLabels;

    private int mBoardIndex;
    private int mTeamIndex;
    private GlassPane mGlassPane;
    private DropManager mDropManager;
}
