package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.panel.PieceCrafterPanel;
import com.drewhannay.chesscrafter.panel.PieceMenuPanel;

public final class PieceCrafterFrame extends ChessFrame {

    @Override
    void initComponents() {
        setTitle("Piece Crafter");
        setSize(400, 600);

        add(new PieceCrafterPanel(new PieceMenuPanel()));
    }
}
