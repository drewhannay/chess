package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import logic.PieceBuilder;
import utility.GUIUtility;

public class PieceMaker extends JPanel
{
	public PieceMaker(CustomSetupMenu customSetupMenu, JFrame optionsFrame)
	{
		m_frame = optionsFrame;
		m_frame.setVisible(true);
		m_frame.add(this);
		m_frame.setVisible(true);
		m_frame.setSize(400, 600);
		m_frame.setLocationRelativeTo(Driver.getInstance());
		PieceBuilder.initPieceTypes();
		initGUIComponents(customSetupMenu);
	}

	private void initGUIComponents(final CustomSetupMenu customSetupMenu)
	{
		m_builder = new PieceBuilder();

		setSize(550, 875);
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		JPanel pieceCreationPanel = new JPanel();
		pieceCreationPanel.setLayout(new GridBagLayout());
		pieceCreationPanel.setBorder(BorderFactory.createTitledBorder("New Piece"));

		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout());

		namePanel.add(new JLabel("Piece Name:"));
		final JTextField pieceNameField = new JTextField(15);
		pieceNameField.setToolTipText("Enter the name of the new piece here");
		GUIUtility.requestFocus(pieceNameField);
		namePanel.add(pieceNameField);

		constraints.gridx = 0;
		constraints.gridy = 0;
		pieceCreationPanel.add(namePanel, constraints);

