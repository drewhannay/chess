package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public final class SaveGameAction extends ChessAction {
    public SaveGameAction() {
        super(true);

        putValue(NAME, Messages.getString("Driver.saveGame"));

        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
}
