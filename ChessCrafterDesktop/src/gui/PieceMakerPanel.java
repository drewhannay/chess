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
import java.util.Map;

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
import utility.FileUtility;
import utility.GuiUtility;
import utility.ImageUtility;

public class PieceMakerPanel extends JPanel
{
	public PieceMakerPanel(PieceMenuPanel menuPanel)
	{
		mLeaperCheckBox = new JCheckBox("Can jump other Pieces", false);
		mPieceNameField = new JTextField(15);
		mKnightMovementsCheckBox = new JCheckBox("Knight-like Movements", false);
		mNorthField = new JTextField(4);
		mNorthEastField = new JTextField(4);
		mEastField = new JTextField(4); 
		mSouthEastField = new JTextField(4);
		mSouthField = new JTextField(4);
		mSouthWestField = new JTextField(4);
		mWestField = new JTextField(4);
		mNorthWestField = new JTextField(4);
		mKnightOneField = new JTextField(4);
		mKnightTwoField = new JTextField(4);
		new PieceMakerPanel(null, menuPanel);
	}

	public PieceMakerPanel(String pieceName, PieceMenuPanel menuPanel)
	{
		mPieceMenuPanel = menuPanel;
		mFrame = new JFrame("Piece Editor");
		mFrame.add(this);
		mFrame.setSize(400, 600);
		mFrame.setLocationRelativeTo(Driver.getInstance());
		mLeaperCheckBox = new JCheckBox("Can jump other Pieces", false);
		mKnightMovementsCheckBox = new JCheckBox("Knight-like Movements", false);
		mPieceNameField = new JTextField(15);
		mNorthField = new JTextField(4);
		mNorthEastField = new JTextField(4);
		mEastField = new JTextField(4); 
		mSouthEastField = new JTextField(4);
		mSouthField = new JTextField(4);
		mSouthWestField = new JTextField(4);
		mWestField = new JTextField(4);
		mNorthWestField = new JTextField(4);
		mKnightOneField = new JTextField(4);
		mKnightTwoField = new JTextField(4);
		PieceBuilder.initPieceTypes();
		PieceBuilder builder = null;
		if (pieceName != null)
			builder = PieceBuilder.loadFromDisk(pieceName);
		initGUIComponents(builder);
	}

	private void initGUIComponents(PieceBuilder builder)
	{
		if (builder == null)
			mBuilder = new PieceBuilder();
		else
			mBuilder = builder;

		setSize(550, 875);
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		JPanel pieceCreationPanel = new JPanel();
		pieceCreationPanel.setLayout(new GridBagLayout());
		pieceCreationPanel.setBorder(BorderFactory.createTitledBorder("New Piece"));

		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout());

		namePanel.add(new JLabel("Piece Name:"));
		mPieceNameField.setToolTipText("Enter the name of the new piece here");
		if (builder != null)
			mPieceNameField.setText(builder.getName());
		GuiUtility.requestFocus(mPieceNameField);
		namePanel.add(mPieceNameField);

		constraints.gridx = 0;
		constraints.gridy = 0;
		pieceCreationPanel.add(namePanel, constraints);

