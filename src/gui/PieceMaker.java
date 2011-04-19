package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logic.Builder;
import logic.PieceBuilder;

/**
 * @author jmccormi
 * Class to make new pieces for new variants
 */
public class PieceMaker extends JPanel{

	/**
	 * Because it told me to
	 */
	private static final long serialVersionUID = -6530771731937840358L;
	/**
	 * Builder for the new variant
	 */
	private Builder b;
	/**
	 * New Piece builder for the new.... piece
	 */
	private PieceBuilder builder;
	/**
	 * Boolean to see if we are making a knight piece
	 */
	private boolean knightLike = false;
	
	/**
	 * Constructor for piece making window
	 * @param b reference to the builder
	 */
	public PieceMaker(Builder b){
		this.b = b;
		PieceBuilder.initPieceTypes();
		initComponents();
	}
	
	
	/**
	 * Method to set up the piece making window.
	 */
	public void initComponents(){
			//Create a new PieceBuilder.
		builder = new PieceBuilder();
	
		//Create the pop up and set the size, location and layout.
		setSize(550,875);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Driver.getInstance().setLocationRelativeTo(null);

		JPanel piecePanel = new JPanel();
		piecePanel.setLayout(new GridBagLayout());
		piecePanel.setBorder(BorderFactory.createTitledBorder("New Piece"));

		
			//Add a JLabel and JTextField for the Piece name.
			JPanel namePanel =  new JPanel();
			namePanel.setLayout(new FlowLayout());
			
			namePanel.add(new JLabel("Piece Name:"));
			final JTextField name = new JTextField(15);
			namePanel.add(name);
		
		c.gridx=0;
		c.gridy=0;
		piecePanel.add(namePanel, c);
		
			final ImageIcon temp = new ImageIcon("./images/WhiteSquare.gif");
			temp.setImage(temp.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
			final JPanel lightIconPanel = new JPanel();
			lightIconPanel.setLayout(new FlowLayout());
			final JButton lightIconButton = new JButton();
			lightIconButton.setSize(48, 48);
			lightIconButton.setIcon(temp);
		
			//Add JButtons for choosing the images for the new type.
			final JButton chooseLightImage = new JButton("Choose image for light piece");
			chooseLightImage.addActionListener(new ActionListener() {
		
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Create the JFileChooser and add image for the new piece
					final JFileChooser fc = new JFileChooser("./images"); //default directory is in the images folder
					int returnVal = fc.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						ImageIcon icon = makeIcon(fc, builder);
						lightIconButton.setIcon(icon); //Adds icon to button
						builder.setLightImage(icon);
					}
				}
			});
			lightIconPanel.add(chooseLightImage);
			lightIconPanel.add(lightIconButton);
			
		c.gridx = 0;
		c.gridy = 2;
		piecePanel.add(lightIconPanel, c);
	
			final JPanel darkIconPanel = new JPanel();
			darkIconPanel.setLayout(new FlowLayout());
			final JButton darkIconButton = new JButton();
			darkIconButton.setSize(48, 48);
			darkIconButton.setIcon(temp);
			
