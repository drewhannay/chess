package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import java.awt.*;

public class TeamLabel extends JLabel {

    private final int mTeamID;
    private final String mTeamName;

    public TeamLabel(int teamID, String teamName) {
        mTeamName = teamName;
        mTeamID = teamID;

        setText(mTeamName);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$
        setOpaque(true);
    }

    public int getTeamId() {
        return mTeamID;
    }

    public void setInCheck() {
        setBackground(Color.RED);
        setForeground(Color.WHITE);
        setText(mTeamName + " " + Messages.getString("PlayGamePanel.inCheck"));
    }

    public void setActive() {
        setBackground(Color.CYAN);
        setForeground(Color.BLACK);
    }

    public void setInActive() {
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setText(mTeamName);
    }
}
