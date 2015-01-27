package com.drewhannay.chesscrafter.dialog;

import com.drewhannay.chesscrafter.frame.GameFrame;
import com.drewhannay.chesscrafter.logic.GameBuilder;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;

public class NewGameDialog extends JDialog {

    private final GameFrame mGameFrame;

    public NewGameDialog(GameFrame owner) {
        super(owner, true);

        mGameFrame = owner;

        setSize(400, 400);
        initGuiComponents();
        setLocationRelativeTo(owner);
        getContentPane().setBackground(new Color(21, 0, 255, 100));
        setVisible(true);
    }

    private void initGuiComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.insets = new Insets(5, 50, 5, 50);
        constraints.anchor = GridBagConstraints.CENTER;
        add(GuiUtility.createJLabel(Messages.getString("NewGamePanel.howToPlay")), constraints);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);

        JButton humanPlayButton = new JButton(Messages.getString("NewGamePanel.humanPlay"));
        humanPlayButton.addActionListener(event -> {
            Game game = GameBuilder.buildGame(GameBuilder.getClassicConfiguration());
            mGameFrame.addGame(game);
            dispose();
        });
        constraints.gridy = 1;
        constraints.ipadx = 7;
        constraints.insets = new Insets(5, 5, 0, 5);
        buttonPanel.add(humanPlayButton, constraints);

        constraints.gridy = 1;
        add(buttonPanel, constraints);
    }
}
