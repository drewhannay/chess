package com.drewhannay.chesscrafter.action;

import com.drewhannay.chesscrafter.frame.AboutFrame;
import com.drewhannay.chesscrafter.frame.FrameManager;
import com.drewhannay.chesscrafter.utility.AppConstants;
import com.drewhannay.chesscrafter.utility.Messages;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AboutAction extends ChessAction {
    AboutAction() {
        super(false);

        // TODO: should insert app name into formatted string
        putValue(NAME, Messages.getString("Driver.about") + AppConstants.APP_NAME);
        putValue(SHORT_DESCRIPTION, Messages.getString("Driver.informationAbout") + AppConstants.APP_NAME);

        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FrameManager.INSTANCE.openAboutFrame();
    }
}