			final JButton chooseDarkImage = new JButton("Choose image for dark piece");
			chooseDarkImage.addActionListener(new ActionListener() {
		
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Create the JFileChooser and add image for the new piece
					final JFileChooser fc = new JFileChooser("./images"); //default directory is in the images folder
					int returnVal = fc.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						ImageIcon icon = makeIcon(fc, builder);
						darkIconButton.setIcon(icon); //Adds icon to button
						builder.setDarkImage(icon);
					}
				}
			});
			darkIconPanel.add(chooseDarkImage);
			darkIconPanel.add(darkIconButton);
			
		c.gridx=0;
		c.gridy=3;
		piecePanel.add(darkIconPanel, c);
	
		//Add components for collecting the directions of movement.
	
			final String[] directions = new String[]{"North","Northeast","East","Southeast", "South","Southwest","West","Northwest"};
			final JComboBox dropdown = new JComboBox(directions);
		
			//Collect max distance of movement, -1 for infinity.
			final JTextField dist = new JTextField(3);
			dist.setToolTipText("Greatest amount of spaces piece can travel in chosen direction");
			
			final JTextField knight = new JTextField(2);
			knight.setToolTipText("Enter the knight-like directions you would like");
			knight.setEnabled(false);
			
			final JTextField knightSecond = new JTextField(2);
			knightSecond.setToolTipText("Enter the other direction for the knight-like piece");
			knightSecond.setEnabled(false);
			
			final JCheckBox knightOn = new JCheckBox("Knight-like Movements", false);
			knightOn.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					knightLike = !knightLike;
					if(knightLike){
					knight.setEnabled(true);
					knightSecond.setEnabled(true);
					}
					else {
						knight.setEnabled(false);
						knightSecond.setEnabled(false);
					}
				}
			});
			
			final JCheckBox leaper = new JCheckBox("Can jump other Pieces?", false);
			
			final JPanel knightMoving = new JPanel();
			knightMoving.setLayout(new FlowLayout());
			knightMoving.add(knight);
			knightMoving.add(new JLabel("x"));
			knightMoving.add(knightSecond);
			
			//TODO add promotable as an option to determine what can be promoted
			final JCheckBox promotable = new JCheckBox("This piece can be promoted", false);
			
			//Create button and add ActionListener for adding movement directions to a piece
			final JButton addInstruction = new JButton("Add Movement Directions to this Piece");
			addInstruction.setToolTipText("Pressing this will add movement direction and max distance in that direction.");
			addInstruction.addActionListener(new ActionListener() {
		
				@Override
				public void actionPerformed(ActionEvent e) {
					if (isIntDist()) {//Make sure their number is an int.
						if (dropdown.getSelectedItem() != null) {//Make sure there's directions left in the drop down.				
							//Add the move to the piece type and remove that direction from the drop down.
							builder.addMove(stringToChar((String) dropdown.getSelectedItem()), Integer.parseInt(dist
									.getText()));
							dropdown.removeItemAt(dropdown.getSelectedIndex());
							dist.setText(""); //Clear their int distance.
							
							//TODO add knight instructions here
						}
					}
				}
		
				/**
				 * Determine if the user entered a valid integer.
				 * @return If the text is a valid integer
				 */
				private boolean isIntDist() {
					try {
						Integer.parseInt(dist.getText());
						return true;
					} catch (Exception e) {
						return false;
					}
				}
				
				/**
				 * Translate a direction string to it's corresponding char.
				 * @param s The string to translate
				 * @return The char corresponding to the given String.
				 */
				private char stringToChar(String s) {
					//TODO Can this be any better?
					if (s.equals("North"))
						return 'N';
					if (s.equals("South"))
						return 'S';
					if (s.equals("East"))
						return 'E';
					if (s.equals("West"))
						return 'W';
					if (s.equals("Northeast"))
						return 'R';
					if (s.equals("Northwest"))
						return 'L';
					if (s.equals("Southeast"))
						return 'r';
					else
						return 'l';
				}
			});
			
		c.gridx= 0;
		c.gridy= 4;
		piecePanel.add(addInstruction, c);
		
		//Setting up a panel handle movement instructions 
			JPanel movementSetup = new JPanel();
			movementSetup.setLayout(new BoxLayout(movementSetup,BoxLayout.Y_AXIS));
			movementSetup.setLayout(new GridBagLayout());
			
			//Adds options and labels for setting up movement for the new piece
			c.insets = new Insets(5, 0, 5, 0);
			c.gridx = 0;
			c.gridy = 0;
			movementSetup.add(new JLabel("<html><u>Normal Movement Setup:</u></br></html>"), c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx =0;
			c.gridy = 1;
			movementSetup.add(new JLabel("Direction of Movement: "), c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 1;
			c.gridy = 1;
			movementSetup.add(dropdown, c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 2;
			movementSetup.add(new JLabel("Max Distance of Movement: "), c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 1;
			c.gridy = 2;
			movementSetup.add(dist, c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 3;
			c.gridwidth = 3;
			movementSetup.add(addInstruction, c);
			c.gridx= 0;
			c.gridy = 4;
			movementSetup.add(promotable, c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 5;
			movementSetup.add(leaper, c);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 6;
			movementSetup.add(knightOn, c);
			c.insets = new Insets(5, 0, 5, 0);
			c.gridx = 0;
			c.gridy = 7;
			movementSetup.add(new JLabel("<html><u>Knight-like Movement Directions:</u></br></html>"), c);
			c.gridx = 0;
			c.gridy = 8;
			movementSetup.add(knightMoving, c);
	
		c.gridx = 0;
		c.gridy = 5;
		piecePanel.add(movementSetup, c);
		
			//Create button and add ActionListener
			final JButton done = new JButton("Save Piece Type");
			done.addActionListener(new ActionListener() {
		
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					if (name.getText() == "" || PieceBuilder.isPieceType(name.getText())) { //Make sure the name is valid.
						JOptionPane.showMessageDialog(null, "Please enter a unique piece name.",
								"Invalid Piece Name", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					if(knight.isEnabled()){
						if(isIntKnights()){
							builder.addMove('x', Integer.parseInt(knight.getText())); //Knight movements stored in builder.
							builder.addMove('y', Integer.parseInt(knightSecond.getText()));
						}
					}
					builder.setName(name.getText());//Set the name in the PieceBuilder
					PieceBuilder.savePieceType(builder);//Save the piece type in the PieceBuilder class.
					
					//Refreshing the window
					name.setText("");
					lightIconButton.setIcon(temp);
					darkIconButton.setIcon(temp);
					dist.setText("");
					leaper.setSelected(false);
					knightOn.setSelected(false);
					knight.setText("");
					knight.setEnabled(false);
					knightSecond.setText("");
					knightSecond.setEnabled(false);
					dropdown.removeAllItems();
					for(int i = 0; i < directions.length; i++)
						dropdown.addItem(directions[i]);
					
				}
				
				/**
				 * Determine if the user entered a valid integer.
				 * @return If the text is a valid integer
				 */
				private boolean isIntKnights() {
					try {
						Integer.parseInt(knight.getText());
						Integer.parseInt(knightSecond.getText());
						return true;
					} catch (Exception e) {
						return false;
					}
				}
			});
	
			final JButton help = new JButton("Help");
			help.addActionListener(new ActionListener(){
		
				public void actionPerformed(ActionEvent arg0) {
					JOptionPane.showMessageDialog(null, 
							"Please enter the specific movement capabilities that you want for this piece.\n" +
							"To add instructions for one direction choose that direction from the drop down menu,\n" +
							"then fill in the farthest distance you would like it to move in that direction.\n" + 
							"When you are finished press the \"Add Movement\" button to add it to the piece.\n" + 
							"Repeat this process for each direction you would like the piece to move.\n" +
							"If you would like the piece to e able to jump others please check the box.\n" +
							"If you want this piece to be able to move like a knight please enter \n"+
							"the directions you would like it to move in the following format: \n" +
							
							"When you have finished adding movements to the piece press Save and Quit.\n"+
							"This piece will then be available for reuse from the piece selection screen.\n", "Help", 1);
				}
				
			});
		
			final JButton next = new JButton("Next");
			next.setToolTipText("Press me when you have made all of your pieces");
			next.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Driver.getInstance().setPanel(new RuleMaker(b));
				}
			});
			
			//Create button and add ActionListener
			final JButton cancel = new JButton("Back");
			cancel.addActionListener(new ActionListener() {
		
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Driver.getInstance().setPanel(new BoardCustomMenu(b));
				}
			});
		
			JPanel buttons = new JPanel();
			buttons.setLayout(new FlowLayout());
			buttons.add(help);
			buttons.add(done);
		
		c.gridx = 0;
		c.gridy = 7;
		piecePanel.add(buttons, c);
		
		c.gridx = 0;
		c.gridy = 0;
		add(piecePanel, c);
		
		JPanel mainButtons = new JPanel();
		mainButtons.setLayout(new FlowLayout());
		c.gridx = 0;
		c.gridy = 1;
		mainButtons.add(cancel, c);
		c.gridx = 1;
		c.gridy = 1;
		mainButtons.add(next, c);
		c.gridx = 0;
		c.gridy = 1;
		add(mainButtons, c);

		}
	
	/**
	 * Makes the icon for the for the new piece
	 * @param fc The file chooser to select the piece
	 * @param builder The builder that is building the piece
	 * @return the icon to add to the button displaying the icon for the new piece
	 */
	public ImageIcon makeIcon(JFileChooser fc, PieceBuilder builder){
		//If a valid File was chosen, make an ImageIcon from the file path.
		String file = fc.getSelectedFile().getAbsolutePath();
		
		// default center section
		ImageIcon icon = new ImageIcon(file);
		//Scale the image to 48x48.
		icon.setImage(icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		builder.setLightImage(icon);
		return icon;
	}
	
}
