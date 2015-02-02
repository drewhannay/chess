package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.dialog.NewGameDialog;
import com.drewhannay.chesscrafter.logic.GameBuilder;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.History;
import com.drewhannay.chesscrafter.panel.GamePanel;
import com.drewhannay.chesscrafter.panel.HintPanel;
import com.drewhannay.chesscrafter.utility.AppConstants;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GameFrame extends ChessFrame {

    private static final String KEY_HINTS = "Hints";
    private static final String KEY_TABS = "Tabs";

    private final CardLayout mCardLayout;
    private final JPanel mCardPanel;

    private final HintPanel mHintPanel;
    private final JTabbedPane mTabbedPane;

    GameFrame() {
        mHintPanel = new HintPanel();
        mTabbedPane = new JTabbedPane();
        mCardLayout = new CardLayout();
        mCardPanel = new JPanel(mCardLayout);
    }

    @Override
    void doInitComponents() {
        super.doInitComponents();

        setTitle(AppConstants.APP_NAME);

        mCardPanel.add(mHintPanel, KEY_HINTS);
        mCardPanel.add(mTabbedPane, KEY_TABS);

        add(mCardPanel);

        setFocusable(true);
        addWindowFocusListener(mWindowFocusListener);

        mTabbedPane.addContainerListener(new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent e) {
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                if (mTabbedPane.getComponentCount() == 0) {
                    mCardLayout.show(mCardPanel, KEY_HINTS);
                }
            }
        });
    }

    @Override
    void onWindowCloseKeyboardRequest() {
        if (mTabbedPane.getTabCount() != 0) {
            mTabbedPane.remove(mTabbedPane.getSelectedIndex());
        }
    }

    public void addGame(@NotNull Game game) {
        GamePanel panel = new GamePanel(getGlassPane(), game);
        mTabbedPane.addTab(game.getGameType() + " " + mTabbedPane.getTabCount(), panel);
        mTabbedPane.setSelectedComponent(panel);

        mCardLayout.show(mCardPanel, KEY_TABS);
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
            ChessActions.OPEN_GAME.getAction().setActionListener(event -> openGame());
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
            ChessActions.DECLARE_DRAW.getAction().removeActionListener();
            ChessActions.SAVE_GAME.getAction().removeActionListener();
            ChessActions.NEW_GAME.getAction().removeActionListener();
        }
    };

    private void openGame() {
        try {
            File gameFile = FileUtility.chooseFile(FileUtility.HISTORY_EXTENSION_FILTER);
            if (gameFile != null) {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(new FileReader(gameFile));
                History history = GsonUtility.fromJson(jsonElement, History.class);
                // TODO: should read variant name from history
                Game game = GameBuilder.buildGame(GameBuilder.getClassicConfiguration(), history);
                addGame(game);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    Messages.getString("Driver.noValidSavedGames"), Messages.getString("Driver.invalidSavedGames"),
                    JOptionPane.PLAIN_MESSAGE);
        }
    }
}
