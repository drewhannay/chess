package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.logic.PieceTypeBuilder;
import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.ImageUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;

public class PieceCrafterPanel extends ChessPanel {

    private final JTextField mPieceNameField;
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

    private PieceTypeBuilder mBuilder;
    private PieceMenuPanel mPieceMenuPanel;
    private BufferedImage mLightImage;
    private BufferedImage mDarkImage;
    private List<String> mTempBidirectionalMovements;

    public interface PieceListChangedListener {
        public void onPieceListChanged();
    }

    public PieceCrafterPanel(PieceMenuPanel menuPanel) {
        this(null, menuPanel);
    }

    public PieceCrafterPanel(String pieceName, PieceMenuPanel menuPanel) {
        mPieceMenuPanel = menuPanel;
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
        mAddKnightMoveButton = new JButton(Messages.getString("PieceMakerPanel.add"));
        mRemoveKnightMoveButton = new JButton(Messages.getString("PieceMakerPanel.remove"));

        PieceTypeBuilder builder = null;
        //TODO Fix editing pieces
        //if (pieceName != null)
        //	builder = PieceBuilder.loadFromDisk(pieceName);

        mTempBidirectionalMovements = Lists.newArrayList();
        /*if (builder != null)
        {
			List<BidirectionalMovement> bidirectionalMovements = builder.getPieceMovements().getTwoHopMovements();
			mBidirectionalMovementComboBox = new JComboBox(bidirectionalMovements.toArray());
			for (BidirectionalMovement movement : bidirectionalMovements)
				mTempBidirectionalMovements.add(movement.toString());

			mBidirectionalMovementComboBox.setSelectedIndex(0);
		}
		else
		{*/
        mBidirectionalMovementComboBox = new JComboBox();
        mBidirectionalMovementComboBox.setEnabled(false);
        //}

        initGUIComponents(builder);
    }

