package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NetLoadingPanel extends JPanel
{
	public NetLoadingPanel()
	{
		initGUIComponents();
	}

	public void initGUIComponents()
	{
		setBorder(BorderFactory.createLoweredBevelBorder());

		m_cancelButton = new JButton("Cancel");
		m_cancelButton.setToolTipText("Press me to go back to the Main Menu");
		m_cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				AnimatedLabel.m_isFinished = true;
//				NewGameMenu.mIsCancelled = true;
				Driver.getInstance().setFileMenuVisibility(true);
				Driver.getInstance().revertToMainPanel();
			}
		});

		m_waitLabel = new AnimatedLabel();

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(m_waitLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		add(m_cancelButton, constraints);
	}

	private static final long serialVersionUID = 8365806731061105369L;

	private JLabel m_waitLabel;
	private JButton m_cancelButton;
}
