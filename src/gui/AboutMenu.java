package gui;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import utility.AppConstants;
import utility.GuiUtility;

public class AboutMenu extends JFrame
{
	public AboutMenu(JPanel mainPanel)
	{
		initGUIComponents(mainPanel);
	}

	private void initGUIComponents(JPanel mainPanel)
	{
		setTitle("About " + AppConstants.APP_NAME);
		setSize(350, 375);
		setResizable(false);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		JLabel frontPageImage = new JLabel(GuiUtility.createImageIcon(300, 200, "/front_page_image.jpeg"));
		JLabel piecePicture = new JLabel(GuiUtility.createImageIcon(48, 48, "/d_King.png"));

		Font font = new Font("Verdana", Font.BOLD, 18);
		JLabel title = new JLabel(AppConstants.APP_NAME + "\n");
		title.setFont(font);

		JPanel topPanel = new JPanel();
		topPanel.add(title);
		topPanel.add(frontPageImage);

		JTextArea infoTextArea = new JTextArea();
		infoTextArea.setEditable(false);
		infoTextArea.setText("Version 1.1\n\n" + "Visit our project site");

		JButton siteButton = new JButton();
		siteButton.setIcon(piecePicture.getIcon());
		siteButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (Desktop.isDesktopSupported())
				{
					try
					{
						Desktop.getDesktop().browse(new URI(AppConstants.PROJECT_URL));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});

		constraints.gridy = 0;
		add(title, constraints);
		constraints.gridy = 1;
		add(frontPageImage, constraints);
		constraints.gridy = 2;
		add(infoTextArea, constraints);
		constraints.gridy = 3;
		add(siteButton, constraints);

		setVisible(true);
	}

	private static final long serialVersionUID = 7316549497827793096L;
}