    private void initGUIComponents(PieceTypeBuilder builder) {
        if (builder == null)
            mBuilder = new PieceTypeBuilder();
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

        namePanel.add(GuiUtility.createJLabel(Messages.getString("PieceMakerPanel.pieceName")));
        mPieceNameField.setToolTipText(Messages.getString("PieceMakerPanel.enterNameOfNewPiece"));
        if (builder != null)
            mPieceNameField.setText(builder.getName());
        GuiUtility.requestFocus(mPieceNameField);
        namePanel.add(mPieceNameField);

        constraints.gridx = 0;
        constraints.gridy = 0;
        pieceCreationPanel.add(namePanel, constraints);

        ImageIcon blankSquare = GuiUtility.createSystemImageIcon(48, 48, "/WhiteSquare.png");

        blankSquare.setImage(blankSquare.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
        final JPanel lightIconPanel = new JPanel();
        lightIconPanel.setLayout(new FlowLayout());
        lightIconPanel.setOpaque(false);
        final JLabel lightIconLabel = GuiUtility.createJLabel("");
        lightIconLabel.setSize(48, 48);

        try {
            lightIconLabel.setIcon(builder == null ? blankSquare : ImageUtility.getLightImage(builder.getName(), 24));
        } catch (IOException e3) {
            e3.printStackTrace();
        }

        if (builder != null) {
            try {
                mLightImage = GuiUtility.createBufferedImage(48, 48, "l_" + builder.getName() + ".png");
                mDarkImage = GuiUtility.createBufferedImage(48, 48, "d_" + builder.getName() + ".png");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        final JButton lightImageButton = new JButton(Messages.getString("PieceMakerPanel.chooseLightImage"));
        lightImageButton.setToolTipText(Messages.getString("PieceMakerPanel.clickForLight"));
        lightImageButton.addActionListener(new ImageButtonActionListener(lightIconLabel, false));
        lightIconPanel.add(lightImageButton);
        lightIconPanel.add(lightIconLabel);

        constraints.gridx = 0;
        constraints.gridy = 3;
        pieceCreationPanel.add(lightIconPanel, constraints);

        final JPanel darkIconPanel = new JPanel();
        darkIconPanel.setLayout(new FlowLayout());
        darkIconPanel.setOpaque(false);
        final JLabel darkIconLabel = GuiUtility.createJLabel("");
        darkIconLabel.setSize(48, 48);

        try {
            darkIconLabel.setIcon(builder == null ? blankSquare : ImageUtility.getDarkImage(builder.getName(), 24));
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        final JButton darkImageButton = new JButton(Messages.getString("PieceMakerPanel.chooseDark"));
        darkImageButton.setToolTipText(Messages.getString("PieceMakerPanel.clickForDark"));
        darkImageButton.addActionListener(new ImageButtonActionListener(darkIconLabel, true));
        darkIconPanel.add(darkImageButton);
        darkIconPanel.add(darkIconLabel);

        constraints.gridx = 0;
        constraints.gridy = 2;
        pieceCreationPanel.add(darkIconPanel, constraints);

        //PieceMovements movements = builder == null ? null : builder.getPieceMovements();
        //PieceMovements movements = null;

        /*mNorthField.setToolTipText(Messages.getString("PieceMakerPanel.north"));
        mNorthField.setText(builder == null ? "0" : "" + movements.getDistance(Direction.NORTH));
        mNorthEastField.setToolTipText(Messages.getString("PieceMakerPanel.northEast"));
        mNorthEastField.setText(builder == null ? "0" : "" + movements.getDistance(Direction.NORTHEAST));
        mNorthWestField.setToolTipText(Messages.getString("PieceMakerPanel.northwest"));
        mNorthWestField.setText(builder == null ? "0" : "" + movements.getDistance(Direction.NORTHWEST));
        mEastField.setToolTipText(Messages.getString("PieceMakerPanel.east"));
        mEastField.setText(builder == null ? "0" : "" + movements.getDistance(Direction.EAST));
        mSouthEastField.setToolTipText(Messages.getString("PieceMakerPanel.southEast"));
        mSouthEastField.setText(builder == null ? "0" : "" + movements.getDistance(Direction.SOUTHEAST));
        mSouthField.setToolTipText(Messages.getString("PieceMakerPanel.south"));
        mSouthField.setText(builder == null ? "0" : "" + movements.getDistance(Direction.SOUTH));
        mSouthWestField.setToolTipText(Messages.getString("PieceMakerPanel.southWest"));
        mSouthWestField.setText(builder == null ? "0" : "" + movements.getDistance(Direction.SOUTHWEST));
        mWestField.setToolTipText(Messages.getString("PieceMakerPanel.west"));
        mWestField.setText(builder == null ? "0" : "" + movements.getDistance(Direction.WEST));
        */

        JLabel movementPictureHolder = GuiUtility.createJLabel(GuiUtility.createSystemImageIcon(130, 130, "/movement_directions.png"));

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
        distanceField.setToolTipText(Messages.getString("PieceMakerPanel.greatestSpaces"));

        mKnightOneField.setToolTipText(Messages.getString("PieceMakerPanel.enterKnightLike"));
        mKnightTwoField.setToolTipText(Messages.getString("PieceMakerPanel.enterOtherDirection"));
        mKnightOneField.addKeyListener(getKnightFieldKeyListener());
        mKnightTwoField.addKeyListener(getKnightFieldKeyListener());

        mAddKnightMoveButton.setEnabled(false);
        mAddKnightMoveButton.addActionListener(event -> {
            mBidirectionalMovementComboBox.setEnabled(true);
            mRemoveKnightMoveButton.setEnabled(true);
            String toAdd = mKnightOneField.getText() + " x " + mKnightTwoField.getText();
            mTempBidirectionalMovements.add(toAdd);
            mBidirectionalMovementComboBox.addItem(toAdd);
            mKnightOneField.setText("");
            mKnightTwoField.setText("");
            mAddKnightMoveButton.setEnabled(false);
        });

        mRemoveKnightMoveButton.setEnabled(mBidirectionalMovementComboBox.getItemCount() != 0);
        mRemoveKnightMoveButton.addActionListener(e -> {
            mTempBidirectionalMovements.remove(mBidirectionalMovementComboBox.getSelectedItem().toString());
            mBidirectionalMovementComboBox.removeAllItems();
            for (String kMovement : mTempBidirectionalMovements) {
                mBidirectionalMovementComboBox.addItem(kMovement);
            }
            mBidirectionalMovementComboBox.setEnabled(mBidirectionalMovementComboBox.getItemCount() != 0);
            mRemoveKnightMoveButton.setEnabled(mBidirectionalMovementComboBox.getItemCount() != 0);
        });

        final JPanel knightMovementPanel = new JPanel();
        knightMovementPanel.setOpaque(false);
        knightMovementPanel.setToolTipText(Messages.getString("PieceMakerPanel.useForKnight"));
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
        inputPanel.add(GuiUtility.createJLabel(Messages.getString("PieceMakerPanel.51")), constraints);

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
        movementPanel.add(GuiUtility.createJLabel(Messages.getString("PieceMakerPanel.normalMovementHTML")), constraints);
        constraints.insets = new Insets(5, 0, 0, 0);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        movementPanel.add(movement, constraints);
        constraints.insets = new Insets(5, 0, 0, 0);
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.gridx = 0;
        constraints.gridy = 6;
        movementPanel.add(GuiUtility.createJLabel(Messages.getString("PieceMakerPanel.knightLikeMovementHTML")), constraints);
        constraints.gridx = 0;
        constraints.gridy = 7;
        movementPanel.add(knightMovementPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        pieceCreationPanel.add(movementPanel, constraints);

        final JButton savePieceButton = new JButton(Messages.getString("PieceMakerPanel.saveAndReturn"));
        savePieceButton.setToolTipText(Messages.getString("PieceMakerPanel.pressToSave"));
        savePieceButton.addActionListener(event -> {
            String pieceName = mPieceNameField.getText().trim();
            //TODO fix this when checking for an existing piece
            if (pieceName.isEmpty() || PieceTypeBuilder.parsePieceType(mPieceNameField.getText()) != null) {
                JOptionPane.showMessageDialog(
                        PieceCrafterPanel.this,
                        Messages.getString("PieceMakerPanel.enterUniqueName"), Messages.getString("PieceMakerPanel.invalidPieceName"),
                        JOptionPane.PLAIN_MESSAGE);
                return;
            }

            if (isIntegerDistance(mNorthField) && isIntegerDistance(mNorthEastField) && isIntegerDistance(mNorthWestField)
                    && isIntegerDistance(mEastField) && isIntegerDistance(mWestField) && isIntegerDistance(mSouthField)
                    && isIntegerDistance(mSouthEastField) && isIntegerDistance(mSouthWestField)) {
                mBuilder.addMovement(Direction.NORTH, Integer.parseInt(mNorthField.getText()));
                mBuilder.addMovement(Direction.NORTHEAST, Integer.parseInt(mNorthEastField.getText()));
                mBuilder.addMovement(Direction.NORTHWEST, Integer.parseInt(mNorthWestField.getText()));
                mBuilder.addMovement(Direction.EAST, Integer.parseInt(mEastField.getText()));
                mBuilder.addMovement(Direction.WEST, Integer.parseInt(mWestField.getText()));
                mBuilder.addMovement(Direction.SOUTH, Integer.parseInt(mSouthField.getText()));
                mBuilder.addMovement(Direction.SOUTHEAST, Integer.parseInt(mSouthEastField.getText()));
                mBuilder.addMovement(Direction.SOUTHWEST, Integer.parseInt(mSouthWestField.getText()));
            }

            if (mBidirectionalMovementComboBox.getItemCount() != 0) {
                mBuilder.clearBidirectionalMovements();
                for (int i = 0; i < mBidirectionalMovementComboBox.getItemCount(); i++) {
                    String line = mBidirectionalMovementComboBox.getItemAt(i).toString();
                    StringTokenizer tokenizer = new StringTokenizer(line);

                    int k1 = Integer.parseInt(tokenizer.nextToken());
                    tokenizer.nextToken();
                    int k2 = Integer.parseInt(tokenizer.nextToken());
                    mBuilder.addBidirectionalMovement(TwoHopMovement.with(k2, k1));
                }
            }

            try {
                ImageUtility.writeLightImage(pieceName, mLightImage);
                ImageUtility.writeDarkImage(pieceName, mDarkImage);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        PieceCrafterPanel.this,
                        Messages.getString("PieceMakerPanel.cannotWriteImageFiles"), Messages.getString("PieceMakerPanel.ImageError"), JOptionPane.PLAIN_MESSAGE);
                return;
            }

            mBuilder.setName(pieceName);
//            mBuilder.setCanJump(mLeaperCheckBox.isSelected());
            //TODO Write new piece to disk
            //PieceBuilder.savePieceType(mBuilder);
            //PieceBuilder.writeToDisk(mBuilder);

            //refreshVariants();

            mPieceMenuPanel.refreshList();
            PieceCrafterPanel.this.removeAll();
        });

        final JButton cancelButton = new JButton(Messages.getString("PieceMakerPanel.cancel"));
        cancelButton.setToolTipText(Messages.getString("PieceMakerPanel.pressToReturn"));
        cancelButton.addActionListener(event -> {
            if (mPieceNameField.getText().trim().isEmpty()) {
                PieceCrafterPanel.this.removeAll();
            } else {
                switch (JOptionPane.showConfirmDialog(PieceCrafterPanel.this,
                        Messages.getString("PieceMakerPanel.ifYouContinue"), Messages.getString("PieceMakerPanel.pieceMaker"),
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE)) {
                    case JOptionPane.YES_OPTION:
                        PieceCrafterPanel.this.removeAll();
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
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
    }

    /**
     * TODO Do we still need to do this if the Variants are loading the pieces separately now?
     * Once a piece has been changed, we need to update any variants that use
     * this piece to use the new version.
     */
    /*
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
						piece = PieceBuilder.makePiece(Piece.getId(), false, piece.getOriginalCoordinates());
				}
				for (Piece piece : builder.getBlackTeam())
				{
					if (piece.getName().equals(mPieceNameField.getText()))
						piece = PieceBuilder.makePiece(Piece.getId(), true, piece.getOriginalCoordinates());
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
	*/
    private boolean isIntegerDistance(JTextField textField) {
        if (textField.getText().equals(""))
            return false;
        try {
            Integer.parseInt(textField.getText());
            return true;
        } catch (Exception e) {
            JOptionPane
                    .showMessageDialog(
                            PieceCrafterPanel.this,
                            Messages.getString("PieceMakerPanel.allMovementDist") + textField.getToolTipText()
                                    + Messages.getString("PieceMakerPanel.directionBox"), Messages.getString("PieceMakerPanel.error"), JOptionPane.PLAIN_MESSAGE);
            return false;
        }
    }

    private KeyListener getKnightFieldKeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                mAddKnightMoveButton.setEnabled(isIntegerDistance(mKnightOneField) && isIntegerDistance(mKnightTwoField));
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
            }
        };
    }

    private final class ImageButtonActionListener implements ActionListener {
        public ImageButtonActionListener(JLabel imageLabel, boolean isDarkImage) {
            m_imageLabel = imageLabel;
            m_isDarkImage = isDarkImage;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            Object[] options =
                    new String[]{
                            Messages.getString("PieceMakerPanel.browseComputer"), Messages.getString("PieceMakerPanel.imageFromInternet"), Messages.getString("PieceMakerPanel.cancel")};

            switch (JOptionPane.showOptionDialog(PieceCrafterPanel.this,
                    Messages.getString("PieceMakerPanel.whereFrom"), Messages.getString("PieceMakerPanel.chooseImage"),
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0])) {
                case JOptionPane.YES_OPTION:
                    JFileChooser fileChooser = new JFileChooser("~/");
                    fileChooser.setFileFilter(new FileFilter() {
                        @Override
                        public String getDescription() {
                            return "PNG Files";
                        }

                        @Override
                        public boolean accept(File f) {
                            if (f.isDirectory() || f.getName().endsWith(".png"))
                                return true;
                            else
                                return false;
                        }
                    });

                    if (fileChooser.showOpenDialog(PieceCrafterPanel.this) == JFileChooser.APPROVE_OPTION) {
                        try {
                            if (m_isDarkImage) {
                                mDarkImage = ImageIO.read(fileChooser.getSelectedFile());
                                m_imageLabel.setIcon(new ImageIcon(mDarkImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
                            } else {
                                mLightImage = ImageIO.read(fileChooser.getSelectedFile());
                                m_imageLabel.setIcon(new ImageIcon(mLightImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
                            }
                        } catch (IOException e) {
                            // TODO we should show the user an error message if this
                            // fails
                            e.printStackTrace();
                        }
                    }
                    break;
                case JOptionPane.NO_OPTION:
                    String url = JOptionPane.showInputDialog(PieceCrafterPanel.this,
                            Messages.getString("PieceMakerPanel.enterURL"), Messages.getString("PieceMakerPanel.inputURL"),
                            JOptionPane.PLAIN_MESSAGE);
                    try {
                        if (m_isDarkImage) {
                            mDarkImage = ImageIO.read(new URL(url));
                            m_imageLabel.setIcon(new ImageIcon(mDarkImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
                        } else {
                            mLightImage = ImageIO.read(new URL(url));
                            m_imageLabel.setIcon(new ImageIcon(mLightImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
                        }
                    } catch (Exception e) {
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
}
