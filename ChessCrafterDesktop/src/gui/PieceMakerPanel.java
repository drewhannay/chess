package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
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

import com.google.common.collect.Lists;

import logic.BidirectionalMovement;
import logic.GameBuilder;
import logic.PieceBuilder;
import logic.PieceMovements;
import logic.PieceMovements.MovementDirection;
import models.Piece;
import utility.FileUtility;
import utility.GuiUtility;
import utility.ImageUtility;

public class PieceMakerPanel extends ChessPanel
{
	public interface PieceListChangedListener
	{
		public void onPieceListChanged();
	}

	public PieceMakerPanel(String pieceName, JFrame frame)
	{
		mPieceChangedListeners = Lists.newArrayList();
		mFrame = frame;
		mLeaperCheckBox = new JCheckBox(Messages.getString("PieceMakerPanel.canJump"), false); //$NON-NLS-1$
		mLeaperCheckBox.setOpaque(false);
		mLeaperCheckBox.setForeground(Color.white);
		mPieceNameField = new JTextField(15);
		mNorthField = new JTextField(4);
		mNorthEastField = new JTextField(4);
		mEastField = new JTextField(4);
		mSouthEastField = new JTextField(4);
		mSouthField = new JTextField(4);
		mSouthWestField = new JTextField(4);
		mWestField = new JTextField(4);
		mNorthWestField = new JTextField(4);
		mRowMovementField = new JTextField(4);
		mColMovementField = new JTextField(4);
		mAddBidirectionalMoveButton = new JButton(Messages.getString("PieceMakerPanel.add")); //$NON-NLS-1$
		mRemoveBidirectionalMoveButton = new JButton(Messages.getString("PieceMakerPanel.remove")); //$NON-NLS-1$
		PieceBuilder builder = null;
		if (pieceName != null)
		{
			builder = PieceBuilder.loadFromDisk(pieceName);
		}
		mBidirectionalMovementComboBox = new JComboBox();
		mDefaultComboBoxModel = new DefaultComboBoxModel();
		mBidirectionalMovementComboBox.setModel(mDefaultComboBoxModel);
		mDefaultComboBoxModel.addListDataListener(mBidirectionalMovementComboBox);
		if (builder != null)
		{
			Set<BidirectionalMovement> bidirectionalMovements = builder.getPieceMovements().getBidirectionalMovements();

			for (BidirectionalMovement movement : bidirectionalMovements)
				mBidirectionalMovementComboBox.addItem(movement);

			if (mBidirectionalMovementComboBox.getItemCount() != 0)
				mBidirectionalMovementComboBox.setSelectedIndex(0);
			else
				mBidirectionalMovementComboBox.setEnabled(false);
		}
		else
		{
			mBidirectionalMovementComboBox.setEnabled(false);
		}

		initGUIComponents(builder);
	}

	private void initGUIComponents(PieceBuilder builder)
	{
		if (builder == null)
			mBuilder = new PieceBuilder();
		else
			mBuilder = builder;

		mFrame.setSize(550, 875);
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		JPanel pieceCreationPanel = new JPanel();
		pieceCreationPanel.setOpaque(false);
		pieceCreationPanel.setLayout(new GridBagLayout());

		JPanel namePanel = new JPanel();
		namePanel.setOpaque(false);
		namePanel.setLayout(new FlowLayout());

		namePanel.add(GuiUtility.createJLabel(Messages.getString("PieceMakerPanel.pieceName"))); //$NON-NLS-1$
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
			e4.printStackTrace();
		}

