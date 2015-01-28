package com.drewhannay.chesscrafter.frame;

import com.drewhannay.chesscrafter.panel.ChessPanel;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;

public class HelpFrame extends ChessFrame {

    HelpFrame() {
    }

    @Override
    void doInitComponents() {
        super.doInitComponents();

        setTitle(Messages.getString("HelpFrame.help"));

        ChessPanel helpPanel = new ChessPanel();
        helpPanel.setLayout(new BorderLayout());

        JPanel gamePlayHelpPanel = new JPanel();
        gamePlayHelpPanel.setOpaque(false);
        JScrollPane gamePlayHelpScrollPane = new JScrollPane();
        gamePlayHelpScrollPane.setOpaque(false);
        gamePlayHelpScrollPane.setViewportView(gamePlayHelpPanel);
        gamePlayHelpScrollPane.getViewport().setOpaque(false);

        JPanel variantMakingHelpPanel = new JPanel();
        variantMakingHelpPanel.setOpaque(false);
        JScrollPane variantMakingHelpScrollPane = new JScrollPane();
        variantMakingHelpScrollPane.setOpaque(false);
        variantMakingHelpScrollPane.setViewportView(variantMakingHelpPanel);
        variantMakingHelpScrollPane.getViewport().setOpaque(false);

        JPanel generalHelpPanel = new JPanel();
        generalHelpPanel.setOpaque(false);
        JScrollPane generalHelpScrollPane = new JScrollPane();
        generalHelpScrollPane.setOpaque(false);
        generalHelpScrollPane.setViewportView(generalHelpPanel);
        generalHelpScrollPane.getViewport().setOpaque(false);

        JPanel pieceMakingHelpPanel = new JPanel();
        pieceMakingHelpPanel.setOpaque(false);
        JScrollPane pieceMakingHelpScrollPane = new JScrollPane();
        pieceMakingHelpScrollPane.setOpaque(false);
        pieceMakingHelpScrollPane.setViewportView(pieceMakingHelpPanel);
        pieceMakingHelpScrollPane.getViewport().setOpaque(false);

        JTabbedPane helpTypesTabbedPane = new JTabbedPane();
        helpTypesTabbedPane.setOpaque(false);
        helpTypesTabbedPane.addTab(Messages.getString("HelpFrame.generalHelp"), null, generalHelpScrollPane);
        helpTypesTabbedPane.addTab(Messages.getString("HelpFrame.gamePlayHelp"), null, gamePlayHelpScrollPane);
        helpTypesTabbedPane.addTab(Messages.getString("HelpFrame.variantHelp"), null, variantMakingHelpScrollPane);
        helpTypesTabbedPane.addTab(Messages.getString("HelpFrame.pieceMakingHelp"), null, pieceMakingHelpScrollPane);

        helpPanel.add(helpTypesTabbedPane, BorderLayout.CENTER);

        JLabel gamePlayHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.gamePlayText"));
        gamePlayHelpPanel.add(gamePlayHelpText);

        JLabel generalHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.generalHelpText"));
        generalHelpPanel.add(generalHelpText);

        JLabel variantMakingHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.variantHelpText"));
        variantMakingHelpPanel.add(variantMakingHelpText);

        JLabel pieceMakingHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.pieceOptionsText"));
        pieceMakingHelpPanel.add(pieceMakingHelpText);

        add(helpPanel);
    }
}
