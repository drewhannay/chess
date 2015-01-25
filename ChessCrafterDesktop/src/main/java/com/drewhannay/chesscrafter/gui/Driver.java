package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.logic.GameBuilder;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.History;
import com.drewhannay.chesscrafter.timer.ChessTimer;
import com.drewhannay.chesscrafter.utility.AppConstants;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;

public final class Driver extends JFrame {
    public static void main(String[] args) {
        try {
            getInstance().initGuiComponents();
        } catch (Exception e) {
            System.out.println(Messages.getString("Driver.errorSettingUpInitialScreen"));
            e.printStackTrace();
        }
    }

    private Driver() {
    }

    public static Driver getInstance() {
        if (sInstance == null)
            sInstance = new Driver();

        return sInstance;
    }

    public void setFileMenuVisibility(boolean isVisible) {
        mFileMenu.setVisible(isVisible);
    }

    public void setOptionsMenuVisibility(boolean isVisible) {
        mOptionsMenu.setVisible(isVisible);
    }

    public void setUpNewGame() {
        ChessTimer.stopTimers();
        setPanel(new NewGamePanel());
    }

    public void setPanel(JPanel panel) {
        if (panel instanceof PlayGamePanel)
            m_playGameScreen = (PlayGamePanel) panel;
        else if (panel instanceof WatchGamePanel)
            m_watchGameScreen = (WatchGamePanel) panel;

        remove(mMainPanel);
        if (mOtherPanel != null)
            remove(mOtherPanel);
        mMainMenuItem.setVisible(true);
        add(panel);
        // FIXME
        if (panel.toString().contains(Messages.getString("Driver.playGame")) || panel.toString().contains(Messages.getString("Driver.playNetGame")))
            activateWindowListener();
        mOtherPanel = panel;
        pack();
        centerFrame();
    }

    public void revertToMainPanel() {
        remove(mOtherPanel);
        mMainMenuItem.setVisible(false);
        add(mMainPanel);
        deactivateWindowListener();
        pack();
        centerFrame();
    }

    public void setMenu(JMenu menu) {
        mOptionsMenu = menu;
        mMenuBar.add(mOptionsMenu);
        mOptionsMenu.setVisible(true);
        mOptionsMenu.setMnemonic('O');
        mOptionsMenu.setText(Messages.getString("Driver.options"));
        mOptionsMenu.setToolTipText(Messages.getString("Driver.accessGameOptions"));
    }

    private void initGuiComponents() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        setTitle(AppConstants.APP_NAME);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(685, 450);
        setLayout(new BorderLayout());
        setResizable(true);

        // put the window in the center of the screen, regardless of resolution
        setLocationRelativeTo(null);

        // make the app match the look and feel of the user's system
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("TabbedPane.contentOpaque", false);
        createWindowsTrayIconIfNecessary();

        // create the menu bar
        createMenuBar();

        // set up a new panel to hold everything in the main window
        mMainPanel = new ChessPanel();
        mMainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        mMainPanel.setLayout(new GridBagLayout());

        mButtonPanel = new JPanel();
        mButtonPanel.setOpaque(false);
        mButtonPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        // home screen image
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        try {
            mMainPanel.add(new JLabel(GuiUtility.createImageIcon(400, 400, "/chess_logo.png")), c);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // new game
        c.gridx = 0;
        c.gridy = 0;
        mButtonPanel.add(createNewGameButton(), c);

        // open variant menu
        c.gridx = 0;
        c.gridy = 1;
        mButtonPanel.add(variantMenuButton(), c);

        // Pieces
        c.gridx = 0;
        c.gridy = 2;
        mButtonPanel.add(pieceMenuButton(), c);

        // continue
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        mButtonPanel.add(createContinueGameButton(), c);

        // view completed game
        c.gridx = 0;
        c.gridy = 4;
        mButtonPanel.add(createViewCompletedGamesButton(), c);

        c.gridx = 1;
        c.gridy = 0;
        mMainPanel.add(mButtonPanel, c);

        add(mMainPanel, BorderLayout.CENTER);
        pack();
        setVisible(true);
        //GameController.setGame(GameBuilder.buildClassic());
        //setPanel(new PlayGamePanel());
    }

