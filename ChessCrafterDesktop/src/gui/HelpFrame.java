package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
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
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		JPanel gamePlayHelpPanel = new JPanel();
		JPanel variantMakingHelpPanel = new JPanel();
		JPanel generalHelpPanel = new JPanel();
		JPanel pieceMakingHelpPanel = new JPanel();

		JTabbedPane helpTypesTabbedPane = new JTabbedPane();
		helpTypesTabbedPane.addTab("General Help", null, generalHelpPanel, "Click for General Help with " + AppConstants.APP_NAME);
		helpTypesTabbedPane.addTab("Game Play Help", null, gamePlayHelpPanel, "Click for help playing Chess");
		helpTypesTabbedPane.addTab("New Game Type Help", null, variantMakingHelpPanel, "Click for help making a new Game Type");
		helpTypesTabbedPane.addTab("Piece Making Help", null, pieceMakingHelpPanel, "Click for help making new Game Pieces");
		helpTypesTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(helpTypesTabbedPane, constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 1;

		JTextArea gamePlayTextArea = new JTextArea();
		gamePlayTextArea.setEditable(false);
		gamePlayTextArea.setText("To move a piece simply click on it and the places you can move it will be highlighted.\n"
				+ "\tIf that piece has no moves, no squares will be highlighted.\n\n" + "For games with an objective piece:\n"
				+ "\tIf the Objective is in check the attacking piece will be highlighted in Red\n"
				+ "\tWhile the piece is in check, only moves that break check are allowed\n\n"
				+ "If you want to undo a move simply press Undo\n" + "\tThis feature is unavailable in Network Play\n\n"
				+ "Options specific to the game can be found under Options:\n"
				+ "\t1. Save and Quit will save your current game and return to the main menu\n"
				+ "\t\t- This feature is unavailable in Network Play\n" + "\t2. Declare Draw will declare the game to be a draw\n"
				+ "\t\t- In network Play this feature will request a draw from the other player\n\n"
				+ "Once a game has been completed or decalred a draw it can no longer be played,\n"
				+ "but it can be reviewed from the View Completed Games on the Main Menu");
		gamePlayHelpPanel.add(gamePlayTextArea);

		JTextArea generalHelpText = new JTextArea();
		generalHelpText.setEditable(false);
		generalHelpText.setText("Thanks for playing " + AppConstants.APP_NAME + "!\n\n"
				+ "Our Main Menu is designed to quickly guide you to the major 4 features of " + AppConstants.APP_NAME + ":\n"
				+ "\t1. New Game - This will start a new game of Chess which you can play against\n"
				+ "\t\t\tanother person or against an AI\n"
				+ "\t2. Create New Game Type - This will allow you craft a Chess Variant of your own\n"
				+ "\t3. View Completed Games - This will let you watch games you've already completed\n"
				+ "\t4. Load Game - This will load a game you were previously playing\n" + "\t\t\tso you can keep playing\n\n"
				+ "Also at any time you can use the File menu or the Options menu to see what\n" + "options are available to you.");
		generalHelpPanel.add(generalHelpText);

		JTextArea variantMakingHelpTextArea = new JTextArea();
		variantMakingHelpTextArea.setEditable(false);
		variantMakingHelpTextArea.setText("Once you choose to create a new Game Type you will need to choose a few options:\n"
				+ "\t1. Create a Name for your game\n" + "\t2. Set up what the board will look like\n"
				+ "\t3. Create any custom pieces you need\n" + "\t4. Determine the rules and how to win of your game\n"
				+ "\t5. Determine how many turns each player should get per round\n\n" + "To place a piece on the board:\n"
				+ "\t1. Click on the piece name from the list of piece types\n"
				+ "\t2. Click on the Square containing the piece color type you want to place\n"
				+ "\t3. Click anywhere on the board to drop the piece\n" + "\t\t- You can keep clicking other squares to drop more\n"
				+ "\t4. To change what piece you are dropping choose a new one from the list\n\n"
				+ "To Remove pieces or change squares on the board:\n" + "\t1. Clear your piece selection\n"
				+ "\t\t - You can do this by clicking on the piece that has a Blue background\n"
				+ "\t2. Click on any square with a piece in it to remove the piece\n"
				+ "\t3. Click on any empty square to change it's color or whether it is habitable\n\n"
				+ "To allow a piece to promote to something else. Simply click on the piece in the list\n"
				+ "and choose Promote This Piece\n\n"
				+ "When you are completly finished press the Save button to save your variant.\n"
				+ "You can then play it from the New Game option on the home page just like normal chess.");
		variantMakingHelpPanel.add(variantMakingHelpTextArea);

		JTextArea pieceMakingHelpTextArea = new JTextArea();
		pieceMakingHelpTextArea.setEditable(false);
		pieceMakingHelpTextArea
				.setText("When you craft a new Chess variant you can make your own custom pieces! Follow these steps:\n"
						+ "\t1. Create a name and choose the icons for your new piece\n"
						+ "\t2. Enter the specific movement directions/distances you would like it to have\n"
						+ "\t\t - To add instructions for a direction choose it from the drop down, then fill in\n"
						+ "\t\t the farthest distance that piece should move in that direction into the box\n"
						+ "\t\t - When you are finished with that particular direction press\n"
						+ "\t\t the \"Add Movement\" button to add it to the piece\n"
						+ "\t\t - Repeat this process for each direction the piece needs\n"
						+ "\t 3. If you would like your piece to be able to jump over other pieces, check the box\n"
						+ "\t 4. If you want this piece to move like a Knight does enter the directions in this format:\n\n"
						+ "\t\t\t\t\t\t 3 x 2\n\n" + "\t\t - Enter movements either by column and then row or vice versa.\n"
						+ "\t\t As an example a normal knight moves 2 x 1 squares\n"
						+ "\t\t - Entering one combination (e.g. 3 x 2) will allow the piece\n"
						+ "\t\t to move both 3 x 2 and 2 x 3 spaces\n"
						+ "\t 5. When you have finished making your piece press \"Save Piece\"\n"
						+ "\t\t - This piece will then be available for you to use in your variant\n"
						+ "\t 6. Repeat this process for all pieces that you need and then press\n"
						+ "\t the \"Done\" button at the bottom");
		pieceMakingHelpPanel.add(pieceMakingHelpTextArea);

		setVisible(true);
	}
	
	private static final long serialVersionUID = -3375921014569944071L;
}
