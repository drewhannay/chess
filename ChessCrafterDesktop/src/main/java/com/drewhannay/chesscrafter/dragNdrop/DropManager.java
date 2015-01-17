package com.drewhannay.chesscrafter.dragNdrop;

import com.drewhannay.chesscrafter.controllers.GameController;
import com.drewhannay.chesscrafter.gui.PlayGamePanel;
import com.drewhannay.chesscrafter.gui.SquareJLabel;
import com.drewhannay.chesscrafter.models.ChessCoordinate;
import com.drewhannay.chesscrafter.models.MoveBuilder;
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

        ChessCoordinate origin = originSquareLabel.getCoordinates();
        ChessCoordinate destination = destinationSquareLabel.getCoordinates();

        MoveBuilder moveBuilder = new MoveBuilder(origin, destination);
        GameController.playMove(moveBuilder.build());
        PlayGamePanel.boardRefresh();
    }
}
