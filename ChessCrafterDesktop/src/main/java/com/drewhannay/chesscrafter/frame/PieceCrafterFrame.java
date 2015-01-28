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

        PieceCrafterMasterPanel masterPanel = new PieceCrafterMasterPanel();
        PieceCrafterDetailPanel detailPanel = new PieceCrafterDetailPanel();

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
