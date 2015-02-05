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
import com.google.common.collect.Maps;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PieceCrafterDetailPanel extends ChessPanel {

    private final JTextField mPieceNameField;
    private final Map<String, Integer> mCardinalMovementMap;
    private final Map<String, Integer> mCapturingMovementMap;
    private final Map<String, Integer> mTwoHopMovementMap;

    private final JComboBox<String> mBidirectionalMovementComboBox;
    private final GlassPane mGlassPane;

    private BufferedImage mImage;
    private JList mCardinalMovements;
    private JList mCapturingMovements;
    private JList mTwoHopMovements;

    public PieceCrafterDetailPanel(GlassPane glassPane) {
        mCardinalMovementMap = Maps.newHashMap();
        mCapturingMovementMap = Maps.newHashMap();
        mTwoHopMovementMap = Maps.newHashMap();
        mPieceNameField = new JTextField(15);

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
        allMovementsPanel.setLayout(new GridBagLayout());

        mCardinalMovements = setupJList(mCardinalMovementMap);

        JScrollPane cardinalScrollPane = new JScrollPane(mCardinalMovements);
        cardinalScrollPane.setPreferredSize(new Dimension(150,140));
        cardinalScrollPane.setBorder(BorderFactory.createTitledBorder(Messages.getString("PieceCrafterDetailPanel.movements")));
        cardinalScrollPane.getVerticalScrollBar().setUnitIncrement(25);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = 3;
        constraints.insets = new Insets(0,10,10,10);
        allMovementsPanel.add(cardinalScrollPane, constraints);

        JButton addMovement = new JButton("+");
        addMovement.addActionListener(event -> createMovementPopup(mCardinalMovements.getSelectedValue()));
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.insets = new Insets(0,10,0,0);
        allMovementsPanel.add(addMovement, constraints);

        JButton removeMovement = new JButton("-");
        removeMovement.addActionListener(event ->{
            mCardinalMovementMap.remove(mCardinalMovements.getSelectedValue());
            refreshLists();
        });
        constraints.gridx = 1;
        allMovementsPanel.add(removeMovement, constraints);

        JButton editMovement = new JButton("Edit");
        editMovement.addActionListener(event -> createMovementPopup(mCardinalMovements.getSelectedValue()));
        constraints.gridx = 2;
        allMovementsPanel.add(editMovement, constraints);

        mCapturingMovements = setupJList(mCapturingMovementMap);

        JScrollPane captureScrollPane = new JScrollPane(mCapturingMovements);
        captureScrollPane.setPreferredSize(new Dimension(150,140));
        captureScrollPane.setBorder(BorderFactory.createTitledBorder(Messages.getString("PieceCrafterDetailPanel.capturing")));
        captureScrollPane.getVerticalScrollBar().setUnitIncrement(25);

        constraints = new GridBagConstraints();
        constraints.gridwidth = 3;
        constraints.gridx = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(0,10,10,10);
        allMovementsPanel.add(captureScrollPane, constraints);

        JButton addCapture = new JButton("+");
        addCapture.addActionListener(event -> createMovementPopup(mCapturingMovements.getSelectedValue()));
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.gridx = 3;

        constraints.insets = new Insets(0,10,0,0);
        allMovementsPanel.add(addCapture, constraints);

        JButton removeCapture = new JButton("-");
        removeCapture.addActionListener(event ->{
            mCapturingMovementMap.remove(mCapturingMovements.getSelectedValue());
            refreshLists();
        });
        constraints.gridx = 4;
        allMovementsPanel.add(removeCapture, constraints);

        JButton editCapture = new JButton("Edit");
        editCapture.addActionListener(event -> createMovementPopup(mCapturingMovements.getSelectedValue()));
        constraints.gridx = 5;
        allMovementsPanel.add(editCapture, constraints);

        mTwoHopMovements = setupJList(mTwoHopMovementMap);

        JScrollPane twoHopScrollPane = new JScrollPane(mTwoHopMovements);
        twoHopScrollPane.setPreferredSize(new Dimension(150,140));
        twoHopScrollPane.setBorder(BorderFactory.createTitledBorder(Messages.getString("PieceCrafterDetailPanel.twoHop")));
        twoHopScrollPane.getVerticalScrollBar().setUnitIncrement(25);

        constraints = new GridBagConstraints();
        constraints.gridwidth = 3;
        constraints.gridx = 6;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(0,10,10,10);
        allMovementsPanel.add(twoHopScrollPane, constraints);

        JButton addTwoHop = new JButton("+");
        addTwoHop.addActionListener(event -> createMovementPopup(mTwoHopMovements.getSelectedValue()));
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.gridx = 6;

        constraints.insets = new Insets(0,10,0,0);
        allMovementsPanel.add(addTwoHop, constraints);

        JButton removeTwoHop = new JButton("-");
        removeTwoHop.addActionListener(event ->{
            mCapturingMovementMap.remove(mTwoHopMovements.getSelectedValue());
            refreshLists();
        });
        constraints.gridx = 7;
        allMovementsPanel.add(removeTwoHop, constraints);

        JButton editTwoHop = new JButton("Edit");
        editTwoHop.addActionListener(event -> createMovementPopup(mTwoHopMovements.getSelectedValue()));
        constraints.gridx = 8;
        allMovementsPanel.add(editTwoHop, constraints);

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
        gbc.weighty = 0.7;
        add(namePanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.ipady = 0;
        add(allMovementsPanel, gbc);

        gbc.gridy = 2;
        add(boardAndSave, gbc);
    }

    private void refreshLists() {
        mCardinalMovements = setupJList(mCardinalMovementMap);
    }

    private void createMovementPopup(Object selectedValue) {

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


    private JList setupJList(Map<String, Integer> movements){
        DefaultListModel model = new DefaultListModel();
        for(Map.Entry<String, Integer> entry : movements.entrySet()){
            model.addElement(entry.getKey());
        }

        return new JList(model);
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