    private JButton pieceMenuButton() {
        JButton pieceButton = new JButton(Messages.getString("Driver.pieces"));
        pieceButton.setToolTipText(Messages.getString("Driver.createEditRemove"));
        try {
            pieceButton.setIcon(GuiUtility.createImageIcon(10, 30, "/pieces.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pieceButton.addActionListener(e -> setPanel(new PieceMenuPanel()));

        return pieceButton;
    }

    private void activateWindowListener() {
        if (mWindowListener == null) {
            mWindowListener = new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Object[] options = new String[]{
                            Messages.getString("Driver.saveGame"), Messages.getString("Driver.dontSave"), Messages.getString("Driver.cancel")};
                    switch (JOptionPane
                            .showOptionDialog(
                                    Driver.getInstance(),
                                    Messages.getString("Driver.saveBeforeQuitting"),
                                    Messages.getString("Driver.quitQ"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0])) {
                        case JOptionPane.YES_OPTION:
                            ((PlayGamePanel) mOtherPanel).saveGame();
                            break;
                        case JOptionPane.NO_OPTION:
                            System.exit(0);
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            break;
                    }
                }
            };
        } else {
            removeWindowListener(mWindowListener);
        }

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(mWindowListener);
    }

    private void deactivateWindowListener() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        removeWindowListener(mWindowListener);
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

    private void createMenuBar() {
        mMenuBar = new JMenuBar();

        mMenuBar.add(createFileMenu());
        mMenuBar.add(createHelpMenu());

        setJMenuBar(mMenuBar);
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu(Messages.getString("Driver.file"));
        fileMenu.setMnemonic('F');

        JMenuItem newGameItem = new JMenuItem(Messages.getString("Driver.newGame"), KeyEvent.VK_N);
        newGameItem.setToolTipText(Messages.getString("Driver.startNewGame"));
        newGameItem.addActionListener(event -> setPanel(new NewGamePanel()));

        fileMenu.add(newGameItem);

        mMainMenuItem = new JMenuItem(Messages.getString("Driver.mainMenu"), KeyEvent.VK_M);
        mMainMenuItem.setToolTipText(Messages.getString("Driver.returnToMain"));
        mMainMenuItem.addActionListener(event -> {
            if (mOptionsMenu != null)
                mOptionsMenu.setVisible(false);
            if (mOtherPanel != null)
                remove(mOtherPanel);
            // FIXME Shouldn't need to deal with timers from here
            ChessTimer.stopTimers();
            revertToMainPanel();
        });
        mMainMenuItem.setVisible(false);
        fileMenu.add(mMainMenuItem);

        JMenuItem preferences = new JMenuItem(Messages.getString("Driver.preferences"), KeyEvent.VK_P);
        preferences.setToolTipText(Messages.getString("Driver.changePreferences"));
        preferences.addActionListener(event -> PreferenceUtility.createPreferencePopup(Driver.this));
        fileMenu.add(preferences);

        JMenuItem exitMenuItem = new JMenuItem(Messages.getString("Driver.quit"), KeyEvent.VK_Q);
        exitMenuItem.setToolTipText(Messages.getString("Driver.closeProgram"));
        exitMenuItem.addActionListener(event -> {
            int answer = JOptionPane.showConfirmDialog(Driver.getInstance(),
                    Messages.getString("Driver.sureYouWannaQuit"), Messages.getString("Driver.quitQ"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (answer == 0)
                System.exit(0);
        });
        fileMenu.add(exitMenuItem);

        // keep a reference to the file menu so we can toggle its visibility
        mFileMenu = fileMenu;
        return fileMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu(Messages.getString("Driver.help"));
        helpMenu.setMnemonic('H');

        JMenuItem helpMenuItem = new JMenuItem(Messages.getString("Driver.browseHelp"), KeyEvent.VK_H);
        helpMenuItem.setToolTipText(Messages.getString("Driver.clickToGetHelp"));
        helpMenuItem.addActionListener(event -> new HelpFrame());
        helpMenu.add(helpMenuItem);

        JMenuItem aboutItem = new JMenuItem(Messages.getString("Driver.about") + AppConstants.APP_NAME, KeyEvent.VK_A);
        aboutItem.setToolTipText(Messages.getString("Driver.informationAbout") + AppConstants.APP_NAME);
        aboutItem.addActionListener(event -> new AboutFrame(mMainPanel));
        helpMenu.add(aboutItem);

        return helpMenu;
    }

    private JButton createNewGameButton() {
        JButton newGameButton = new JButton(Messages.getString("Driver.newGame"));

        newGameButton.setToolTipText(Messages.getString("Driver.startANewGame"));
        try {
            newGameButton.setIcon(GuiUtility.createImageIcon(30, 30, "/start_game.png"));
        } catch (IOException ae) {
            ae.printStackTrace();
        }
        newGameButton.addActionListener(event -> setPanel(new NewGamePanel()));

        return newGameButton;
    }

    private JButton createContinueGameButton() {
        JButton continueGameButton = new JButton(Messages.getString("Driver.loadGame"));
        continueGameButton.setToolTipText(Messages.getString("Driver.loadASavedGame"));
        try {
            continueGameButton.setIcon(GuiUtility.createImageIcon(30, 30, "/saved_games.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        continueGameButton.addActionListener(event -> {
                    String[] files = FileUtility.getGamesInProgressFileArray();

                    if (files.length == 0) {
                        JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.noSavedGames"),
                                Messages.getString("Driver.noCompletedGames"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    final JFrame poppedFrame = new JFrame(Messages.getString("Driver.loadSavedGame"));
                    poppedFrame.setSize(225, 200);
                    poppedFrame.setResizable(false);
                    poppedFrame.setLocationRelativeTo(Driver.this);
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

                            // set the help menu info to be specific for game play
                            if (mOptionsMenu != null)
                                mOptionsMenu.setVisible(true);

                            m_playGameScreen = new PlayGamePanel(game);
                            setPanel(m_playGameScreen);
                            poppedFrame.dispose();
                        } catch (IOException e) {
                            e.printStackTrace();
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
        );

        return continueGameButton;
    }

    private JButton createViewCompletedGamesButton() {
        JButton viewCompletedGameButton = new JButton(Messages.getString("Driver.completedGames"));
        viewCompletedGameButton.setToolTipText(Messages.getString("Driver.reviewFinishedGame"));
        try {
            viewCompletedGameButton.setIcon(GuiUtility.createImageIcon(30, 30, "/view_completed.png"));
        } catch (IOException ae) {
            ae.printStackTrace();
        }
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

                    //File file = FileUtility.getCompletedGamesFile(completedGamesList.getSelectedValue().toString());
                    //m_watchGameScreen = new WatchGamePanel(file);
                    setPanel(m_watchGameScreen);
                    mOtherPanel = m_watchGameScreen;
                    deactivateWindowListener();
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

    private JButton variantMenuButton() {
        JButton variantButton = new JButton(Messages.getString("Driver.variants"));
        variantButton.setToolTipText(Messages.getString("Driver.createEditRemoveVariants"));
        try {
            variantButton.setIcon(GuiUtility.createImageIcon(30, 30, "/chess_variants.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        variantButton.addActionListener(e -> setPanel(new VariantMenuPanel()));

        return variantButton;
    }

    public static void centerFrame() {
//		Driver driver = getInstance();
//		int width = driver.getWidth();
//		int height = driver.getHeight();
//		if (s_screenWidth == null)
//		{
//			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			s_screenWidth = screenSize.getWidth();
//			s_screenHeight = screenSize.getHeight();
//		}
//		driver.setLocation((int) ((s_screenWidth / 2) - (width / 2)), (int) ((s_screenHeight / 2) - (height / 2)));
        getInstance().setLocationRelativeTo(null);
    }

    public void setPanel(Object newPanel) {
        setPanel((JPanel) newPanel);
    }

    private static Double s_screenWidth;
    private static Double s_screenHeight;

    private static final long serialVersionUID = -3533604157531154757L;

    private static Driver sInstance;

    private JMenuBar mMenuBar;
    private JMenu mFileMenu;
    private JMenu mOptionsMenu;
    private JMenuItem mMainMenuItem;
    private JPanel mMainPanel;
    private JPanel mOtherPanel;
    private JPanel mButtonPanel;
    private WindowListener mWindowListener;
    private PlayGamePanel m_playGameScreen;
    private WatchGamePanel m_watchGameScreen;
}
