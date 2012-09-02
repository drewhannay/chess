package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import logic.PieceBuilder;

/**
 * Class to make new pieces for new variants
 * @author John mcCormick 
 */
public class PieceMaker extends JPanel
{
	private static final long serialVersionUID = -6530771731937840358L;
	/**
	 * New Piece builder for the new.... piece
	 */
	private PieceBuilder builder;
	/**
	 * Boolean to see if we are making a knight piece
	 */
	private boolean knightLike = false;
	/**
	 * Frame to hold the window
	 */
	private JFrame frame;
	/**
	 * holder for this PieceMaker
	 */
	private PieceMaker holder = this;

	/**
	 * Constructor for piece making window
	 * 
	 * @param variant CustomSetupMenu that we came from
	 * @param optionsFrame Frame that is holding this PieceMaker window
	 */
	public PieceMaker(CustomSetupMenu variant, JFrame optionsFrame)
	{
		frame = optionsFrame;
		frame.setVisible(true);
		frame.add(this);
		frame.setVisible(true);
		frame.setSize(400, 600);
		frame.setLocationRelativeTo(Driver.getInstance());
		PieceBuilder.initPieceTypes();
		initComponents(variant);
	}

	/**
	 * Method to set up the piece making window.
	 */
	public void initComponents(final CustomSetupMenu variant)
	{
		// Create a new PieceBuilder.
		builder = new PieceBuilder();

		// Create the pop up and set the size, location and layout.
		setSize(550, 875);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JPanel pieceCreationPanel = new JPanel();
		pieceCreationPanel.setLayout(new GridBagLayout());
		pieceCreationPanel.setBorder(BorderFactory.createTitledBorder("New Piece"));

		// Add a JLabel and JTextField for the Piece name.
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout());

