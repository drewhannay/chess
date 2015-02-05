package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Direction;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.TwoHopMovement;
import com.drewhannay.chesscrafter.utility.FileUtility;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.Messages;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PieceCrafterDetailPanel extends ChessPanel {

    private final JTextField mPieceNameField;
    private final JTextField[] mCardinalMovements;
    private final JTextField[] mCapturingMovements;
    private final JTextField[][] mTwoHopMovements;
    private final JComboBox<String> mBidirectionalMovementComboBox;
    private final GlassPane mGlassPane;
    private final JButton mAddKnightMoveButton;
    private final JButton mRemoveKnightMoveButton;

    private BufferedImage mImage;
    private BufferedImage mDarkImage;
    private List<String> mBidirectionalMovements;

    public PieceCrafterDetailPanel(GlassPane glassPane) {
        mCardinalMovements = new JTextField[8];
        mCapturingMovements = new JTextField[8];
        mTwoHopMovements = new JTextField[10][10];
        mPieceNameField = new JTextField(15);
        mAddKnightMoveButton = new JButton(Messages.getString("PieceMakerPanel.add"));
        mRemoveKnightMoveButton = new JButton(Messages.getString("PieceMakerPanel.remove"));

        mBidirectionalMovements = Lists.newArrayList();

        mGlassPane = glassPane;

        mBidirectionalMovementComboBox = new JComboBox<>();
        mBidirectionalMovementComboBox.setEnabled(false);

        initComponents();
    }

    public void newPieceType() {
        // TODO: unimplemented method stub
    }

    public void loadPieceType(PieceType pieceType) {
        Map<Direction, Integer> movements = pieceType.getMovements();
        Set<TwoHopMovement> twoHopMovements = pieceType.getTwoHopMovements();

        mPieceNameField.setText(pieceType.getInternalId());
        //TODO fill in piece info
    }

    private String getUserFriendlyValue(Integer movementValue) {
        int intValue = Optional.fromNullable(movementValue).or(0);
        return String.valueOf(intValue == Integer.MAX_VALUE ? "\u221e" : intValue);
    }

    private void initComponents(){
        setLayout(new GridBagLayout());
        JPanel namePanel =  new JPanel();
        namePanel.setOpaque(false);

        namePanel.add(GuiUtility.createJLabel(Messages.getString("PieceCrafterDetailPanel.pieceName")));
        namePanel.add(mPieceNameField);

        JButton imageButton = new JButton(Messages.getString("PieceCrafterDetailPanel.image"));
        imageButton.setToolTipText(Messages.getString("PieceCrafterDetailPanel.pieceIcon"));
        imageButton.addActionListener(new ImageButtonActionListener(imageButton, false));
        imageButton.setPreferredSize(new Dimension(75, 75));
        namePanel.add(imageButton);

        JPanel allMovementsPanel = new JPanel();
        allMovementsPanel.setOpaque(false);

        List<String> directions = Lists.newArrayList("North", "Northeast", "East", "Southeast", "South", "Southwest", "West", "Northwest");

        JPanel cardinalMovements = new JPanel();
        cardinalMovements.setOpaque(false);
        cardinalMovements.setLayout(new GridBagLayout());

        JPanel capturingMovements = new JPanel();
        capturingMovements.setOpaque(false);
        capturingMovements.setLayout(new GridBagLayout());

        int counter = 0;
        for(String direction : directions) {
            mCardinalMovements[counter] = new JTextField(4);
            mCapturingMovements[counter] = new JTextField(4);
            setupMovementsFields(cardinalMovements, mCardinalMovements, direction, counter);
            setupMovementsFields(capturingMovements, mCapturingMovements, direction, counter);
            counter++;
        }
        JScrollPane cardinalScrollPane = new JScrollPane(cardinalMovements);
        cardinalScrollPane.setPreferredSize(new Dimension(150,140));
        cardinalScrollPane.setBorder(BorderFactory.createTitledBorder("Movements"));
        cardinalScrollPane.getVerticalScrollBar().setUnitIncrement(25);
        allMovementsPanel.add(cardinalScrollPane);

        JScrollPane captureScrollPane = new JScrollPane(capturingMovements);
        captureScrollPane.setPreferredSize(new Dimension(150,140));
        captureScrollPane.setBorder(BorderFactory.createTitledBorder("Capturing"));
        captureScrollPane.getVerticalScrollBar().setUnitIncrement(25);
        allMovementsPanel.add(captureScrollPane);

        JPanel twoHopMovements = new JPanel();
        twoHopMovements.setOpaque(false);
        twoHopMovements.setLayout(new GridBagLayout());

        //TODO Add two hops

        JPanel boardAndSave = new JPanel();
        boardAndSave.setOpaque(false);
        boardAndSave.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        BoardPanel board = new BoardPanel(BoardSize.CLASSIC_SIZE, new SquareConfig(new DropManager(() -> {}, pair -> {}), mGlassPane), bc -> ImmutableSet.of());
        boardAndSave.add(board, gbc);

        JButton save = new JButton(Messages.getString("PieceMakerPanel.saveAndReturn"));
        save.addActionListener(event ->{
            //TODO save piece here
        });
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.insets = new Insets(25, 0, 25, 25);
        boardAndSave.add(save, gbc);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        add(namePanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.ipady = 0;
        add(allMovementsPanel, gbc);

        gbc.gridy = 2;
        add(boardAndSave, gbc);
    }

    private boolean isIntegerDistance(JTextField textField) {
        if (textField.getText().equals(""))
            return false;
        try {
            Integer.parseInt(textField.getText());
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PieceCrafterDetailPanel.this,
                    Messages.getString("PieceMakerPanel.allMovementDist") + textField.getToolTipText()
                            + Messages.getString("PieceMakerPanel.directionBox"), Messages.getString("PieceMakerPanel.error"), JOptionPane.PLAIN_MESSAGE);
            return false;
        }
    }


    private void setupMovementsFields(JPanel movementPanel, JTextField[] movementFields, String direction, int row){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.insets = new Insets(5,5,5,5);
        movementPanel.add(new JLabel(direction), gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        movementPanel.add(movementFields[row], gbc);
    }

    private final class ImageButtonActionListener implements ActionListener {

        private JButton mImageButton;

        public ImageButtonActionListener(JButton imageButton, boolean isDarkImage) {
            mImageButton = imageButton;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            Object[] options = new String[]{
                    Messages.getString("PieceMakerPanel.browseComputer"), Messages.getString("PieceMakerPanel.imageFromInternet"), Messages.getString("PieceMakerPanel.cancel")};

            switch (JOptionPane.showOptionDialog(PieceCrafterDetailPanel.this,
                    Messages.getString("PieceMakerPanel.whereFrom"), Messages.getString("PieceMakerPanel.chooseImage"),
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0])) {
                case JOptionPane.YES_OPTION:
                    File file = FileUtility.chooseFile(FileUtility.IMAGE_EXTENSION_FILTER);
                    if (file != null) {
                        try {
                            mImage = ImageIO.read(file);
                            mImageButton.setIcon(new ImageIcon(mImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
                        } catch (IOException e) {
                            // TODO we should show the user an error message if this fails
                            e.printStackTrace();
                        }
                    }
                    break;
                case JOptionPane.NO_OPTION:
                    String url = JOptionPane.showInputDialog(PieceCrafterDetailPanel.this,
                            Messages.getString("PieceMakerPanel.enterURL"), Messages.getString("PieceMakerPanel.inputURL"),
                            JOptionPane.PLAIN_MESSAGE);
                    try {
                        mImage = ImageIO.read(new URL(url));
                        mImageButton.setIcon(new ImageIcon(mImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case JOptionPane.CANCEL_OPTION:
                    break;
            }
        }
    }
}
