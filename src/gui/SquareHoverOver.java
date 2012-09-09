package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rules.Rules;

import utility.GUIUtility;

import logic.Game;
import logic.Piece;

public class SquareHoverOver extends JPanel
{
	public SquareHoverOver(Piece piece, JFrame hoverOverFrame, CustomSetupMenu customSetupMenu){
		m_frame = hoverOverFrame;
		m_frame.setVisible(true);
		m_frame.add(this);
		m_frame.setSize(400, 600);
		m_frame.setLocationRelativeTo(Driver.getInstance());
		m_movements = piece.getPieceMovements();
		m_piece = piece;
		initGUIComponents(customSetupMenu.m_whiteRules, customSetupMenu.m_blackRules);
	}
	
	public SquareHoverOver(Piece piece, JFrame hoverOverFrame, Game game){
		m_frame = hoverOverFrame;
		m_frame.setVisible(true);
		m_frame.add(this);
		m_frame.setSize(400, 600);
		m_frame.setLocationRelativeTo(Driver.getInstance());
		m_movements = piece.getPieceMovements();
		m_piece = piece;
		initGUIComponents(game.getRules(false), game.getRules(true));
	}
	
	private void initGUIComponents(Rules whiteRules, Rules blackRules){
		setLayout(new GridBagLayout());
		setSize(600, 600);
		setBorder(BorderFactory.createLoweredBevelBorder());
		GridBagConstraints constraints = new GridBagConstraints();
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout());
		namePanel.add(new JLabel("Piece Name:"));
		
		final JTextField pieceNameField = new JTextField(15);
		pieceNameField.setText(m_piece.getName());
		pieceNameField.setEditable(false);
		namePanel.add(pieceNameField);
		
		final JTextField northField = new JTextField(6);
		northField.setToolTipText("North");
		northField.setText(checkMovement('N'));
		northField.setEditable(false);
		
		final JTextField northEastField = new JTextField(6);
		northEastField.setToolTipText("Northeast");
		northEastField.setText(checkMovement('R'));
		northEastField.setEditable(false);
		
		final JTextField northWestField = new JTextField(6);
		northWestField.setToolTipText("Northwest");
		northWestField.setText(checkMovement('L'));
		northWestField.setEditable(false);
		
		final JTextField eastField = new JTextField(6);
		eastField.setToolTipText("East");
		eastField.setText(checkMovement('E'));
		eastField.setEditable(false);
		
		final JTextField southEastField = new JTextField(6);
		southEastField.setToolTipText("Southeast");
		southEastField.setText(checkMovement('r'));
		southEastField.setEditable(false);
		
		final JTextField southField = new JTextField(6);
		southField.setToolTipText("South");
		southField.setText(checkMovement('S'));
		southField.setEditable(false);
		
		final JTextField southWestField = new JTextField(6);
		southWestField.setToolTipText("Southwest");
		southWestField.setText(checkMovement('l'));
		southWestField.setEditable(false);
		
		final JTextField westField = new JTextField(6);
		westField.setToolTipText("West");
		westField.setText(checkMovement('W'));
		westField.setEditable(false);

		JPanel movementPanel = new JPanel();
		movementPanel.setLayout(new GridBagLayout());
		
		JLabel movementPictureHolder = new JLabel(GUIUtility.createImageIcon(130, 130, "/movement_directions.png"));

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		movementPanel.add(northWestField, constraints);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		movementPanel.add(northField, constraints);
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		movementPanel.add(northEastField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		movementPanel.add(westField, constraints);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		movementPanel.add(movementPictureHolder, constraints);
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		movementPanel.add(eastField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		movementPanel.add(southWestField, constraints);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		movementPanel.add(southField, constraints);
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		movementPanel.add(southEastField, constraints);
		
		JPanel knightMovementsPanel = new JPanel();
		JTextField knightFirstMovement = new JTextField(3);
		JTextField knightSecondMovement = new JTextField(3);
		
		knightMovementsPanel.add(new JLabel("Knight-like Movements: "));
		knightMovementsPanel.add(knightFirstMovement);
		knightMovementsPanel.add(new JLabel(" x "));
		knightMovementsPanel.add(knightSecondMovement);
		
		knightFirstMovement.setText(checkMovement('x'));
		knightSecondMovement.setText(checkMovement('y'));
		
		if(m_piece.isBlack()){
			if(m_piece.getName().equals(blackRules.getObjectiveName()))
			{
				objectivePieceIcon = GUIUtility.createImageIcon(48, 48, "/objectivePiece.png");	
			}
			else{
				objectivePieceIcon = GUIUtility.createImageIcon(48, 48, "/Uninhabitable.png");
			}
		}
		else{
			if(m_piece.getName().equals(whiteRules.getObjectiveName()))
			{
				objectivePieceIcon = GUIUtility.createImageIcon(48, 48, "/objectivePiece.png");
			}
			else{
				objectivePieceIcon = GUIUtility.createImageIcon(48, 48, "/Uninhabitable.png");
			}
		}
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(10, 10, 10, 10);
		add(namePanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(movementPanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(knightMovementsPanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		add(new JLabel("This is the Objective Piece:"), constraints);
		constraints.gridx = 1;
		constraints.gridy = 3;
		add(new JLabel(objectivePieceIcon), constraints);
		
		m_frame.pack();
	}
	
	private String checkMovement(Character direction){
		if(m_piece.getName().equals("Pawn"))
		{
			if(m_piece.isBlack()){
				if(direction.equals('S'))
					return "1";
			}
			else{
				if(direction.equals('N'))
					return "1";
			}
			return "0";
		}
		if(m_movements.containsKey(direction))
		{
			if(m_movements.get(direction) == -1)
				return("Unlimited");
			else
				return "" + m_movements.get(direction);
		}
		else
		{
			return "0";
		}
	}

	private ImageIcon objectivePieceIcon;
	final private JFrame m_frame;
	private Map<Character, Integer> m_movements;
	private Piece m_piece;
}
