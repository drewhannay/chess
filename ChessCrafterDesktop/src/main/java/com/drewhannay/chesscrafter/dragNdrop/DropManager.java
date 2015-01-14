package com.drewhannay.chesscrafter.dragNdrop;

import com.drewhannay.chesscrafter.controllers.GameController;
import com.drewhannay.chesscrafter.gui.PlayGamePanel;
import com.drewhannay.chesscrafter.gui.SquareJLabel;
import com.drewhannay.chesscrafter.models.ChessCoordinate;
import com.drewhannay.chesscrafter.models.Move;
import com.google.common.collect.ImmutableList;

import javax.swing.*;
import java.util.List;

public class DropManager extends AbstractDropManager {
    /*
     * public void setBoard(BoardController board) { m_board = board; }
	 */

    @Override
    public void dropped(DropEvent event, boolean fromDisplayBoard) {
        SquareJLabel originSquareLabel = (SquareJLabel) event.getOriginComponent();
        SquareJLabel destinationSquareLabel = (SquareJLabel) isInTarget(event.getDropLocation());

        final List<JComponent> dummyList = ImmutableList.of();
        setComponentList(dummyList);

        if (destinationSquareLabel == null) {
            PlayGamePanel.boardRefresh();
            return;
        }

        ChessCoordinate originCoordinates = originSquareLabel.getCoordinates();
        ChessCoordinate destinationCoordinates = destinationSquareLabel.getCoordinates();

        try {
            GameController.playMove(Move.from(originCoordinates, destinationCoordinates));
        } catch (Exception e) {
            e.printStackTrace();
        }
        PlayGamePanel.boardRefresh();
    }
}
