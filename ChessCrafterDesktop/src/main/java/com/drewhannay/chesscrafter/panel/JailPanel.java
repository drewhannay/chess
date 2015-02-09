package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.utility.UiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class JailPanel extends ChessPanel {

    private final int mJailDimension;
    private final List<SquareJLabel> mSquareLabels;

    public JailPanel(int totalPieces) {
        super(false);

        mJailDimension = (totalPieces / 4) + (totalPieces % 4);
        mSquareLabels = new ArrayList<>(totalPieces);

        setBorder(UiUtility.createBorder(Messages.getString("PlayGamePanel.capturedPieces")));
        setLayout(new GridLayout(mJailDimension, mJailDimension));
        createGrid();
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

    public void updateJailPopulation(@NotNull Collection<Piece> pieces, Function<Integer, Color> teamColor) {
        Iterator<Piece> pieceIterator = pieces.iterator();
        mSquareLabels.stream().forEach(square -> {
            Piece piece = pieceIterator.hasNext() ? pieceIterator.next() : null;
            square.setPiece(piece, piece != null ? teamColor.apply(piece.getTeamId()) : null);
        });
    }
}
