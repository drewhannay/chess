package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public final class NewGameAction extends ChessAction {
    NewGameAction() {
        super(true);

        putValue(NAME, Messages.getString("Driver.newGame"));
        putValue(SHORT_DESCRIPTION, Messages.getString("Driver.startNewGame"));

        putValue(MNEMONIC_KEY, KeyEvent.VK_N);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
}
