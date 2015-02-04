package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.label.TeamLabel;
import com.drewhannay.chesscrafter.logic.Status;
import com.drewhannay.chesscrafter.models.Team;
import com.drewhannay.chesscrafter.utility.Messages;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public final class TeamStatusPanel extends ChessPanel {

    private final int mTeamId;
    private final String mTeamName;
    private final JTextField mPlayerName;
    private final TeamLabel mTeamLabel;
    private final JailPanel mJailPanel;

    public TeamStatusPanel(Team team) {
        super(false);

        mTeamId = team.getTeamId();
        mTeamName = team.getTeamName();
        mPlayerName = new JTextField(20);
        mTeamLabel = new TeamLabel();
        // TODO: figure out a way to get the total number of pieces in the game for each team
        mJailPanel = new JailPanel(16);

        initComponents();
    }

    @Override
    public String getName() {
        return mTeamName;
    }

    public JailPanel getJail() {
        return mJailPanel;
    }

    public int getTeamId() {
        return mTeamId;
    }

    public void updateStatus(int activeTeamId, Status status) {
        if (status == Status.DRAW) {
            mTeamLabel.setDraw();
        } else if (status == Status.STALEMATE) {
            mTeamLabel.setStalemate();
        } else if (mTeamId == activeTeamId) {
            if (Status.IN_CHECK_STATUS.contains(status)) {
                mTeamLabel.setInCheck();
            } else if (status == Status.CHECKMATE) {
                mTeamLabel.setCheckmate();
            } else {
                mTeamLabel.setActive();
            }
        } else {
            mTeamLabel.setInactive();
        }
    }

    private void initComponents() {
        GridBagConstraints constraints = new GridBagConstraints();

        setLayout(new GridBagLayout());

        JPanel teamMetaData = new JPanel();
        teamMetaData.setOpaque(false);
        teamMetaData.setLayout(new GridBagLayout());
        teamMetaData.setBorder(BorderFactory.createTitledBorder(Messages.getString("GamePanel.info")));

        GridBagConstraints teamConstraint = new GridBagConstraints();

        // add team name label
        teamConstraint.fill = GridBagConstraints.BOTH;
        teamConstraint.gridx = 0;
        teamConstraint.gridy = 0;
        //teamConstraint.weighty = 0.2;
        teamConstraint.insets = new Insets(5, 0, 5, 5);
        teamMetaData.add(new JLabel(Messages.getString("GamePanel.player")), teamConstraint);

        // add player name
        teamConstraint.weightx = 1.0;
        teamConstraint.gridx = 1;
        teamConstraint.insets = new Insets(5, 0, 5, 0);
        teamMetaData.add(mPlayerName, teamConstraint);

        // add status label
        teamConstraint.gridx = 0;
        teamConstraint.gridy = 1;
        teamConstraint.weightx = 0.0;
        teamMetaData.add(new JLabel(Messages.getString("GamePanel.status")), teamConstraint);

        // add player status
        teamConstraint.gridx = 1;
        teamConstraint.weightx = 1.0;
        teamMetaData.add(mTeamLabel, teamConstraint);

        // add the JailPanel
        teamConstraint.gridy = 2;
        teamConstraint.gridx = 0;
        teamConstraint.gridwidth = 2;
        teamConstraint.weighty = 0.1;
        teamConstraint.weightx = 1.0;
        teamMetaData.add(mJailPanel, teamConstraint);

        // add teamMetadata
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(10, 10, 10, 10);
        add(teamMetaData, constraints);


    }
}
