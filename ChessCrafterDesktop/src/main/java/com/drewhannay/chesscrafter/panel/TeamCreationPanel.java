package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class TeamCreationPanel extends ChessPanel {

    static class TeamInfo {
        int teamId;
        Color color;
        String teamName;

        SquareJLabel label;
    }

    private final List<TeamInfo> mTeamInfos;

    private PieceType mPieceType;
    private JTextField[] mTeamNameFields;
    private JLabel[] mTeamColorFields;

    public TeamCreationPanel(SquareConfig squareConfig) {
        super(false);
        setLayout(new GridBagLayout());
        int teamIDCounter = 1;
        mTeamInfos = new ArrayList<>(2);
        mTeamNameFields = new JTextField[2];
        mTeamColorFields = new JLabel[2];

        TeamInfo white = new TeamInfo();
        white.color = Color.WHITE;
        white.teamName = "White";
        white.teamId = teamIDCounter++;

        TeamInfo black = new TeamInfo();
        black.color = Color.BLACK;
        black.teamName = "Black";
        black.teamId = teamIDCounter;

        mTeamInfos.add(white);
        mTeamInfos.add(black);

        initComponents(squareConfig);
    }

    public void setPieceType(PieceType pieceType) {
        mPieceType = pieceType;

        mTeamInfos.forEach(teamInfo -> teamInfo.label.setPiece(new Piece(teamInfo.teamId, mPieceType), teamInfo.color));
    }

    private void createMouseListener(JLabel colorField, TeamInfo teamInfo){
        colorField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Color c = JColorChooser.showDialog(colorField, Messages.getString("TeamCreationPanel.teamColor"), colorField.getBackground());
                colorField.setBackground(c);
                teamInfo.color = c;
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
    }

    private void initComponents(SquareConfig squareConfig) {
        mTeamInfos.forEach(teamInfo -> {
            int offssetTeamId = teamInfo.teamId - 1;
            GridBagConstraints constraints = new GridBagConstraints();

            JPanel teamPanel = new JPanel();
            teamPanel.setLayout(new GridBagLayout());
            teamPanel.setOpaque(false);

            constraints.gridy = 0;
            constraints.insets = new Insets(0,0,10,0);
            constraints.gridwidth = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1.0;
            teamPanel.add(GuiUtility.createJLabel(Messages.getString("TeamCreationPanel.team") + teamInfo.teamId), constraints);

            teamInfo.label = new SquareJLabel(BoardCoordinate.at(1, teamInfo.teamId));
            squareConfig.configureSquare(teamInfo.label);
            constraints.gridy = 1;
            constraints.weightx = 0.5;
            constraints.weighty = 1.0;
            constraints.insets = new Insets(0,0,10,0);
            teamPanel.add(teamInfo.label, constraints);

            constraints.gridy = 2;
            constraints.gridwidth = 1;
            constraints.weighty = 0.0;
            constraints.weightx = 0.0;
            constraints.insets = new Insets(0,0,10,10);
            teamPanel.add(GuiUtility.createJLabel(Messages.getString("TeamCreationPanel.name")), constraints);

            constraints.gridy = 2;
            constraints.gridx = 1;
            constraints.weightx = 1.0;
            constraints.insets = new Insets(0,0,10,0);
            mTeamNameFields[offssetTeamId] = new JTextField(20);
            mTeamNameFields[offssetTeamId].setText(Messages.getString("TeamCreationPanel.myTeam"));
            mTeamNameFields[offssetTeamId].setPreferredSize(new Dimension(50,25));
            constraints.anchor = GridBagConstraints.WEST;
            teamPanel.add(mTeamNameFields[offssetTeamId], constraints);

            constraints.gridy = 3;
            constraints.gridx = 0;
            constraints.weightx = 0.0;
            constraints.insets = new Insets(0,0,10,10);
            constraints.anchor = GridBagConstraints.CENTER;
            teamPanel.add(GuiUtility.createJLabel(Messages.getString("TeamCreationPanel.color")), constraints);

            constraints.gridy = 3;
            constraints.gridx = 1;
            constraints.weightx = 1.0;
            constraints.insets = new Insets(0,0,10,0);
            constraints.anchor = GridBagConstraints.WEST;
            mTeamColorFields[offssetTeamId] = new JLabel();
            mTeamColorFields[offssetTeamId].setBackground(teamInfo.color);
            mTeamColorFields[offssetTeamId].setOpaque(true);
            createMouseListener(mTeamColorFields[offssetTeamId], teamInfo);
            teamPanel.add(mTeamColorFields[offssetTeamId], constraints);

            constraints.gridx = teamInfo.teamId;
            constraints.gridy = 0;
            constraints.insets = new Insets(0,25,0,25);
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.BOTH;
            add(teamPanel, constraints);
        });
    }
}
