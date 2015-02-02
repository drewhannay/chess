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
    private JTextField[] mTeamColorFields;
    private int mTeamIDCounter;

    public TeamCreationPanel(SquareConfig squareConfig) {
        super(false);
        setLayout(new GridBagLayout());
        mTeamIDCounter = 1;
        mTeamInfos = new ArrayList<>(4);
        mTeamNameFields = new JTextField[4];
        mTeamColorFields = new JTextField[4];

        TeamInfo white = new TeamInfo();
        white.color = Color.WHITE;
        white.teamName = "White";
        white.teamId = mTeamIDCounter++;

        TeamInfo black = new TeamInfo();
        black.color = Color.BLACK;
        black.teamName = "Black";
        black.teamId = mTeamIDCounter++;

        mTeamInfos.add(white);
        mTeamInfos.add(black);
        initComponents(squareConfig);
    }

    public void setPieceType(PieceType pieceType) {
        mPieceType = pieceType;

        mTeamInfos.forEach(teamInfo -> teamInfo.label.setPiece(new Piece(teamInfo.teamId, mPieceType), teamInfo.color));
    }

    private void initComponents(SquareConfig squareConfig) {
        mTeamInfos.forEach(teamInfo -> {
            createTeamPanel(squareConfig, teamInfo);
        });
        JButton addTeam = new JButton(Messages.getString("TeamCreationPanel.addTeam"));
        addTeam.addActionListener(event -> {
            TeamInfo newTeam = new TeamInfo();
            newTeam.teamId = mTeamIDCounter;
            newTeam.color = Color.gray;
            newTeam.teamName = Messages.getString("TeamCreationPanel.team", mTeamIDCounter++);
            mTeamInfos.add(newTeam);
            createTeamPanel(squareConfig, newTeam);
            remove(addTeam);
            if (mTeamIDCounter <= 4) {
                add(addTeam);
            }
            validate();
            repaint();
        });
        add(addTeam);
    }

    private void createTeamPanel(SquareConfig squareConfig, TeamInfo teamInfo) {
        int offsetTeamId = teamInfo.teamId - 1;
        GridBagConstraints constraints = new GridBagConstraints();

        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BoxLayout(teamPanel, BoxLayout.PAGE_AXIS));
        teamPanel.setOpaque(false);

        teamPanel.add(GuiUtility.createJLabel(Messages.getString("TeamCreationPanel.team", teamInfo.teamId)));

        teamInfo.label = new SquareJLabel(BoardCoordinate.at(1, teamInfo.teamId));
        squareConfig.configureSquare(teamInfo.label);
        teamInfo.label.setMaximumSize(new Dimension(100, 100));

        teamPanel.add(teamInfo.label);

        JPanel teamNamePanel = new JPanel();
        teamNamePanel.setOpaque(false);
        teamNamePanel.setLayout(new BoxLayout(teamNamePanel, BoxLayout.LINE_AXIS));
        teamNamePanel.add(GuiUtility.createJLabel(Messages.getString("TeamCreationPanel.name")));

        Dimension minSize = new Dimension(5, 25);
        Dimension prefSize = new Dimension(5, 25);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 25);
        teamNamePanel.add(new Box.Filler(minSize, prefSize, maxSize));

        teamNamePanel.add(Box.createHorizontalGlue());

        mTeamNameFields[offsetTeamId] = new JTextField(20);
        mTeamNameFields[offsetTeamId].setText(teamInfo.teamName);
        mTeamNameFields[offsetTeamId].setPreferredSize(new Dimension(50, 25));
        teamNamePanel.add(mTeamNameFields[offsetTeamId]);

        teamPanel.add(new Box.Filler(new Dimension(5, 10), new Dimension(5, 10), new Dimension(Short.MAX_VALUE, 10)));
        teamPanel.add(teamNamePanel);

        JPanel teamColorPanel = new JPanel();
        teamColorPanel.setOpaque(false);
        teamColorPanel.setLayout(new BoxLayout(teamColorPanel, BoxLayout.LINE_AXIS));
        teamColorPanel.add(GuiUtility.createJLabel(Messages.getString("TeamCreationPanel.color")));

        teamColorPanel.add(new Box.Filler(minSize, prefSize, maxSize));
        teamColorPanel.add(Box.createHorizontalGlue());

        mTeamColorFields[offsetTeamId] = new JTextField(20);
        mTeamColorFields[offsetTeamId].setBackground(teamInfo.color);
        mTeamColorFields[offsetTeamId].setEditable(false);
        createMouseListener(mTeamColorFields[offsetTeamId], teamInfo);
        teamColorPanel.add(mTeamColorFields[offsetTeamId]);

        teamPanel.add(new Box.Filler(new Dimension(5, 10), new Dimension(5, 10), new Dimension(Short.MAX_VALUE, 10)));
        teamPanel.add(teamColorPanel);

        constraints.gridx = teamInfo.teamId;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(0, 25, 10, 25);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        add(teamPanel, constraints);
    }

    private void createMouseListener(JTextField colorField, TeamInfo teamInfo) {
        colorField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Color c = JColorChooser.showDialog(colorField, Messages.getString("TeamCreationPanel.teamColor"), colorField.getBackground());
                colorField.setBackground(c);
                teamInfo.color = c;
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
}
