package utility;

import gui.Driver;
import gui.NewGameMenu;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public final class GUIUtility
{
	private GUIUtility()
	{
	}

	public static void requestFocus(final JComponent component)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				component.requestFocus();
			}
		});
	}

	public static void setupCancelButton(JButton cancelButton, final JFrame popup)
	{
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				popup.dispose();
			}
		});
	}

	public static void setupVariantCancelButton(JButton cancelButton, final JPanel displayPanel, final JFrame popup)
	{
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				displayPanel.removeAll();
				popup.setVisible(false);
			}
		});
	}

	public static void setupReturnToMainButton(JButton backButton)
	{
		backButton.setToolTipText("Press me to go back to the Main Menu");
		backButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Driver.getInstance().revertToMainPanel();
			}
		});
	}
	
	public static ImageIcon createImageIcon(int imageWidth, int imageHeight, String imageLocation, JPanel sourcePanel){
		BufferedImage bufferedImage = null;
		try
		{
			bufferedImage = ImageIO.read(sourcePanel.getClass().getResource(imageLocation));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		ImageIcon imageIcon = new ImageIcon(bufferedImage);
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));
		return imageIcon;
	}
	
	public static void installAIFiles(JComboBox aiComboBox, NewGameMenu source, String[] aiFiles){
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(Driver.getInstance());
		File file = fileChooser.getSelectedFile();

		File destinationDirectory = new File(System.getProperty("user.home") + "/chess/AI");

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if (!file.renameTo(new File(destinationDirectory, file.getName())))
			{
				JOptionPane.showMessageDialog(Driver.getInstance(), "File was not installed successfully", "Error",
						JOptionPane.PLAIN_MESSAGE);
			}
			else
			{
				aiComboBox.removeAllItems();
				for (String fileName : aiFiles)
					aiComboBox.addItem(fileName);
			}
		}
		else
		{
			return;
		}
	}
}
