package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
 * @author John mcCormick Class to make new pieces for new variants
 */
public class PieceMaker extends JPanel
{

	/**
	 * Because it told me to
	 */
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
	 * @param b reference to the builder
	 */
	/*
	 * public PieceMaker(Builder b) { this.b = b; PieceBuilder.initPieceTypes();
	 * initComponents(); }
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

		JPanel piecePanel = new JPanel();
		piecePanel.setLayout(new GridBagLayout());
		piecePanel.setBorder(BorderFactory.createTitledBorder("New Piece"));

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
		piecePanel.add(namePanel, c);

		final ImageIcon temp = new ImageIcon("./images/WhiteSquare.gif");
		temp.setImage(temp.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		final JPanel lightIconPanel = new JPanel();
		lightIconPanel.setLayout(new FlowLayout());
		final JButton lightIconButton = new JButton();
		lightIconButton.setSize(48, 48);
		lightIconButton.setIcon(temp);

		// Add JButtons for choosing the images for the new type.
		final JButton chooseLightImage = new JButton("Choose image for light piece");
		chooseLightImage.setToolTipText("Click me to choose an Light Colored Icon for this piece");
		chooseLightImage.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// Create the JFileChooser and add image for the new piece
				final JFileChooser fc = new JFileChooser("~/"); // default
																// directory is
																// in the images
																// folder
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					ImageIcon icon = makeIcon(fc, builder);
					lightIconButton.setIcon(icon); // Adds icon to button
					builder.setLightImage(icon);
				}
			}
		});
		lightIconPanel.add(chooseLightImage);
		lightIconPanel.add(lightIconButton);

		builder.setLightImage(temp);

		c.gridx = 0;
		c.gridy = 3;
		piecePanel.add(lightIconPanel, c);

		final JPanel darkIconPanel = new JPanel();
		darkIconPanel.setLayout(new FlowLayout());
		final JButton darkIconButton = new JButton();
		darkIconButton.setSize(48, 48);
		darkIconButton.setIcon(temp);

		final JButton chooseDarkImage = new JButton("Choose image for dark piece");
		chooseDarkImage.setToolTipText("Click me to choose an Dark Colored Icon for this piece");
		chooseDarkImage.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// Create the JFileChooser and add image for the new piece
				final JFileChooser fc = new JFileChooser("~/"); // default
																// directory is
																// in the images
																// folder
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					ImageIcon icon = makeIcon(fc, builder);
					darkIconButton.setIcon(icon); // Adds icon to button
					builder.setDarkImage(icon);
				}
			}
		});
		darkIconPanel.add(chooseDarkImage);
		darkIconPanel.add(darkIconButton);

		builder.setDarkImage(temp);

		c.gridx = 0;
		c.gridy = 2;
		piecePanel.add(darkIconPanel, c);

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
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		// Makes the image an icon and ands it to a JLabel
		ImageIcon picture = new ImageIcon(bi);
		JLabel pictureHolder = new JLabel();
		picture.setImage(picture.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH));
		pictureHolder.setIcon(picture);

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
		movement.add(pictureHolder, c);
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
		final JTextField dist = new JTextField(3);
		dist.setToolTipText("Greatest amount of spaces piece can travel in chosen direction");

		final JTextField knight = new JTextField(2);
		knight.setToolTipText("Enter the knight-like directions you would like");
		knight.setEnabled(false);

		final JTextField knightSecond = new JTextField(2);
		knightSecond.setToolTipText("Enter the other direction for the knight-like piece");
		knightSecond.setEnabled(false);

		final JCheckBox knightOn = new JCheckBox("Knight-like Movements", false);
		knightOn.setToolTipText("Press me to turn on Knight-Like Movements for this piece");
		knightOn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				knightLike = !knightLike;
				if (knightLike)
				{
					knight.setEnabled(true);
					knightSecond.setEnabled(true);
				}
				else
				{
					knight.setEnabled(false);
					knightSecond.setEnabled(false);
				}
			}
		});

		final JCheckBox leaper = new JCheckBox("Can jump other Pieces", false);
		leaper.setToolTipText("Press me to allow this piece to jump others");

		final JPanel knightMoving = new JPanel();
		knightMoving.setToolTipText("Use me to set up Knight Like Movements. See Variant Help for instructions");
		knightMoving.setLayout(new FlowLayout());
		knightMoving.add(knight);
		knightMoving.add(new JLabel("x"));
		knightMoving.add(knightSecond);

		// Setting up a panel handle movement instructions
		JPanel movementSetup = new JPanel();
		movementSetup.setLayout(new BoxLayout(movementSetup, BoxLayout.Y_AXIS));
		movementSetup.setLayout(new GridBagLayout());

		// Adds options and labels for setting up movement for the new piece
		c.insets = new Insets(5, 0, 5, 0);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		movementSetup.add(new JLabel("<html><u>Normal Movement Setup:</u></br></html>"), c);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		movementSetup.add(movement, c);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 5;
		movementSetup.add(leaper, c);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 6;
		movementSetup.add(knightOn, c);
		c.insets = new Insets(5, 0, 5, 0);
		c.gridx = 0;
		c.gridy = 7;
		movementSetup.add(new JLabel("<html><u>Knight-like Movement Directions:</u></br></html>"), c);
		c.gridx = 0;
		c.gridy = 8;
		movementSetup.add(knightMoving, c);

		c.gridx = 0;
		c.gridy = 5;
		piecePanel.add(movementSetup, c);

		// Create button and add ActionListener
		final JButton done = new JButton("Save Piece");
		done.setToolTipText("Press me to save this Piece type");
		done.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
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

				if (knight.isEnabled())
				{
					if (isIntKnights())
					{
						builder.addMove('x', Integer.parseInt(knight.getText())); // Knight
																					// movements
																					// stored
																					// in
																					// builder.
						builder.addMove('y', Integer.parseInt(knightSecond.getText()));
					}
				}
				builder.setName(name.getText());// Set the name in the
												// PieceBuilder
				PieceBuilder.savePieceType(builder);// Save the piece type in
													// the PieceBuilder class.

				// Refreshing the window
				builder = new PieceBuilder();
				name.setText("");
				lightIconButton.setIcon(temp);
				darkIconButton.setIcon(temp);
				dist.setText("");
				leaper.setSelected(false);
				knightOn.setSelected(false);
				knight.setText("");
				knight.setEnabled(false);
				knightSecond.setText("");
				knightSecond.setEnabled(false);
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
					Integer.parseInt(knight.getText());
					Integer.parseInt(knightSecond.getText());
					return true;
				}
				catch (Exception e)
				{
					return false;
				}
			}
		});

		final JButton next = new JButton("Done");
		next.setToolTipText("Press me when you have made all of your pieces");
		next.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (name.getText().equals(""))
				{
					variant.setupPieces();
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
						variant.setupPieces();
						holder.removeAll();
						frame.setVisible(false);
					}
				}
			}
		});

		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.add(done);

		c.gridx = 0;
		c.gridy = 7;
		piecePanel.add(buttons, c);

		c.gridx = 0;
		c.gridy = 0;
		add(piecePanel, c);

		JPanel mainButtons = new JPanel();
		mainButtons.setLayout(new FlowLayout());
		c.gridx = 0;
		c.gridy = 1;
		mainButtons.add(next, c);
		c.gridx = 0;
		c.gridy = 1;
		add(mainButtons, c);

		frame.pack();
	}

	/**
	 * Makes the icon for the for the new piece
	 * 
	 * @param fc The file chooser to select the piece
	 * @param builder The builder that is building the piece
	 * @return the icon to add to the button displaying the icon for the new
	 * piece
	 */
	public ImageIcon makeIcon(JFileChooser fc, PieceBuilder builder)
	{
		// If a valid File was chosen, make an ImageIcon from the file path.
		String file = fc.getSelectedFile().getAbsolutePath();

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
