package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.frame.FrameManager;
import com.drewhannay.chesscrafter.utility.Messages;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class HelpAction extends ChessAction {
    HelpAction() {
        super(false);

        putValue(NAME, Messages.getString("Driver.browseHelp"));
        putValue(SHORT_DESCRIPTION, Messages.getString("Driver.clickToGetHelp"));

        putValue(MNEMONIC_KEY, KeyEvent.VK_H);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FrameManager.INSTANCE.openHelpFrame();
    }
}
