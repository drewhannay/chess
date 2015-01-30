package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class JailPanel extends ChessPanel {

    private final int mJailDimension;
    private final List<SquareJLabel> mSquareLabels;

    public JailPanel(int totalPieces) {
        super(false);

        mJailDimension = (totalPieces / 4) + (totalPieces % 4);
        mSquareLabels = new ArrayList<>(totalPieces);

        setBorder(GuiUtility.createBorder(Messages.getString("PlayGamePanel.capturedPieces")));
        setPreferredSize(new Dimension((mJailDimension + 1) * 25, (mJailDimension + 1) * 25));

        setLayout(new GridLayout(mJailDimension, mJailDimension));
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

    private void createGrid() {
        for (int x = 1; x <= mJailDimension; x++) {
            for (int y = 1; y <= mJailDimension; y++) {
                SquareJLabel square = new SquareJLabel(BoardCoordinate.at(x, y));
                mSquareLabels.add(square);
                add(square);
            }
        }
    }

    public void updateJailPopulation(@NotNull Collection<Piece> pieces) {
        Iterator<Piece> pieceIterator = pieces.iterator();
        mSquareLabels.stream().forEach(square -> square.setPiece(pieceIterator.hasNext() ? pieceIterator.next() : null));
    }
}
