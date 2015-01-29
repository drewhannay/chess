package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class QuitAction extends ChessAction {
    QuitAction() {
        super(false);

        putValue(NAME, Messages.getString("Driver.quit"));
        putValue(SHORT_DESCRIPTION, Messages.getString("Driver.closeProgram"));

        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO:
        System.exit(0);
    }
}
