package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.frame.FrameManager;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class GameCrafterAction extends ChessAction {
    GameCrafterAction() {
        super(false);

        putValue(NAME, "Game Crafter");

        putValue(MNEMONIC_KEY, KeyEvent.VK_G);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FrameManager.INSTANCE.openGameCrafterFrame();
    }
}
