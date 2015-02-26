package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.panel.PieceCrafterMasterPanel;
import com.drewhannay.chesscrafter.panel.GameCrafterPanel;

import javax.swing.JSplitPane;
import java.awt.Dimension;

public final class GameCrafterFrame extends ChessFrame {

    @Override
    void doInitComponents() {
        super.doInitComponents();

        setTitle("Game Crafter");

        GameCrafterPanel detailPanel = new GameCrafterPanel(getGlassPane());
        PieceCrafterMasterPanel masterPanel = new PieceCrafterMasterPanel(detailPanel::onPieceTypeSelected);

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
