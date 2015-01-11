package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.gui.Driver;
import com.drewhannay.chesscrafter.models.Preference;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

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

        Preference preference = getPreference();
        currentSaveLocationField.setText(preference.getSaveLocation());
        highlightingCheckBox.setSelected(preference.isHighlightMoves());
        pieceToolTipCheckBox.setSelected(preference.showPieceToolTips());

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

                Preference preference = new Preference();
                preference.setSaveLocation(currentSaveLocationField.getText());
                preference.setHighlightMoves(highlightingCheckBox.isSelected());
                preference.setShowPieceToolTips(pieceToolTipCheckBox.isSelected());
                savePreference(preference);
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

    public static Preference getPreference() {
        StringBuilder jsonString = new StringBuilder();

        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(FileUtility.getPreferencesFile());
            dataInputStream = new DataInputStream(fileInputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                jsonString.append(line);

            bufferedReader.close();
            dataInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!Strings.isNullOrEmpty(jsonString.toString()))
            return (Preference) GsonUtility.loadObjectFromJSonString(jsonString.toString(), Preference.class);
        else
            return createDefaultPreference();
    }

    public static Preference createDefaultPreference() {
        Preference defaultPref = new Preference();
        savePreference(defaultPref);
        return defaultPref;
    }

    public static void savePreference(Preference preference) {
        File preferencesFile = FileUtility.getPreferencesFile();
        FileOutputStream f_out;
        try {
            if (!preferencesFile.exists()) {
                preferencesFile.createNewFile();
            }
            f_out = new FileOutputStream(preferencesFile);
            DataOutputStream out = new DataOutputStream(f_out);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

            writer.write(preference.getJsonString());

            writer.close();
            out.close();
            f_out.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
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
}
