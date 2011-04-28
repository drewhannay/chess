package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import logic.AIAdapter;
import logic.AIPlugin;
import logic.Builder;
import logic.Game;
import net.NetworkClient;
import net.NetworkServer;
import timer.BronsteinDelay;
import timer.ChessTimer;
import timer.Fischer;
import timer.HourGlass;
import timer.NoTimer;
import timer.SimpleDelay;
import timer.Word;

/**
 * NewGameMenu.java
 * 
 * GUI to initiate the a new game.
 * 
 * @author Drew Hannay & Daniel Opdyke & John McCormick
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 1
 * February 24, 2011
 */
public class NewGameMenu extends JPanel {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -6371389704966320508L;

	// Variables declaration - do not modify
	/**
	 * JButton to initiate a new AI game.
	 */
	private JButton AIPlay;
	/**
	 * JButton to initiate a new Human versus Human game.
	 */
	private JButton humanPlay;
	/**
	 * JButton to initiate a new network game.
	 */
	private JButton networkPlay;
	/**
	 * JButton to return to previous screen.
	 */
	private JButton backButton;
	/**
	 * The host of the game for playing
	 */
	private String host = "";

	public static boolean cancelled = false;

	/**
	 * Represents whether an option has been
	 * clicked; reset once the game begins.
	 * This is to prevent multiple instances of a new
	 * game from being created upon accidental double
	 * clicks.
	 */
	private boolean clicked;

	// End of variables declaration

	/**
	 * Constructor
	 * Call initComponents to initialize the GUI.
	 */
	public NewGameMenu() {
		initComponents();
	}

