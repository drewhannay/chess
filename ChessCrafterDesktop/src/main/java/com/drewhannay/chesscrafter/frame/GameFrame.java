package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.dialog.NewGameDialog;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.panel.GamePanel;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameFrame extends ChessFrame {

    private final JTabbedPane mTabbedPane;

    public GameFrame(@NotNull Game game) {

        mTabbedPane = new JTabbedPane();
        addGame(game);
        add(mTabbedPane);
        pack();

        setFocusable(true);
        addWindowFocusListener(mWindowFocusListener);
    }

    public void addGame(@NotNull Game game) {
        GamePanel panel = new GamePanel(this, game);
        mTabbedPane.addTab(game.getGameType(), panel);
        mTabbedPane.setSelectedComponent(panel);
    }

    private final WindowFocusListener mWindowFocusListener = new WindowFocusListener() {
        @Override
        public void windowGainedFocus(WindowEvent e) {
            ChessActions.DECLARE_DRAW.getAction().setActionListener(event -> {
                GamePanel panel = (GamePanel) mTabbedPane.getSelectedComponent();
                panel.declareDraw();
                panel.endOfGame();
            });
            ChessActions.SAVE_GAME.getAction().setActionListener(event -> {
                GamePanel panel = (GamePanel) mTabbedPane.getSelectedComponent();
                panel.saveGame();
                PreferenceUtility.clearTooltipListeners();
            });
            ChessActions.NEW_GAME.getAction().setActionListener(event -> new NewGameDialog(GameFrame.this));
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
            ChessActions.DECLARE_DRAW.getAction().removeActionListener();
            ChessActions.SAVE_GAME.getAction().removeActionListener();
            ChessActions.NEW_GAME.getAction().removeActionListener();
        }
    };
}