		final ImageIcon blankSquare = new ImageIcon("./images/WhiteSquare.gif");
		blankSquare.setImage(blankSquare.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		final JPanel lightIconPanel = new JPanel();
		lightIconPanel.setLayout(new FlowLayout());
		final JLabel lightIconLabel = new JLabel();
		lightIconLabel.setSize(48, 48);
		lightIconLabel.setIcon(blankSquare);

		final JButton lightImageButton = new JButton("Choose image for light piece");
		lightImageButton.setToolTipText("Click me to choose an Light Colored Icon for this piece");
		lightImageButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Object[] options = new String[] { "Browse My Computer", "Image from Internet", "Cancel" };

				switch (JOptionPane.showOptionDialog(Driver.getInstance(), "Where would you like to get the image from?", "Choose Image",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
				{
				case JOptionPane.YES_OPTION:
					final JFileChooser fileChooser = new JFileChooser("~/");
					if (fileChooser.showOpenDialog(Driver.getInstance()) == JFileChooser.APPROVE_OPTION)
					{
						ImageIcon lightIcon = GUIUtility.createImageIcon(48, 48, fileChooser.toString(), PieceMaker.this);
						lightIconLabel.setIcon(lightIcon);
						m_builder.setLightImage(lightIcon);
					}
					break;
				case JOptionPane.NO_OPTION:
					String url = JOptionPane.showInputDialog(Driver.getInstance(), "Enter URL of the image:", "Input URL", JOptionPane.PLAIN_MESSAGE);
					try
					{
						ImageIcon image = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(48, 48, Image.SCALE_SMOOTH));
						lightIconLabel.setIcon(image);
						m_builder.setLightImage(image);
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

		m_builder.setLightImage(blankSquare);

		constraints.gridx = 0;
		constraints.gridy = 3;
		pieceCreationPanel.add(lightIconPanel, constraints);

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

				switch (JOptionPane.showOptionDialog(Driver.getInstance(), "Where would you like to get the image from?", "Choose Image",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
				{
				case JOptionPane.YES_OPTION:
					JFileChooser fileChooser = new JFileChooser("~/");
					if (fileChooser.showOpenDialog(Driver.getInstance()) == JFileChooser.APPROVE_OPTION)
					{
						ImageIcon icon = GUIUtility.createImageIcon(48, 48, fileChooser.toString(), PieceMaker.this);
						darkIconLabel.setIcon(icon);
						m_builder.setDarkImage(icon);
					}
					break;
				case JOptionPane.NO_OPTION:
					String url = JOptionPane.showInputDialog(Driver.getInstance(), "Enter the URL of the image:", "Input URL",
							JOptionPane.PLAIN_MESSAGE);
					try
					{
						ImageIcon image = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(48, 48, Image.SCALE_SMOOTH));
						darkIconLabel.setIcon(image);
						m_builder.setDarkImage(image);
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

		m_builder.setDarkImage(blankSquare);

		constraints.gridx = 0;
		constraints.gridy = 2;
		pieceCreationPanel.add(darkIconPanel, constraints);

		final JComboBox dropdown = new JComboBox(DIRECTIONS);
		dropdown.setToolTipText("This dropdown has all of the valid directions you can still set movement for");

		final JTextField northField = new JTextField(4);
		northField.setToolTipText("North");
		northField.setText("0");
		final JTextField northEastField = new JTextField(4);
		northEastField.setToolTipText("Northeast");
		northEastField.setText("0");
		final JTextField northWestField = new JTextField(4);
		northWestField.setToolTipText("Northwest");
		northWestField.setText("0");
		final JTextField eastField = new JTextField(4);
		eastField.setToolTipText("East");
		eastField.setText("0");
		final JTextField southEastField = new JTextField(4);
		southEastField.setToolTipText("Southeast");
		southEastField.setText("0");
		final JTextField southField = new JTextField(4);
		southField.setToolTipText("South");
		southField.setText("0");
		final JTextField southWestField = new JTextField(4);
		southWestField.setToolTipText("Southwest");
		southWestField.setText("0");
		final JTextField westField = new JTextField(4);
		westField.setToolTipText("West");
		westField.setText("0");

		JLabel movementPictureHolder = new JLabel(GUIUtility.createImageIcon(130, 130, "/movement_directions.png", this));

		JPanel movement = new JPanel();
		movement.setLayout(new GridBagLayout());

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		movement.add(northWestField, constraints);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		movement.add(northField, constraints);
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		movement.add(northEastField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		movement.add(westField, constraints);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		movement.add(movementPictureHolder, constraints);
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		movement.add(eastField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		movement.add(southWestField, constraints);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		movement.add(southField, constraints);
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		movement.add(southEastField, constraints);

		final JTextField distanceField = new JTextField(3);
		distanceField.setToolTipText("Greatest amount of spaces piece can travel in chosen direction");

		final JTextField firstKnightDirectionField = new JTextField(2);
		firstKnightDirectionField.setToolTipText("Enter the knight-like directions you would like");
		firstKnightDirectionField.setEnabled(false);

		final JTextField secondKnightDirectionField = new JTextField(2);
		secondKnightDirectionField.setToolTipText("Enter the other direction for the knight-like piece");
		secondKnightDirectionField.setEnabled(false);

		final JCheckBox knightMovementsCheckBox = new JCheckBox("Knight-like Movements", false);
		knightMovementsCheckBox.setToolTipText("Press me to turn on Knight-Like Movements for this piece");
		knightMovementsCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				m_isKnightLikePiece = !m_isKnightLikePiece;
				firstKnightDirectionField.setEnabled(m_isKnightLikePiece);
				secondKnightDirectionField.setEnabled(m_isKnightLikePiece);
			}
		});

		final JCheckBox leaperCheckBox = new JCheckBox("Can jump other Pieces", false);
		leaperCheckBox.setToolTipText("Press me to allow this piece to jump others");

		final JPanel knightMovementPanel = new JPanel();
		knightMovementPanel.setToolTipText("Use me to set up Knight Like Movements. See Variant Help for instructions");
		knightMovementPanel.setLayout(new FlowLayout());
		knightMovementPanel.add(firstKnightDirectionField);
		knightMovementPanel.add(new JLabel("x"));
		knightMovementPanel.add(secondKnightDirectionField);

		JPanel movementPanel = new JPanel();
		movementPanel.setLayout(new BoxLayout(movementPanel, BoxLayout.Y_AXIS));
		movementPanel.setLayout(new GridBagLayout());

		constraints.insets = new Insets(5, 0, 5, 0);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		movementPanel.add(new JLabel("<html><u>Normal Movement Setup:</u></br></html>"), constraints);
		constraints.insets = new Insets(5, 0, 0, 0);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		movementPanel.add(movement, constraints);
		constraints.insets = new Insets(5, 0, 0, 0);
		constraints.gridx = 0;
		constraints.gridy = 5;
		movementPanel.add(leaperCheckBox, constraints);
		constraints.insets = new Insets(5, 0, 0, 0);
		constraints.gridx = 0;
		constraints.gridy = 6;
		movementPanel.add(knightMovementsCheckBox, constraints);
		constraints.insets = new Insets(5, 0, 5, 0);
		constraints.gridx = 0;
		constraints.gridy = 7;
		movementPanel.add(new JLabel("<html><u>Knight-like Movement Directions:</u></br></html>"), constraints);
		constraints.gridx = 0;
		constraints.gridy = 8;
		movementPanel.add(knightMovementPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		pieceCreationPanel.add(movementPanel, constraints);

		final JButton savePieceButton = new JButton("Save Piece");
		savePieceButton.setToolTipText("Press me to save this Piece type");
		savePieceButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (pieceNameField.getText() == "" || PieceBuilder.isPieceType(pieceNameField.getText()))
				{
					JOptionPane.showMessageDialog(Driver.getInstance(), "Please enter a unique piece name.", "Invalid Piece Name",
							JOptionPane.PLAIN_MESSAGE);
					return;
				}

				if (isIntegerDistance(northField) && isIntegerDistance(northEastField) && isIntegerDistance(northWestField)
						&& isIntegerDistance(eastField) && isIntegerDistance(westField) && isIntegerDistance(southField)
						&& isIntegerDistance(southEastField) && isIntegerDistance(southWestField))
				{
					m_builder.addMove('N', Integer.parseInt(northField.getText()));
					m_builder.addMove('R', Integer.parseInt(northEastField.getText()));
					m_builder.addMove('L', Integer.parseInt(northWestField.getText()));
					m_builder.addMove('E', Integer.parseInt(eastField.getText()));
					m_builder.addMove('W', Integer.parseInt(westField.getText()));
					m_builder.addMove('S', Integer.parseInt(southField.getText()));
					m_builder.addMove('r', Integer.parseInt(southEastField.getText()));
					m_builder.addMove('l', Integer.parseInt(southWestField.getText()));
				}

				if (firstKnightDirectionField.isEnabled())
				{
					if (isIntegerDistance(firstKnightDirectionField) && isIntegerDistance(secondKnightDirectionField))
					{
						m_builder.addMove('x', Integer.parseInt(firstKnightDirectionField.getText()));
						m_builder.addMove('y', Integer.parseInt(secondKnightDirectionField.getText()));
					}
				}
				m_builder.setName(pieceNameField.getText());
				PieceBuilder.savePieceType(m_builder);

				m_builder = new PieceBuilder();
				pieceNameField.setText("");
				lightIconLabel.setIcon(blankSquare);
				darkIconLabel.setIcon(blankSquare);
				distanceField.setText("");
				leaperCheckBox.setSelected(false);
				knightMovementsCheckBox.setSelected(false);
				firstKnightDirectionField.setText("");
				firstKnightDirectionField.setEnabled(false);
				secondKnightDirectionField.setText("");
				secondKnightDirectionField.setEnabled(false);
				m_isKnightLikePiece = false;
				dropdown.removeAllItems();
				for (int i = 0; i < DIRECTIONS.length; i++)
					dropdown.addItem(DIRECTIONS[i]);
			}
		});

		final JButton doneButton = new JButton("Done");
		doneButton.setToolTipText("Press me when you have made all of your pieces");
		doneButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (pieceNameField.getText().equals(""))
				{
					customSetupMenu.setupPiecesList();
					PieceMaker.this.removeAll();
					m_frame.setVisible(false);
				}
				else
				{
					switch (JOptionPane.showConfirmDialog(Driver.getInstance(),
							"If you continue the piece you are working on will not be saved. Continue?", "Piece Maker",
							JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE))
					{
					case JOptionPane.YES_OPTION:
						customSetupMenu.setupPiecesList();
						PieceMaker.this.removeAll();
						m_frame.setVisible(false);
						break;
					case JOptionPane.NO_OPTION:
						break;
					}
				}
			}
		});

		constraints.gridx = 0;
		constraints.gridy = 7;
		pieceCreationPanel.add(savePieceButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 0;
		add(pieceCreationPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		add(doneButton, constraints);

		m_frame.pack();
	}

	private boolean isIntegerDistance(JTextField textField)
	{
		try
		{
			Integer.parseInt(textField.getText());
			return true;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Driver.getInstance(), "All movement distances must be whole numbers. Please enter a number in the "
					+ textField.getToolTipText() + " direction box.", "Error", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
	}

	private static final long serialVersionUID = -6530771731937840358L;
	private static final String[] DIRECTIONS = new String[] { "North", "Northeast", "East", "Southeast", "South", "Southwest", "West",
			"Northwest" };

	private PieceBuilder m_builder;
	private boolean m_isKnightLikePiece;
	private JFrame m_frame;
}
