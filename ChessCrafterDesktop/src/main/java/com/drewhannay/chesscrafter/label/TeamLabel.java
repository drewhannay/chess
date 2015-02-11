package com.drewhannay.chesscrafter.label;

import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

public class TeamLabel extends JLabel {

    public TeamLabel() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);
        setMinimumSize(new Dimension(136, 20));
        setPreferredSize(new Dimension(136, 20));
        setMaximumSize(new Dimension(136, 20));
    }

    public void setDraw() {
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setText(Messages.getString("GamePanel.draw"));
    }

    public void setStalemate() {
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setText(Messages.getString("GamePanel.stalemate"));
    }

    public void setCheckmate() {
        setBackground(Color.RED);
        setForeground(Color.WHITE);
        setText(Messages.getString("GamePanel.checkmate"));
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
