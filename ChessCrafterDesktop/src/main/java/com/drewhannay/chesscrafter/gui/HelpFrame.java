package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;

public class HelpFrame extends JFrame {
    public HelpFrame() {
        initGUIComponents();
    }

    private void initGUIComponents() {
        setTitle(Messages.getString("HelpFrame.help"));
        // setSize(825, 525);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        mHelpPanel = new ChessPanel();
        mHelpPanel.setLayout(new BorderLayout());

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
        helpTypesTabbedPane
                .addTab(Messages.getString("HelpFrame.generalHelp"), null, generalHelpScrollPane);
        helpTypesTabbedPane
                .addTab(Messages.getString("HelpFrame.gamePlayHelp"), null, gamePlayHelpScrollPane);
        helpTypesTabbedPane
                .addTab(Messages.getString("HelpFrame.variantHelp"), null, variantMakingHelpScrollPane);
        helpTypesTabbedPane
                .addTab(Messages.getString("HelpFrame.pieceMakingHelp"), null, pieceMakingHelpScrollPane);
        helpTypesTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        mHelpPanel.add(helpTypesTabbedPane, BorderLayout.CENTER);

        JLabel gamePlayHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.gamePlayText"));
        gamePlayHelpPanel.add(gamePlayHelpText);

        JLabel generalHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.generalHelpText"));
        generalHelpPanel.add(generalHelpText);

        JLabel variantMakingHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.variantHelpText"));
        variantMakingHelpPanel.add(variantMakingHelpText);

        JLabel pieceMakingHelpText = GuiUtility.createJLabel(Messages.getString("HelpFrame.pieceOptionsText"));
        pieceMakingHelpPanel.add(pieceMakingHelpText);

        add(mHelpPanel);

        pack();

        setVisible(true);
    }

    private static final long serialVersionUID = -3375921014569944071L;
    private ChessPanel mHelpPanel;
}
