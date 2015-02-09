package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.Main;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.prefs.Preferences;

public final class PreferenceUtility {

    private static final String HIGHLIGHT_MOVES = "highlightMoves";

    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(Main.class);

    private PreferenceUtility() {
    }

    public static void createPreferencePopup(Component relativeComponent) {
        JFrame popupFrame = new JFrame(Messages.getString("PreferenceUtility.preferences"));
        popupFrame.setSize(370, 120);
        popupFrame.setLocationRelativeTo(relativeComponent);
        popupFrame.setLayout(new GridBagLayout());
        popupFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        GridBagConstraints constraints = new GridBagConstraints();

        String userSaveLocation = "Delete Me!";

        JPanel holder = new JPanel();
        holder.setBorder(BorderFactory.createTitledBorder(Messages.getString("PreferenceUtility.defaultCompletedLocation")));
        JLabel currentSaveLocationLabel = new JLabel(Messages.getString("PreferenceUtility.currentSaveLocation"));
        JTextField currentSaveLocationField = new JTextField("Delete Me!");
        currentSaveLocationField.setEditable(false);
        int width = Math.max(userSaveLocation.length() + 15, 300);
        currentSaveLocationField.setPreferredSize(new Dimension(width, 25));
        JButton changeLocationButton = new JButton(Messages.getString("PreferenceUtility.chooseNewSaveLocation"));
        JButton resetButton = new JButton(Messages.getString("PreferenceUtility.resetToDefaultLocation"));
        JCheckBox highlightingCheckBox = new JCheckBox(Messages.getString("PreferenceUtility.enableHighlighting"));

        JButton closeButton = new JButton(Messages.getString("PreferenceUtility.close"));
        closeButton.addActionListener(event1 -> popupFrame.dispose());

        holder.add(currentSaveLocationLabel);
        holder.add(currentSaveLocationField);

        currentSaveLocationField.setText(userSaveLocation);
        highlightingCheckBox.setSelected(getHighlightMovesPreference());

        resetButton.addActionListener(event -> {
            currentSaveLocationField.setText("Delete Me");
        });

        changeLocationButton.addActionListener(event -> {
            // TODO: delete me
        });

        highlightingCheckBox.addActionListener(event -> setHighlightMovesPreference(highlightingCheckBox.isSelected()));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(5, 5, 5, 5);
        popupFrame.add(holder, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.EAST;
        popupFrame.add(changeLocationButton, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        popupFrame.add(resetButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        popupFrame.add(highlightingCheckBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        popupFrame.add(closeButton, constraints);

        popupFrame.pack();
        popupFrame.setVisible(true);
    }

    /**
     * Method to get the preference for if moves should be Highlighted
     *
     * @return returns the boolean value for move highlighting
     */
    public static boolean getHighlightMovesPreference() {
        return PREFERENCES.getBoolean(HIGHLIGHT_MOVES, true);
    }

    private static void setHighlightMovesPreference(boolean highlightMoves) {
        PREFERENCES.putBoolean(HIGHLIGHT_MOVES, highlightMoves);
    }
}
