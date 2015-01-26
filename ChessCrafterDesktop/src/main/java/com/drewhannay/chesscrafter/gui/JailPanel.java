package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

public class JailPanel extends JPanel {

    private static final long serialVersionUID = 9042633590279303353L;
    private SquareJLabel[][] mSquareLabels;
    private int mTeamIndex;
    private int mJailDimension;

    public JailPanel(int totalPieces, int teamIndex) {
        mJailDimension = (totalPieces / 4) + (totalPieces % 4);
        setOpaque(false);
        setLayout(new GridLayout(mJailDimension, mJailDimension));
        setPreferredSize(new Dimension(mJailDimension * 48, mJailDimension * 48));
        mSquareLabels = new SquareJLabel[mJailDimension][mJailDimension];
        mTeamIndex = teamIndex;
        setBorder(GuiUtility.createBorder(Messages.getString("PlayGamePanel.capturedPieces")));
        setLayout(new GridLayout(mJailDimension, mJailDimension));
        setPreferredSize(new Dimension((mJailDimension + 1) * 25, (mJailDimension + 1) * 25));
        createGrid(mJailDimension);
    }
    public void rescaleBoard(int panelWidth, int panelHeight){
        Double jailScale = (panelHeight * .30);
        Double scale = (panelHeight * 1.0) / (panelWidth * 1.0);
        if(scale > .75) {
            jailScale = panelHeight * (.30 - (scale - .75) / 1.75);
        }
        jailScale = jailScale / mJailDimension;
        if(jailScale > 0) {
            setPreferredSize(new Dimension(mJailDimension * jailScale.intValue(), mJailDimension * jailScale.intValue()));
            jailScale = jailScale * .9;
            for (int y = mJailDimension; y > 0; y--) {
                for (int x = 1; x <= mJailDimension; x++) {
                    mSquareLabels[x - 1][y - 1].setImageScale(jailScale.intValue() - 1);
                    mSquareLabels[x - 1][y - 1].refresh();
                }
            }
        }
    }

    private void createGrid(int jailDimensions) {
        for (int x = 1; x <= jailDimensions; x++) {
            for (int y = 1; y <= jailDimensions; y++) {
                SquareJLabel square = new SquareJLabel(BoardCoordinate.at(x, y), true, 24);
                add(square);
                mSquareLabels[x - 1][y - 1] = square;
            }
        }
    }

    public void updateJailPopulation(@NotNull Collection<Piece> pieces) {
        Iterator<Piece> pieceIterator = pieces.iterator();
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
        refreshSquares();
    }

    public void refreshSquares() {
        for (SquareJLabel[] labelArray : mSquareLabels)
            for (SquareJLabel label : labelArray)
                label.refresh();
    }

    public int getJailDimension(){
        return mJailDimension;
    }
}
