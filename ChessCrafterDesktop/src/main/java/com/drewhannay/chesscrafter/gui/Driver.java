package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.controllers.GameController;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.timer.ChessTimer;
import com.drewhannay.chesscrafter.utility.AppConstants;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public final class Driver extends JFrame {
    public static void main(String[] args) {
        try {
            getInstance().initGuiComponents();
        } catch (Exception e) {
            System.out.println(Messages.getString("Driver.errorSettingUpInitialScreen")); //$NON-NLS-1$
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
        if (PlayGamePanel.mOptionsMenu != null)
            PlayGamePanel.mOptionsMenu.setVisible(false);
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
        if (panel.toString().contains(Messages.getString("Driver.playGame")) || panel.toString().contains(Messages.getString("Driver.playNetGame"))) //$NON-NLS-1$ //$NON-NLS-2$
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
        mOptionsMenu.setText(Messages.getString("Driver.options")); //$NON-NLS-1$
        mOptionsMenu.setToolTipText(Messages.getString("Driver.accessGameOptions")); //$NON-NLS-1$
    }

    private void initGuiComponents() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        setTitle(AppConstants.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(685, 450);
        setLayout(new BorderLayout());
        setResizable(true);

        // put the window in the center of the screen, regardless of resolution
        setLocationRelativeTo(null);

        // make the app match the look and feel of the user's system
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("TabbedPane.contentOpaque", false); //$NON-NLS-1$
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
            mMainPanel.add(new JLabel(GuiUtility.createImageIcon(400, 400, "/chess_logo.png")), c); //$NON-NLS-1$
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
        JButton pieceButton = new JButton(Messages.getString("Driver.pieces")); //$NON-NLS-1$
        pieceButton.setToolTipText(Messages.getString("Driver.createEditRemove")); //$NON-NLS-1$
        try {
            pieceButton.setIcon(GuiUtility.createImageIcon(10, 30, "/pieces.png")); //$NON-NLS-1$
        } catch (IOException e) {
            e.printStackTrace();
        }
        pieceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPanel(new PieceMenuPanel());
            }
        });

        return pieceButton;
    }

    private void activateWindowListener() {
        if (mWindowListener == null) {
            mWindowListener = new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Object[] options = new String[]{
                            Messages.getString("Driver.saveGame"), Messages.getString("Driver.dontSave"), Messages.getString("Driver.cancel")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    switch (JOptionPane
                            .showOptionDialog(
                                    Driver.getInstance(),
                                    Messages.getString("Driver.saveBeforeQuitting"), //$NON-NLS-1$
                                    Messages.getString("Driver.quitQ"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0])) //$NON-NLS-1$
                    {
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        removeWindowListener(mWindowListener);
    }

    private void createWindowsTrayIconIfNecessary() {
        try {
            BufferedImage frontPageImage = FileUtility.getFrontPageImage();
            if (System.getProperty("os.name").startsWith("Windows")) //$NON-NLS-1$ //$NON-NLS-2$
            {
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
        JMenu fileMenu = new JMenu(Messages.getString("Driver.file")); //$NON-NLS-1$
        fileMenu.setMnemonic('F');

        JMenuItem newGameItem = new JMenuItem(Messages.getString("Driver.newGame"), KeyEvent.VK_N); //$NON-NLS-1$
        newGameItem.setToolTipText(Messages.getString("Driver.startNewGame")); //$NON-NLS-1$
        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setPanel(new NewGamePanel());
            }
        });

        fileMenu.add(newGameItem);

        mMainMenuItem = new JMenuItem(Messages.getString("Driver.mainMenu"), KeyEvent.VK_M); //$NON-NLS-1$
        mMainMenuItem.setToolTipText(Messages.getString("Driver.returnToMain")); //$NON-NLS-1$
        mMainMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (mOptionsMenu != null)
                    mOptionsMenu.setVisible(false);
                if (mOtherPanel != null)
                    remove(mOtherPanel);
                // FIXME Shouldn't need to deal with timers from here
                ChessTimer.stopTimers();
                revertToMainPanel();
            }
        });
        mMainMenuItem.setVisible(false);
        fileMenu.add(mMainMenuItem);

        JMenuItem preferences = new JMenuItem(Messages.getString("Driver.preferences"), KeyEvent.VK_P); //$NON-NLS-1$
        preferences.setToolTipText(Messages.getString("Driver.changePreferences")); //$NON-NLS-1$
        preferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                PreferenceUtility.createPreferencePopup(Driver.this);
            }
        });
        fileMenu.add(preferences);

        JMenuItem exitMenuItem = new JMenuItem(Messages.getString("Driver.quit"), KeyEvent.VK_Q); //$NON-NLS-1$
        exitMenuItem.setToolTipText(Messages.getString("Driver.closeProgram")); //$NON-NLS-1$
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int answer = JOptionPane.showConfirmDialog(Driver.getInstance(),
                        Messages.getString("Driver.sureYouWannaQuit"), Messages.getString("Driver.quitQ"), //$NON-NLS-1$ //$NON-NLS-2$
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (answer == 0)
                    System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        // keep a reference to the file menu so we can toggle its visibility
        mFileMenu = fileMenu;
        return fileMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu(Messages.getString("Driver.help")); //$NON-NLS-1$
        helpMenu.setMnemonic('H');

        JMenuItem helpMenuItem = new JMenuItem(Messages.getString("Driver.browseHelp"), KeyEvent.VK_H); //$NON-NLS-1$
        helpMenuItem.setToolTipText(Messages.getString("Driver.clickToGetHelp")); //$NON-NLS-1$
        helpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                new HelpFrame();
            }
        });
        helpMenu.add(helpMenuItem);

        JMenuItem aboutItem = new JMenuItem(Messages.getString("Driver.about") + AppConstants.APP_NAME, KeyEvent.VK_A); //$NON-NLS-1$
        aboutItem.setToolTipText(Messages.getString("Driver.informationAbout") + AppConstants.APP_NAME); //$NON-NLS-1$
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                new AboutFrame(mMainPanel);
            }
        });
        helpMenu.add(aboutItem);

        return helpMenu;
    }

    private JButton createNewGameButton() {
        JButton newGameButton = new JButton(Messages.getString("Driver.newGame")); //$NON-NLS-1$

        newGameButton.setToolTipText(Messages.getString("Driver.startANewGame")); //$NON-NLS-1$
        try {
            newGameButton.setIcon(GuiUtility.createImageIcon(30, 30, "/start_game.png")); //$NON-NLS-1$
        } catch (IOException ae) {
            ae.printStackTrace();
        }
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setPanel(new NewGamePanel());
            }
        });

        return newGameButton;
    }

    private JButton createContinueGameButton() {
        JButton continueGameButton = new JButton(Messages.getString("Driver.loadGame")); //$NON-NLS-1$
        continueGameButton.setToolTipText(Messages.getString("Driver.loadASavedGame")); //$NON-NLS-1$
        try {
            continueGameButton.setIcon(GuiUtility.createImageIcon(30, 30, "/saved_games.png")); //$NON-NLS-1$
        } catch (IOException e) {
            e.printStackTrace();
        }
        continueGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String[] files = FileUtility.getGamesInProgressFileArray();

                if (files.length == 0) {
                    JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.noSavedGames"), //$NON-NLS-1$
                            Messages.getString("Driver.noCompletedGames"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
                    return;
                }

                final JFrame poppedFrame = new JFrame(Messages.getString("Driver.loadSavedGame")); //$NON-NLS-1$
                poppedFrame.setSize(225, 200);
                poppedFrame.setResizable(false);
                poppedFrame.setLocationRelativeTo(Driver.this);
                GridBagConstraints constraints = new GridBagConstraints();

                final ChessPanel popupPanel = new ChessPanel();
                popupPanel.setLayout(new GridBagLayout());

                final JList gamesInProgressList = new JList(FileUtility.getGamesInProgressFileArray());
                final JScrollPane scrollPane = new JScrollPane(gamesInProgressList);
                scrollPane.setPreferredSize(new Dimension(200, 200));
                gamesInProgressList.setSelectedIndex(0);

                JButton nextButton = new JButton(Messages.getString("Driver.loadSavedGame")); //$NON-NLS-1$
                nextButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        FileInputStream fileInputStream;
                        ObjectInputStream objectInputStream;
                        Game gameToPlay;
                        try {
                            if (gamesInProgressList.getSelectedValue() == null) {
                                JOptionPane.showMessageDialog(Driver.getInstance(),
                                        Messages.getString("Driver.selectGame"), Messages.getString("Driver.error"), //$NON-NLS-1$ //$NON-NLS-2$
                                        JOptionPane.PLAIN_MESSAGE);
                                return;
                            }
                            fileInputStream = new FileInputStream(FileUtility.getGamesInProgressFile(gamesInProgressList
                                    .getSelectedValue().toString()));
                            objectInputStream = new ObjectInputStream(fileInputStream);
                            gameToPlay = (Game) objectInputStream.readObject();
                            GameController.setGame(gameToPlay);

                            // set the help menu info to be specific for game
                            // play
                            if (mOptionsMenu != null)
                                mOptionsMenu.setVisible(true);

                            m_playGameScreen = new PlayGamePanel();
                            setPanel(m_playGameScreen);
                            poppedFrame.dispose();
                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(Driver.getInstance(),
                                    Messages.getString("Driver.noValidSavedGames"), Messages.getString("Driver.invalidSavedGames"), //$NON-NLS-1$ //$NON-NLS-2$
                                    JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                });

                JButton cancelButton = new JButton(Messages.getString("Driver.cancel")); //$NON-NLS-1$
                GuiUtility.setupDoneButton(cancelButton, poppedFrame);

                JButton deleteButton = new JButton(Messages.getString("Driver.deleteSavedGame")); //$NON-NLS-1$
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        if (gamesInProgressList.getSelectedValue() != null) {
                            boolean didDeleteSuccessfully = FileUtility.getGamesInProgressFile(
                                    gamesInProgressList.getSelectedValue().toString()).delete();
                            if (!didDeleteSuccessfully) {
                                JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.savedGameNotDeleted"), //$NON-NLS-1$
                                        Messages.getString("Driver.error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
                            } else {
                                gamesInProgressList.setListData(FileUtility.getGamesInProgressFileArray());
                                gamesInProgressList.setSelectedIndex(0);
                                if (gamesInProgressList.getSelectedValue() == null) {
                                    JOptionPane.showMessageDialog(Driver.getInstance(), Messages
                                                    .getString("Driver.noMoreCompletedGames"), Messages.getString("Driver.noCompletedGames"), //$NON-NLS-1$ //$NON-NLS-2$
                                            JOptionPane.PLAIN_MESSAGE);
                                    poppedFrame.dispose();
                                }
                                scrollPane.getViewport().add(gamesInProgressList, null);
                            }
                        } else {
                            JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.noSaveFiles"), //$NON-NLS-1$
                                    Messages.getString("Driver.noSaveFileSelected"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
                        }
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
        });

        return continueGameButton;
    }

    private JButton createViewCompletedGamesButton() {

        JButton viewCompletedGameButton = new JButton(Messages.getString("Driver.completedGames")); //$NON-NLS-1$
        viewCompletedGameButton.setToolTipText(Messages.getString("Driver.reviewFinishedGame")); //$NON-NLS-1$
        try {
            viewCompletedGameButton.setIcon(GuiUtility.createImageIcon(30, 30, "/view_completed.png")); //$NON-NLS-1$
        } catch (IOException ae) {
            ae.printStackTrace();
        }
        viewCompletedGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    String[] files = FileUtility.getCompletedGamesFileArray();
                    if (files.length == 0) {
                        JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.noCompletedToDisplay"), //$NON-NLS-1$
                                Messages.getString("Driver.noCompleted"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
                        return;
                    }

                    final JFrame poppedFrame = new JFrame(Messages.getString("Driver.viewCompleted")); //$NON-NLS-1$
                    poppedFrame.setLayout(new GridBagLayout());
                    poppedFrame.setSize(225, 200);
                    poppedFrame.setResizable(false);
                    poppedFrame.setLocationRelativeTo(Driver.this);
                    GridBagConstraints constraints = new GridBagConstraints();

                    final JList completedGamesList = new JList(FileUtility.getCompletedGamesFileArray());
                    final JScrollPane scrollPane = new JScrollPane(completedGamesList);
                    scrollPane.setPreferredSize(new Dimension(200, 200));
                    completedGamesList.setSelectedIndex(0);

                    JButton nextButton = new JButton(Messages.getString("Driver.next")); //$NON-NLS-1$
                    nextButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent event) {
                            if (completedGamesList.getSelectedValue() == null) {
                                JOptionPane.showMessageDialog(Driver.getInstance(),
                                        Messages.getString("Driver.selectGame"), Messages.getString("Driver.error"), //$NON-NLS-1$ //$NON-NLS-2$
                                        JOptionPane.PLAIN_MESSAGE);
                                return;
                            }

                            //File file = FileUtility.getCompletedGamesFile(completedGamesList.getSelectedValue().toString());
                            //m_watchGameScreen = new WatchGamePanel(file);
                            setPanel(m_watchGameScreen);
                            mOtherPanel = m_watchGameScreen;
                            deactivateWindowListener();
                            poppedFrame.dispose();
                        }
                    });

                    JButton cancelButton = new JButton(Messages.getString("Driver.cancel")); //$NON-NLS-1$
                    GuiUtility.setupDoneButton(cancelButton, poppedFrame);

                    JButton deleteButton = new JButton(Messages.getString("Driver.deleteCompleted")); //$NON-NLS-1$
                    deleteButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent event) {
                            if (completedGamesList.getSelectedValue() != null) {
                                boolean didDeleteCompletedGameSuccessfully = FileUtility.getCompletedGamesFile(
                                        completedGamesList.getSelectedValue().toString()).delete();
                                if (!didDeleteCompletedGameSuccessfully) {
                                    JOptionPane.showMessageDialog(Driver.getInstance(),
                                            Messages.getString("Driver.completedNotDeleted"), //$NON-NLS-1$
                                            Messages.getString("Driver.error"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
                                } else {
                                    completedGamesList.removeAll();
                                    completedGamesList.setListData(FileUtility.getCompletedGamesFileArray());
                                    completedGamesList.setSelectedIndex(0);
                                    if (completedGamesList.getSelectedValue() == null) {
                                        JOptionPane.showMessageDialog(
                                                Driver.getInstance(),
                                                Messages.getString("Driver.noMoreCompleted"), Messages.getString("Driver.noCompleted"), //$NON-NLS-1$ //$NON-NLS-2$
                                                JOptionPane.PLAIN_MESSAGE);
                                        poppedFrame.dispose();
                                    }
                                    scrollPane.getViewport().add(completedGamesList, null);
                                }
                            } else {
                                JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("Driver.currentlyNoCompleted"), //$NON-NLS-1$
                                        Messages.getString("Driver.noCompletedSelected"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
                            }
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
                            Messages.getString("Driver.eitherNoCompletedOrGameMissing"), Messages.getString("Driver.noCompleted"), //$NON-NLS-1$ //$NON-NLS-2$
                            JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        return viewCompletedGameButton;
    }

    private JButton variantMenuButton() {
        JButton variantButton = new JButton(Messages.getString("Driver.variants")); //$NON-NLS-1$
        variantButton.setToolTipText(Messages.getString("Driver.createEditRemoveVariants")); //$NON-NLS-1$
        try {
            variantButton.setIcon(GuiUtility.createImageIcon(30, 30, "/chess_variants.jpg")); //$NON-NLS-1$
        } catch (IOException e) {
            e.printStackTrace();
        }
        variantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPanel(new VariantMenuPanel());
            }
        });

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

    public PlayGamePanel getPlayGameScreen() {
        if (m_playGameScreen == null)
            m_playGameScreen = new PlayGamePanel();
        return m_playGameScreen;
    }

    public WatchGamePanel getWatchGameScreen(File acnFile) {
        //if (m_watchGameScreen == null)
        //	m_watchGameScreen = new WatchGamePanel(acnFile);
        return m_watchGameScreen;
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
