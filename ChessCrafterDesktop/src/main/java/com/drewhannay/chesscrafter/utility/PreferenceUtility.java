package com.drewhannay.chesscrafter.utility;

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
import java.io.File;
import java.util.prefs.Preferences;

public final class PreferenceUtility {

    private static final String SAVELOCATION = "saveLocation";
    private static final String HIGHLIGHTMOVES = "highlightMoves";
    private static final Preferences mPreference = Preferences.userRoot().node("ChessCrafterPreferences");

    private PreferenceUtility() {
    }

    public static void createPreferencePopup(Component relativeComponent) {
        JFrame popupFrame = new JFrame(Messages.getString("PreferenceUtility.preferences"));
        popupFrame.setSize(370, 120);
        popupFrame.setLocationRelativeTo(relativeComponent);
        popupFrame.setLayout(new GridBagLayout());
        popupFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        GridBagConstraints constraints = new GridBagConstraints();

        String userSaveLocation = PreferenceUtility.getSaveLocationPreference();
        String defaultSaveLocation = FileUtility.getDefaultCompletedLocation();

        JPanel holder = new JPanel();
        holder.setBorder(BorderFactory.createTitledBorder(Messages.getString("PreferenceUtility.defaultCompletedLocation")));
        JLabel currentSaveLocationLabel = new JLabel(Messages.getString("PreferenceUtility.currentSaveLocation"));
        JTextField currentSaveLocationField = new JTextField(FileUtility.getDefaultCompletedLocation());
        currentSaveLocationField.setEditable(false);
        int width = Math.max(userSaveLocation.length() + 15, 300);
        currentSaveLocationField.setPreferredSize(new Dimension(width, 25));
        JButton changeLocationButton = new JButton(Messages.getString("PreferenceUtility.chooseNewSaveLocation"));
        JButton resetButton = new JButton(Messages.getString("PreferenceUtility.resetToDefaultLocation"));
        JCheckBox highlightingCheckBox = new JCheckBox(Messages.getString("PreferenceUtility.enableHighlighting"));

        JButton closeButton = new JButton(Messages.getString("PreferenceUtility.close"));
        GuiUtility.setupDoneButton(closeButton, popupFrame);

        holder.add(currentSaveLocationLabel);
        holder.add(currentSaveLocationField);

        currentSaveLocationField.setText(mPreference.get(SAVELOCATION, "default"));
        highlightingCheckBox.setSelected(mPreference.getBoolean(HIGHLIGHTMOVES, true));

        resetButton.addActionListener(event -> {
            currentSaveLocationField.setText(FileUtility.getDefaultCompletedLocation());
            PreferenceUtility.setSaveLocationPreference(defaultSaveLocation);
        });

        changeLocationButton.addActionListener(event -> {
            File directory = FileUtility.chooseDirectory();
            if (directory != null) {
                currentSaveLocationField.setText(directory.getAbsolutePath());
                PreferenceUtility.setSaveLocationPreference(directory.getAbsolutePath());
            }
        });

        highlightingCheckBox.addActionListener(event -> mPreference.putBoolean(HIGHLIGHTMOVES, highlightingCheckBox.isSelected()));

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
     * Generic method to get string preference from the system
     *
     * @param key the String key for the preference to return
     * @return returns the value paired with the given key
     */
    public static String getStringPreference(String key) {
        return mPreference.get(key, "default");
    }

    /**
     * Generic method to get a boolean preference from the system
     *
     * @param key the String key for the preference to return
     * @return returns the boolean value matched with the given key
     */
    public static Boolean getBooleanPreference(String key) {
        return mPreference.getBoolean(key, true);
    }

    /**
     * Method to get the preference for if moves should be Highlighted
     *
     * @return returns the boolean value for move highlighting
     */
    public static boolean getHighlightMovesPreference() {
        return mPreference.getBoolean(HIGHLIGHTMOVES, true);
    }

    /**
     * Method to get the preference for Save game location
     *
     * @return returns the string value for the save game location
     */
    public static String getSaveLocationPreference() {
        return mPreference.get(SAVELOCATION, "default");
    }

    /**
     * Method to set the preference for Save game location
     */
    public static void setSaveLocationPreference(String location) {
        mPreference.put(SAVELOCATION, location);
    }
}
