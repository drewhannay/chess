package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.frame.FrameManager;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class PieceCrafterAction extends ChessAction {
    PieceCrafterAction() {
        super(false);

        putValue(NAME, "Piece Crafter");

        putValue(MNEMONIC_KEY, KeyEvent.VK_P);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FrameManager.INSTANCE.openPieceCrafterFrame();
    }
}