		namePanel.add(new JLabel("Piece Name:"));
		final JTextField name = new JTextField(15);
		name.setToolTipText("Enter the name of the new piece here");
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				name.requestFocus();
			}
		});
		namePanel.add(name);

		c.gridx = 0;
		c.gridy = 0;
		pieceCreationPanel.add(namePanel, c);

		final ImageIcon blankSquare = new ImageIcon("./images/WhiteSquare.gif");
		blankSquare.setImage(blankSquare.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		final JPanel lightIconPanel = new JPanel();
		lightIconPanel.setLayout(new FlowLayout());
		final JLabel lightIconLabel = new JLabel();
		lightIconLabel.setSize(48, 48);
		lightIconLabel.setIcon(blankSquare);

		// Add JButtons for choosing the images for the new type.
		final JButton lightImageButton = new JButton("Choose image for light piece");
		lightImageButton.setToolTipText("Click me to choose an Light Colored Icon for this piece");
		lightImageButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent event)
			{
				Object[] options = new String[] { "Browse My Computer", "Image from Internet", "Cancel" };

				switch (JOptionPane.showOptionDialog(null, "Where would you like to get the image from?", "Choose Image",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
				{
				case JOptionPane.YES_OPTION:
					final JFileChooser fileChooser = new JFileChooser("~/");
					int returnVal = fileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						ImageIcon icon = makeIcon(fileChooser, builder);
						lightIconLabel.setIcon(icon); // Adds icon to button
						builder.setLightImage(icon);
					}
					break;
				case JOptionPane.NO_OPTION:
					String url = JOptionPane.showInputDialog(null, "Enter URL of the image:", "Input URL", JOptionPane.PLAIN_MESSAGE);
					try
					{
						ImageIcon image = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(48, 48, Image.SCALE_SMOOTH));
						lightIconLabel.setIcon(image);
						builder.setLightImage(image);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					break;
				case JOptionPane.CANCEL_OPTION:
					break;
				}
			}
		});
		lightIconPanel.add(lightImageButton);
		lightIconPanel.add(lightIconLabel);

		builder.setLightImage(blankSquare);

		c.gridx = 0;
		c.gridy = 3;
		pieceCreationPanel.add(lightIconPanel, c);

		final JPanel darkIconPanel = new JPanel();
		darkIconPanel.setLayout(new FlowLayout());
		final JLabel darkIconLabel = new JLabel();
		darkIconLabel.setSize(48, 48);
		darkIconLabel.setIcon(blankSquare);

		final JButton darkImageButton = new JButton("Choose image for dark piece");
		darkImageButton.setToolTipText("Click me to choose an Dark Colored Icon for this piece");
		darkImageButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent event)
			{
				Object[] options = new String[] { "Browse My Computer", "Image from Internet", "Cancel" };

				switch (JOptionPane.showOptionDialog(null, "Where would you like to get the image from?", "Choose Image",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
				{
				case JOptionPane.YES_OPTION:
					final JFileChooser fileChooser = new JFileChooser("~/");
					int returnVal = fileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						ImageIcon icon = makeIcon(fileChooser, builder);
						darkIconLabel.setIcon(icon); // Adds icon to button
						builder.setDarkImage(icon);
					}
					break;
				case JOptionPane.NO_OPTION:
					String url = JOptionPane.showInputDialog(null, "Enter the URL of the image:", "Input URL",
							JOptionPane.PLAIN_MESSAGE);
					try
					{
						ImageIcon image = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(48, 48, Image.SCALE_SMOOTH));
						darkIconLabel.setIcon(image);
						builder.setDarkImage(image);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					break;
				case JOptionPane.CANCEL_OPTION:
					break;
				}
			}
		});
		darkIconPanel.add(darkImageButton);
		darkIconPanel.add(darkIconLabel);

		builder.setDarkImage(blankSquare);

		c.gridx = 0;
		c.gridy = 2;
		pieceCreationPanel.add(darkIconPanel, c);

		// Add components for collecting the directions of movement.

		final String[] directions = new String[] { "North", "Northeast", "East", "Southeast", "South", "Southwest", "West",
				"Northwest" };
		final JComboBox dropdown = new JComboBox(directions);
		dropdown.setToolTipText("This dropdown has all of the valid directions you can still set movement for");

		final JTextField north = new JTextField(4);
		north.setToolTipText("North");
		north.setText("0");
		final JTextField northeast = new JTextField(4);
		northeast.setToolTipText("Northeast");
		northeast.setText("0");
		final JTextField northwest = new JTextField(4);
		northwest.setToolTipText("Northwest");
		northwest.setText("0");
		final JTextField east = new JTextField(4);
		east.setToolTipText("East");
		east.setText("0");
		final JTextField southeast = new JTextField(4);
		southeast.setToolTipText("Southeast");
		southeast.setText("0");
		final JTextField south = new JTextField(4);
		south.setToolTipText("South");
		south.setText("0");
		final JTextField southwest = new JTextField(4);
		southwest.setToolTipText("Southwest");
		southwest.setText("0");
		final JTextField west = new JTextField(4);
		west.setToolTipText("West");
		west.setText("0");

		BufferedImage bi = null;
		try
		{
			// read the image from the class resources
			bi = ImageIO.read(getClass().getResource("/movement_directions.png"));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		// Makes the image an icon and ands it to a JLabel
		ImageIcon movementPicture = new ImageIcon(bi);
		JLabel movementPictureHolder = new JLabel();
		movementPicture.setImage(movementPicture.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH));
		movementPictureHolder.setIcon(movementPicture);

		JPanel movement = new JPanel();
		movement.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.anchor = GridBagConstraints.EAST;
		movement.add(northwest, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.CENTER;
		movement.add(north, c);
		c.gridx = 2;
		c.anchor = GridBagConstraints.WEST;
		movement.add(northeast, c);
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		movement.add(west, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.CENTER;
		movement.add(movementPictureHolder, c);
		c.gridx = 2;
		c.anchor = GridBagConstraints.WEST;
		movement.add(east, c);
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		movement.add(southwest, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.CENTER;
		movement.add(south, c);
		c.gridx = 2;
		c.anchor = GridBagConstraints.WEST;
		movement.add(southeast, c);

		// Collect max distance of movement, -1 for infinity.
		final JTextField distance = new JTextField(3);
		distance.setToolTipText("Greatest amount of spaces piece can travel in chosen direction");

		final JTextField firstKnightDirection = new JTextField(2);
		firstKnightDirection.setToolTipText("Enter the knight-like directions you would like");
		firstKnightDirection.setEnabled(false);

		final JTextField secondKnightDirection = new JTextField(2);
		secondKnightDirection.setToolTipText("Enter the other direction for the knight-like piece");
		secondKnightDirection.setEnabled(false);

		final JCheckBox knightMovements = new JCheckBox("Knight-like Movements", false);
		knightMovements.setToolTipText("Press me to turn on Knight-Like Movements for this piece");
		knightMovements.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent event)
			{
				knightLike = !knightLike;
				if (knightLike)
				{
					firstKnightDirection.setEnabled(true);
					secondKnightDirection.setEnabled(true);
				}
				else
				{
					firstKnightDirection.setEnabled(false);
					secondKnightDirection.setEnabled(false);
				}
			}
		});

		final JCheckBox leaper = new JCheckBox("Can jump other Pieces", false);
		leaper.setToolTipText("Press me to allow this piece to jump others");

		final JPanel knightMovementPanel = new JPanel();
		knightMovementPanel.setToolTipText("Use me to set up Knight Like Movements. See Variant Help for instructions");
		knightMovementPanel.setLayout(new FlowLayout());
		knightMovementPanel.add(firstKnightDirection);
		knightMovementPanel.add(new JLabel("x"));
		knightMovementPanel.add(secondKnightDirection);

		// Setting up a panel handle movement instructions
		JPanel movementPanel = new JPanel();
		movementPanel.setLayout(new BoxLayout(movementPanel, BoxLayout.Y_AXIS));
		movementPanel.setLayout(new GridBagLayout());

		// Adds options and labels for setting up movement for the new piece
		c.insets = new Insets(5, 0, 5, 0);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		movementPanel.add(new JLabel("<html><u>Normal Movement Setup:</u></br></html>"), c);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		movementPanel.add(movement, c);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 5;
		movementPanel.add(leaper, c);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 6;
		movementPanel.add(knightMovements, c);
		c.insets = new Insets(5, 0, 5, 0);
		c.gridx = 0;
		c.gridy = 7;
		movementPanel.add(new JLabel("<html><u>Knight-like Movement Directions:</u></br></html>"), c);
		c.gridx = 0;
		c.gridy = 8;
		movementPanel.add(knightMovementPanel, c);

		c.gridx = 0;
		c.gridy = 5;
		pieceCreationPanel.add(movementPanel, c);

		// Create button and add ActionListener
		final JButton savePieceButton = new JButton("Save Piece");
		savePieceButton.setToolTipText("Press me to save this Piece type");
		savePieceButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent event)
			{

				if (name.getText() == "" || PieceBuilder.isPieceType(name.getText()))
				{ // Make sure the name is valid.
					JOptionPane.showMessageDialog(null, "Please enter a unique piece name.", "Invalid Piece Name",
							JOptionPane.PLAIN_MESSAGE);
					return;
				}

				if (isIntDist(north) && isIntDist(northeast) && isIntDist(northwest) && isIntDist(east) && isIntDist(west)
						&& isIntDist(south) && isIntDist(southeast) && isIntDist(southwest))
				{
					builder.addMove('N', Integer.parseInt(north.getText()));
					builder.addMove('R', Integer.parseInt(northeast.getText()));
					builder.addMove('L', Integer.parseInt(northwest.getText()));
					builder.addMove('E', Integer.parseInt(east.getText()));
					builder.addMove('W', Integer.parseInt(west.getText()));
					builder.addMove('S', Integer.parseInt(south.getText()));
					builder.addMove('r', Integer.parseInt(southeast.getText()));
					builder.addMove('l', Integer.parseInt(southwest.getText()));
				}

				if (firstKnightDirection.isEnabled())
				{
					if (isIntKnights())
					{
						builder.addMove('x', Integer.parseInt(firstKnightDirection.getText())); // Knight
						// movements
						// stored
						// in
						// builder.
						builder.addMove('y', Integer.parseInt(secondKnightDirection.getText()));
					}
				}
				builder.setName(name.getText());// Set the name in the
												// PieceBuilder
				PieceBuilder.savePieceType(builder);// Save the piece type in
													// the PieceBuilder class.

				// Refreshing the window
				builder = new PieceBuilder();
				name.setText("");
				lightIconLabel.setIcon(blankSquare);
				darkIconLabel.setIcon(blankSquare);
				distance.setText("");
				leaper.setSelected(false);
				knightMovements.setSelected(false);
				firstKnightDirection.setText("");
				firstKnightDirection.setEnabled(false);
				secondKnightDirection.setText("");
				secondKnightDirection.setEnabled(false);
				knightLike = false;
				dropdown.removeAllItems();
				for (int i = 0; i < directions.length; i++)
					dropdown.addItem(directions[i]);

			}

			/**
			 * Determine if the user entered a valid integer.
			 * 
			 * @return If the text is a valid integer
			 */
			private boolean isIntKnights()
			{
				try
				{
					Integer.parseInt(firstKnightDirection.getText());
					Integer.parseInt(secondKnightDirection.getText());
					return true;
				}
				catch (Exception e)
				{
					return false;
				}
			}
		});

		final JButton doneButton = new JButton("Done");
		doneButton.setToolTipText("Press me when you have made all of your pieces");
		doneButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (name.getText().equals(""))
				{
					variant.setupPiecesList();
					holder.removeAll();
					frame.setVisible(false);
				}
				else
				{
					int answer = JOptionPane.showConfirmDialog(null,
							"If you continue the piece you are working on will not be saved. Continue?", "Piece Maker",
							JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (answer == 0)
					{
						variant.setupPiecesList();
						holder.removeAll();
						frame.setVisible(false);
					}
				}
			}
		});

		c.gridx = 0;
		c.gridy = 7;
		pieceCreationPanel.add(savePieceButton, c);

		c.gridx = 0;
		c.gridy = 0;
		add(pieceCreationPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		add(doneButton, c);

		frame.pack();
	}

	/**
	 * Makes the icon for the for the new piece
	 * 
	 * @param fileChooser The file chooser to select the piece
	 * @param builder The builder that is building the piece
	 * @return the icon to add to the button displaying the icon for the new
	 * piece
	 */
	public ImageIcon makeIcon(JFileChooser fileChooser, PieceBuilder builder)
	{
		// If a valid File was chosen, make an ImageIcon from the file path.
		String file = fileChooser.getSelectedFile().getAbsolutePath();

		// default center section
		ImageIcon icon = new ImageIcon(file);
		// Scale the image to 48x48.
		icon.setImage(icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		return icon;
	}

	/**
	 * Determine if the user entered a valid integer.
	 * 
	 * @return If the text is a valid integer
	 */
	private boolean isIntDist(JTextField name)
	{
		try
		{
			Integer.parseInt(name.getText());
			return true;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,
					"All movement distances must be a number. Please enter a number in the " + name.getToolTipText()
							+ " direction box.", "Error", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
	}

}
