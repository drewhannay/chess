package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.action.ChessActions;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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

        JLabel underline = new JLabel(Messages.getString("HelpPanel.underline"));
        underline.setForeground(Color.white);
        c.anchor = GridBagConstraints.SOUTH;
        add(underline, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridy = 2;
        add(createJButton(Messages.getString("HelpPanel.newGameInstructions"), ChessActions.NEW_GAME), c);

        c.gridy = 3;
        add(createJButton(Messages.getString("HelpPanel.savedGameInstructions"), ChessActions.OPEN_GAME), c);

        c.gridy = 4;
        add(createJButton(Messages.getString("HelpPanel.pieceCrafterInstructions"), ChessActions.PIECE_CRAFTER), c);

        c.gridy = 5;
        add(createJButton(Messages.getString("HelpPanel.gameCrafterInstructions"), ChessActions.GAME_CRAFTER), c);

    }

    private JButton createJButton(String buttonText, ChessActions action){
        JButton newButton = new JButton(action.getAction());
        newButton.setForeground(Color.white);
        newButton.setFont(new Font(newButton.getName(), Font.PLAIN, 14));
        newButton.setFocusPainted(false);
        newButton.setMargin(new Insets(0, 0, 0, 0));
        newButton.setContentAreaFilled(false);
        newButton.setBorderPainted(false);
        newButton.setText(buttonText);
        setOpaque(false);
        return newButton;
    }
}
