package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.gui.Driver;
import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class QuitAction extends ChessAction {
    public QuitAction() {
        super(false);

        putValue(NAME, Messages.getString("Driver.quit"));
        putValue(SHORT_DESCRIPTION, Messages.getString("Driver.closeProgram"));

        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int answer = JOptionPane.showConfirmDialog(Driver.getInstance(),
                Messages.getString("Driver.sureYouWannaQuit"), Messages.getString("Driver.quitQ"),
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (answer == 0)
            System.exit(0);
    }
}
