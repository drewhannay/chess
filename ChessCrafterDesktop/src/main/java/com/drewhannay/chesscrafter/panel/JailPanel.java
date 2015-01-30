package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        createGrid();
    }

    public void rescaleBoard(double panelWidth, double panelHeight) {
        double jailScale = panelHeight * .30;
        double scale = panelHeight / panelWidth;
        if (scale > .75) {
            jailScale = panelHeight * (.30 - (scale - .75) / 1.75);
        }
        jailScale = jailScale / mJailDimension;
        if (jailScale > 0) {
            setPreferredSize(new Dimension(mJailDimension * (int) jailScale, mJailDimension * (int) jailScale));
        }
    }

    private List<SquareJLabel> getSquareLabels() {
        return Stream.of(mSquareLabels).flatMap(Stream::of).collect(Collectors.toList());
    }

    private void createGrid() {
        for (int x = 1; x <= mJailDimension; x++) {
            for (int y = 1; y <= mJailDimension; y++) {
                SquareJLabel square = new SquareJLabel(BoardCoordinate.at(x, y));
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
    }
}
