package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.dialog.NewGameDialog;
import com.drewhannay.chesscrafter.logic.GameBuilder;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.History;
import com.drewhannay.chesscrafter.panel.ChessPanel;
import com.drewhannay.chesscrafter.panel.GamePanel;
import com.drewhannay.chesscrafter.panel.HintPanel;
import com.drewhannay.chesscrafter.utility.AppConstants;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.FileReader;
import java.io.IOException;

public class GameFrame extends ChessFrame {

    private static final String KEY_HINTS = "Hints";
    private static final String KEY_TABS = "Tabs";

    private final CardLayout mCardLayout;
    private final JPanel mCardPanel;

    private int mGameCount = 0;

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

        mGameCount = 1;

        setFocusable(true);
        addWindowFocusListener(mWindowFocusListener);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mTabbedPane.remove(mTabbedPane.getSelectedIndex());
            }
        });

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

    public void addGame(@NotNull Game game) {
        GamePanel panel = new GamePanel(this, game);
        if(mGameCount == 1)
            mTabbedPane.addTab(game.getGameType(), panel);
        else
            mTabbedPane.addTab(game.getGameType() + mGameCount, panel);
        mGameCount++;
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
        String[] files = FileUtility.getGamesInProgressFileArray();

        if (files.length == 0) {
            JOptionPane.showMessageDialog(null, Messages.getString("Driver.noSavedGames"),
                    Messages.getString("Driver.noCompletedGames"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        final JFrame poppedFrame = new JFrame(Messages.getString("Driver.loadSavedGame"));
        poppedFrame.setSize(225, 200);
        poppedFrame.setResizable(false);
        poppedFrame.setLocationRelativeTo(null);
        GridBagConstraints constraints = new GridBagConstraints();

        ChessPanel popupPanel = new ChessPanel();
        popupPanel.setLayout(new GridBagLayout());

        JList<String> gamesInProgressList = new JList<>(FileUtility.getGamesInProgressFileArray());
        JScrollPane scrollPane = new JScrollPane(gamesInProgressList);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        gamesInProgressList.setSelectedIndex(0);

        JButton nextButton = new JButton(Messages.getString("Driver.loadSavedGame"));
        nextButton.addActionListener(event1 -> {
            if (gamesInProgressList.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(null,
                        Messages.getString("Driver.selectGame"), Messages.getString("Driver.error"),
                        JOptionPane.PLAIN_MESSAGE);
                return;
            }
            String fileName = gamesInProgressList.getSelectedValue();
            try {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(new FileReader(FileUtility.getGamesInProgressFile(fileName)));
                History history = GsonUtility.fromJson(jsonElement, History.class);
                // TODO: should read variant name from history
                Game game = GameBuilder.buildGame(GameBuilder.getClassicConfiguration(), history);
                addGame(game);

                poppedFrame.dispose();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        Messages.getString("Driver.noValidSavedGames"), Messages.getString("Driver.invalidSavedGames"),
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

        JButton cancelButton = new JButton(Messages.getString("Driver.cancel"));
        GuiUtility.setupDoneButton(cancelButton, poppedFrame);

        JButton deleteButton = new JButton(Messages.getString("Driver.deleteSavedGame"));
        deleteButton.addActionListener(event1 -> {
            if (gamesInProgressList.getSelectedValue() != null) {
                boolean didDeleteSuccessfully = FileUtility.getGamesInProgressFile(
                        gamesInProgressList.getSelectedValue()).delete();
                if (!didDeleteSuccessfully) {
                    JOptionPane.showMessageDialog(null, Messages.getString("Driver.savedGameNotDeleted"),
                            Messages.getString("Driver.error"), JOptionPane.ERROR_MESSAGE);
                } else {
                    gamesInProgressList.setListData(FileUtility.getGamesInProgressFileArray());
                    gamesInProgressList.setSelectedIndex(0);
                    if (gamesInProgressList.getSelectedValue() == null) {
                        JOptionPane.showMessageDialog(null, Messages.getString("Driver.noMoreCompletedGames"), Messages.getString("Driver.noCompletedGames"),
                                JOptionPane.PLAIN_MESSAGE);
                        poppedFrame.dispose();
                    }
                    scrollPane.getViewport().add(gamesInProgressList, null);
                }
            } else {
                JOptionPane.showMessageDialog(null, Messages.getString("Driver.noSaveFiles"),
                        Messages.getString("Driver.noSaveFileSelected"), JOptionPane.PLAIN_MESSAGE);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(5, 5, 5, 5);
        popupPanel.add(scrollPane, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        popupPanel.add(deleteButton, constraints);

        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.EAST;
        popupPanel.add(nextButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        popupPanel.add(cancelButton, constraints);

        poppedFrame.add(popupPanel);
        poppedFrame.setVisible(true);
        poppedFrame.pack();
    }
}
