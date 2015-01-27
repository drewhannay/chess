package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.dialog.NewGameDialog;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.panel.GamePanel;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;
import org.jetbrains.annotations.NotNull;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class PlayGameFrame extends ChessFrame {

    private final GamePanel mPanel;

    public PlayGameFrame(@NotNull Game game) {
        mPanel = new GamePanel(this, game);
        add(mPanel);
        pack();

        setFocusable(true);
        addWindowFocusListener(mWindowFocusListener);
    }

    private final WindowFocusListener mWindowFocusListener = new WindowFocusListener() {
        @Override
        public void windowGainedFocus(WindowEvent e) {
            ChessActions.DECLARE_DRAW.getAction().setActionListener(event -> {
                mPanel.declareDraw();
                mPanel.endOfGame();
            });
            ChessActions.SAVE_GAME.getAction().setActionListener(event -> {
                mPanel.saveGame();
                PreferenceUtility.clearTooltipListeners();
            });
            ChessActions.NEW_GAME.getAction().setActionListener(event -> new NewGameDialog(PlayGameFrame.this));
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
            ChessActions.DECLARE_DRAW.getAction().removeActionListener();
            ChessActions.SAVE_GAME.getAction().removeActionListener();
            ChessActions.NEW_GAME.getAction().removeActionListener();
        }
    };
}
