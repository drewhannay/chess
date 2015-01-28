package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.panel.ChessPanel;
import com.drewhannay.chesscrafter.utility.AppConstants;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class AboutFrame extends ChessFrame {

    AboutFrame() {
    }

    @Override
    void doInitComponents() {
        super.doInitComponents();

        setTitle(Messages.getString("AboutFrame.about") + AppConstants.APP_NAME);

        setSize(350, 450);
        setResizable(false);

        GridBagConstraints constraints = new GridBagConstraints();

        ChessPanel aboutPanel = new ChessPanel();
        aboutPanel.setLayout(new GridBagLayout());

        JLabel frontPageImage = new JLabel(GuiUtility.createSystemImageIcon(250, 250, "/chess_logo.png"));
        JLabel piecePicture = new JLabel(GuiUtility.createSystemImageIcon(48, 48, "/d_King.png"));

        Font font = new Font(Messages.getString("AboutFrame.verdana"), Font.BOLD, 18);
        JLabel title = GuiUtility.createJLabel(AppConstants.APP_NAME + Messages.getString("AboutFrame.newline"));
        title.setFont(font);

        JLabel versionLabel = GuiUtility.createJLabel(Messages.getString("AboutFrame.version"));
        JLabel visitSiteLabel = GuiUtility.createJLabel(Messages.getString("AboutFrame.visitOurSite"));

        JButton siteButton = new JButton();
        siteButton.setIcon(piecePicture.getIcon());
        siteButton.addActionListener(event -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(AppConstants.PROJECT_URL));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        constraints.gridy = 0;
        constraints.insets = new Insets(10, 5, 10, 5);
        aboutPanel.add(title, constraints);
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        aboutPanel.add(frontPageImage, constraints);
        constraints.gridy = 2;
        constraints.insets = new Insets(10, 5, 5, 5);
        aboutPanel.add(versionLabel, constraints);
        constraints.gridy = 3;
        constraints.insets = new Insets(10, 5, 5, 5);
        aboutPanel.add(visitSiteLabel, constraints);
        constraints.gridy = 4;
        aboutPanel.add(siteButton, constraints);

        add(aboutPanel);
    }
}
