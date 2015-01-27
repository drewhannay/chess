package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.frame.Driver;
import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class OpenGameAction extends ChessAction {
    public OpenGameAction() {
        super(false);

        putValue(NAME, Messages.getString("Driver.openGame"));
        putValue(SHORT_DESCRIPTION, Messages.getString("Driver.openASavedGame"));

        putValue(MNEMONIC_KEY, KeyEvent.VK_O);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Driver.openGame();
    }
}
