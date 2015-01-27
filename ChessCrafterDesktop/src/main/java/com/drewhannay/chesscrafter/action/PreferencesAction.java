package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class PreferencesAction extends ChessAction {
    PreferencesAction() {
        super(false);

        putValue(NAME, Messages.getString("Driver.preferences"));
        putValue(SHORT_DESCRIPTION, Messages.getString("Driver.changePreferences"));

        putValue(MNEMONIC_KEY, KeyEvent.VK_P);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PreferenceUtility.createPreferencePopup(null);
    }
}
