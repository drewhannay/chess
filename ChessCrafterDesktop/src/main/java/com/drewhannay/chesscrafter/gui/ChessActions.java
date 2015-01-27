package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.action.AboutAction;
import com.drewhannay.chesscrafter.action.ChessAction;
import com.drewhannay.chesscrafter.action.DeclareDrawAction;
import com.drewhannay.chesscrafter.action.HelpAction;
import com.drewhannay.chesscrafter.action.NewGameAction;
import com.drewhannay.chesscrafter.action.OpenGameAction;
import com.drewhannay.chesscrafter.action.PreferencesAction;
import com.drewhannay.chesscrafter.action.QuitAction;
import com.drewhannay.chesscrafter.action.SaveGameAction;
import com.drewhannay.chesscrafter.utility.Messages;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyEvent;

public enum ChessActions {
    ABOUT(new AboutAction()),
    DECLARE_DRAW(new DeclareDrawAction()),
    HELP(new HelpAction()),
    NEW_GAME(new NewGameAction()),
    OPEN_GAME(new OpenGameAction()),
    PREFERENCES(new PreferencesAction()),
    SAVE_GAME(new SaveGameAction()),
    QUIT(new QuitAction());

    private final ChessAction mAction;

    private ChessActions(ChessAction action) {
        mAction = action;
    }

    @NotNull
    public ChessAction getAction() {
        return mAction;
    }

    public static JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(Messages.getString("Driver.file"));
        fileMenu.setMnemonic(KeyEvent.VK_F);

        fileMenu.add(NEW_GAME.mAction);
        fileMenu.add(OPEN_GAME.mAction);
        fileMenu.add(PREFERENCES.mAction);
        fileMenu.add(QUIT.mAction);

        JMenu optionsMenu = new JMenu(Messages.getString("Driver.options"));
        optionsMenu.setMnemonic(KeyEvent.VK_O);
        optionsMenu.setToolTipText(Messages.getString("Driver.accessGameOptions"));

        optionsMenu.add(DECLARE_DRAW.mAction);
        optionsMenu.add(SAVE_GAME.mAction);

        JMenu helpMenu = new JMenu(Messages.getString("Driver.help"));
        helpMenu.setMnemonic(KeyEvent.VK_H);

        helpMenu.add(HELP.mAction);
        helpMenu.add(ABOUT.mAction);

        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }
}
