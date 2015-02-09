package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.panel.PieceCrafterDetailPanel;
import com.drewhannay.chesscrafter.panel.PieceCrafterMasterPanel;

import javax.swing.*;
import java.awt.*;

public final class PieceCrafterFrame extends ChessFrame {

    @Override
    void doInitComponents() {
        super.doInitComponents();

        setTitle("Piece Crafter");

        PieceCrafterDetailPanel detailPanel = new PieceCrafterDetailPanel(getGlassPane());
        PieceCrafterMasterPanel masterPanel = new PieceCrafterMasterPanel(detailPanel::loadPieceType);

        setPreferredSize(new Dimension(800, 575));
        masterPanel.setMinimumSize(new Dimension(200, 575));
        detailPanel.setMinimumSize(new Dimension(600, 575));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, masterPanel, detailPanel);
        splitPane.setPreferredSize(new Dimension(530, 575));
        splitPane.setDividerLocation(0.2);
        splitPane.setDividerSize(1);
        splitPane.setResizeWeight(0.2);
        splitPane.setEnabled(false);
        add(splitPane);
    }
}