	/**
	 * Initialize components of the GUI
	 * Create all the GUI components, set their specific properties and add them to the 
	 * window. Also add any necessary ActionListeners.
	 */
	public void initComponents() {

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//Create button and add ActionListener
		humanPlay = new JButton("Human Play");
		humanPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!clicked){
					clicked = true;
					setupPopup(false);
				}}});

		//Create button and add ActionListener
		networkPlay = new JButton("Network Play");
		networkPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFrame pop = new JFrame("New Game");
				pop.setLayout(new FlowLayout());
				pop.setSize(350, 150);
				pop.setResizable(false);
				pop.setLocationRelativeTo(null);
				JPanel options = new JPanel();
				final JLabel label = new JLabel("Would you like to host a game or connect to one?");
				final JButton client = new JButton("Connect");
				client.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						final JFrame popped = new JFrame("New Game");
						popped.setLayout(new GridBagLayout());
						popped.setSize(370, 150); 
						popped.setResizable(false);
						popped.setLocationRelativeTo(null);
						GridBagConstraints c = new GridBagConstraints();

						final JLabel hoster = new JLabel("Which computer would you like to connect to?");
						final JTextField computer = new JTextField("", 2);

						final JButton save = new JButton("Next");
						save.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if(computer.getText().equals("")) {
									JOptionPane.showMessageDialog(null, "Please enter a number");
									return;
								}
								else if(computer.getText().length() <2){
									try{
										int hostNumber = Integer.parseInt(computer.getText());
										if(hostNumber>25 || hostNumber<1) throw new Exception();
										host = "cslab0" + hostNumber;
									} catch(Exception ne){
										JOptionPane.showMessageDialog(null, "Please enter a number between 1-25 in the box");
										return;
									}
								}
								else{
									try{
										int hostNumber = Integer.parseInt(computer.getText());
										if(hostNumber>25 || hostNumber<1) throw new Exception();
										host = "cslab" + hostNumber;
									} catch(Exception ne){
										JOptionPane.showMessageDialog(null, "Please enter a number between 1-25 in the box");
										return;
									}
								}
								NewGameMenu.cancelled = false;
								Thread client;
								try{
									client = new Thread(new Runnable() {
										@Override
										public void run() {
											try {
												new NetworkClient().join(host);
											} catch (Exception e) {
												System.out.println(e.getMessage());
												e.printStackTrace();
											}
										}
									});
									client.start();
									Driver.getInstance().setPanel(new NetLoading(client));
								}catch(Exception e1){
									System.out.println(e1.getMessage());
									e1.printStackTrace();
								}

								popped.dispose();
								pop.dispose();
							}
						});

						final JButton back = new JButton("Back");
						back.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								popped.dispose();
							}
						});

						JPanel everything = new JPanel();
						everything.setLayout(new GridBagLayout());

						c.gridx = 0;
						c.gridy = 0;
						c.gridwidth = 2;
						c.insets = new Insets(3,3,3,3);
						popped.add(hoster, c);
						c.gridx = 0;
						c.gridy = 1;
						c.gridwidth = 1;
						everything.add(new JLabel("cslab: "), c);
						c.gridx = 1;
						c.gridy = 1;
						c.gridwidth = 1;
						everything.add(computer, c);
						c.gridx = 0;
						c.gridy = 2;
						c.gridwidth = 1;
						everything.add(back, c);
						c.gridx = 1;
						c.gridy = 2;
						c.gridwidth = 1;
						everything.add(save, c);

						c.gridx = 0;
						c.gridy = 1;
						c.gridwidth = 2;
						popped.add(everything, c);
						popped.setVisible(true);
					}
				});
				final JButton host = new JButton("Host");
				host.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						setupPopup(true);
						pop.dispose();
					}
				});
				options.add(label);
				pop.add(options);
				pop.add(host);
				pop.add(client);

				pop.setVisible(true);
			}
		});

		//Create button and add ActionListener
		AIPlay = new JButton("AI Play");
		AIPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFrame popped = new JFrame("New Game");
				popped.setLayout(new GridBagLayout());
				popped.setSize(170, 150); 
				popped.setResizable(false);
				popped.setLocationRelativeTo(null);
				GridBagConstraints c = new GridBagConstraints();

				final JComboBox dropdown = new JComboBox(Builder.getArray());
				c.gridx =0;
				c.gridy = 0;
				popped.add(new JLabel("Type: "), c);
				c.gridx = 1;
				c.gridy = 0;
				c.insets = new Insets(3, 0, 3, 0);
				popped.add(dropdown, c);
				c.gridx = 0;
				c.gridy = 1;
				popped.add(new JLabel("AI: "), c);

				File dir = new File("AI");
				dir.mkdir();
				String[] allFiles = dir.list();
				List<String> tempFiles = new ArrayList<String>();
				for(String st:allFiles)
					if(st.endsWith(".java"))
						tempFiles.add(st);
				String[] files = new String[tempFiles.size()];
				tempFiles.toArray(files);
				
				if(files.length == 0){
					JOptionPane.showMessageDialog(null,
							"There are no AI files to use.\nPlease insert your AI java files in the AI directory.",
							"No AI files", JOptionPane.ERROR_MESSAGE);
					return;
				}
				final JComboBox ai = new JComboBox(files);
				c.gridx = 1;
				c.gridy = 1;
				c.fill = GridBagConstraints.HORIZONTAL;
				popped.add(ai, c);

				JButton next = new JButton("Next");
				next.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						final String choice = (String) ai.getSelectedItem();
						File file = new File("AI/" + choice);
						if(ai.getSelectedItem() == null){
							JOptionPane.showMessageDialog(null,
									"You have not selected an AI file",
									"No AI file", JOptionPane.ERROR_MESSAGE);
							return;
						}
						Game toPlay = Builder.newGame((String) dropdown.getSelectedItem());



						JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
						StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, Locale.getDefault(), null);

						String[] compileOptions = new String[]{"-d","bin"};
						Iterable<String> compilationOptions = Arrays.asList(compileOptions);

						// prepare the source file to compile
						List<File> sourceFileList = new ArrayList<File>();
						sourceFileList.add(file);
						Iterable<? extends JavaFileObject> compilationUnits =fileManager.getJavaFileObjectsFromFiles(sourceFileList);
						CompilationTask task = compiler.getTask(null,fileManager, null, compilationOptions, null, compilationUnits);

						boolean result = task.call();
						if (result) {
							System.out.println ("Compilation was successful");
						} else {
							System.out.println ("Compilation failed");
						}
						try {
							fileManager.close ();
						} catch (IOException e) {
						}

						final AIPlugin plugin;
						final AIAdapter ai = new AIAdapter(toPlay);
						try {
							ClassLoader c = ClassLoader.getSystemClassLoader();
							Class<?> klazz = c.loadClass("logic." + choice.substring(0, choice.indexOf(".java")));
							Constructor<?> construct = klazz.getConstructor();
							plugin = (AIPlugin) construct.newInstance();

							

							Thread aiThread;
							aiThread = new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										ai.runGame(plugin);
									} catch (Exception e) {
										System.out.println(e.getMessage());
										e.printStackTrace();
									}
								}
							});
							aiThread.start();
						}catch(Exception e1){
							System.out.println(e1.getMessage());
							e1.printStackTrace();
						}

						System.out.println(toPlay.equals(ai.getGame()));
						Driver.getInstance().setPanel(new PlayNetGame(toPlay, false, false));
						popped.dispose();
					}
				});

				JButton back = new JButton("Back");
				back.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						popped.dispose();
					}
				});

				JPanel buttons = new JPanel();
				buttons.setLayout(new FlowLayout());
				buttons.add(back);
				buttons.add(next);

				c.gridx = 0;
				c.gridy = 2;
				c.gridwidth =2;
				popped.add(buttons, c);

				popped.setVisible(true);
			}
		});

		//Create button and add ActionListener
		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Return to the main screen.
				Driver.getInstance().helpMenu.setText("Help");
				Driver.getInstance().gamePlayHelp.setVisible(false);
				Driver.getInstance().revertPanel();
			}
		});
		
		try {
			if(InetAddress.getLocalHost().getHostName().contains("cslab")){
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.HORIZONTAL;
				c.insets = new Insets(5,5,0,0);
				add(humanPlay, c);
				c.gridx = 1;
				c.gridy = 0;
				c.fill = GridBagConstraints.HORIZONTAL;
				c.insets = new Insets(5,5,0,5);
				add(networkPlay, c);
				c.gridx = 0;
				c.gridy = 1;
				c.gridwidth = 2;
				c.insets = new Insets(0,5,20,5);
				c.fill = GridBagConstraints.HORIZONTAL;
				add(AIPlay, c);
				c.gridx = 0;
				c.gridy = 2;
				add(backButton, c);			
			}
			else{
				//Layout stuff. Make it better later.
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.HORIZONTAL;
				c.insets = new Insets(5,5,0,5);
				add(humanPlay, c);
				c.gridx = 0;
				c.gridy = 1;
				c.insets = new Insets(0,5,20,5);
				c.fill = GridBagConstraints.HORIZONTAL;
				add(AIPlay, c);
				c.gridx = 0;
				c.gridy = 2;
				add(backButton, c);
			}
		}catch(Exception e){
			
		}

	}
	
	/**
	 * This is the method to open the pop up to create a new game.
	 * @param isNetwork boolean to see if this is a network game or not
	 */
	public void setupPopup(final boolean isNetwork){
		clicked = true;
		final JFrame popup = new JFrame("New Game");
		popup.setLayout(new FlowLayout());
		popup.setSize(200, 150); //TODO Figure out if there's a better way to set the size of the window.
		popup.setResizable(false);
		popup.setLocationRelativeTo(null);//This line makes the window show up in the center of the user's screen, regardless of resolution.

		//Make a JComboBox drop down filled with the names of all the saved game types.
		final JComboBox dropdown = new JComboBox(Builder.getArray());
		popup.add(new JLabel("Type: "));
		popup.add(dropdown);

		//Create button and add ActionListener
		final JButton done = new JButton("Start");
		String[] timerNames = { "No timer", "Bronstein Delay", "Fischer", "Fischer After", "Hour Glass",
				"Simple Delay", "Word" };
		final JComboBox timers = new JComboBox(timerNames);
		final JLabel totalTimeText = new JLabel("Total Time (sec): ");
		totalTimeText.setVisible(false);
		final TextField totalTime = new TextField("120", 3);
		totalTime.setVisible(false);
		final JLabel increaseText = new JLabel("Increment/delay (sec): ");
		increaseText.setVisible(false);
		final TextField increase = new TextField("10", 3);
		increase.setVisible(false);

		timers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String timerName = (String) timers.getSelectedItem();
				if (timerName.equals("No timer") == false) {
					totalTimeText.setVisible(true);
					totalTime.setVisible(true);
					increaseText.setVisible(true);
					increase.setVisible(true);
				}
				else {
					totalTimeText.setVisible(false);
					totalTime.setVisible(false);
					increaseText.setVisible(false);
					increase.setVisible(false);
				}
			}
		});

		popup.add(new JLabel("Timer: "));
		popup.add(timers);
		popup.add(totalTimeText);
		popup.add(totalTime);
		popup.add(increaseText);
		popup.add(increase);
		popup.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		popup.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {
				clicked = false;
				popup.setVisible(false);
				popup.dispose();
			}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});
		done.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clicked = false;
				String timerName = (String) timers.getSelectedItem();
				long startTime = Integer.parseInt(totalTime.getText()) * 1000;
				long increment = Integer.parseInt(increase.getText()) * 1000;
				ChessTimer blackTimer = null;
				ChessTimer whiteTimer = null;
				//TODO something else here - possibly reflection? Make this better.
				if (timerName.equals("No timer")) {
					blackTimer = new NoTimer();
					whiteTimer = new NoTimer();
				}
				else if (timerName.equals("Bronstein Delay")) {
					blackTimer = new BronsteinDelay(increment, startTime, true);
					whiteTimer = new BronsteinDelay(increment, startTime, false);
				}
				else if (timerName.equals("Fischer")) {
					blackTimer = new Fischer(increment, startTime, false, true);
					whiteTimer = new Fischer(increment, startTime, false, false);
				}
				else if (timerName.equals("Fischer After")) {
					blackTimer = new Fischer(increment, startTime, true, true);
					whiteTimer = new Fischer(increment, startTime, true, false);
				}
				else if (timerName.equals("Hour Glass")) {
					blackTimer = new HourGlass(startTime / 2, true); //time is halved since this is actually
					//the timer the player is not allowed to exceed.
					whiteTimer = new HourGlass(startTime / 2, false);
				}
				else if (timerName.equals("Simple Delay")) {
					blackTimer = new SimpleDelay(increment, startTime, true);
					whiteTimer = new SimpleDelay(increment, startTime, false);
				}
				else {
					blackTimer = new Word(startTime);
					whiteTimer = new Word(startTime);
				}
				//Create the new panel and display it, then get rid of this pop up.

				if(isNetwork){
					Game toPlay = Builder.newGame((String) dropdown.getSelectedItem());
					toPlay.setTimers(whiteTimer, blackTimer);
					final PlayNetGame game;
					if(host.equals(arg0)){
						game = new PlayNetGame(toPlay, false, true);
					}
					else{
						game = new PlayNetGame(toPlay, false, false);
					}
					try {
						NewGameMenu.cancelled = false;
						Thread host = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									new NetworkServer().host(game);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						Driver.getInstance().setPanel(new NetLoading(host));
						host.start();

					} catch (Exception e) {
						System.out.println("Host");
						e.printStackTrace();
					}
				}
				else{
					Game toPlay = Builder.newGame((String) dropdown.getSelectedItem());
					toPlay.setTimers(whiteTimer, blackTimer);
					PlayGame game = new PlayGame(toPlay, false);
					Driver.getInstance().setPanel(game);
				}
				popup.dispose();
			}
		});
		

		//Create button and add ActionListener
		final JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clicked = false;
				//Get rid of this pop up.
				popup.dispose();
			}
		});
		popup.add(back);
		popup.add(done);
		popup.setVisible(true);
	}


}
