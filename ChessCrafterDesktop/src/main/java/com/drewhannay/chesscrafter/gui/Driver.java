package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.action.NewGameAction;
import com.drewhannay.chesscrafter.action.OpenGameAction;
import com.drewhannay.chesscrafter.logic.GameBuilder;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.History;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public final class Driver extends ChessFrame {
    public static void main(String[] args) {
        getInstance();
    }

    private Driver() {
    }

    public static Driver getInstance() {
        if (sInstance == null)
            sInstance = new Driver();

        return sInstance;
    }

    public void setUpNewGame() {
        setPanel(new NewGamePanel());
    }

    public void setPanel(JPanel panel) {
        remove(mMainPanel);
        if (mOtherPanel != null) {
            remove(mOtherPanel);
        }
        add(panel);
        mOtherPanel = panel;
        pack();
        centerFrame();
    }

    public void revertToMainPanel() {
        remove(mOtherPanel);
        add(mMainPanel);
        pack();
        centerFrame();
    }

    @Override
    void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // TODO: do this once in the Main class
        // make the app match the look and feel of the user's system
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        UIManager.put("TabbedPane.contentOpaque", false);
        createWindowsTrayIconIfNecessary();
        enableOSXFullscreen(this);

        // TODO: do this once in the Main class
        // put menus in the OS X menu bar if we're on OS X
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // set up a new panel to hold everything in the main window
        mMainPanel = new ChessPanel();
        mMainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        mMainPanel.setLayout(new GridBagLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        // home screen image
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        mMainPanel.add(new JLabel(GuiUtility.createSystemImageIcon(400, 400, "/chess_logo.png")), c);

        // new game
        c.gridx = 0;
        c.gridy = 0;
        buttonPanel.add(createNewGameButton(), c);

        // open variant creator
        c.gridx = 0;
        c.gridy = 1;
        buttonPanel.add(createVariantCreatorButton(), c);

        // open piece creator
        c.gridx = 0;
        c.gridy = 2;
        buttonPanel.add(createPieceCreatorButton(), c);

        // continue
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        buttonPanel.add(createContinueGameButton(), c);

        // view completed game
        c.gridx = 0;
        c.gridy = 4;
        buttonPanel.add(createViewCompletedGamesButton(), c);

        c.gridx = 1;
        c.gridy = 0;
        mMainPanel.add(buttonPanel, c);

        add(mMainPanel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private JButton createPieceCreatorButton() {
        JButton pieceButton = new JButton(Messages.getString("Driver.pieces"));
        pieceButton.setToolTipText(Messages.getString("Driver.createEditRemove"));
        pieceButton.setIcon(GuiUtility.createSystemImageIcon(10, 30, "/pieces.png"));
        pieceButton.addActionListener(e -> setPanel(new PieceMenuPanel()));

        return pieceButton;
    }

    private void createWindowsTrayIconIfNecessary() {
        try {
            BufferedImage frontPageImage = FileUtility.getFrontPageImage();
            if (System.getProperty("os.name").startsWith("Windows")) {
                final SystemTray sysTray = SystemTray.getSystemTray();
                TrayIcon tray = new TrayIcon(frontPageImage.getScaledInstance(25, 18, Image.SCALE_SMOOTH));
                sysTray.add(tray);
            }
            setIconImage(frontPageImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableOSXFullscreen(Window window) {
        Preconditions.checkNotNull(window);
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class<?> params[] = new Class[]{Window.class, Boolean.TYPE};
            @SuppressWarnings("unchecked")
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (Exception e) {
            System.out.println("OS X Fullscreen FAIL" + e.getLocalizedMessage());
        }
    }

    private JButton createNewGameButton() {
        JButton newGameButton = new JButton(new NewGameAction());
        newGameButton.setIcon(GuiUtility.createSystemImageIcon(30, 30, "/start_game.png"));
        return newGameButton;
    }

    private JButton createContinueGameButton() {
        JButton continueGameButton = new JButton(new OpenGameAction());
        continueGameButton.setToolTipText(Messages.getString("Driver.openASavedGame"));
        continueGameButton.setIcon(GuiUtility.createSystemImageIcon(30, 30, "/saved_games.png"));

        return continueGameButton;
    }

    private JButton createViewCompletedGamesButton() {
        JButton viewCompletedGameButton = new JButton(Messages.getString("Driver.completedGames"));
        viewCompletedGameButton.setToolTipText(Messages.getString("Driver.reviewFinishedGame"));
        viewCompletedGameButton.setIcon(GuiUtility.createSystemImageIcon(30, 30, "/view_completed.png"));
        viewCompletedGameButton.addActionListener(event -> {
            try {
                String[] files = FileUtility.getCompletedGamesFileArray();
                if (files.length == 0) {
                    JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.noCompletedToDisplay"),
                            Messages.getString("Driver.noCompleted"), JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                final JFrame poppedFrame = new JFrame(Messages.getString("Driver.viewCompleted"));
                poppedFrame.setLayout(new GridBagLayout());
                poppedFrame.setSize(225, 200);
                poppedFrame.setResizable(false);
                poppedFrame.setLocationRelativeTo(Driver.this);
                GridBagConstraints constraints = new GridBagConstraints();

                final JList<String> completedGamesList = new JList<>(FileUtility.getCompletedGamesFileArray());
                final JScrollPane scrollPane = new JScrollPane(completedGamesList);
                scrollPane.setPreferredSize(new Dimension(200, 200));
                completedGamesList.setSelectedIndex(0);

                JButton nextButton = new JButton(Messages.getString("Driver.next"));
                nextButton.addActionListener(event1 -> {
                    if (completedGamesList.getSelectedValue() == null) {
                        JOptionPane.showMessageDialog(Driver.getInstance(),
                                Messages.getString("Driver.selectGame"), Messages.getString("Driver.error"),
                                JOptionPane.PLAIN_MESSAGE);
                        return;
                    }

//                    File file = FileUtility.getCompletedGamesFile(completedGamesList.getSelectedValue().toString());
//                    setPanel(new WatchGamePanel(file));
//                    mOtherPanel = m_watchGameScreen;
                    poppedFrame.dispose();
                });

                JButton cancelButton = new JButton(Messages.getString("Driver.cancel"));
                GuiUtility.setupDoneButton(cancelButton, poppedFrame);

                JButton deleteButton = new JButton(Messages.getString("Driver.deleteCompleted"));
                deleteButton.addActionListener(event1 -> {
                    if (completedGamesList.getSelectedValue() != null) {
                        boolean didDeleteCompletedGameSuccessfully = FileUtility.getCompletedGamesFile(
                                completedGamesList.getSelectedValue()).delete();
                        if (!didDeleteCompletedGameSuccessfully) {
                            JOptionPane.showMessageDialog(Driver.getInstance(),
                                    Messages.getString("Driver.completedNotDeleted"),
                                    Messages.getString("Driver.error"), JOptionPane.PLAIN_MESSAGE);
                        } else {
                            completedGamesList.removeAll();
                            completedGamesList.setListData(FileUtility.getCompletedGamesFileArray());
                            completedGamesList.setSelectedIndex(0);
                            if (completedGamesList.getSelectedValue() == null) {
                                JOptionPane.showMessageDialog(
                                        Driver.getInstance(),
                                        Messages.getString("Driver.noMoreCompleted"), Messages.getString("Driver.noCompleted"),
                                        JOptionPane.PLAIN_MESSAGE);
                                poppedFrame.dispose();
                            }
                            scrollPane.getViewport().add(completedGamesList, null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.currentlyNoCompleted"),
                                Messages.getString("Driver.noCompletedSelected"), JOptionPane.PLAIN_MESSAGE);
                    }
                });

                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.gridwidth = 2;
                constraints.insets = new Insets(5, 5, 5, 5);
                poppedFrame.add(scrollPane, constraints);

                constraints.gridx = 0;
                constraints.gridy = 1;
                poppedFrame.add(deleteButton, constraints);

                constraints.weighty = 1.0;
                constraints.weightx = 1.0;
                constraints.gridx = 0;
                constraints.gridy = 2;
                constraints.gridwidth = 1;
                constraints.anchor = GridBagConstraints.EAST;
                poppedFrame.add(nextButton, constraints);

                constraints.gridx = 1;
                constraints.gridy = 2;
                constraints.anchor = GridBagConstraints.WEST;
                poppedFrame.add(cancelButton, constraints);

                poppedFrame.setVisible(true);
                poppedFrame.pack();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(Driver.getInstance(),
                        Messages.getString("Driver.eitherNoCompletedOrGameMissing"), Messages.getString("Driver.noCompleted"),
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

        return viewCompletedGameButton;
    }

    private JButton createVariantCreatorButton() {
        JButton variantButton = new JButton(Messages.getString("Driver.variants"));
        variantButton.setToolTipText(Messages.getString("Driver.createEditRemoveVariants"));
        variantButton.setIcon(GuiUtility.createSystemImageIcon(30, 30, "/chess_variants.jpg"));
        variantButton.addActionListener(e -> setPanel(new VariantMenuPanel()));

        return variantButton;
    }

    public void openGame() {
        String[] files = FileUtility.getGamesInProgressFileArray();

        if (files.length == 0) {
            JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.noSavedGames"),
                    Messages.getString("Driver.noCompletedGames"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        final JFrame poppedFrame = new JFrame(Messages.getString("Driver.loadSavedGame"));
        poppedFrame.setSize(225, 200);
        poppedFrame.setResizable(false);
        poppedFrame.setLocationRelativeTo(this);
        GridBagConstraints constraints = new GridBagConstraints();

        final ChessPanel popupPanel = new ChessPanel();
        popupPanel.setLayout(new GridBagLayout());

        final JList<String> gamesInProgressList = new JList<>(FileUtility.getGamesInProgressFileArray());
        final JScrollPane scrollPane = new JScrollPane(gamesInProgressList);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        gamesInProgressList.setSelectedIndex(0);

        JButton nextButton = new JButton(Messages.getString("Driver.loadSavedGame"));
        nextButton.addActionListener(event1 -> {
            if (gamesInProgressList.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(Driver.getInstance(),
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

                new PlayGameFrame(game);
//                setPanel();
                poppedFrame.dispose();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(Driver.getInstance(),
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
                    JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.savedGameNotDeleted"),
                            Messages.getString("Driver.error"), JOptionPane.ERROR_MESSAGE);
                } else {
                    gamesInProgressList.setListData(FileUtility.getGamesInProgressFileArray());
                    gamesInProgressList.setSelectedIndex(0);
                    if (gamesInProgressList.getSelectedValue() == null) {
                        JOptionPane.showMessageDialog(Driver.getInstance(), Messages
                                        .getString("Driver.noMoreCompletedGames"), Messages.getString("Driver.noCompletedGames"),
                                JOptionPane.PLAIN_MESSAGE);
                        poppedFrame.dispose();
                    }
                    scrollPane.getViewport().add(gamesInProgressList, null);
                }
            } else {
                JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.noSaveFiles"),
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

    private void centerFrame() {
        getInstance().setLocationRelativeTo(null);
    }

    private static final long serialVersionUID = -3533604157531154757L;

    private static Driver sInstance;

    private JPanel mMainPanel;
    private JPanel mOtherPanel;
}
