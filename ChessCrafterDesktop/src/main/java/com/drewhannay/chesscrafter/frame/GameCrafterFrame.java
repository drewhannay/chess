package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.panel.PieceCrafterMasterPanel;
import com.drewhannay.chesscrafter.panel.VariantCreationPanel;

import javax.swing.*;
import java.awt.*;

public final class GameCrafterFrame extends ChessFrame {

    @Override
    void doInitComponents() {
        super.doInitComponents();

        setTitle("Game Crafter");

        VariantCreationPanel detailPanel = new VariantCreationPanel();
        PieceCrafterMasterPanel masterPanel = new PieceCrafterMasterPanel(() -> {
            // TODO: hacky method call
            ChessActions.PIECE_CRAFTER.getAction().actionPerformed(null);
        }, detailPanel::onPieceTypeSelected);

        setPreferredSize(new Dimension(530, 575));
        masterPanel.setMinimumSize(new Dimension(200, 575));
        detailPanel.setMinimumSize(new Dimension(300, 575));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, masterPanel, detailPanel);
        splitPane.setPreferredSize(new Dimension(530, 575));
        splitPane.setDividerLocation(0.3);
        splitPane.setDividerSize(1);
        splitPane.setResizeWeight(0.5);
        splitPane.setEnabled(false);
        add(splitPane);
    }
}
