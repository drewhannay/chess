package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.MotionAdapter;
import com.drewhannay.chesscrafter.dragNdrop.SquareListener;
import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
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

    private final GlassPane mGlassPane;
    private final DropManager mDropManager;

    public BoardPanel(BoardSize boardSize, GlassPane globalGlassPane, DropManager dropManager,
                      Function<BoardCoordinate, Set<BoardCoordinate>> getMovesCallback) {
        super(false);

        mGlassPane = globalGlassPane;
        mDropManager = dropManager;
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

    public void updatePieceLocations(Board board) {
        mSquareLabels.forEach(square -> square.setPiece(board.getPiece(square.getCoordinates())));
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
            JLabel label = GuiUtility.createJLabel(String.valueOf(y - 1));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        } else if (y == 0) {
            JLabel label = GuiUtility.createJLabel(String.valueOf((char) (x - 1 + 'A')));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        } else {
            SquareJLabel square = new SquareJLabel(BoardCoordinate.at(x, y));
            square.addMouseMotionListener(new MotionAdapter(mGlassPane));
            square.addMouseListener(new SquareListener(mDropManager, mGlassPane, () -> {
                Set<BoardCoordinate> coordinates = mGetMovesCallback.apply(square.getCoordinates());
                return !coordinates.isEmpty() ? getLabelsForCoordinates(coordinates) : Collections.emptyList();
            }));
            mSquareLabels.add(square);
            return square;
        }
    }
}
