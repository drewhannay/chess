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
		
		final JPanel ldwCheckBox = new JPanel();
		ldwCheckBox.setLayout(new GridLayout(3, 1));
		ldwCheckBox.add(new JCheckBox("Capture Mandatory"));
		ldwCheckBox.add(new JCheckBox("Can't Move Objective"));

		final JPanel obwCheckBox = new JPanel();
		obwCheckBox.setLayout(new GridLayout(5, 1));
		obwCheckBox.add(new JCheckBox("Capture All", false));
		obwCheckBox.add(new JCheckBox("Capture All of Type", false));
		obwCheckBox.add(new JCheckBox("Protect Objective", false));
		obwCheckBox.add(new JCheckBox("Lose All Pieces", false));
		obwCheckBox.add(new JCheckBox("Check # Times", false));

		final JPanel acpwCheckBox = new JPanel();
		acpwCheckBox.setLayout(new GridLayout(4, 1));
		acpwCheckBox.add(new JCheckBox("Capturer changes Color"));
		acpwCheckBox.add(new JCheckBox("Captured piece returns to start"));

		final JPanel ldbCheckBox = new JPanel();
		ldbCheckBox.setLayout(new GridLayout(3, 1));
		ldbCheckBox.add(new JCheckBox("Capture Mandatory"));
		ldbCheckBox.add(new JCheckBox("Can't Move Objective"));

		final JPanel obbCheckBox = new JPanel();
		obbCheckBox.setLayout(new GridLayout(5, 1));
		obbCheckBox.add(new JCheckBox("Capture All", false));
		obbCheckBox.add(new JCheckBox("Capture All of Type", false));
		obbCheckBox.add(new JCheckBox("Protect Objective", false));
		obbCheckBox.add(new JCheckBox("Lose All Pieces", false));
		obbCheckBox.add(new JCheckBox("Check # Times", false));

		final JPanel acpbCheckBox = new JPanel();
		acpbCheckBox.setLayout(new GridLayout(4, 1));
		acpbCheckBox.add(new JCheckBox("Capturer changes Color"));
		acpbCheckBox.add(new JCheckBox("Captured piece returns to start"));

		final JPanel sCheckBox = new JPanel();
		sCheckBox.setLayout(new GridLayout(3, 1));
		sCheckBox.add(new JCheckBox("Pawn Promotion"));
		sCheckBox.add(new JCheckBox("Move to other board"));

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
				/*                           
				if (((Checkbox) ldwCheckBox.getComponent(0)).getState()) {
					whiteRules.addAdjustTeamDests(new AdjustTeamDests("mustCapture"));
				}
				if (((Checkbox) ldwCheckBox.getComponent(1)).getState()) {
					whiteRules.addCropLegalDests("stationaryObjective");
				}
				if (((Checkbox) obwCheckBox.getComponent(0)).getState()) {
					whiteRules.addEndOfGame(new EndOfGame("loseAllPieces", true, 0, ""));
				}
				if (((Checkbox) obwCheckBox.getComponent(1)).getState()) {
					whiteRules.addEndOfGame(new EndOfGame("captureAllOfType", false, 0, "Knight")); //TODO - let the user define this. Currently not supported by GUI.
				}
				if (((Checkbox) obwCheckBox.getComponent(2)).getState()) {
					whiteRules.addEndOfGame(new EndOfGame("classic", false, 0, ""));
				}
				if (((Checkbox) obwCheckBox.getComponent(3)).getState()) {
					whiteRules.addEndOfGame(new EndOfGame("loseAllPieces", false, 0, ""));
				}
				if (((Checkbox) obwCheckBox.getComponent(4)).getState()) {
					whiteRules.addEndOfGame(new EndOfGame("checkNTimes", false, 3, "")); //TODO let the user define this. Currently not supported by GUI.
				}
				if (((Checkbox) acpwCheckBox.getComponent(0)).getState()) {
					whiteRules.addAfterMove("captureTeamSwap");
				}
				if (((Checkbox) acpwCheckBox.getComponent(1)).getState()) {
					whiteRules.addAfterMove("goHome");
				}

				if (((Checkbox) ldbCheckBox.getComponent(0)).getState()) {
					blackRules.addAdjustTeamDests(new AdjustTeamDests("mustCapture"));
				}
				if (((Checkbox) ldbCheckBox.getComponent(1)).getState()) {
					blackRules.addCropLegalDests("stationaryObjective");
				}
				if (((Checkbox) obbCheckBox.getComponent(0)).getState()) {
					blackRules.addEndOfGame(new EndOfGame("loseAllPieces", true, 0, ""));
				}
				if (((Checkbox) obbCheckBox.getComponent(1)).getState()) {
					blackRules.addEndOfGame(new EndOfGame("captureAllOfType", false, 0, "Knight")); //TODO - let the user define this. Currently not supported by GUI.
				}
				if (((Checkbox) obbCheckBox.getComponent(2)).getState()) {
					blackRules.addEndOfGame(new EndOfGame("classic", false, 0, ""));
				}
				if (((Checkbox) obbCheckBox.getComponent(3)).getState()) {
					blackRules.addEndOfGame(new EndOfGame("loseAllPieces", false, 0, ""));
				}
				if (((Checkbox) obbCheckBox.getComponent(4)).getState()) {
					blackRules.addEndOfGame(new EndOfGame("checkNTimes", false, 3, "")); //TODO let the user define this. Currently not supported by GUI.
				}
				if (((Checkbox) acpbCheckBox.getComponent(0)).getState()) {
					blackRules.addAfterMove("captureTeamSwap");
				}
				if (((Checkbox) acpbCheckBox.getComponent(1)).getState()) {
					blackRules.addAfterMove("goHome");
				}
				if (!((Checkbox) sCheckBox.getComponent(0)).getState()) {
					whiteRules.addGetPromotionSquares("noPromos");
					whiteRules.addPromote("noPromos");
					blackRules.addGetPromotionSquares("noPromos");
					blackRules.addPromote("noPromos");
				}
				if (((Checkbox) sCheckBox.getComponent(1)).getState() && boards.length == 2) {
					whiteRules.setGetBoard(new GetBoard("oppositeBoard"));
					blackRules.setGetBoard(new GetBoard("oppositeBoard"));
				}
				 */
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
				whiteLegalDests.add(ldwCheckBox, c);
				
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
				whiteObj.add(obwCheckBox, c);
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
				whiteCapture.add(acpwCheckBox, c);
			
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
				blackLegalDests.add(ldbCheckBox, c);
				
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
				blackObj.add(obbCheckBox, c);
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
				blackCapture.add(acpbCheckBox, c);
		
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
		
	}
}
