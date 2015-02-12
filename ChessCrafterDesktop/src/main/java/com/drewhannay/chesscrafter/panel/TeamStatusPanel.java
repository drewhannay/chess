package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.label.TeamLabel;
import com.drewhannay.chesscrafter.logic.Status;
import com.drewhannay.chesscrafter.models.Team;
import com.drewhannay.chesscrafter.utility.Messages;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

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
        setLayout(new MigLayout("", "[100:pref,fill]", "[pref!]"));

        JPanel jailHolderPanel = new JPanel();
        jailHolderPanel.setOpaque(false);
        jailHolderPanel.add(mJailPanel);

        jailHolderPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                mJailPanel.updateDimensions(e.getComponent().getWidth(), e.getComponent().getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        JPanel statusPanel = new JPanel();
        statusPanel.setOpaque(false);
        statusPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // add team name label
        gbc.insets = new Insets(0,0,5,0);
        statusPanel.add(new JLabel(Messages.getString("GamePanel.player")), gbc);
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        gbc.insets = new Insets(0,5,5,0);
        statusPanel.add(mPlayerName, gbc);

        // add status label
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,0,0,0);
        statusPanel.add(new JLabel(Messages.getString("GamePanel.status")), gbc);
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        gbc.insets = new Insets(0,5,0,0);
        statusPanel.add(mTeamLabel, gbc);

        // add teamMetadata
        add(statusPanel, "cell 0 0");
        add(jailHolderPanel, "cell 0 1, grow");
    }
}
