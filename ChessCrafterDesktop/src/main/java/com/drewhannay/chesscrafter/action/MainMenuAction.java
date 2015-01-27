package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.gui.Driver;
import com.drewhannay.chesscrafter.utility.Messages;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class MainMenuAction extends ChessAction {
    public MainMenuAction() {
        super(false);

        putValue(NAME, Messages.getString("Driver.mainMenu"));
        putValue(SHORT_DESCRIPTION, Messages.getString("Driver.returnToMain"));

        putValue(MNEMONIC_KEY, KeyEvent.VK_M);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Driver.getInstance().revertToMainPanel();
    }
}
