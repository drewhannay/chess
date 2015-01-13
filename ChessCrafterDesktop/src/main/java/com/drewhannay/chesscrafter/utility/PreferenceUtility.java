package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.gui.Driver;
import com.google.common.collect.Lists;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.prefs.Preferences;

public final class PreferenceUtility {
    private PreferenceUtility() {
    }

    public interface PieceToolTipPreferenceChangedListener {
        public void onPieceToolTipPreferenceChanged();
    }

    public static void createPreferencePopup(Component relativeComponent) {
        final JFrame popupFrame = new JFrame(Messages.getString("PreferenceUtility.preferences")); //$NON-NLS-1$
        popupFrame.setSize(370, 120);
        popupFrame.setLocationRelativeTo(relativeComponent);
        popupFrame.setLayout(new GridBagLayout());
        popupFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        GridBagConstraints constraints = new GridBagConstraints();

        final JPanel holder = new JPanel();
        holder.setBorder(BorderFactory.createTitledBorder(Messages.getString("PreferenceUtility.defaultCompletedLocation"))); //$NON-NLS-1$
        final JLabel currentSaveLocationLabel = new JLabel(Messages.getString("PreferenceUtility.currentSaveLocation")); //$NON-NLS-1$
        final JTextField currentSaveLocationField = new JTextField(FileUtility.getDefaultCompletedLocation());
        currentSaveLocationField.setEditable(false);
        final JButton changeLocationButton = new JButton(Messages.getString("PreferenceUtility.chooseNewSaveLocation")); //$NON-NLS-1$
        final JButton resetButton = new JButton(Messages.getString("PreferenceUtility.resetToDefaultLocation")); //$NON-NLS-1$
        final JCheckBox highlightingCheckBox = new JCheckBox(Messages.getString("PreferenceUtility.enableHighlighting")); //$NON-NLS-1$
        final JCheckBox pieceToolTipCheckBox = new JCheckBox(Messages.getString("PreferenceUtility.showPieceTooltips")); //$NON-NLS-1$

        final JButton cancelButton = new JButton(Messages.getString("PreferenceUtility.cancel")); //$NON-NLS-1$

        final JButton doneButton = new JButton(Messages.getString("PreferenceUtility.done")); //$NON-NLS-1$
        GuiUtility.setupDoneButton(doneButton, popupFrame);

        holder.add(currentSaveLocationLabel);
        holder.add(currentSaveLocationField);

        final String defaultSaveLocation = FileUtility.getDefaultCompletedLocation();

        currentSaveLocationField.setText(mPreference.get(SAVELOCATION, "default"));
        highlightingCheckBox.setSelected(mPreference.getBoolean(HIGHLIGHTMOVES, true));
        pieceToolTipCheckBox.setSelected(mPreference.getBoolean(PIECETOOLTIPS, false));

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                currentSaveLocationField.setText(defaultSaveLocation);
            }
        });

        changeLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
                    if (returnVal == JFileChooser.APPROVE_OPTION)
                        currentSaveLocationField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    else
                        return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        doneButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mPreference.put(SAVELOCATION, currentSaveLocationField.getText());
                mPreference.putBoolean(HIGHLIGHTMOVES, highlightingCheckBox.isSelected());
                mPreference.putBoolean(PIECETOOLTIPS, pieceToolTipCheckBox.isSelected());
            }
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
     * @param key the String key for the preference to return
     * @return returns the value paired with the given key
     */
    public static String getStringPreference(String key) {
        return mPreference.get(key, "default");
    }

    /**
     * Generic method to get a boolean preference from the system
     * @param key the String key for the preference to return
     * @return returns the boolean value matched with the given key
     */
    public static Boolean getBooleanPreference(String key) {
        return mPreference.getBoolean(key, true);
    }

    /**
     * Method to get the preference for if moves should be Highlighted
     * @return returns the boolean value for move highlighting
     */
    public static Boolean getHighlightMovesPreference(){
        return mPreference.getBoolean(HIGHLIGHTMOVES, true);
    }
    /**
     * Method to get the preference for if Pieces should display a tooltip
     * @return returns the boolean value for piece highlighting
     */
    public static Boolean getPieceToolTipPreference(){
        return mPreference.getBoolean(PIECETOOLTIPS, true);
    }
    /**
     * Method to get the preference for Save game location
     * @return returns the string value for the save game location
     */
    public static String getSaveLocationPreference(){
        return mPreference.get(SAVELOCATION, "default");
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
    private static final String SAVELOCATION = "saveLocation"; //$NON-NLS-1$
    private static final String HIGHLIGHTMOVES = "highlightMoves"; //$NON-NLS-1$
    private static final String PIECETOOLTIPS = "pieceToolTips"; //$NON-NLS-1$
    private static final Preferences mPreference = Preferences.userRoot().node("ChessCrafterPreferences");
}