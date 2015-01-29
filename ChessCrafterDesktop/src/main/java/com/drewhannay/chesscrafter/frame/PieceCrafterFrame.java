package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.panel.PieceCrafterPanel;
import com.drewhannay.chesscrafter.panel.PieceMenuPanel;

import java.awt.*;

public final class PieceCrafterFrame extends ChessFrame {

    @Override
    void doInitComponents() {
        super.doInitComponents();

        setTitle("Piece Crafter");
        setSize(400, 600);
        setPreferredSize(new Dimension(400, 600));

        add(new PieceCrafterPanel(new PieceMenuPanel()));
    }
}
