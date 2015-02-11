package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.utility.UiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class JailPanel extends ChessPanel {

    private final int JAIL_WIDTH = 4;
    private final List<SquareJLabel> mSquareLabels;
    private final BoardSize mJailSize;

    public JailPanel(int totalPieces) {
        super(false);

        if(totalPieces % JAIL_WIDTH == 0) {
            mJailSize = BoardSize.withDimensions(JAIL_WIDTH, totalPieces / JAIL_WIDTH);
        } else {
            mJailSize = BoardSize.withDimensions(JAIL_WIDTH, (totalPieces / JAIL_WIDTH) + 1);
        }
        mSquareLabels = new ArrayList<>(totalPieces);

        setBorder(UiUtility.createBorder(Messages.getString("PlayGamePanel.capturedPieces")));
        setLayout(new GridLayout(mJailSize.height, mJailSize.width));
        createGrid();
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension squareSize = mSquareLabels.get(0).getMinimumSize();
        return new Dimension(mJailSize.width * squareSize.width, mJailSize.height * squareSize.height);
    }

    public void updateDimensions(int width, int height) {
        int newHeight = height;
        int newWidth = width;
        if (width < height) {
            newHeight = (width / mJailSize.width) * mJailSize.height;
        } else {
            newWidth = (height / mJailSize.height) * mJailSize.width;
        }

        setMinimumSize(new Dimension(newWidth, newHeight));
        setPreferredSize(new Dimension(newWidth, newHeight));
        setMaximumSize(new Dimension(newWidth, newHeight));

        revalidate();
        repaint();
    }

    private void createGrid() {
        for (int x = 1; x <= mJailSize.width; x++) {
            for (int y = 1; y <= mJailSize.height; y++) {
                SquareJLabel square = new SquareJLabel(BoardCoordinate.at(x, y));
                mSquareLabels.add(square);
                add(square);
            }
        }
    }

    public void updateJailPopulation(@NotNull Collection<Piece> pieces, Function<Integer, Color> teamColor) {
        Iterator<Piece> pieceIterator = pieces.iterator();
        mSquareLabels.stream().forEach(square -> {
            Piece piece = pieceIterator.hasNext() ? pieceIterator.next() : null;
            square.setPiece(piece, piece != null ? teamColor.apply(piece.getTeamId()) : null);
        });
    }
}
