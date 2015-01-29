package com.drewhannay.chesscrafter.utility;

import com.google.common.collect.Lists;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;

public final class PreferenceUtility {
    private PreferenceUtility() {
    }

    public interface PieceToolTipPreferenceChangedListener {
        public void onPieceToolTipPreferenceChanged();
    }

    public static void createPreferencePopup(Component relativeComponent) {
        JFrame popupFrame = new JFrame(Messages.getString("PreferenceUtility.preferences"));
        popupFrame.setSize(370, 120);
        popupFrame.setLocationRelativeTo(relativeComponent);
        popupFrame.setLayout(new GridBagLayout());
        popupFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        GridBagConstraints constraints = new GridBagConstraints();

        JPanel holder = new JPanel();
        holder.setBorder(BorderFactory.createTitledBorder(Messages.getString("PreferenceUtility.defaultCompletedLocation")));
        JLabel currentSaveLocationLabel = new JLabel(Messages.getString("PreferenceUtility.currentSaveLocation"));
        JTextField currentSaveLocationField = new JTextField(FileUtility.getDefaultCompletedLocation());
        currentSaveLocationField.setEditable(false);
        JButton changeLocationButton = new JButton(Messages.getString("PreferenceUtility.chooseNewSaveLocation"));
        JButton resetButton = new JButton(Messages.getString("PreferenceUtility.resetToDefaultLocation"));
        JCheckBox highlightingCheckBox = new JCheckBox(Messages.getString("PreferenceUtility.enableHighlighting"));
        JCheckBox pieceToolTipCheckBox = new JCheckBox(Messages.getString("PreferenceUtility.showPieceTooltips"));

        JButton cancelButton = new JButton(Messages.getString("PreferenceUtility.cancel"));

        JButton doneButton = new JButton(Messages.getString("PreferenceUtility.done"));
        GuiUtility.setupDoneButton(doneButton, popupFrame);

        holder.add(currentSaveLocationLabel);
        holder.add(currentSaveLocationField);

        String defaultSaveLocation = FileUtility.getDefaultCompletedLocation();

        currentSaveLocationField.setText(mPreference.get(SAVELOCATION, "default"));
        highlightingCheckBox.setSelected(mPreference.getBoolean(HIGHLIGHTMOVES, true));
        pieceToolTipCheckBox.setSelected(mPreference.getBoolean(PIECETOOLTIPS, false));

        resetButton.addActionListener(event -> currentSaveLocationField.setText(defaultSaveLocation));

        changeLocationButton.addActionListener(event -> {
            File directory = FileUtility.chooseDirectory(null);
            if (directory != null) {
                currentSaveLocationField.setText(directory.getAbsolutePath());
            }
        });

        doneButton.addActionListener(e -> {
            mPreference.put(SAVELOCATION, currentSaveLocationField.getText());
            mPreference.putBoolean(HIGHLIGHTMOVES, highlightingCheckBox.isSelected());
            mPreference.putBoolean(PIECETOOLTIPS, pieceToolTipCheckBox.isSelected());
        });

        GuiUtility.setupDoneButton(cancelButton, popupFrame);

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

        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        popupFrame.add(pieceToolTipCheckBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        popupFrame.add(cancelButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        popupFrame.add(doneButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;

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
     * Method to get the preference for if Pieces should display a tooltip
     *
     * @return returns the boolean value for piece highlighting
     */
    public static Boolean getPieceToolTipPreference() {
        return mPreference.getBoolean(PIECETOOLTIPS, true);
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

    public static void addPieceToolTipListener(PieceToolTipPreferenceChangedListener listener) {
        if (mToolTipListeners == null)
            mToolTipListeners = Lists.newArrayList();
        if (!mToolTipListeners.contains(listener))
            mToolTipListeners.add(listener);
    }

    public static void clearTooltipListeners() {
        if (mToolTipListeners != null)
            mToolTipListeners.clear();
    }

    private static List<PieceToolTipPreferenceChangedListener> mToolTipListeners;
    private static final String SAVELOCATION = "saveLocation";
    private static final String HIGHLIGHTMOVES = "highlightMoves";
    private static final String PIECETOOLTIPS = "pieceToolTips";
    private static final Preferences mPreference = Preferences.userRoot().node("ChessCrafterPreferences");
}
