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

public class JailPanel extends JPanel {

    public JailPanel(int totalPieces, int teamIndex) {
        int jailDimension = (totalPieces / 4) + (totalPieces % 4);
        setOpaque(false);
        setLayout(new GridLayout(jailDimension, jailDimension));
        setPreferredSize(new Dimension(jailDimension * 48, jailDimension * 48));
        mSquareLabels = new SquareJLabel[jailDimension][jailDimension];
        mTeamIndex = teamIndex;
        setBorder(GuiUtility.createBorder(Messages.getString("PlayGamePanel.capturedPieces")));
        setLayout(new GridLayout(jailDimension, jailDimension));
        setPreferredSize(new Dimension((jailDimension + 1) * 25, (jailDimension + 1) * 25));
        createGrid(jailDimension);
    }

    private void createGrid(int jailDimensions) {
        for (int x = 1; x <= jailDimensions; x++) {
            for (int y = 1; y <= jailDimensions; y++) {
                SquareJLabel square = new SquareJLabel(BoardCoordinate.at(x, y), true, 25);
                add(square);
                mSquareLabels[x - 1][y - 1] = square;
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

    private static final long serialVersionUID = 9042633590279303353L;

    private SquareJLabel[][] mSquareLabels;

    private int mTeamIndex;
}
