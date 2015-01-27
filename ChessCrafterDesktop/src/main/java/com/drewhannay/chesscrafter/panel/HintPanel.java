package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;

public final class HintPanel extends ChessPanel {

    public HintPanel() {
        setLayout(new GridBagLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        // home screen image
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel(GuiUtility.createSystemImageIcon(400, 400, "/chess_logo.png")), c);

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
        add(buttonPanel, c);
    }

    private JButton createPieceCreatorButton() {
        JButton pieceButton = new JButton(Messages.getString("Driver.pieces"));
        pieceButton.setToolTipText(Messages.getString("Driver.createEditRemove"));
        pieceButton.setIcon(GuiUtility.createSystemImageIcon(10, 30, "/pieces.png"));
        // TODO:
//        pieceButton.addActionListener(e -> setPanel(new PieceMenuPanel()));

        return pieceButton;
    }

    private JButton createNewGameButton() {
        JButton newGameButton = new JButton(ChessActions.NEW_GAME.getAction());
        newGameButton.setIcon(GuiUtility.createSystemImageIcon(30, 30, "/start_game.png"));
        return newGameButton;
    }

    private JButton createContinueGameButton() {
        JButton continueGameButton = new JButton(ChessActions.OPEN_GAME.getAction());
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
                    JOptionPane.showMessageDialog(this, Messages.getString("Driver.noCompletedToDisplay"),
                            Messages.getString("Driver.noCompleted"), JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                final JFrame poppedFrame = new JFrame(Messages.getString("Driver.viewCompleted"));
                poppedFrame.setLayout(new GridBagLayout());
                poppedFrame.setSize(225, 200);
                poppedFrame.setResizable(false);
                poppedFrame.setLocationRelativeTo(this);
                GridBagConstraints constraints = new GridBagConstraints();

                final JList<String> completedGamesList = new JList<>(FileUtility.getCompletedGamesFileArray());
                final JScrollPane scrollPane = new JScrollPane(completedGamesList);
                scrollPane.setPreferredSize(new Dimension(200, 200));
                completedGamesList.setSelectedIndex(0);

                JButton nextButton = new JButton(Messages.getString("Driver.next"));
                nextButton.addActionListener(event1 -> {
                    if (completedGamesList.getSelectedValue() == null) {
                        JOptionPane.showMessageDialog(this, Messages.getString("Driver.selectGame"), Messages.getString("Driver.error"), JOptionPane.PLAIN_MESSAGE);
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
                            JOptionPane.showMessageDialog(this, Messages.getString("Driver.completedNotDeleted"),
                                    Messages.getString("Driver.error"), JOptionPane.PLAIN_MESSAGE);
                        } else {
                            completedGamesList.removeAll();
                            completedGamesList.setListData(FileUtility.getCompletedGamesFileArray());
                            completedGamesList.setSelectedIndex(0);
                            if (completedGamesList.getSelectedValue() == null) {
                                JOptionPane.showMessageDialog(
                                        this,
                                        Messages.getString("Driver.noMoreCompleted"), Messages.getString("Driver.noCompleted"),
                                        JOptionPane.PLAIN_MESSAGE);
                                poppedFrame.dispose();
                            }
                            scrollPane.getViewport().add(completedGamesList, null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, Messages.getString("Driver.currentlyNoCompleted"),
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
                JOptionPane.showMessageDialog(this,
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
        // TODO:
//        variantButton.addActionListener(e -> setPanel(new VariantMenuPanel()));

        return variantButton;
    }
}
