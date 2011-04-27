package gui;

import javax.swing.JLabel;
/**
 * @author jmccormi
 * Some code borrowed from http://www.java2s.com/Code/Java/Swing-JFC/Animationlabel.htm
 */
public class AnimatedLabel extends JLabel implements Runnable {
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 7373349083032786721L;

	protected String[] strings;

	protected int index = 0;

	public static boolean finished = false;
	
	public AnimatedLabel() {
		this.setText("Waiting");
		Thread tr = new Thread(this);
		tr.start();
	}

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
				System.out.println("fail");
			}
		}
	}
}
