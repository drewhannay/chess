package gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import utility.FileUtility;
import utility.GuiUtility;

public final class PreferenceUtility
{
	private PreferenceUtility()
	{
	}

	public static void createPreferencePopup(Component relativeComponent)
	{
		final JFrame popupFrame = new JFrame(Messages.getString("PreferenceUtility.squareOptions")); //$NON-NLS-1$
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
		final JButton cancelButton = new JButton(Messages.getString("PreferenceUtility.done")); //$NON-NLS-1$
		GuiUtility.setupCancelButton(cancelButton, popupFrame);

		holder.add(currentSaveLocationLabel);
		holder.add(currentSaveLocationField);

		final String defaultSaveLocation = FileUtility.getDefaultCompletedLocation();

		try
		{
			final File preferencesFile = FileUtility.getPreferencesFile();
			if (!preferencesFile.exists())
			{
				try
				{
					preferencesFile.createNewFile();
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferencesFile, true));
					bufferedWriter.write("DefaultPreferencesSet = true"); //$NON-NLS-1$
					bufferedWriter.newLine();
					bufferedWriter.write("DefaultSaveLocation = " + defaultSaveLocation); //$NON-NLS-1$
					bufferedWriter.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			FileInputStream fileInputStream = null;
			fileInputStream = new FileInputStream(preferencesFile);
			DataInputStream dataInputStream = new DataInputStream(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
			String line;
			line = bufferedReader.readLine();
			line = bufferedReader.readLine();
			fileInputStream.close();
			dataInputStream.close();
			bufferedReader.close();
			currentSaveLocationField.setText(line.substring(22));

			resetButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					try
					{
						PrintWriter printWriter = new PrintWriter(preferencesFile);
						printWriter.print(""); //$NON-NLS-1$
						printWriter.close();
						BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferencesFile, true));
						bufferedWriter.write("DefaultPreferencesSet = true"); //$NON-NLS-1$
						bufferedWriter.newLine();
						bufferedWriter.write("DefaultSaveLocation = " + defaultSaveLocation); //$NON-NLS-1$
						bufferedWriter.close();
						currentSaveLocationField.setText(defaultSaveLocation);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});

			changeLocationButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					try
					{
						FileWriter fileWriter = new FileWriter(preferencesFile, true);
						if (!preferencesFile.exists())
						{
							preferencesFile.createNewFile();
							BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
							bufferedWriter.write("DefaultPreferencesSet = false"); //$NON-NLS-1$
							bufferedWriter.newLine();
							bufferedWriter.write("DefaultSaveLocation = " + defaultSaveLocation); //$NON-NLS-1$
							bufferedWriter.close();
						}
						PrintWriter printWriter = new PrintWriter(preferencesFile);
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
						BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
						bufferedWriter.write("DefaultPreferencesSet = true"); //$NON-NLS-1$
						bufferedWriter.newLine();
						if (returnVal == JFileChooser.APPROVE_OPTION)
						{
							printWriter.print(""); //$NON-NLS-1$
							printWriter.close();
							bufferedWriter.write("DefaultSaveLocation = " + fileChooser.getSelectedFile().getAbsolutePath()); //$NON-NLS-1$
							bufferedWriter.close();
							currentSaveLocationField.setText(fileChooser.getSelectedFile().getAbsolutePath());
						}
						else
						{
							printWriter.close();
							bufferedWriter.close();
							return;
						}

						fileWriter.close();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Driver.getInstance(), Messages.getString("PreferenceUtility.notAValidLocation"), //$NON-NLS-1$
					Messages.getString("PreferenceUtility.invalidLocation"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
			e.printStackTrace();
		}

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
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		popupFrame.add(cancelButton, constraints);

		popupFrame.pack();
		popupFrame.setVisible(true);
	}
}
