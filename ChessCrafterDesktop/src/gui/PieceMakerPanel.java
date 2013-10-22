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
import utility.GuiUtility;
import utility.ImageUtility;

public class PieceMakerPanel extends JPanel
{
	public PieceMakerPanel(PieceMenuPanel menuPanel)
	{
		mLeaperCheckBox = new JCheckBox(Messages.getString("PieceMakerPanel.canJump"), false); //$NON-NLS-1$
		mPieceNameField = new JTextField(15);
		mKnightMovementsCheckBox = new JCheckBox(Messages.getString("PieceMakerPanel.knightLike"), false); //$NON-NLS-1$
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
		mFrame = new JFrame(Messages.getString("PieceMakerPanel.pieceEditor")); //$NON-NLS-1$
		mFrame.add(this);
		mFrame.setSize(400, 600);
		mFrame.setLocationRelativeTo(Driver.getInstance());
		mLeaperCheckBox = new JCheckBox(Messages.getString("PieceMakerPanel.canJump"), false); //$NON-NLS-1$
		mKnightMovementsCheckBox = new JCheckBox(Messages.getString("PieceMakerPanel.knightLike"), false); //$NON-NLS-1$
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
		pieceCreationPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PieceMakerPanel.newPiece"))); //$NON-NLS-1$

		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout());

		namePanel.add(new JLabel(Messages.getString("PieceMakerPanel.pieceName"))); //$NON-NLS-1$
		mPieceNameField.setToolTipText(Messages.getString("PieceMakerPanel.enterNameOfNewPiece")); //$NON-NLS-1$
		if (builder != null)
			mPieceNameField.setText(builder.getName());
		GuiUtility.requestFocus(mPieceNameField);
		namePanel.add(mPieceNameField);

		constraints.gridx = 0;
		constraints.gridy = 0;
		pieceCreationPanel.add(namePanel, constraints);

