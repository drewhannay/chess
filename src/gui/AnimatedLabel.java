package gui;

import javax.swing.JLabel;
/**
 * @author jmccormi
 * Some code borrowed from http://www.java2s.com/Code/Java/Swing-JFC/Animationlabel.htm
 * Class to animate a JLabel for the net loading screen
 */
public class AnimatedLabel extends JLabel implements Runnable {
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 7373349083032786721L;
	/**
	 * String Array to hold the various moving parts
	 */
	protected String[] strings;
	/**
	 * Faux timer to keep track of which string is being used
	 */
	protected int index = 0;
	/**
	 * Boolean to turn off the thread when the game has started.
	 */
	public static boolean finished = false;
	/**
	 * Constructor
	 */
	public AnimatedLabel() {
		this.setText("Waiting");
		Thread tr = new Thread(this);
		tr.start();
	}
	/**
	 * Runs the thread which causes the net loading screen to have an animated "Waiting" JLabel
	 */
	public void run() {
		finished = false;
		while (!finished) {
			index++;
			if (index == 1) {
				this.setText("Waiting. ");
			} else if (index == 2) {
				this.setText("Waiting.. ");
			} else {
				this.setText("Waiting... ");
				index = 0;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
			}
		}
	}
}
