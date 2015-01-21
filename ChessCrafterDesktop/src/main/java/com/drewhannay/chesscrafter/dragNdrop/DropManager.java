package com.drewhannay.chesscrafter.dragNdrop;

import com.drewhannay.chesscrafter.controllers.GameController;
import com.drewhannay.chesscrafter.gui.PlayGamePanel;
import com.drewhannay.chesscrafter.gui.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.MoveBuilder;
import com.drewhannay.chesscrafter.models.PieceType;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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

        BoardCoordinate origin = originSquareLabel.getCoordinates();
        BoardCoordinate destination = destinationSquareLabel.getCoordinates();

        MoveBuilder moveBuilder = GameController.getGame().newMoveBuilder(origin, destination);
        if (moveBuilder.needsPromotion()) {
            Set<PieceType> promotionOptions = moveBuilder.getPromotionOptions();
            PlayGamePanel.createPromotionPopup(promotionOptions, moveBuilder);
            return;
            //PieceType promotionChoice = getPromotionChoice(promotionOptions);
            //moveBuilder.setPromotionType(promotionChoice);
        }
        PlayGamePanel.updateLabels(GameController.playMove(moveBuilder.build()));
        PlayGamePanel.boardRefresh();
    }

    private PieceType getPromotionChoice(Set<PieceType> promotionOptions) {
        System.out.println("Pick a pieceType to promote to:");
        for (PieceType option : promotionOptions) {
            System.out.println(option.getName());
        }
        Scanner in = new Scanner(System.in);
        String name = "";
        PieceType promotionChoice = null;
        while (promotionChoice == null) {
            System.out.print("Enter your choice: ");
            name = in.nextLine();
            promotionChoice = getPieceTypeByName(name, promotionOptions);
        }

        return promotionChoice;
    }

    @Nullable
    private PieceType getPieceTypeByName(String name, Set<PieceType> pieceTypes) {
        for (PieceType pieceType : pieceTypes) {
            if (pieceType.getName().equals(name)) {
                return pieceType;
            }
        }

        return null;
    }
}