		blankSquare.setImage(blankSquare.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		final JPanel lightIconPanel = new JPanel();
		lightIconPanel.setLayout(new FlowLayout());
		lightIconPanel.setOpaque(false);
		final JLabel lightIconLabel = GuiUtility.createJLabel(""); //$NON-NLS-1$
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
		darkIconPanel.setOpaque(false);
		final JLabel darkIconLabel = GuiUtility.createJLabel(""); //$NON-NLS-1$
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

		PieceMovements movements = builder == null ? null : builder.getPieceMovements();

		mNorthField.setToolTipText(Messages.getString("PieceMakerPanel.north")); //$NON-NLS-1$
		mNorthField.setText(builder == null ? "0" : "" + movements.getDistance(MovementDirection.NORTH)); //$NON-NLS-1$ //$NON-NLS-2$
		mNorthEastField.setToolTipText(Messages.getString("PieceMakerPanel.northEast")); //$NON-NLS-1$
		mNorthEastField.setText(builder == null ? "0" : "" + movements.getDistance(MovementDirection.NORTHEAST)); //$NON-NLS-1$ //$NON-NLS-2$
		mNorthWestField.setToolTipText(Messages.getString("PieceMakerPanel.northwest")); //$NON-NLS-1$
		mNorthWestField.setText(builder == null ? "0" : "" + movements.getDistance(MovementDirection.NORTHWEST)); //$NON-NLS-1$ //$NON-NLS-2$
		mEastField.setToolTipText(Messages.getString("PieceMakerPanel.east")); //$NON-NLS-1$
		mEastField.setText(builder == null ? "0" : "" + movements.getDistance(MovementDirection.EAST)); //$NON-NLS-1$ //$NON-NLS-2$
		mSouthEastField.setToolTipText(Messages.getString("PieceMakerPanel.southEast")); //$NON-NLS-1$
		mSouthEastField.setText(builder == null ? "0" : "" + movements.getDistance(MovementDirection.SOUTHEAST)); //$NON-NLS-1$ //$NON-NLS-2$
		mSouthField.setToolTipText(Messages.getString("PieceMakerPanel.south")); //$NON-NLS-1$
		mSouthField.setText(builder == null ? "0" : "" + movements.getDistance(MovementDirection.SOUTH)); //$NON-NLS-1$ //$NON-NLS-2$
		mSouthWestField.setToolTipText(Messages.getString("PieceMakerPanel.southWest")); //$NON-NLS-1$
		mSouthWestField.setText(builder == null ? "0" : "" + movements.getDistance(MovementDirection.SOUTHWEST)); //$NON-NLS-1$ //$NON-NLS-2$
		mWestField.setToolTipText(Messages.getString("PieceMakerPanel.west")); //$NON-NLS-1$
		mWestField.setText(builder == null ? "0" : "" + movements.getDistance(MovementDirection.WEST)); //$NON-NLS-1$ //$NON-NLS-2$

		JLabel movementPictureHolder = null;
		try
		{
			movementPictureHolder = GuiUtility.createJLabel(GuiUtility.createImageIcon(130, 130, "/movement_directions.png")); //$NON-NLS-1$
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		JPanel movement = new JPanel();
		movement.setLayout(new GridBagLayout());
		movement.setOpaque(false);

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

		mRowMovementField.setToolTipText(Messages.getString("PieceMakerPanel.enterKnightLike")); //$NON-NLS-1$
		mColMovementField.setToolTipText(Messages.getString("PieceMakerPanel.enterOtherDirection")); //$NON-NLS-1$
		mRowMovementField.addKeyListener(getBidirectionalFieldKeyListener());
		mColMovementField.addKeyListener(getBidirectionalFieldKeyListener());

		mAddBidirectionalMoveButton.setEnabled(false);
		mAddBidirectionalMoveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				mBidirectionalMovementComboBox.setEnabled(true);
				mRemoveBidirectionalMoveButton.setEnabled(true);
				BidirectionalMovement toAdd = new BidirectionalMovement(Integer.parseInt(mRowMovementField.getText()), Integer
						.parseInt(mColMovementField.getText()));

				if (mDefaultComboBoxModel.getIndexOf(toAdd) == -1)
				{
					mDefaultComboBoxModel.addElement(toAdd);
				}

				mRowMovementField.setText(""); //$NON-NLS-1$
				mColMovementField.setText(""); //$NON-NLS-1$
				mAddBidirectionalMoveButton.setEnabled(false);
			}
		});

		mRemoveBidirectionalMoveButton.setEnabled(mDefaultComboBoxModel.getSize() != 0);
		mRemoveBidirectionalMoveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				mDefaultComboBoxModel.removeElement(mDefaultComboBoxModel.getSelectedItem());
				mBidirectionalMovementComboBox.setEnabled(mDefaultComboBoxModel.getSize() != 0);
				mRemoveBidirectionalMoveButton.setEnabled(mDefaultComboBoxModel.getSize() != 0);
			}
		});

		mLeaperCheckBox.setToolTipText(Messages.getString("PieceMakerPanel.pressForJump")); //$NON-NLS-1$
		if (builder != null)
			mLeaperCheckBox.setSelected(builder.canJump());

		final JPanel bidirectionalMovementPanel = new JPanel();
		bidirectionalMovementPanel.setOpaque(false);
		bidirectionalMovementPanel.setToolTipText(Messages.getString("PieceMakerPanel.useForKnight")); //$NON-NLS-1$
		bidirectionalMovementPanel.setLayout(new GridBagLayout());

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(1, 1, 1, 1);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		bidirectionalMovementPanel.add(mBidirectionalMovementComboBox, constraints);

		final JPanel inputPanel = new JPanel();
		inputPanel.setOpaque(false);
		inputPanel.setLayout(new GridBagLayout());

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		inputPanel.add(mRowMovementField, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		inputPanel.add(GuiUtility.createJLabel(Messages.getString("PieceMakerPanel.51")), constraints); //$NON-NLS-1$

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		inputPanel.add(mColMovementField, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		bidirectionalMovementPanel.add(inputPanel, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.insets = new Insets(10, 0, 10, 0);
		bidirectionalMovementPanel.add(mRemoveBidirectionalMoveButton, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		bidirectionalMovementPanel.add(mAddBidirectionalMoveButton, constraints);

		JPanel movementPanel = new JPanel();
		movementPanel.setOpaque(false);
		movementPanel.setLayout(new BoxLayout(movementPanel, BoxLayout.Y_AXIS));
		movementPanel.setLayout(new GridBagLayout());

		constraints.insets = new Insets(5, 0, 5, 0);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		movementPanel.add(GuiUtility.createJLabel(Messages.getString("PieceMakerPanel.normalMovementHTML")), constraints); //$NON-NLS-1$
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
		constraints.insets = new Insets(5, 0, 5, 0);
		constraints.gridx = 0;
		constraints.gridy = 6;
		movementPanel.add(GuiUtility.createJLabel(Messages.getString("PieceMakerPanel.knightLikeMovementHTML")), constraints); //$NON-NLS-1$
		constraints.gridx = 0;
		constraints.gridy = 7;
		movementPanel.add(bidirectionalMovementPanel, constraints);

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
				String builderName = mBuilder == null? null : mBuilder.getName();
				
				if (pieceName.isEmpty() || (PieceBuilder.isPieceType(pieceName) && !pieceName.equals(builderName)))
				{
					JOptionPane.showMessageDialog(
							PieceMakerPanel.this,
							Messages.getString("PieceMakerPanel.enterUniqueName"), Messages.getString("PieceMakerPanel.invalidPieceName"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.PLAIN_MESSAGE);
					return;
				}

				if (isIntegerDistance(mNorthField) && isIntegerDistance(mNorthEastField) && isIntegerDistance(mNorthWestField)
						&& isIntegerDistance(mEastField) && isIntegerDistance(mWestField) && isIntegerDistance(mSouthField)
						&& isIntegerDistance(mSouthEastField) && isIntegerDistance(mSouthWestField))
				{
					mBuilder.addMovement(MovementDirection.NORTH, Integer.parseInt(mNorthField.getText()));
					mBuilder.addMovement(MovementDirection.NORTHEAST, Integer.parseInt(mNorthEastField.getText()));
					mBuilder.addMovement(MovementDirection.NORTHWEST, Integer.parseInt(mNorthWestField.getText()));
					mBuilder.addMovement(MovementDirection.EAST, Integer.parseInt(mEastField.getText()));
					mBuilder.addMovement(MovementDirection.WEST, Integer.parseInt(mWestField.getText()));
					mBuilder.addMovement(MovementDirection.SOUTH, Integer.parseInt(mSouthField.getText()));
					mBuilder.addMovement(MovementDirection.SOUTHEAST, Integer.parseInt(mSouthEastField.getText()));
					mBuilder.addMovement(MovementDirection.SOUTHWEST, Integer.parseInt(mSouthWestField.getText()));
				}

				if (mDefaultComboBoxModel.getSize() != 0)
				{
					for (int i = 0; i < mBidirectionalMovementComboBox.getItemCount(); i++)
					{
						mBuilder.addBidirectionalMovement((BidirectionalMovement) mBidirectionalMovementComboBox.getItemAt(i));
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
							PieceMakerPanel.this,
							Messages.getString("PieceMakerPanel.cannotWriteImageFiles"), Messages.getString("PieceMakerPanel.ImageError"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					return;
				}

				mBuilder.setName(pieceName);
				mBuilder.setCanJump(mLeaperCheckBox.isSelected());
				PieceBuilder.savePieceType(mBuilder);
				refreshVariants();

				for (PieceListChangedListener listener : mPieceChangedListeners)
					listener.onPieceListChanged();
				mFrame.dispose();
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
				}
				else
				{
					switch (JOptionPane.showConfirmDialog(PieceMakerPanel.this,
							Messages.getString("PieceMakerPanel.ifYouContinue"), Messages.getString("PieceMakerPanel.pieceMaker"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE))
					{
					case JOptionPane.YES_OPTION:
						mFrame.dispose();
						break;
					case JOptionPane.NO_OPTION:
						break;
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

		mFrame.add(this);
		mFrame.pack();
		mFrame.setVisible(true);
	}

	/**
	 * Once a piece has been changed, we need to update any variants that use
	 * this piece to use the new version.
	 */
	private void refreshVariants()
	{
		String[] vars = FileUtility.getVariantsFileArray();
		for (String s : vars)
		{
			try
			{
				File variantFile = FileUtility.getVariantsFile(s);
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(variantFile));
				GameBuilder builder = (GameBuilder) in.readObject();

				for (Piece piece : builder.getWhiteTeam())
				{
					if (piece.getName().equals(mPieceNameField.getText()))
						piece = PieceBuilder.makePiece(piece.getName(), false, piece.getOriginalSquare(), piece.getBoard());
				}
				for (Piece piece : builder.getBlackTeam())
				{
					if (piece.getName().equals(mPieceNameField.getText()))
						piece = PieceBuilder.makePiece(piece.getName(), true, piece.getOriginalSquare(), piece.getBoard());
				}

				in.close();

				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(variantFile));
				out.writeObject(builder);
				out.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private boolean isIntegerDistance(JTextField textField)
	{
		if (textField.getText().equals("")) //$NON-NLS-1$
			return false;
		try
		{
			Integer.parseInt(textField.getText());
			return true;
		}
		catch (Exception e)
		{
			JOptionPane
					.showMessageDialog(
							PieceMakerPanel.this,
							Messages.getString("PieceMakerPanel.allMovementDist") + textField.getToolTipText() //$NON-NLS-1$
									+ Messages.getString("PieceMakerPanel.directionBox"), Messages.getString("PieceMakerPanel.error"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		}
	}

	private KeyListener getBidirectionalFieldKeyListener()
	{
		return new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent arg0)
			{
			}

			@Override
			public void keyReleased(KeyEvent arg0)
			{
				mAddBidirectionalMoveButton.setEnabled(isIntegerDistance(mRowMovementField) && isIntegerDistance(mColMovementField));
			}

			@Override
			public void keyPressed(KeyEvent arg0)
			{
			}
		};
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

			switch (JOptionPane.showOptionDialog(PieceMakerPanel.this,
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

				if (fileChooser.showOpenDialog(PieceMakerPanel.this) == JFileChooser.APPROVE_OPTION)
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
				String url = JOptionPane.showInputDialog(PieceMakerPanel.this,
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

	public void addPieceListListener(PieceListChangedListener listener)
	{
		mPieceChangedListeners.add(listener);
	}
	
	public void clearPieceListeners()
	{
		mPieceChangedListeners.clear();
	}
	
	private static final long serialVersionUID = -6530771731937840358L;

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
	private final JTextField mRowMovementField;
	private final JTextField mColMovementField;
	private final JComboBox mBidirectionalMovementComboBox;
	private final JButton mAddBidirectionalMoveButton;
	private final JButton mRemoveBidirectionalMoveButton;
	private final DefaultComboBoxModel mDefaultComboBoxModel;
	private PieceBuilder mBuilder;
	private BufferedImage mLightImage;
	private BufferedImage mDarkImage;
	private JFrame mFrame;
	private List<PieceListChangedListener> mPieceChangedListeners;
}
