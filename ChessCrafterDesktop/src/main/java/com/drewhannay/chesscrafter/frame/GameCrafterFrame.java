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

        VariantCreationPanel detailPanel = new VariantCreationPanel(getGlassPane());
        PieceCrafterMasterPanel masterPanel = new PieceCrafterMasterPanel(() -> {
            // TODO: hacky method call
            ChessActions.PIECE_CRAFTER.getAction().actionPerformed(null);
        }, detailPanel::onPieceTypeSelected);

        Dimension frameSize = new Dimension(755, 475);

        setPreferredSize(frameSize);
        masterPanel.setMinimumSize(new Dimension(225, 475));
        detailPanel.setMinimumSize(new Dimension(500, 475));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, masterPanel, detailPanel);
        splitPane.setPreferredSize(frameSize);
        splitPane.setDividerLocation(0.2);
        splitPane.setDividerSize(1);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(false);
        add(splitPane);
    }
}
