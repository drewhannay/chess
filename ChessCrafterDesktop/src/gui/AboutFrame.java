package gui;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import utility.AppConstants;
import utility.FileUtility;
import utility.GuiUtility;

public class AboutFrame extends JFrame
{
	public AboutFrame(JPanel mainPanel)
	{
		initGUIComponents(mainPanel);
	}

	private void initGUIComponents(JPanel mainPanel)
	{
		setTitle(Messages.getString("AboutFrame.about") + AppConstants.APP_NAME);  //$NON-NLS-1$
		setSize(350, 375);
		setResizable(false);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		JLabel frontPageImage = null;
		JLabel piecePicture = null;
		try
		{
			frontPageImage = new JLabel(GuiUtility.createImageIcon(300, 200, FileUtility.getImagePath("front_page_image.jpeg", true))); //$NON-NLS-1$
			piecePicture = new JLabel(GuiUtility.createImageIcon(48, 48, FileUtility.getImagePath("d_King.png",true))); //$NON-NLS-1$
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		Font font = new Font(Messages.getString("AboutFrame.verdana"), Font.BOLD, 18); //$NON-NLS-1$
		JLabel title = new JLabel(AppConstants.APP_NAME + Messages.getString("AboutFrame.newline")); //$NON-NLS-1$
		title.setFont(font);

		JPanel topPanel = new JPanel();
		topPanel.add(title);
		topPanel.add(frontPageImage);

		JTextArea infoTextArea = new JTextArea();
		infoTextArea.setEditable(false);
		infoTextArea.setText(Messages.getString("AboutFrame.version") + Messages.getString("AboutFrame.visitOurSite")); //$NON-NLS-1$ //$NON-NLS-2$

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
