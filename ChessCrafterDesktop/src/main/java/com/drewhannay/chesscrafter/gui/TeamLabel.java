package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.logic.Result;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TeamLabel extends JLabel {

    public TeamLabel(int teamID, String teamName){
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$
        setOpaque(true);
        mTeamName = teamName;
        mTeamID = teamID;
        setText(mTeamName);
    }

    public void changeTurns(Result result, Boolean isActive) {
        if(result == Result.CHECKMATE) {
            PlayGamePanel.endOfGame(result);
        }
        if (result == Result.CHECK && isActive) {
            setBackground(Color.RED);
            setForeground(Color.WHITE);
            setText(mTeamName + " " + Messages.getString("PlayGamePanel.inCheck"));
        } else if(isActive){
            setBackground(Color.CYAN);
            setForeground(Color.BLACK);
        }
        else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            setText(mTeamName);
        }
    }

    private String mTeamName;
    private int mTeamID;
}
