package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
import javax.swing.filechooser.FileFilter;

import logic.PieceBuilder;
import utility.GuiUtility;
import utility.ImageUtility;

public class PieceMakerPanel extends JPanel
{
	public PieceMakerPanel(CustomSetupPanel customSetupMenu, JFrame optionsFrame)
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

	private void initGUIComponents(final CustomSetupPanel customSetupMenu)
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
		GuiUtility.requestFocus(pieceNameField);
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
		lightImageButton.addActionListener(new ImageButtonActionListener(lightIconLabel, false));
		lightIconPanel.add(lightImageButton);
		lightIconPanel.add(lightIconLabel);

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
		darkImageButton.addActionListener(new ImageButtonActionListener(darkIconLabel, true));
		darkIconPanel.add(darkImageButton);
		darkIconPanel.add(darkIconLabel);

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

		JLabel movementPictureHolder = new JLabel(GuiUtility.createImageIcon(130, 130, "/movement_directions.png"));

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
				String pieceName = pieceNameField.getText().trim();
				if (pieceName.isEmpty() || PieceBuilder.isPieceType(pieceNameField.getText()))
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

				try
				{
					ImageUtility.writeLightImage(pieceName, m_lightImage);
					ImageUtility.writeDarkImage(pieceName, m_darkImage);
				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(Driver.getInstance(), "Cannot write image files..did you remember to set both images?", "Image Error",
							JOptionPane.PLAIN_MESSAGE);
					return;
				}

				m_builder.setName(pieceName);
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
				if (pieceNameField.getText().trim().isEmpty())
				{
					customSetupMenu.setupPiecesList();
					PieceMakerPanel.this.removeAll();
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
						PieceMakerPanel.this.removeAll();
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

	private final class ImageButtonActionListener implements ActionListener
	{
		public ImageButtonActionListener(JLabel imageLabel, boolean isDarkImage)
		{
			m_imageLabel = imageLabel;
			m_isDarkImage = isDarkImage;
		}

		@Override
		public void actionPerformed(ActionEvent event)
		{
			Object[] options = new String[] { "Browse My Computer", "Image from Internet", "Cancel" };

			switch (JOptionPane.showOptionDialog(Driver.getInstance(), "Where would you like to get the image from?", "Choose Image",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
			{
			case JOptionPane.YES_OPTION:
				JFileChooser fileChooser = new JFileChooser("~/");
				fileChooser.setFileFilter(new FileFilter()
				{
					@Override
					public String getDescription()
					{
						return "PNG Files";
					}
					
					@Override
					public boolean accept(File f)
					{
						if (f.isDirectory() || f.getName().endsWith(".png"))
                            return true;
						else
							return false;
					}
				});

				if (fileChooser.showOpenDialog(Driver.getInstance()) == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						if (m_isDarkImage)
						{
							m_darkImage = ImageIO.read(new File(fileChooser.toString()));
							m_imageLabel.setIcon(new ImageIcon(m_darkImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
						}
						else
						{
							m_lightImage = ImageIO.read(new File(fileChooser.toString()));
							m_imageLabel.setIcon(new ImageIcon(m_lightImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
						}
					}
					catch (IOException e)
					{
						// TODO we should show the user an error message if this fails
						e.printStackTrace();
					}
				}
				break;
			case JOptionPane.NO_OPTION:
				String url = JOptionPane.showInputDialog(Driver.getInstance(), "Enter the URL of the image:", "Input URL",
						JOptionPane.PLAIN_MESSAGE);
				try
				{
					if (m_isDarkImage)
					{
						m_darkImage = ImageIO.read(new URL(url));
						m_imageLabel.setIcon(new ImageIcon(m_darkImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
					}
					else
					{
						m_lightImage = ImageIO.read(new URL(url));
						m_imageLabel.setIcon(new ImageIcon(m_lightImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
					}
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

		private final boolean m_isDarkImage;

		private JLabel m_imageLabel;
	}

	private static final long serialVersionUID = -6530771731937840358L;
	private static final String[] DIRECTIONS = new String[] { "North", "Northeast", "East", "Southeast", "South", "Southwest", "West",
			"Northwest" };

	private PieceBuilder m_builder;
	private boolean m_isKnightLikePiece;
	private BufferedImage m_lightImage;
	private BufferedImage m_darkImage;
	private JFrame m_frame;
}
