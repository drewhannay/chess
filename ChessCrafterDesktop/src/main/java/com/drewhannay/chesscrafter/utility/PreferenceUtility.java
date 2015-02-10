package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.Main;
import com.drewhannay.chesscrafter.dialog.ChessDialog;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.WindowConstants;
import java.awt.Component;
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
        ChessDialog preferenceDialog = new ChessDialog(true);
        preferenceDialog.setSize(250, 120);
        preferenceDialog.setLocationRelativeTo(relativeComponent);
        preferenceDialog.setLayout(new GridBagLayout());
        preferenceDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        preferenceDialog.setTitle(Messages.getString("PreferenceUtility.preferences"));
        GridBagConstraints constraints = new GridBagConstraints();

        JCheckBox highlightingCheckBox = new JCheckBox(Messages.getString("PreferenceUtility.enableHighlighting"));

        JButton closeButton = new JButton(Messages.getString("PreferenceUtility.close"));
        closeButton.addActionListener(event1 -> preferenceDialog.dispose());

        highlightingCheckBox.setSelected(getHighlightMovesPreference());
        highlightingCheckBox.addActionListener(event -> setHighlightMovesPreference(highlightingCheckBox.isSelected()));

        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        preferenceDialog.add(highlightingCheckBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        preferenceDialog.add(closeButton, constraints);

        preferenceDialog.setVisible(true);
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