		final ImageIcon blankSquare = new ImageIcon(FileUtility.getImagePath("WhiteSquare.gif", true));
		blankSquare.setImage(blankSquare.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		final JPanel lightIconPanel = new JPanel();
		lightIconPanel.setLayout(new FlowLayout());
		final JLabel lightIconLabel = new JLabel();
		lightIconLabel.setSize(48, 48);
		try
		{
			lightIconLabel.setIcon(builder == null ? blankSquare : ImageUtility.getLightImage(builder.getName()));
		}
		catch (IOException e3)
		{
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		if (builder != null)
		{
			try
			{
				mLightImage = ImageIO.read(new File(FileUtility.getImagePath("l_" + builder.getName() + ".png", false)));
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}

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
		darkIconLabel.setIcon(builder == null ? blankSquare : ImageUtility.getDarkImage(builder.getName()));

		if (builder != null)
		{
			try
			{
				mDarkImage = ImageIO.read(new File(FileUtility.getImagePath("d_" + builder.getName() + ".png", false)));
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}

		final JButton darkImageButton = new JButton("Choose image for dark piece");
		darkImageButton.setToolTipText("Click me to choose an Dark Colored Icon for this piece");
		darkImageButton.addActionListener(new ImageButtonActionListener(darkIconLabel, true));
		darkIconPanel.add(darkImageButton);
		darkIconPanel.add(darkIconLabel);

		constraints.gridx = 0;
		constraints.gridy = 2;
		pieceCreationPanel.add(darkIconPanel, constraints);

		//TODO: What is this? I don't think we use it, and I can't imagine needing it...
		final JComboBox dropdown = new JComboBox(DIRECTIONS);
		dropdown.setToolTipText("This dropdown has all of the valid directions you can still set movement for");

		Map<Character, Integer> moveMap = builder == null ? null : builder.getMovements();

		mNorthField.setToolTipText("North");
		mNorthField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.NORTH));
		mNorthEastField.setToolTipText("Northeast");
		mNorthEastField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.NORTHEAST));
		mNorthWestField.setToolTipText("Northwest");
		mNorthWestField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.NORTHWEST));
		mEastField.setToolTipText("East");
		mEastField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.EAST));
		mSouthEastField.setToolTipText("Southeast");
		mSouthEastField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.SOUTHEAST));
		mSouthField.setToolTipText("South");
		mSouthField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.SOUTH));
		mSouthWestField.setToolTipText("Southwest");
		mSouthWestField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.SOUTHWEST));
		mWestField.setToolTipText("West");
		mWestField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.WEST));

		JLabel movementPictureHolder = null;
		try
		{
			movementPictureHolder = new JLabel(GuiUtility.createImageIcon(130, 130,
					FileUtility.getImagePath("movement_directions.png", true)));
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JPanel movement = new JPanel();
		movement.setLayout(new GridBagLayout());

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		movement.add(mNorthWestField, constraints);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		movement.add(mNorthField, constraints);
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		movement.add(mNorthEastField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		movement.add(mWestField, constraints);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		movement.add(movementPictureHolder, constraints);
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		movement.add(mEastField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		movement.add(mSouthWestField, constraints);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		movement.add(mSouthField, constraints);
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		movement.add(mSouthEastField, constraints);

		final JTextField distanceField = new JTextField(3);
		distanceField.setToolTipText("Greatest amount of spaces piece can travel in chosen direction");

		mKnightOneField.setToolTipText("Enter the knight-like directions you would like");

		mKnightTwoField.setToolTipText("Enter the other direction for the knight-like piece");
		mKnightTwoField.setEnabled(false);

		if (builder == null)
		{
			mKnightOneField.setEnabled(false);
			mKnightTwoField.setEnabled(false);
		}
		else
		{
			mKnightOneField.setEnabled(builder.getIsKnightLike());
			if (mKnightOneField.isEnabled())
				mKnightOneField.setText(builder.getMovements().get(PieceBuilder.KNIGHT_ONE)+"");
			mKnightTwoField.setEnabled(builder.getIsKnightLike());
			if (mKnightTwoField.isEnabled())
				mKnightTwoField.setText(builder.getMovements().get(PieceBuilder.KNIGHT_TWO)+"");
		}
		
		mKnightMovementsCheckBox.setToolTipText("Press me to turn on Knight-Like Movements for this piece");

		if (builder != null)
			mKnightMovementsCheckBox.setSelected(builder.getIsKnightLike());

		mKnightMovementsCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mKnightOneField.setEnabled(mKnightMovementsCheckBox.isSelected());
				mKnightTwoField.setEnabled(mKnightMovementsCheckBox.isSelected());
			}
		});

		
		mLeaperCheckBox.setToolTipText("Press me to allow this piece to jump others");
		if (builder != null)
			mLeaperCheckBox.setSelected(builder.canJump());

		final JPanel knightMovementPanel = new JPanel();
		knightMovementPanel.setToolTipText("Use me to set up Knight Like Movements. See Variant Help for instructions");
		knightMovementPanel.setLayout(new FlowLayout());
		knightMovementPanel.add(mKnightOneField);
		knightMovementPanel.add(new JLabel("x"));
		knightMovementPanel.add(mKnightTwoField);

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
		movementPanel.add(mLeaperCheckBox, constraints);
		constraints.insets = new Insets(5, 0, 0, 0);
		constraints.gridx = 0;
		constraints.gridy = 6;
		movementPanel.add(mKnightMovementsCheckBox, constraints);
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

		final JButton savePieceButton = new JButton("Save and Return");
		savePieceButton.setToolTipText("Press to save this Piece type");
		savePieceButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				String pieceName = mPieceNameField.getText().trim();
				if (pieceName.isEmpty() || PieceBuilder.isPieceType(mPieceNameField.getText()))
				{
					JOptionPane.showMessageDialog(Driver.getInstance(), "Please enter a unique piece name.", "Invalid Piece Name",
							JOptionPane.PLAIN_MESSAGE);
					return;
				}

				if (isIntegerDistance(mNorthField) && isIntegerDistance(mNorthEastField) && isIntegerDistance(mNorthWestField)
						&& isIntegerDistance(mEastField) && isIntegerDistance(mWestField) && isIntegerDistance(mSouthField)
						&& isIntegerDistance(mSouthEastField) && isIntegerDistance(mSouthWestField))
				{
					mBuilder.addMove(PieceBuilder.NORTH, Integer.parseInt(mNorthField.getText()));
					mBuilder.addMove(PieceBuilder.NORTHEAST, Integer.parseInt(mNorthEastField.getText()));
					mBuilder.addMove(PieceBuilder.NORTHWEST, Integer.parseInt(mNorthWestField.getText()));
					mBuilder.addMove(PieceBuilder.EAST, Integer.parseInt(mEastField.getText()));
					mBuilder.addMove(PieceBuilder.WEST, Integer.parseInt(mWestField.getText()));
					mBuilder.addMove(PieceBuilder.SOUTH, Integer.parseInt(mSouthField.getText()));
					mBuilder.addMove(PieceBuilder.SOUTHEAST, Integer.parseInt(mSouthEastField.getText()));
					mBuilder.addMove(PieceBuilder.SOUTHWEST, Integer.parseInt(mSouthWestField.getText()));
				}

				if (mKnightOneField.isEnabled())
				{
					if (isIntegerDistance(mKnightOneField) && isIntegerDistance(mKnightTwoField))
					{
						mBuilder.addMove(PieceBuilder.KNIGHT_ONE, Integer.parseInt(mKnightOneField.getText()));
						mBuilder.addMove(PieceBuilder.KNIGHT_TWO, Integer.parseInt(mKnightTwoField.getText()));
					}
				}

				try
				{
					ImageUtility.writeLightImage(pieceName, mLightImage);
					ImageUtility.writeDarkImage(pieceName, mDarkImage);
				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(Driver.getInstance(),
							"Cannot write image files. Did you remember to set both images?", "Image Error", JOptionPane.PLAIN_MESSAGE);
					return;
				}

				mBuilder.setName(pieceName);
				mBuilder.setCanJump(mLeaperCheckBox.isSelected());
				mBuilder.setIsKnightLike(mKnightMovementsCheckBox.isSelected());
				PieceBuilder.savePieceType(mBuilder);
				PieceBuilder.writeToDisk(mBuilder);
				
				mFrame.dispose();
				mPieceMenuPanel.refreshList();
				PieceMakerPanel.this.removeAll();
			}
		});

		final JButton cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("Press to return without saving.");
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (mPieceNameField.getText().trim().isEmpty())
				{
					mFrame.dispose();
					PieceMakerPanel.this.removeAll();
				}
				else
				{
					if (hasChanged())
					{
						switch (JOptionPane.showConfirmDialog(Driver.getInstance(),
								"If you continue, the piece you are working on will not be saved. Continue?", "Piece Maker",
								JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE))
						{
						case JOptionPane.YES_OPTION:
							mFrame.dispose();
							PieceMakerPanel.this.removeAll();
							break;
						case JOptionPane.NO_OPTION:
							break;
						}	
					}
					else
					{
						mFrame.dispose();
						PieceMakerPanel.this.removeAll();
					}
				}
			}
		});
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(pieceCreationPanel, constraints);

		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 7;
		add(cancelButton, constraints);

		
		constraints.gridx = 1;
		constraints.gridy = 7;
		add(savePieceButton, constraints);
		
		mFrame.setVisible(true);
		mFrame.pack();
	}

	protected boolean hasChanged()
	{
		boolean changed = false;
		if (mBuilder != null)
		{
			try
			{
			if (mBuilder.canJump() != mLeaperCheckBox.isSelected())
				changed = true;
			if (!mBuilder.getName().equals(mPieceNameField.getText()))
				changed = true;
			if (mBuilder.getIsKnightLike() != mKnightMovementsCheckBox.isSelected())
				changed = true;
			Map<Character, Integer> moveMap = mBuilder.getMovements();
			if (!mNorthField.getText().equals(moveMap.get(PieceBuilder.NORTH)+""))
					changed = true;
			if (!mNorthEastField.getText().equals(moveMap.get(PieceBuilder.NORTHEAST)+""))
				changed = true;
			if (!mNorthWestField.getText().equals(moveMap.get(PieceBuilder.NORTHWEST)+""))
				changed = true;
			if (!mEastField.getText().equals(moveMap.get(PieceBuilder.EAST)+""))
				changed = true;
			if (!mSouthEastField.getText().equals(moveMap.get(PieceBuilder.SOUTHEAST)+""))
				changed = true;
			if (!mSouthField.getText().equals(moveMap.get(PieceBuilder.SOUTH)+""))
				changed = true;
			if (!mSouthWestField.getText().equals(moveMap.get(PieceBuilder.SOUTHWEST)+""))
				changed = true;
			if (!mWestField.getText().equals(moveMap.get(PieceBuilder.WEST)+""))
				changed = true;
			if (mKnightOneField.isEnabled() && !mKnightOneField.getText().equals(moveMap.get(PieceBuilder.KNIGHT_ONE)+""))
				changed = true;
			if (mKnightTwoField.isEnabled() && !mKnightTwoField.getText().equals(moveMap.get(PieceBuilder.KNIGHT_TWO)+""))
				changed = true;
			}
			catch (Exception e)
			{
				return true;
			}
		}
		return changed;
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
			JOptionPane.showMessageDialog(Driver.getInstance(),
					"All movement distances must be whole numbers. Please enter a number in the " + textField.getToolTipText()
							+ " direction box.", "Error", JOptionPane.PLAIN_MESSAGE);
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
							mDarkImage = ImageIO.read(fileChooser.getSelectedFile());
							m_imageLabel.setIcon(new ImageIcon(mDarkImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
						}
						else
						{
							mLightImage = ImageIO.read(fileChooser.getSelectedFile());
							m_imageLabel.setIcon(new ImageIcon(mLightImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
						}
					}
					catch (IOException e)
					{
						// TODO we should show the user an error message if this
						// fails
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
						mDarkImage = ImageIO.read(new URL(url));
						m_imageLabel.setIcon(new ImageIcon(mDarkImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
					}
					else
					{
						mLightImage = ImageIO.read(new URL(url));
						m_imageLabel.setIcon(new ImageIcon(mLightImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
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

	private final JCheckBox mKnightMovementsCheckBox;
	private final JTextField mPieceNameField;
	private final JCheckBox mLeaperCheckBox;
	private final JTextField mNorthField;
	private final JTextField mNorthEastField;
	private final JTextField mEastField;
	private final JTextField mSouthEastField;
	private final JTextField mSouthField;
	private final JTextField mSouthWestField;
	private final JTextField mWestField;
	private final JTextField mNorthWestField;
	private final JTextField mKnightOneField;
	private final JTextField mKnightTwoField;
	private PieceBuilder mBuilder;
	private PieceMenuPanel mPieceMenuPanel;
	private BufferedImage mLightImage;
	private BufferedImage mDarkImage;
	private JFrame mFrame;
}
