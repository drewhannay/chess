package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.Board;
import logic.Builder;
import rules.AdjustTeamDests;
import rules.EndOfGame;
import rules.GetBoard;
import rules.Rules;

/**
 * @author jmccormi
 * Window to set up the special rules for the variants
 */
public class RuleMaker extends JPanel {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8365806731061105370L;
	/**
	 * The Builder reference
	 */
	private Builder b;
	/**
	 * Rules for the White Team
	 */
	private Rules whiteRules = new Rules(false);
	/**
	 * Rules for the Black Team
	 */
	private Rules blackRules = new Rules(false);
	
	/**
	 * Constructor for setting up the rules window
	 * @param b the builder containing everything
	 */
	public RuleMaker(Builder b){
		this.b = b;
		initComponents();
	}
	
	/**
	 * Setting up the window and rules
	 */
	public void initComponents(){
		
		//Setting up the panel
		setLayout(new GridBagLayout());
		setSize(600, 600); 
		setBorder(BorderFactory.createLoweredBevelBorder());
		GridBagConstraints c = new GridBagConstraints();
		
		final Board[] boards = b.getBoards();
		
		//Legal Destination Rules for White
		final JPanel legalDestWhiteCheckBox = new JPanel();
		legalDestWhiteCheckBox.setLayout(new GridLayout(3, 1));
		final JCheckBox wCaptureMand = new JCheckBox("Capture Mandatory");
		legalDestWhiteCheckBox.add(wCaptureMand);
		final JCheckBox wNoMoveObj = new JCheckBox("Can't Move Objective");
		legalDestWhiteCheckBox.add(wNoMoveObj);

		//Objectives rules for White
		final JPanel objectiveWhiteCheckBox = new JPanel();
		objectiveWhiteCheckBox.setLayout(new GridLayout(5, 1));
			final JCheckBox wCaptureAll = new JCheckBox("Capture All", false);
			wCaptureAll.setEnabled(false);
			objectiveWhiteCheckBox.add(wCaptureAll);
			final JCheckBox wCaptureAllType = new JCheckBox("Capture All of Type", false);
			wCaptureAllType.setEnabled(false);
			objectiveWhiteCheckBox.add(wCaptureAllType);
			final JCheckBox wProtectObj = new JCheckBox("Protect Objective", true);
			objectiveWhiteCheckBox.add(wProtectObj);
			final JCheckBox wLoseAll = new JCheckBox("Lose All Pieces", false);
			wLoseAll.setEnabled(false);
			objectiveWhiteCheckBox.add(wLoseAll);
			final JCheckBox wCheckTimes = new JCheckBox("Check # Times", false);
			wCheckTimes.setEnabled(false);
			objectiveWhiteCheckBox.add(wCheckTimes);

		//Capturing pieces rules for White
		final JPanel afterCaptureWhiteCheckBox = new JPanel();
		afterCaptureWhiteCheckBox.setLayout(new GridLayout(4, 1));
			final JCheckBox wChangeColor = new JCheckBox("Capturer changes Color");
			wChangeColor.setEnabled(false);
			afterCaptureWhiteCheckBox.add(wChangeColor);
			final JCheckBox wPieceReturn = new JCheckBox("Captured piece returns to start");
			afterCaptureWhiteCheckBox.add(wPieceReturn);
			final JCheckBox wDrop = new JCheckBox("Captured Pieces Drop");
			afterCaptureWhiteCheckBox.add(wDrop);
			final JCheckBox wCapturedColorAndDrop = new JCheckBox("Captured Piece Changes Color and Drops");
			afterCaptureWhiteCheckBox.add(wCapturedColorAndDrop);
		
		//Legal Destination Rules for Black
		final JPanel legalDestBlackCheckBox = new JPanel();
		legalDestBlackCheckBox.setLayout(new GridLayout(3, 1));
			final JCheckBox bCaptureMand = new JCheckBox("Capture Mandatory");
			legalDestBlackCheckBox.add(bCaptureMand);
			final JCheckBox bNoMoveObj = new JCheckBox("Can't Move Objective");
			legalDestBlackCheckBox.add(bNoMoveObj);

		//Objectives rules for Black
		final JPanel objectiveBlackCheckBox = new JPanel();
		objectiveBlackCheckBox.setLayout(new GridLayout(5, 1));
			final JCheckBox bCaptureAll = new JCheckBox("Capture All", false);
			bCaptureAll.setEnabled(false);
			objectiveBlackCheckBox.add(bCaptureAll);
			final JCheckBox bCaptureAllType = new JCheckBox("Capture All of Type", false);
			bCaptureAllType.setEnabled(false);
			objectiveBlackCheckBox.add(bCaptureAllType);
			final JCheckBox bProtectObj = new JCheckBox("Protect Objective", true);
			objectiveBlackCheckBox.add(bProtectObj);
			final JCheckBox bLoseAll = new JCheckBox("Lose All Pieces", false);
			bLoseAll.setEnabled(false);
			objectiveBlackCheckBox.add(bLoseAll);
			final JCheckBox bCheckTimes = new JCheckBox("Check # Times", false);
			bCheckTimes.setEnabled(false);
			objectiveBlackCheckBox.add(bCheckTimes);

		//Capturing pieces rules for Black
		final JPanel afterCapturepBlackCheckBox = new JPanel();
		afterCapturepBlackCheckBox.setLayout(new GridLayout(4, 1));
			final JCheckBox bChangeColor = new JCheckBox("Capturer changes Color");
			bChangeColor.setEnabled(false);
			afterCapturepBlackCheckBox.add(bChangeColor);
			final JCheckBox bPieceReturn = new JCheckBox("Captured piece returns to start");
			afterCapturepBlackCheckBox.add(bPieceReturn);
			final JCheckBox bDrop = new JCheckBox("Captured Pieces Drop");
			afterCapturepBlackCheckBox.add(bDrop);
			final JCheckBox bCapturedColorAndDrop = new JCheckBox("Captured Piece Changes Color and Drops");
			afterCapturepBlackCheckBox.add(bCapturedColorAndDrop);

		//Special overall rules
		final JPanel sCheckBox = new JPanel();
		sCheckBox.setLayout(new GridLayout(3, 1));
			final JCheckBox atomic = new JCheckBox("Atomic Chess");
			sCheckBox.add(atomic);
			final JCheckBox pawnPromotion= new JCheckBox("Pawn Promotion", true);
			sCheckBox.add(pawnPromotion);
			final JCheckBox switchBoard = new JCheckBox("Move to other board");
			switchBoard.setEnabled(false);
			sCheckBox.add(switchBoard);

		
				
		//Create button and add ActionListener for going back
		final JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Driver.getInstance().setPanel(new BoardCustomMenu(b));
			}
		});
		
		//Button to move to save and move on
		JButton save = new JButton("Next");
		save.setToolTipText("Press me to save these rules");
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (wCaptureMand.isSelected()) {
					whiteRules.addAdjustTeamDests(new AdjustTeamDests("mustCapture"));
				}
				if (wNoMoveObj.isSelected()) {
					whiteRules.addCropLegalDests("stationaryObjective");
				}
				if (wCaptureAll.isSelected()) {
					whiteRules.addEndOfGame(new EndOfGame("loseAllPieces", true, 0, ""));
				}
				if (wCaptureAllType.isSelected()) {
					whiteRules.addEndOfGame(new EndOfGame("captureAllOfType", false, 0, "Knight")); //TODO - let the user define this. Currently not supported by GUI.
				}
				if (wProtectObj.isSelected()) {
					whiteRules.addEndOfGame(new EndOfGame("classic", false, 0, ""));
				}
				if (wLoseAll.isSelected()) {
					whiteRules.addEndOfGame(new EndOfGame("loseAllPieces", false, 0, ""));
				}
				if (wCheckTimes.isSelected()) {
					whiteRules.addEndOfGame(new EndOfGame("checkNTimes", false, 3, "")); //TODO let the user define this. Currently not supported by GUI.
				}
				if (wChangeColor.isSelected()) {
					whiteRules.addAfterMove("captureTeamSwap");
				}
				if (wPieceReturn.isSelected()) {
					whiteRules.addAfterMove("goHome");
				}
				if(wDrop.isSelected()){
					whiteRules.addAfterMove("placeCaptured");
				}
				if(wCapturedColorAndDrop.isSelected()){
					whiteRules.addAfterMove("placeCapturedSwitch");
				}
				if (bCaptureMand.isSelected()) {
					blackRules.addAdjustTeamDests(new AdjustTeamDests("mustCapture"));
				}
				if (bNoMoveObj.isSelected()) {
					blackRules.addCropLegalDests("stationaryObjective");
				}
				if (bCaptureAll.isSelected()) {
					blackRules.addEndOfGame(new EndOfGame("loseAllPieces", true, 0, ""));
				}
				if (bCaptureAllType.isSelected()) {
					blackRules.addEndOfGame(new EndOfGame("captureAllOfType", false, 0, "Knight")); //TODO - let the user define this. Currently not supported by GUI.
				}
				if (bProtectObj.isSelected()) {
					blackRules.addEndOfGame(new EndOfGame("classic", false, 0, ""));
				}
				if (bLoseAll.isSelected()) {
					blackRules.addEndOfGame(new EndOfGame("loseAllPieces", false, 0, ""));
				}
				if (bCheckTimes.isSelected()) {
					blackRules.addEndOfGame(new EndOfGame("checkNTimes", false, 3, "")); //TODO let the user define this. Currently not supported by GUI.
				}
				if (bChangeColor.isSelected()) {
					blackRules.addAfterMove("captureTeamSwap");
				}
				if (bPieceReturn.isSelected()) {
					blackRules.addAfterMove("goHome");
				}
				if(bDrop.isSelected()){
					blackRules.addAfterMove("placeCaptured");
				}
				if(bCapturedColorAndDrop.isSelected()){
					blackRules.addAfterMove("placeCapturedSwitch");
				}
				if (!pawnPromotion.isSelected()) {
					whiteRules.addGetPromotionSquares("noPromos");
					whiteRules.addPromote("noPromos");
					blackRules.addGetPromotionSquares("noPromos");
					blackRules.addPromote("noPromos");
				}
				if (switchBoard.isSelected() && boards.length == 2) {
					whiteRules.setGetBoard(new GetBoard("oppositeBoard"));
					blackRules.setGetBoard(new GetBoard("oppositeBoard"));
				}
				if(atomic.isSelected()){
					whiteRules.addAfterMove("atomicCapture");
					blackRules.addAfterMove("atomicCapture");
				}
				
				
				Driver.getInstance().setPanel(new PlayerCustomMenu(b, whiteRules, blackRules));
				}
			}
		);

		//Setting up the window
		
		//Setting up the white team
		JPanel whiteTeam = new JPanel();
		whiteTeam.setBorder(BorderFactory.createTitledBorder("White Team"));
		whiteTeam.setLayout(new GridBagLayout());
		
			JPanel whiteLegalDests = new JPanel();
			whiteLegalDests.setLayout(new GridBagLayout());
				c.gridx = 0;
				c.gridy = 1;
				whiteLegalDests.add(new JLabel("<html><u> Legal Destination </u></br></html>"), c);
				c.gridx = 0;
				c.gridy = 2;
				whiteLegalDests.add(legalDestWhiteCheckBox, c);
				
		c.gridx = 0;
		c.gridy = 0;
		whiteTeam.add(whiteLegalDests, c);
			
			JPanel whiteObj = new JPanel();
			whiteObj.setLayout(new GridBagLayout());
				c.gridheight = 1;
				c.gridx = 0;
				c.gridy = 1;
				whiteObj.add(new JLabel("<html><u> Objective </u></br></html>"), c);
				c.gridx = 0;
				c.gridy = 2;
				whiteObj.add(objectiveWhiteCheckBox, c);
				c.gridx = 0;
				c.gridy = 3;
				whiteObj.add(new JLabel(" "), c);
				
		c.gridx = 0;
		c.gridy = 1;
		whiteTeam.add(whiteObj, c);
				
			JPanel whiteCapture = new JPanel();
			whiteCapture.setLayout(new GridBagLayout());
				c.gridheight = 1;
				c.gridx = 0;
				c.gridy = 1;
				whiteCapture.add(new JLabel("<html><u>After Capturing a piece</u></br></html>"), c);
				c.gridx = 0;
				c.gridy = 2;
				whiteCapture.add(afterCaptureWhiteCheckBox, c);
			
		c.gridx = 0;
		c.gridy = 2;
		whiteTeam.add(whiteCapture, c);
		
		//Setting up the black team
		JPanel blackTeam = new JPanel();
		blackTeam.setBorder(BorderFactory.createTitledBorder("Black Team"));
		blackTeam.setLayout(new GridBagLayout());
		
			JPanel blackLegalDests = new JPanel();
			blackLegalDests.setLayout(new GridBagLayout());
				c.gridx = 0;
				c.gridy = 1;
				blackLegalDests.add(new JLabel("<html><u> Legal Destination </u></br></html>"), c);
				c.gridx = 0;
				c.gridy = 2;
				blackLegalDests.add(legalDestBlackCheckBox, c);
				
		c.gridx = 0;
		c.gridy = 0;
		blackTeam.add(blackLegalDests, c);	
			
			JPanel blackObj = new JPanel();
			blackObj.setLayout(new GridBagLayout());
				c.gridheight = 1;
				c.gridx = 0;
				c.gridy = 1;
				blackObj.add(new JLabel("<html><u> Objective </u></br></html>"), c);
				c.gridx = 0;
				c.gridy = 2;
				blackObj.add(objectiveBlackCheckBox, c);
				c.gridx = 0;
				c.gridy = 3;
				blackObj.add(new JLabel(" "), c);
				
		c.gridx = 0;
		c.gridy = 1;
		blackTeam.add(blackObj, c);
		
			JPanel blackCapture = new JPanel();
			blackCapture.setLayout(new GridBagLayout());
				c.gridheight = 1;
				c.gridx = 0;
				c.gridy = 1;
				blackCapture.add(new JLabel("<html><u>After Capturing a piece</u></br></html>"), c);
				c.gridx = 0;
				c.gridy = 2;
				blackCapture.add(afterCapturepBlackCheckBox, c);
		
		c.gridx = 0;
		c.gridy = 2;
		blackTeam.add(blackCapture, c);
			
		//Adding White team to window
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		add(whiteTeam, c);
		
		//Adding black team to the window
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 1;
		c.gridy = 0;
		add(blackTeam, c);
		
		//Adding special rules to the window
		JPanel specialRules = new JPanel();
		specialRules.setBorder(BorderFactory.createTitledBorder("Special rules"));
		specialRules.setLayout(new GridBagLayout());
		
			c.gridx = 0;
			c.gridy = 1;
			specialRules.add(sCheckBox, c);
			c.gridx = 0;
			c.gridy = 2;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		add(specialRules, c);
		
		//Adding Buttons to the window
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.add(back);
		buttons.add(save);
			
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		add(buttons, c);
		
		
		//Setting up which buttons turn off others
		//Objective can't move
			wNoMoveObj.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(wNoMoveObj.isSelected()){
						wCaptureAll.setEnabled(false);
						wCaptureAll.setSelected(false);
						wCaptureAllType.setEnabled(false);
						wCaptureAllType.setSelected(false);
						wLoseAll.setEnabled(false);
						wLoseAll.setSelected(false);
						//TODO force an objective piece selection somehow.
					}
					else{
						wCaptureAll.setEnabled(true);
						wCaptureAllType.setEnabled(true);
						wLoseAll.setEnabled(true);
					}
				}
			});
			bNoMoveObj.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(bNoMoveObj.isSelected()){
						bCaptureAll.setEnabled(false);
						bCaptureAll.setSelected(false);
						bCaptureAllType.setEnabled(false);
						bCaptureAllType.setSelected(false);
						bLoseAll.setEnabled(false);
						bLoseAll.setSelected(false);
						//TODO force an objective piece selection somehow.
					}
					else{
						bCaptureAll.setEnabled(true);
						bCaptureAllType.setEnabled(true);
						bLoseAll.setEnabled(true);
					}
				}
			});
		
		//Capture All
			wCaptureAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(wCaptureAll.isSelected()){
						wProtectObj.setEnabled(false);
						wProtectObj.setSelected(false);
						wCaptureAllType.setEnabled(false);
						wCaptureAllType.setSelected(false);
						wLoseAll.setEnabled(false);
						wLoseAll.setSelected(false);
						wCheckTimes.setEnabled(false);
						wCheckTimes.setSelected(false);
						wDrop.setEnabled(false);
						wDrop.setSelected(false);
					}
					else{
						wProtectObj.setEnabled(true);
						wCaptureAllType.setEnabled(true);
						wLoseAll.setEnabled(true);
						wCheckTimes.setEnabled(true);
						wDrop.setEnabled(true);
					}
				}
			});
			bCaptureAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(bCaptureAll.isSelected()){
						bProtectObj.setEnabled(false);
						bProtectObj.setSelected(false);
						bCaptureAllType.setEnabled(false);
						bCaptureAllType.setSelected(false);
						bLoseAll.setEnabled(false);
						bLoseAll.setSelected(false);
						bCheckTimes.setEnabled(false);
						bCheckTimes.setSelected(false);
						bDrop.setEnabled(false);
						bDrop.setSelected(false);
					}
					else{
						bProtectObj.setEnabled(true);
						bCaptureAllType.setEnabled(true);
						bLoseAll.setEnabled(true);
						bCheckTimes.setEnabled(true);
						bDrop.setEnabled(true);
					}
				}
			});
		
		//Capture All of Type
			wCaptureAllType.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(wCaptureAllType.isSelected()){
						wProtectObj.setEnabled(false);
						wProtectObj.setSelected(false);
						wCaptureAll.setEnabled(false);
						wCaptureAll.setSelected(false);
						wLoseAll.setEnabled(false);
						wLoseAll.setSelected(false);
						wCheckTimes.setEnabled(false);
						wCheckTimes.setSelected(false);
						wDrop.setEnabled(false);
						wDrop.setSelected(false);
					}
					else{
						wProtectObj.setEnabled(true);
						wCaptureAll.setEnabled(true);
						wLoseAll.setEnabled(true);
						wCheckTimes.setEnabled(true);
						wDrop.setEnabled(true);
					}
				}
			});
			bCaptureAllType.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(bCaptureAllType.isSelected()){
						bProtectObj.setEnabled(false);
						bProtectObj.setSelected(false);
						bCaptureAll.setEnabled(false);
						bCaptureAll.setSelected(false);
						bLoseAll.setEnabled(false);
						bLoseAll.setSelected(false);
						bCheckTimes.setEnabled(false);
						bCheckTimes.setSelected(false);
						bDrop.setEnabled(false);
						bDrop.setSelected(false);
					}
					else{
						bProtectObj.setEnabled(true);
						bCaptureAll.setEnabled(true);
						bLoseAll.setEnabled(true);
						bCheckTimes.setEnabled(true);
						bDrop.setEnabled(true);
					}
				}
			});
		
		//Protect Objective
			wProtectObj.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(wProtectObj.isSelected()){
						wChangeColor.setEnabled(false);
						wChangeColor.setSelected(false);
						wCaptureAllType.setEnabled(false);
						wCaptureAllType.setSelected(false);
						wCaptureAll.setEnabled(false);
						wCaptureAll.setSelected(false);
						wLoseAll.setEnabled(false);
						wLoseAll.setSelected(false);
						wCheckTimes.setEnabled(false);
						wCheckTimes.setSelected(false);
						//TODO force an objective piece selection somehow.
					}
					else{
						wChangeColor.setEnabled(true);
						wCaptureAllType.setEnabled(true);
						wCaptureAll.setEnabled(true);
						wLoseAll.setEnabled(true);
						wCheckTimes.setEnabled(true);
					}
				}
			});
			bProtectObj.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(bProtectObj.isSelected()){
						bChangeColor.setEnabled(false);
						bChangeColor.setSelected(false);
						bCaptureAll.setEnabled(false);
						bCaptureAll.setSelected(false);
						bCaptureAllType.setEnabled(false);
						bCaptureAllType.setSelected(false);
						bLoseAll.setEnabled(false);
						bLoseAll.setSelected(false);
						bCheckTimes.setEnabled(false);
						bCheckTimes.setSelected(false);
						//TODO force an objective piece selection somehow.
					}
					else{
						bChangeColor.setEnabled(true);
						bCaptureAll.setEnabled(true);
						bCaptureAllType.setEnabled(true);
						bLoseAll.setEnabled(true);
						bCheckTimes.setEnabled(true);
					}
				}
			});
		
		//Lose All Pieces	
			wLoseAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(wLoseAll.isSelected()){
						wProtectObj.setEnabled(false);
						wProtectObj.setSelected(false);
						wCaptureAll.setEnabled(false);
						wCaptureAll.setSelected(false);
						wCaptureAllType.setEnabled(false);
						wCaptureAllType.setSelected(false);
						wCheckTimes.setEnabled(false);
						wCheckTimes.setSelected(false);
						wDrop.setEnabled(false);
						wDrop.setSelected(false);
					}
					else{
						wProtectObj.setEnabled(true);
						wCaptureAll.setEnabled(true);
						wCaptureAllType.setEnabled(true);
						wCheckTimes.setEnabled(true);
						wDrop.setEnabled(true);
					}
				}
			});
			bLoseAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(bLoseAll.isSelected()){
						bProtectObj.setEnabled(false);
						bProtectObj.setSelected(false);
						bCaptureAllType.setEnabled(false);
						bCaptureAllType.setSelected(false);
						bCaptureAll.setEnabled(false);
						bCaptureAll.setSelected(false);
						bCheckTimes.setEnabled(false);
						bCheckTimes.setSelected(false);
						bDrop.setEnabled(false);
						bDrop.setSelected(false);
					}
					else{
						bProtectObj.setEnabled(true);
						bCaptureAllType.setEnabled(true);
						bCaptureAll.setEnabled(true);
						bCheckTimes.setEnabled(true);
						bDrop.setEnabled(true);
					}
				}
			});
			
		//Check # of Times
			wCheckTimes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(wCheckTimes.isSelected()){
						wProtectObj.setEnabled(false);
						wProtectObj.setSelected(false);
						wCaptureAll.setEnabled(false);
						wCaptureAll.setSelected(false);
						wCaptureAllType.setEnabled(false);
						wCaptureAllType.setSelected(false);
						wLoseAll.setEnabled(false);
						wLoseAll.setSelected(false);
						//TODO make a pop up to get how many checks
					}
					else{
						wProtectObj.setEnabled(true);
						wCaptureAll.setEnabled(true);
						wCaptureAllType.setEnabled(true);
						wLoseAll.setEnabled(true);
					}
				}
			});
			bCheckTimes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(bCheckTimes.isSelected()){
						bProtectObj.setEnabled(false);
						bProtectObj.setSelected(false);
						bCaptureAllType.setEnabled(false);
						bCaptureAllType.setSelected(false);
						bCaptureAll.setEnabled(false);
						bCaptureAll.setSelected(false);
						bLoseAll.setEnabled(false);
						bLoseAll.setSelected(false);
						//TODO make a pop up to get how many checks
					}
					else{
						bProtectObj.setEnabled(true);
						bCaptureAllType.setEnabled(true);
						bCaptureAll.setEnabled(true);
						bLoseAll.setEnabled(true);
					}
				}
			});
			
		//Capturer Changes color
			wChangeColor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(wChangeColor.isSelected()){
						wProtectObj.setEnabled(false);
						wProtectObj.setSelected(false);
						atomic.setEnabled(false);
						atomic.setSelected(false);
					}
					else{
						wProtectObj.setEnabled(true);
						atomic.setEnabled(true);
					}
				}
			});
			bChangeColor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(bChangeColor.isSelected()){
						bProtectObj.setEnabled(false);
						bProtectObj.setSelected(false);
						atomic.setEnabled(false);
						atomic.setSelected(false);
					}
					else{
						bProtectObj.setEnabled(true);
						atomic.setEnabled(true);
					}
				}
			});
			
		//Captured returns to start
			wPieceReturn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(wPieceReturn.isSelected()){
						atomic.setEnabled(false);
						atomic.setSelected(false);
						wCapturedColorAndDrop.setEnabled(false);
						wCapturedColorAndDrop.setSelected(false);
						wDrop.setEnabled(false);
						wDrop.setSelected(false);
					}
					else{
						atomic.setEnabled(true);
						wCapturedColorAndDrop.setEnabled(true);
						wDrop.setEnabled(true);
					}
				}
			});
			bPieceReturn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(bPieceReturn.isSelected()){
						atomic.setEnabled(false);
						atomic.setSelected(false);
						bCapturedColorAndDrop.setEnabled(false);
						bCapturedColorAndDrop.setSelected(false);
						bDrop.setEnabled(false);
						bDrop.setSelected(false);
					}
					else{
						atomic.setEnabled(true);
						bCapturedColorAndDrop.setEnabled(true);
						bDrop.setEnabled(true);
					}
				}
			});
			
		//Atomic chess
			atomic.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(atomic.isSelected()){
						wChangeColor.setEnabled(false);
						wChangeColor.setSelected(false);
						bChangeColor.setEnabled(false);
						bChangeColor.setSelected(false);
						wCapturedColorAndDrop.setEnabled(false);
						wCapturedColorAndDrop.setSelected(false);
						bCapturedColorAndDrop.setEnabled(false);
						bCapturedColorAndDrop.setSelected(false);
						wDrop.setEnabled(false);
						wDrop.setSelected(false);
						bDrop.setEnabled(false);
						bDrop.setSelected(false);
						wPieceReturn.setEnabled(false);
						wPieceReturn.setSelected(false);
						bPieceReturn.setEnabled(false);
						bPieceReturn.setSelected(false);
					}
					else{
						wChangeColor.setEnabled(true);
						bChangeColor.setEnabled(true);
						wCapturedColorAndDrop.setEnabled(true);
						bCapturedColorAndDrop.setEnabled(true);
						wDrop.setEnabled(true);
						bDrop.setEnabled(true);
						wPieceReturn.setEnabled(true);
						bPieceReturn.setEnabled(true);
					}
				}
			});
			
		//Captured Pieces color change and drop
			wCapturedColorAndDrop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(wCapturedColorAndDrop.isSelected()){
						atomic.setEnabled(false);
						atomic.setSelected(false);
						wDrop.setEnabled(false);
						wDrop.setSelected(false);
						wPieceReturn.setEnabled(false);
						wPieceReturn.setSelected(false);
					}
					else{
						atomic.setEnabled(true);
						wDrop.setEnabled(true);
						wPieceReturn.setEnabled(true);
					}
				}
			});
			bCapturedColorAndDrop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(bCapturedColorAndDrop.isSelected()){
						atomic.setEnabled(false);
						atomic.setSelected(false);
						bDrop.setEnabled(false);
						bDrop.setSelected(false);
						bPieceReturn.setEnabled(false);
						bPieceReturn.setSelected(false);
					}
					else{
						atomic.setEnabled(true);
						bDrop.setEnabled(true);
						bPieceReturn.setEnabled(true);
					}
				}
			});

		//Captured Pieces dropped
			wDrop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(wDrop.isSelected()){
						atomic.setEnabled(false);
						atomic.setSelected(false);
						wCapturedColorAndDrop.setEnabled(false);
						wCapturedColorAndDrop.setSelected(false);
						wPieceReturn.setEnabled(false);
						wPieceReturn.setSelected(false);
						wCaptureAll.setEnabled(false);
						wCaptureAll.setSelected(false);
						wCaptureAllType.setEnabled(false);
						wCaptureAllType.setSelected(false);
						wLoseAll.setEnabled(false);
						wLoseAll.setSelected(false);
					}
					else{
						atomic.setEnabled(true);
						wCapturedColorAndDrop.setEnabled(true);
						wPieceReturn.setEnabled(true);
						wCaptureAll.setEnabled(true);
						wCaptureAllType.setEnabled(true);
						wLoseAll.setEnabled(true);
					}
				}
			});
			bDrop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(bDrop.isSelected()){
						atomic.setEnabled(false);
						atomic.setSelected(false);
						bCapturedColorAndDrop.setEnabled(false);
						bCapturedColorAndDrop.setSelected(false);
						bPieceReturn.setEnabled(false);
						bPieceReturn.setSelected(false);
						bCaptureAll.setEnabled(false);
						bCaptureAll.setSelected(false);
						bCaptureAllType.setEnabled(false);
						bCaptureAllType.setSelected(false);
						bLoseAll.setEnabled(false);
						bLoseAll.setSelected(false);
					}
					else{
						atomic.setEnabled(true);
						bCapturedColorAndDrop.setEnabled(true);
						bPieceReturn.setEnabled(true);
						bCaptureAll.setEnabled(true);
						bCaptureAllType.setEnabled(true);
						bLoseAll.setEnabled(true);
					}
				}
			});
			
		//Move to other Board
			if(boards.length == 2){
				switchBoard.setEnabled(true);
			}
	}
}
