package utility;

import gui.Driver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
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
}
