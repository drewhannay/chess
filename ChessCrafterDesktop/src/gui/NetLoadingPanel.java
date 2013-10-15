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

import net.AnimatedLabel;

public class NetLoadingPanel extends JPanel
{
	public NetLoadingPanel()
	{
		initGUIComponents();
	}

	public void initGUIComponents()
	{
		setBorder(BorderFactory.createLoweredBevelBorder());

		mCancelButton = new JButton(Messages.getString("NetLoadingPanel.cancel")); //$NON-NLS-1$
		mCancelButton.setToolTipText(Messages.getString("NetLoadingPanel.backToMainMenu")); //$NON-NLS-1$
		mCancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				AnimatedLabel.m_isFinished = true;
				// NewGameMenu.mIsCancelled = true;
				Driver.getInstance().setFileMenuVisibility(true);
				Driver.getInstance().revertToMainPanel();
			}
		});

		mWaitLabel = new AnimatedLabel();

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(mWaitLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		add(mCancelButton, constraints);
	}

	private static final long serialVersionUID = 8365806731061105369L;

	private JLabel mWaitLabel;
	private JButton mCancelButton;
}
