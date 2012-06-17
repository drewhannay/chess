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

/**
 * Class to set up a small loading window for network play
 * 
 * @author John McCormick & Andrew Wolfe & Drew Hannay
 * 
 */
public class NetLoading extends JPanel
{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8365806731061105369L;

	/**
	 * JLabel with instructions for the name field.
	 */
	private JLabel waitLabel;

	/**
	 * The thread that is running the networking.
	 */
	@SuppressWarnings("unused")
	private Thread th;
	/**
	 * The button to cancel network play
	 */
	private JButton cancelButton;

	/**
	 * Constructor. Call initComponents to initialize the GUI.
	 * 
	 * @param th The thread being passed to be loaded.
	 */
	public NetLoading(Thread th)
	{
		this.th = th;
		initComponents();
	}

	/**
	 * Initialize components of the GUI Create all the GUI components, set their
	 * specific properties and add them to the window. Also add any necessary
	 * ActionListeners.
	 */
	public void initComponents()
	{

		setBorder(BorderFactory.createLoweredBevelBorder());

		// Create button and add ActionListener for going back to main menu
		cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("Press me to go back to the Main Menu");
		cancelButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Return to the main screen.
				AnimatedLabel.finished = true;
				NewGameMenu.cancelled = true;
				Driver.getInstance().gamePlayHelp.setVisible(false);
				Driver.getInstance().helpMenu.setText("Help");
				Driver.getInstance().fileMenu.setVisible(true);
				Driver.getInstance().revertPanel();
			}
		});

		// Create JLabel and JTextField.
		waitLabel = new AnimatedLabel();

		// Layout stuff. Don't. Ask.
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 0;
		add(waitLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		add(cancelButton, c);

	}

}
