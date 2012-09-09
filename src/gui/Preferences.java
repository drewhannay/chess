package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import utility.AppConstants;
import utility.FileUtility;
import utility.GUIUtility;

public class Preferences extends JFrame
{
	public Preferences()
	{
		initGUIComponents();
	}

	private void initGUIComponents()
	{
		setTitle(AppConstants.APP_NAME + " Preferences");
		setSize(370, 120);
		setLocationRelativeTo(Driver.getInstance());
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		GridBagConstraints constraints = new GridBagConstraints();

		final JPanel otherOptions = new JPanel();
		otherOptions.setBorder(BorderFactory.createTitledBorder("Options"));

		final JPanel defaultSavePanel = new JPanel();
		defaultSavePanel.setLayout(new GridBagLayout());
		defaultSavePanel.setBorder(BorderFactory.createTitledBorder("Default Completed Game Save Location"));

		final JLabel currentSaveLocationLabel = new JLabel("Current Save Location");
		final JTextField currentSaveLocationField = new JTextField(30);
		currentSaveLocationField.setEditable(false);

		final JButton changeLocationButton = new JButton("Choose New Save Location");
		final JButton resetButton = new JButton("Reset to Default Location");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 0, 10, 0);
		defaultSavePanel.add(currentSaveLocationLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		defaultSavePanel.add(currentSaveLocationField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		defaultSavePanel.add(changeLocationButton, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		defaultSavePanel.add(resetButton, constraints);

		final JButton cancelButton = new JButton("Done");
		GUIUtility.setupCancelButton(cancelButton, this);

		final String defaultSaveLocation = FileUtility.getDefaultCompletedLocation();

		try
		{
			currentSaveLocationField.setText(FileUtility.readPreferencesFileLine(FileUtility.defaultSaveLocation).substring(22));

			resetButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					try
					{
						try
						{
							FileUtility.overwritePreferencesFileLine(FileUtility.defaultSaveLocation, "DefaultSaveLocation = " + defaultSaveLocation);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
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
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
						if (returnVal == JFileChooser.APPROVE_OPTION)
						{
							FileUtility.overwritePreferencesFileLine(FileUtility.defaultSaveLocation, "DefaultSaveLocation = " + fileChooser.getSelectedFile().getAbsolutePath());
							currentSaveLocationField.setText(fileChooser.getSelectedFile().getAbsolutePath());
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});

			final JCheckBox moveHighlighting = new JCheckBox("Highlight Valid Moves");
			if (FileUtility.readPreferencesFileLine(FileUtility.moveHighlighting).contains("true"))
				moveHighlighting.setSelected(true);
			else
				moveHighlighting.setSelected(false);

			otherOptions.add(moveHighlighting);

			moveHighlighting.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					try
					{
						if (moveHighlighting.isSelected())
							FileUtility.overwritePreferencesFileLine(FileUtility.moveHighlighting, "MoveHighlighting = true");
						else
							FileUtility.overwritePreferencesFileLine(FileUtility.moveHighlighting, "MoveHighlighting = false");
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
			JOptionPane.showMessageDialog(Driver.getInstance(), "That is not a valid location to save your completed games.",
					"Invalid Location", JOptionPane.PLAIN_MESSAGE);
			e.printStackTrace();
		}

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		add(defaultSavePanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		add(otherOptions, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		add(cancelButton, constraints);

		pack();
		setVisible(true);
	}
	private static final long serialVersionUID = -7753014974582493592L;
}
