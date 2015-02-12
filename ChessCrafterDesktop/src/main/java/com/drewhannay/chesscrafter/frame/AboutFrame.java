package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.panel.ChessPanel;
import com.drewhannay.chesscrafter.utility.AppConstants;
import com.drewhannay.chesscrafter.utility.UiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URI;

public class AboutFrame extends ChessFrame {

    AboutFrame() {
    }

    @Override
    void doInitComponents() {
        super.doInitComponents();

        setTitle(Messages.getString("AboutFrame.about") + AppConstants.APP_NAME);

        setPreferredSize(new Dimension(250, 250));
        setResizable(false);

        GridBagConstraints constraints = new GridBagConstraints();

        ChessPanel aboutPanel = new ChessPanel();
        aboutPanel.setLayout(new GridBagLayout());

        Font font = new Font(Messages.getString("AboutFrame.verdana"), Font.BOLD, 18);
        JLabel title = UiUtility.createJLabel(AppConstants.APP_NAME + Messages.getString("AboutFrame.newline"));
        title.setFont(font);

        JLabel versionLabel = UiUtility.createJLabel(Messages.getString("AboutFrame.version"));
        JLabel visitSiteLabel = UiUtility.createJLabel(Messages.getString("AboutFrame.visitOurSite"));

        JButton siteButton = new JButton();
        siteButton.setIcon(PieceIconUtility.getPieceIcon(PieceTypeManager.KING_ID, Color.BLACK));
        siteButton.setPreferredSize(new Dimension(80, 80));
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
        constraints.insets = new Insets(10, 5, 5, 5);
        aboutPanel.add(title, constraints);
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 5, 5, 5);
        aboutPanel.add(versionLabel, constraints);
        constraints.gridy = 2;
        constraints.insets = new Insets(10, 5, 5, 5);
        aboutPanel.add(visitSiteLabel, constraints);
        constraints.gridy = 3;
        aboutPanel.add(siteButton, constraints);

        add(aboutPanel);
    }
}
