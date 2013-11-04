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
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
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

import logic.BidirectionalMovement;
import logic.GameBuilder;
import logic.PieceBuilder;
import logic.PieceMovements;
import logic.PieceMovements.MovementDirection;
import models.Piece;
import utility.FileUtility;
import utility.GuiUtility;
import utility.ImageUtility;

import com.google.common.collect.Lists;

public class PieceMakerPanel extends ChessPanel
{
	public interface PieceListChangedListener
	{
		public void onPieceListChanged();
	}

	public PieceMakerPanel(PieceMenuPanel menuPanel, JFrame frame)
	{
		mLeaperCheckBox = new JCheckBox(Messages.getString("PieceMakerPanel.canJump"), false); //$NON-NLS-1$
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
		mBidirectionalMovementComboBox = new JComboBox();
		mAddKnightMoveButton = new JButton(Messages.getString("PieceMakerPanel.add")); //$NON-NLS-1$
		mRemoveKnightMoveButton = new JButton(Messages.getString("PieceMakerPanel.remove")); //$NON-NLS-1$
		new PieceMakerPanel(null, menuPanel, frame);
	}

	public PieceMakerPanel(String pieceName, PieceMenuPanel menuPanel, JFrame frame)
	{
		mFrame = frame;
		mPieceMenuPanel = menuPanel;
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
		mKnightOneField = new JTextField(4);
		mKnightTwoField = new JTextField(4);
		mAddKnightMoveButton = new JButton(Messages.getString("PieceMakerPanel.add")); //$NON-NLS-1$
		mRemoveKnightMoveButton = new JButton(Messages.getString("PieceMakerPanel.remove")); //$NON-NLS-1$
		PieceBuilder.initPieceTypes();
		PieceBuilder builder = null;
		if (pieceName != null)
			builder = PieceBuilder.loadFromDisk(pieceName);

		mTempBidirectionalMovements = Lists.newArrayList();
		if (builder != null)
		{
			List<BidirectionalMovement> bidirectionalMovements = builder.getPieceMovements().getBidirectionalMovements();
			mBidirectionalMovementComboBox = new JComboBox(bidirectionalMovements.toArray());
			for (BidirectionalMovement movement : bidirectionalMovements)
				mTempBidirectionalMovements.add(movement.toString());

			if (mBidirectionalMovementComboBox.getItemCount() != 0)
				mBidirectionalMovementComboBox.setSelectedIndex(0);
			else
				mBidirectionalMovementComboBox.setEnabled(false);
		}
		else
		{
			mBidirectionalMovementComboBox = new JComboBox();
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

		setSize(550, 875);
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

		mKnightOneField.setToolTipText(Messages.getString("PieceMakerPanel.enterKnightLike")); //$NON-NLS-1$
		mKnightTwoField.setToolTipText(Messages.getString("PieceMakerPanel.enterOtherDirection")); //$NON-NLS-1$
		mKnightOneField.addKeyListener(getKnightFieldKeyListener());
		mKnightTwoField.addKeyListener(getKnightFieldKeyListener());

		mAddKnightMoveButton.setEnabled(false);
		mAddKnightMoveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				mBidirectionalMovementComboBox.setEnabled(true);
				mRemoveKnightMoveButton.setEnabled(true);
				String toAdd = mKnightOneField.getText() + " x " + mKnightTwoField.getText(); //$NON-NLS-1$
				mTempBidirectionalMovements.add(toAdd);
				mBidirectionalMovementComboBox.addItem(toAdd);
				mKnightOneField.setText(""); //$NON-NLS-1$
				mKnightTwoField.setText(""); //$NON-NLS-1$
				mAddKnightMoveButton.setEnabled(false);
			}
		});

		mRemoveKnightMoveButton.setEnabled(mBidirectionalMovementComboBox.getItemCount() != 0);
		mRemoveKnightMoveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				mTempBidirectionalMovements.remove(mBidirectionalMovementComboBox.getSelectedItem().toString());
				mBidirectionalMovementComboBox.removeAllItems();
				for (String kMovement : mTempBidirectionalMovements)
				{
					mBidirectionalMovementComboBox.addItem(kMovement);
				}
				mBidirectionalMovementComboBox.setEnabled(mBidirectionalMovementComboBox.getItemCount() != 0);
				mRemoveKnightMoveButton.setEnabled(mBidirectionalMovementComboBox.getItemCount() != 0);
			}
		});

		mLeaperCheckBox.setToolTipText(Messages.getString("PieceMakerPanel.pressForJump")); //$NON-NLS-1$
		if (builder != null)
			mLeaperCheckBox.setSelected(builder.canJump());

		final JPanel knightMovementPanel = new JPanel();
		knightMovementPanel.setOpaque(false);
		knightMovementPanel.setToolTipText(Messages.getString("PieceMakerPanel.useForKnight")); //$NON-NLS-1$
		knightMovementPanel.setLayout(new GridBagLayout());

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(1, 1, 1, 1);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		knightMovementPanel.add(mBidirectionalMovementComboBox, constraints);

		final JPanel inputPanel = new JPanel();
		inputPanel.setOpaque(false);
		inputPanel.setLayout(new GridBagLayout());

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		inputPanel.add(mKnightOneField, constraints);

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
		inputPanel.add(mKnightTwoField, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		knightMovementPanel.add(inputPanel, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.insets = new Insets(10, 0, 10, 0);
		knightMovementPanel.add(mRemoveKnightMoveButton, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		knightMovementPanel.add(mAddKnightMoveButton, constraints);

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

				if (mBidirectionalMovementComboBox.getItemCount() != 0)
				{
					mBuilder.clearBidirectionalMovements();
					for (int i = 0; i < mBidirectionalMovementComboBox.getItemCount(); i++)
					{
						String line = mBidirectionalMovementComboBox.getItemAt(i).toString();
						StringTokenizer tokenizer = new StringTokenizer(line);

						int k1 = Integer.parseInt(tokenizer.nextToken());
						tokenizer.nextToken();
						int k2 = Integer.parseInt(tokenizer.nextToken());
						mBuilder.addBidirectionalMovement(new BidirectionalMovement(k1, k2));
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
				PieceBuilder.writeToDisk(mBuilder);

				refreshVariants();

				mPieceMenuPanel.refreshList();
				
				if (mFrame instanceof Driver)
					Driver.getInstance().popPanel();
				else
				{
					mFrame.remove(PieceMakerPanel.this);
					mFrame.add(mPieceMenuPanel);
					mFrame.pack();
					Driver.centerFrame(mFrame);
				}
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
					if (mFrame instanceof Driver)
						Driver.getInstance().popPanel();
					else
					{
						mFrame.remove(PieceMakerPanel.this);
						mFrame.add(mPieceMenuPanel);
						mFrame.pack();
						Driver.centerFrame(mFrame);
					}
				}
				else
				{
					switch (JOptionPane.showConfirmDialog(PieceMakerPanel.this,
							Messages.getString("PieceMakerPanel.ifYouContinue"), Messages.getString("PieceMakerPanel.pieceMaker"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE))
					{
					case JOptionPane.YES_OPTION:
						mFrame.remove(PieceMakerPanel.this);
						mFrame.add(mPieceMenuPanel);
						mFrame.pack();
						Driver.centerFrame(mFrame);
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

	private KeyListener getKnightFieldKeyListener()
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
				mAddKnightMoveButton.setEnabled(isIntegerDistance(mKnightOneField) && isIntegerDistance(mKnightTwoField));
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
	private final JTextField mKnightOneField;
	private final JTextField mKnightTwoField;
	private final JComboBox mBidirectionalMovementComboBox;
	private final JButton mAddKnightMoveButton;
	private final JButton mRemoveKnightMoveButton;
	private PieceBuilder mBuilder;
	private PieceMenuPanel mPieceMenuPanel;
	private BufferedImage mLightImage;
	private BufferedImage mDarkImage;
	private List<String> mTempBidirectionalMovements;
	private JFrame mFrame;
}
