package com.drewhannay.chesscrafter.label;

import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;

public class TeamLabel extends JLabel {

    public TeamLabel(int teamID) {
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createTitledBorder(""));
        setOpaque(true);
    }

    public void setInCheck() {
        setBackground(Color.RED);
        setForeground(Color.WHITE);
        setText(Messages.getString("GamePanel.inCheck"));
    }

    public void setActive() {
        setBackground(Color.CYAN);
        setForeground(Color.BLACK);
        setText(Messages.getString("GamePanel.myTurn"));
    }

    public void setInactive() {
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setText(Messages.getString("GamePanel.waiting"));
    }
}
