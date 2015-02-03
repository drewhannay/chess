package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BoardPanel extends ChessPanel {

    private final BoardSize mBoardSize;
    private final List<SquareJLabel> mSquareLabels;
    private final Function<BoardCoordinate, Set<BoardCoordinate>> mGetMovesCallback;

    private final SquareConfig mSquareConfig;

    public BoardPanel(BoardSize boardSize, SquareConfig squareConfig,
                      Function<BoardCoordinate, Set<BoardCoordinate>> getMovesCallback) {
        super(false);

        mSquareConfig = squareConfig;
        mGetMovesCallback = getMovesCallback;
        mBoardSize = boardSize;
        mSquareLabels = new ArrayList<>(boardSize.width * boardSize.height);

        GridLayout gridLayout = new GridLayout(mBoardSize.width + 1, mBoardSize.height + 1);
        setLayout(gridLayout);
        createGrid(gridLayout);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension squareSize = mSquareLabels.get(0).getMinimumSize();
        return new Dimension(mBoardSize.width * squareSize.width, mBoardSize.height * squareSize.height);
    }

    public void updatePieceLocations(Board board, Function<Integer, Color> teamColor) {
        mSquareLabels.forEach(square -> {
            Piece piece = board.getPiece(square.getCoordinates());
            square.setPiece(piece, piece != null ? teamColor.apply(piece.getTeamId()) : null);
        });
    }

    public List<SquareJLabel> getAllSquares() {
        return mSquareLabels;
    }

    public List<SquareJLabel> getLabelsForCoordinates(Set<BoardCoordinate> coordinates) {
        List<SquareJLabel> labels = mSquareLabels.stream()
                .filter(label -> coordinates.contains(label.getCoordinates()))
                .collect(Collectors.toList());

        // TODO: doing highlights here violates the POLA, should move it somewhere else
        boolean shouldHighlight = PreferenceUtility.getHighlightMovesPreference();
        if (shouldHighlight) {
            labels.forEach(SquareJLabel::highlight);
        }

        return labels;
    }

    private void createGrid(GridLayout gridLayout) {
        for (int y = gridLayout.getRows() - 1; y >= 0; y--) {
            for (int x = 0; x < gridLayout.getColumns(); x++) {
                add(getComponentForCell(x, y));
            }
        }
    }

    private JLabel getComponentForCell(int x, int y) {
        if (x == 0 && y == 0) {
            return GuiUtility.createJLabel("");
        } else if (x == 0) {
            JLabel label = GuiUtility.createJLabel(String.valueOf(y));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        } else if (y == 0) {
            JLabel label = GuiUtility.createJLabel(String.valueOf((char) (x - 1 + 'A')));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        } else {
            SquareJLabel square = new SquareJLabel(BoardCoordinate.at(x, y));
            mSquareConfig.configureSquare(square, () -> {
                Set<BoardCoordinate> coordinates = mGetMovesCallback.apply(square.getCoordinates());
                return !coordinates.isEmpty() ? getLabelsForCoordinates(coordinates) : Collections.emptyList();
            });
            mSquareLabels.add(square);
            return square;
        }
    }
}
