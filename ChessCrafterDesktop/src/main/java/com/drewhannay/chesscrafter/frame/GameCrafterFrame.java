package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.panel.VariantCreationPanel;

public final class GameCrafterFrame extends ChessFrame {

    @Override
    void doInitComponents() {
        super.doInitComponents();

        setTitle("Game Crafter");

        add(new VariantCreationPanel(null));
    }
}
