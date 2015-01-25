package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.MotionAdapter;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Pair;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;
import com.google.common.collect.Lists;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class BoardPanel extends JPanel {

    private static final long serialVersionUID = 9042633590279303353L;

    private final int mBoardIndex;
    private final SquareJLabel[][] mSquareLabels;
    private final Function<Pair<Integer, BoardCoordinate>, Set<BoardCoordinate>> mGetMovesCallback;
    private final Function<BoardCoordinate, List<SquareJLabel>> mHighlightCallback;

    private final GlassPane mGlassPane;
    private final DropManager mDropManager;

    public BoardPanel(BoardSize boardSize, int boardIndex, GlassPane globalGlassPane, DropManager dropManager,
                      Function<Pair<Integer, BoardCoordinate>, Set<BoardCoordinate>> getMovesCallback) {
        mSquareLabels = new SquareJLabel[boardSize.width][boardSize.height];
        mGlassPane = globalGlassPane;
        mDropManager = dropManager;
        mBoardIndex = boardIndex;
        mGetMovesCallback = getMovesCallback;

        mHighlightCallback = coordinate -> {
            Set<BoardCoordinate> coordinates = mGetMovesCallback.apply(Pair.create(mBoardIndex, coordinate));
            if (!coordinates.isEmpty()) {
                List<SquareJLabel> labels = highlightSquares(coordinates);
                repaint();
                return labels;
            } else {
                return Collections.emptyList();
            }
        };

        setOpaque(false);
        setLayout(new GridLayout(boardSize.width + 1, boardSize.height));
        setPreferredSize(new Dimension((boardSize.width + 1) * 48, (boardSize.height + 1) * 48));
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
                square.addMouseListener(new SquareListener(square, mDropManager, mGlassPane, mHighlightCallback));
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
        refreshSquares();
    }

    public void refreshSquares() {
        for (SquareJLabel[] labelArray : mSquareLabels)
            for (SquareJLabel label : labelArray)
                label.refresh();
    }

    public List<SquareJLabel> highlightSquares(Set<BoardCoordinate> coordinates) {
        List<SquareJLabel> labels = Lists.newArrayList();
        boolean shouldHighlight = PreferenceUtility.getHighlightMovesPreference();
        for (BoardCoordinate coordinate : coordinates) {
            SquareJLabel label = mSquareLabels[coordinate.x - 1][coordinate.y - 1];
            if (shouldHighlight) {
                label.highlight();
            }
            labels.add(label);
        }
        return labels;
    }
}
