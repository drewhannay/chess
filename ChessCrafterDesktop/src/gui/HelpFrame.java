package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import utility.AppConstants;

public class HelpFrame extends JFrame
{
	public HelpFrame()
	{
		initGUIComponents();
	}

	private void initGUIComponents()
	{
		setTitle("Help");
		setSize(825, 525);
		setResizable(false);
		setLocationRelativeTo(Driver.getInstance());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		JPanel gamePlayHelpPanel = new JPanel();
		JPanel variantMakingHelpPanel = new JPanel();
		JPanel generalHelpPanel = new JPanel();
		JPanel pieceMakingHelpPanel = new JPanel();

		JTabbedPane helpTypesTabbedPane = new JTabbedPane();
		helpTypesTabbedPane.setSize(825, 525);
		helpTypesTabbedPane.addTab("General Help", null, generalHelpPanel, "Click for General Help with " + AppConstants.APP_NAME);
		helpTypesTabbedPane.addTab("Game Play Help", null, gamePlayHelpPanel, "Click for help playing Chess");
		helpTypesTabbedPane.addTab("Variant Creation Help", null, variantMakingHelpPanel, "Click for help making a new Variant");
		helpTypesTabbedPane.addTab("Piece Making Help", null, pieceMakingHelpPanel, "Click for help making new Game Pieces");
		helpTypesTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		add(helpTypesTabbedPane);

		JLabel gamePlayTextArea = new JLabel();
		gamePlayTextArea.setText("<html><br/><div><font size=\"3\"><b>Moving your pieces in a game:</b></font></div><div><ul><li><font size=\"3\">To move a piece simply click on it and the places you can move it will be highlighted</font></li><li><font size=\"3\">If that piece has no moves, no squares will be highlighted</font></li><li><font size=\"3\">If you want to undo a move simply press Undo* </font></li></ul></div><div><font size=\"3\"><b>For games with an objective piece: </b></font></div><div><ul><li><font size=\"3\">If the Objective is in check the attacking piece will be highlighted in Red</font></li><li><font size=\"3\">While the piece is in check, only moves that break check are allowed</font></li></ul></div><div><font size=\"3\"><b>Special options available in game:</b></font></div><div><ul><li><font size=\"3\">Save and Quit will save your current game and return to the main menu*</font></li><li><font size=\"3\">Declare Draw will declare the game to be a draw if you are playing Human Play or AI play and request a draw in Network Play</font></li></ul><div><font size=\"3\"><b>Finishing your game:</b></font></div></div><div><ul><li><font size=\"3\">Your game will complete when checkmate or stalemate is reached</font></li><li><font size=\"3\">When your game is finished you will have the option of restarting or returning to the main menu</font></li><li><font size=\"3\">Once a game has been completed or declared a draw it can no longer be played,<br/> but it can be reviewed from the View Completed Games on the Main Menu</font></li></ul></div><div><font size=\"2\">*This feature is unavailable in Network Play</font></div></html>");
		gamePlayHelpPanel.add(gamePlayTextArea);

		JLabel generalHelpText = new JLabel();
		generalHelpText.setText("<html><br/><div><font size=\"3\"><b>Thanks for playing Chess Crafter!</b></font></div><div><font size=\"3\"><br /></font></div><div><font size=\"3\">You can use the main menu to do any of the following:</font></div><div><ol><li><font size=\"3\"><b>New Game</b> - Starts a new game of Chess which you can play against another person on the same computer or against an AI</font></li><li><font size=\"3\"><b>Variants</b> - This will allow you craft a new Chess Variant of your own or edit one you've already created</font></li><li><font size=\"3\"><b>Pieces</b> - You can add new pieces for your variants</font></li><li><font size=\"3\"><b>Completed Games</b> - This will let you watch games you've already completed</font></li><li><font size=\"3\"><b>Load Game</b> - This will load a game you were previously playing so you can keep playing</font></li></ol></div><div><font size=\"3\">Also at any time you can use the File menu to see additional </font><span style=\"font-size: medium;\">options.</span></div></html>");
		generalHelpPanel.add(generalHelpText);

		JLabel variantMakingHelpTextArea = new JLabel();
		variantMakingHelpTextArea.setText("<html><br/><div><b><font size=\"3\">Making new Variants:</font></b></div><div><ul><li><font size=\"3\">Create a Name for your game</font></li><li><font size=\"3\">Set up what the board will look like</font></li><li><font size=\"3\">Create any custom pieces you need</font></li><li><font size=\"3\">Determine the rules and how to win of your game</font></li><li><font size=\"3\">Determine how many turns each player should get per round</font></li></ul></div><div><b><font size=\"3\">To place a piece on the board:</font></b></div><div><ul><li><font size=\"3\">Click on the piece name from the list of piece types</font></li><li><font size=\"3\">Click on the Square containing the piece color type you want to place</font></li><li><font size=\"3\">Click anywhere on the board to drop the piece</font></li><ul><li><font size=\"3\">You can keep clicking other squares to drop more</font></li></ul><li><font size=\"3\">To change what piece you are dropping choose a new one from the list</font></li><li><font size=\"3\">You can allow a piece to be able to be promoted by clicking on it in the list of pieces and choosing Promote This Piece</font></li></ul></div><div><b><font size=\"3\">Remove pieces or change squares on the board:</font></b></div><div><ul><li><font size=\"3\">Clear your piece selection</font></li><ul><li><font size=\"3\">You can do this by clicking on the piece that has a Blue background</font></li></ul><li><font size=\"3\">Click on any square with a piece in it to remove the piece</font></li><li><font size=\"3\">Click on any empty square to change it's color or whether it is habitable</font></li></ul></div><div><font size=\"3\">When you are completely finished press the Save button to save your variant.</font></div><div><font size=\"3\"><br/>You can then play it from the New Game option on the home page just like normal chess.</font></div></html>");
		variantMakingHelpPanel.add(variantMakingHelpTextArea);

		JLabel pieceMakingHelpTextArea = new JLabel();
		pieceMakingHelpTextArea.setText("<html><br/><div><font size=\"3\"><b>Creating a new game piece</b>:</font></div><div><ol><li><font size=\"3\">Create a name and choose the icons for your new piece</font></li><li><font size=\"3\">Enter the specific movement directions/distances you would like it to have for each possible direction (leave 0 for none) </font></li><li><font size=\"3\">If you would like your piece to be able to jump over other pieces, check the box</font></li><ol><li><font size=\"3\">If you want this piece to move like a Knight does enter the directions in this format: 2 x 1</font></li><li><font size=\"3\">Entering one combination (e.g. 2 x 1) will allow the piece to move both 2 x 1 and 1 x 2</font></li></ol><li><font size=\"3\">When you have finished making your piece press &quot;Save Piece&quot;</font></li><li><font size=\"3\">This piece will then be available for you to use in any of your variants</font></li><li><font size=\"3\">Repeat this process for all pieces that you need and then press the &quot;Done&quot; button at the bottom</font></li></ol></div></html>");
		pieceMakingHelpPanel.add(pieceMakingHelpTextArea);

		setVisible(true);
	}
	
	private static final long serialVersionUID = -3375921014569944071L;
}
