package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public final class DeclareDrawAction extends ChessAction {
    public DeclareDrawAction() {
        super(true);

        putValue(NAME, Messages.getString("PlayGamePanel.declareDraw"));

        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
}
