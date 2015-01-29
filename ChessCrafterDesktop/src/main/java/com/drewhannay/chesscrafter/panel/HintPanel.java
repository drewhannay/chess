package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public final class HintPanel extends ChessPanel {

    public HintPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        JLabel pieceImage = new JLabel();
        pieceImage.setIcon(PieceIconUtility.getPieceIcon("King", 125, 2));

        c.gridx = 0;
        c.gridy = 0;
        //c.gridwidth = 3;
        //c.gridheight = 2;
        c.anchor = GridBagConstraints.CENTER;
        add(pieceImage, c);

        JLabel gettingStarted = new JLabel(Messages.getString("HelpPanel.gettingStarted"));
        gettingStarted.setForeground(Color.white);
        gettingStarted.setFont(new Font(gettingStarted.getName(), Font.PLAIN, 26));
        c.gridy = 1;
        add(gettingStarted, c);

        JLabel underline = createJLabel(Messages.getString("HelpPanel.underline"));
        c.anchor = GridBagConstraints.SOUTH;
        add(underline, c);

        JLabel newGameInstructions = createJLabel(Messages.getString("HelpPanel.newGameInstructions"));
        c.anchor = GridBagConstraints.WEST;
        c.gridy = 2;
        add(newGameInstructions, c);

        JLabel savedGameInstructions = createJLabel(Messages.getString("HelpPanel.savedGameInstructions"));
        c.gridy = 3;
        add(savedGameInstructions, c);

        JLabel newVariantInstructions = createJLabel(Messages.getString("HelpPanel.newVariantInstructions"));
        c.gridy = 4;
        add(newVariantInstructions, c);

        JLabel modifyVariantInstructions = createJLabel(Messages.getString("HelpPanel.modifyVariantInstructions"));
        c.gridy = 5;
        add(modifyVariantInstructions, c);

    }

    private JLabel createJLabel(String labelText){
        JLabel newLabel = new JLabel(labelText);
        newLabel.setForeground(Color.white);
        newLabel.setFont(new Font(newLabel.getName(), Font.PLAIN, 14));
        newLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        return newLabel;
    }
}
