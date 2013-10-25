package gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.google.common.collect.Lists;

import utility.FileUtility;
import utility.GuiUtility;
import utility.Preference;

public final class PreferenceUtility
{
	private PreferenceUtility()
	{
	}

	public interface PieceToolTipPreferenceChangedListener
	{
		public void onPieceToolTipPreferenceChanged();
	}

	public static void createPreferencePopup(Component relativeComponent)
	{
		final JFrame popupFrame = new JFrame(Messages.getString("PreferenceUtility.preferences")); //$NON-NLS-1$
		popupFrame.setSize(370, 120);
		popupFrame.setLocationRelativeTo(relativeComponent);
		popupFrame.setLayout(new GridBagLayout());
		popupFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		GridBagConstraints constraints = new GridBagConstraints();

		final JPanel holder = new JPanel();
		holder.setBorder(BorderFactory.createTitledBorder(Messages.getString("PreferenceUtility.defaultCompletedLocation"))); //$NON-NLS-1$
		final JLabel currentSaveLocationLabel = new JLabel(Messages.getString("PreferenceUtility.currentSaveLocation")); //$NON-NLS-1$
		final JTextField currentSaveLocationField = new JTextField(25);
		currentSaveLocationField.setText(FileUtility.getDefaultCompletedLocation());
		currentSaveLocationField.setEditable(false);
		final JButton changeLocationButton = new JButton(Messages.getString("PreferenceUtility.chooseNewSaveLocation")); //$NON-NLS-1$
		final JButton resetButton = new JButton(Messages.getString("PreferenceUtility.resetToDefaultLocation")); //$NON-NLS-1$
		final JCheckBox highlightingCheckBox = new JCheckBox(Messages.getString("PreferenceUtility.enableHighlighting")); //$NON-NLS-1$
		final JCheckBox pieceToolTipCheckBox = new JCheckBox(Messages.getString("PreferenceUtility.showPieceTooltips")); //$NON-NLS-1$

		final JButton cancelButton = new JButton(Messages.getString("PreferenceUtility.cancel")); //$NON-NLS-1$

		final JButton doneButton = new JButton(Messages.getString("PreferenceUtility.done")); //$NON-NLS-1$
		GuiUtility.setupDoneButton(doneButton, popupFrame);

		final JPanel buttonHolder = new JPanel();
		buttonHolder.add(cancelButton);
		buttonHolder.add(doneButton);
		
		holder.add(currentSaveLocationLabel);
		holder.add(currentSaveLocationField);

		final String defaultSaveLocation = FileUtility.getDefaultCompletedLocation();

		Preference preference = getPreference();
		currentSaveLocationField.setText(preference.getSaveLocation());
		highlightingCheckBox.setSelected(preference.isHighlightMoves());
		pieceToolTipCheckBox.setSelected(preference.showPieceToolTips());

		resetButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				currentSaveLocationField.setText(defaultSaveLocation);
			}
		});

		changeLocationButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
					if (returnVal == JFileChooser.APPROVE_OPTION)
						currentSaveLocationField.setText(fileChooser.getSelectedFile().getAbsolutePath());
					else
						return;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		doneButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{

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
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		popupFrame.add(buttonHolder, constraints);

		popupFrame.pack();
		popupFrame.setVisible(true);
	}

	public static Preference getPreference()
	{
		ObjectInputStream in;
		try
		{
			in = new ObjectInputStream(new FileInputStream(FileUtility.getPreferencesFile()));
			Preference toReturn = (Preference) in.readObject();
			in.close();
			return toReturn;
		}
		catch (Exception e)
		{
			return createDefaultPreference();
		}
	}

	public static Preference createDefaultPreference()
	{
		File preferencesFile = FileUtility.getPreferencesFile();
		FileOutputStream f_out;
		Preference preference = null;
		try
		{
			if (!preferencesFile.exists())
			{
				preferencesFile.createNewFile();
			}
			f_out = new FileOutputStream(preferencesFile);
			ObjectOutputStream out = new ObjectOutputStream(f_out);
			preference = new Preference();
			out.writeObject(preference);
			out.close();
			f_out.close();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return preference;
	}

	public static void savePreference(Preference preference)
	{
		if (preference.equals(getPreference()))
			return;

		File preferencesFile = FileUtility.getPreferencesFile();
		FileOutputStream f_out;
		try
		{
			f_out = new FileOutputStream(preferencesFile);
			ObjectOutputStream out = new ObjectOutputStream(f_out);
			out.writeObject(preference);
			out.close();
			f_out.close();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		for (PieceToolTipPreferenceChangedListener listener : mToolTipListeners)
		{
			listener.onPieceToolTipPreferenceChanged();
		}
	}

	public static void addPieceToolTipListener(PieceToolTipPreferenceChangedListener listener)
	{
		if (mToolTipListeners == null)
			mToolTipListeners = Lists.newArrayList();
		if (!mToolTipListeners.contains(listener))
			mToolTipListeners.add(listener);
	}

	private static List<PieceToolTipPreferenceChangedListener> mToolTipListeners;
}