		ImageIcon blankSquare = null;
		try
		{
			blankSquare = GuiUtility.createImageIcon(48, 48, "/WhiteSquare.png"); //$NON-NLS-1$
		}
		catch (IOException e4)
		{
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}

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
			e3.printStackTrace();
		}

		if (builder != null)
		{
			try
			{
				mLightImage = GuiUtility.createBufferedImage(48, 48, "l_" + builder.getName() + ".png"); //$NON-NLS-1$ //$NON-NLS-2$
				mDarkImage = GuiUtility.createBufferedImage(48, 48, "d_" + builder.getName() + ".png"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}

		final JButton lightImageButton = new JButton(Messages.getString("PieceMakerPanel.chooseLightImage")); //$NON-NLS-1$
		lightImageButton.setToolTipText(Messages.getString("PieceMakerPanel.clickForLight")); //$NON-NLS-1$
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

		try
		{
			darkIconLabel.setIcon(builder == null ? blankSquare : ImageUtility.getDarkImage(builder.getName()));
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}

		final JButton darkImageButton = new JButton(Messages.getString("PieceMakerPanel.chooseDark")); //$NON-NLS-1$
		darkImageButton.setToolTipText(Messages.getString("PieceMakerPanel.clickForDark")); //$NON-NLS-1$
		darkImageButton.addActionListener(new ImageButtonActionListener(darkIconLabel, true));
		darkIconPanel.add(darkImageButton);
		darkIconPanel.add(darkIconLabel);

		constraints.gridx = 0;
		constraints.gridy = 2;
		pieceCreationPanel.add(darkIconPanel, constraints);

		// TODO: What is this? I don't think we use it, and I can't imagine
		// needing it...
		final JComboBox dropdown = new JComboBox(DIRECTIONS);
		dropdown.setToolTipText(Messages.getString("PieceMakerPanel.thisDropDown")); //$NON-NLS-1$

		Map<Character, Integer> moveMap = builder == null ? null : builder.getMovements();

		mNorthField.setToolTipText(Messages.getString("PieceMakerPanel.north")); //$NON-NLS-1$
		mNorthField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.NORTH)); //$NON-NLS-1$ //$NON-NLS-2$
		mNorthEastField.setToolTipText(Messages.getString("PieceMakerPanel.northEast")); //$NON-NLS-1$
		mNorthEastField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.NORTHEAST)); //$NON-NLS-1$ //$NON-NLS-2$
		mNorthWestField.setToolTipText(Messages.getString("PieceMakerPanel.northwest")); //$NON-NLS-1$
		mNorthWestField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.NORTHWEST)); //$NON-NLS-1$ //$NON-NLS-2$
		mEastField.setToolTipText(Messages.getString("PieceMakerPanel.east")); //$NON-NLS-1$
		mEastField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.EAST)); //$NON-NLS-1$ //$NON-NLS-2$
		mSouthEastField.setToolTipText(Messages.getString("PieceMakerPanel.southEast")); //$NON-NLS-1$
		mSouthEastField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.SOUTHEAST)); //$NON-NLS-1$ //$NON-NLS-2$
		mSouthField.setToolTipText(Messages.getString("PieceMakerPanel.south")); //$NON-NLS-1$
		mSouthField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.SOUTH)); //$NON-NLS-1$ //$NON-NLS-2$
		mSouthWestField.setToolTipText(Messages.getString("PieceMakerPanel.southWest")); //$NON-NLS-1$
		mSouthWestField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.SOUTHWEST)); //$NON-NLS-1$ //$NON-NLS-2$
		mWestField.setToolTipText(Messages.getString("PieceMakerPanel.west")); //$NON-NLS-1$
		mWestField.setText(builder == null ? "0" : "" + moveMap.get(PieceBuilder.WEST)); //$NON-NLS-1$ //$NON-NLS-2$

		JLabel movementPictureHolder = null;
		try
		{
			movementPictureHolder = new JLabel(GuiUtility.createImageIcon(130, 130, "/movement_directions.png")); //$NON-NLS-1$
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
		distanceField.setToolTipText(Messages.getString("PieceMakerPanel.greatestSpaces")); //$NON-NLS-1$

		mKnightOneField.setToolTipText(Messages.getString("PieceMakerPanel.enterKnightLike")); //$NON-NLS-1$

		mKnightTwoField.setToolTipText(Messages.getString("PieceMakerPanel.enterOtherDirection")); //$NON-NLS-1$
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
				mKnightOneField.setText(builder.getMovements().get(PieceBuilder.KNIGHT_ONE) + ""); //$NON-NLS-1$
			mKnightTwoField.setEnabled(builder.getIsKnightLike());
			if (mKnightTwoField.isEnabled())
				mKnightTwoField.setText(builder.getMovements().get(PieceBuilder.KNIGHT_TWO) + ""); //$NON-NLS-1$
		}

		mKnightMovementsCheckBox.setToolTipText(Messages.getString("PieceMakerPanel.pressForKnightLike")); //$NON-NLS-1$

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

		mLeaperCheckBox.setToolTipText(Messages.getString("PieceMakerPanel.pressForJump")); //$NON-NLS-1$
		if (builder != null)
			mLeaperCheckBox.setSelected(builder.canJump());

		final JPanel knightMovementPanel = new JPanel();
		knightMovementPanel.setToolTipText(Messages.getString("PieceMakerPanel.useForKnight")); //$NON-NLS-1$
		knightMovementPanel.setLayout(new FlowLayout());
		knightMovementPanel.add(mKnightOneField);
		knightMovementPanel.add(new JLabel(Messages.getString("PieceMakerPanel.51"))); //$NON-NLS-1$
		knightMovementPanel.add(mKnightTwoField);

		JPanel movementPanel = new JPanel();
		movementPanel.setLayout(new BoxLayout(movementPanel, BoxLayout.Y_AXIS));
		movementPanel.setLayout(new GridBagLayout());

		constraints.insets = new Insets(5, 0, 5, 0);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		movementPanel.add(new JLabel(Messages.getString("PieceMakerPanel.normalMovementHTML")), constraints); //$NON-NLS-1$
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
		movementPanel.add(new JLabel(Messages.getString("PieceMakerPanel.knightLikeMovementHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 8;
		movementPanel.add(knightMovementPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		pieceCreationPanel.add(movementPanel, constraints);

		final JButton savePieceButton = new JButton(Messages.getString("PieceMakerPanel.saveAndReturn")); //$NON-NLS-1$
		savePieceButton.setToolTipText(Messages.getString("PieceMakerPanel.pressToSave")); //$NON-NLS-1$
		savePieceButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				String pieceName = mPieceNameField.getText().trim();
				if (pieceName.isEmpty() || PieceBuilder.isPieceType(mPieceNameField.getText()))
				{
					JOptionPane.showMessageDialog(
							Driver.getInstance(),
							Messages.getString("PieceMakerPanel.enterUniqueName"), Messages.getString("PieceMakerPanel.invalidPieceName"), //$NON-NLS-1$ //$NON-NLS-2$
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
					JOptionPane.showMessageDialog(
							Driver.getInstance(),
							Messages.getString("PieceMakerPanel.cannotWriteImageFiles"), Messages.getString("PieceMakerPanel.ImageError"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
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

		final JButton cancelButton = new JButton(Messages.getString("PieceMakerPanel.cancel")); //$NON-NLS-1$
		cancelButton.setToolTipText(Messages.getString("PieceMakerPanel.pressToReturn")); //$NON-NLS-1$
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
								Messages.getString("PieceMakerPanel.ifYouContinue"), Messages.getString("PieceMakerPanel.pieceMaker"), //$NON-NLS-1$ //$NON-NLS-2$
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
				if (!mNorthField.getText().equals(moveMap.get(PieceBuilder.NORTH) + "")) //$NON-NLS-1$
					changed = true;
				if (!mNorthEastField.getText().equals(moveMap.get(PieceBuilder.NORTHEAST) + "")) //$NON-NLS-1$
					changed = true;
				if (!mNorthWestField.getText().equals(moveMap.get(PieceBuilder.NORTHWEST) + "")) //$NON-NLS-1$
					changed = true;
				if (!mEastField.getText().equals(moveMap.get(PieceBuilder.EAST) + "")) //$NON-NLS-1$
					changed = true;
				if (!mSouthEastField.getText().equals(moveMap.get(PieceBuilder.SOUTHEAST) + "")) //$NON-NLS-1$
					changed = true;
				if (!mSouthField.getText().equals(moveMap.get(PieceBuilder.SOUTH) + "")) //$NON-NLS-1$
					changed = true;
				if (!mSouthWestField.getText().equals(moveMap.get(PieceBuilder.SOUTHWEST) + "")) //$NON-NLS-1$
					changed = true;
				if (!mWestField.getText().equals(moveMap.get(PieceBuilder.WEST) + "")) //$NON-NLS-1$
					changed = true;
				if (mKnightOneField.isEnabled() && !mKnightOneField.getText().equals(moveMap.get(PieceBuilder.KNIGHT_ONE) + "")) //$NON-NLS-1$
					changed = true;
				if (mKnightTwoField.isEnabled() && !mKnightTwoField.getText().equals(moveMap.get(PieceBuilder.KNIGHT_TWO) + "")) //$NON-NLS-1$
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
			JOptionPane
					.showMessageDialog(
							Driver.getInstance(),
							Messages.getString("PieceMakerPanel.allMovementDist") + textField.getToolTipText() //$NON-NLS-1$
									+ Messages.getString("PieceMakerPanel.directionBox"), Messages.getString("PieceMakerPanel.error"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
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
			Object[] options = new String[] {
					Messages.getString("PieceMakerPanel.browseComputer"), Messages.getString("PieceMakerPanel.imageFromInternet"), Messages.getString("PieceMakerPanel.cancel") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			switch (JOptionPane.showOptionDialog(Driver.getInstance(),
					Messages.getString("PieceMakerPanel.whereFrom"), Messages.getString("PieceMakerPanel.chooseImage"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
			{
			case JOptionPane.YES_OPTION:
				JFileChooser fileChooser = new JFileChooser("~/"); //$NON-NLS-1$
				fileChooser.setFileFilter(new FileFilter()
				{
					@Override
					public String getDescription()
					{
						return "PNG Files"; //$NON-NLS-1$
					}

					@Override
					public boolean accept(File f)
					{
						if (f.isDirectory() || f.getName().endsWith(".png")) //$NON-NLS-1$
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
				String url = JOptionPane.showInputDialog(Driver.getInstance(),
						Messages.getString("PieceMakerPanel.enterURL"), Messages.getString("PieceMakerPanel.inputURL"), //$NON-NLS-1$ //$NON-NLS-2$
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
	private static final String[] DIRECTIONS = new String[] {
			Messages.getString("PieceMakerPanel.north"), Messages.getString("PieceMakerPanel.northEast"), Messages.getString("PieceMakerPanel.east"), Messages.getString("PieceMakerPanel.southEast"), Messages.getString("PieceMakerPanel.south"), Messages.getString("PieceMakerPanel.southWest"), Messages.getString("PieceMakerPanel.west"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			Messages.getString("PieceMakerPanel.northWest") }; //$NON-NLS-1$

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
