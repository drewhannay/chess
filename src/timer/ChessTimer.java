package timer;

import gui.PlayGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import logic.Result;

/**
 * An abstract class to contain information about the current timer.
 * @author alisa.maas
 *
 */
public abstract class ChessTimer implements ActionListener, Serializable {
	/**
	 * The current time on the clock, in ms.
	 */
	protected long time;
	/**
	 * The time the clock was last updated.
	 */
	protected long lastUpdated;

	/**
	 * The label to display
	 */
	protected JLabel label;
	/**
	 * To correctly format the time
	 * displayed to the user.
	 */
	protected NumberFormat nf;

	/**
	 * The timer object; calls the
	 * actionPerformed method every second.
	 */
	protected Timer timer;

	/**
	 * Whether the timer is delayed.
	 */
	protected boolean delay;
	/**
	 * Whose team this clock belongs to.
	 */
	protected boolean isBlack;
	/**
	 * Tells whether the time has recently been reset;
	 * used for undo.
	 */
	protected boolean timeSet;
	/**
	 * The initial start time; also for undo.
	 */
	protected long startTime;
	/**
	 * Which direction is the clock moving?
	 */
	protected int direction = 1; // should be 1 or -1 to account for timer
	// counting up instead. -1 to count up since it gets subtracted.
	protected static boolean stopTimers;
	/**
	 * For Serialization.
	 */
	private static final long serialVersionUID = -1195203886987180343L;

	public void actionPerformed(ActionEvent ae) {
		updateDisplay();
	}
	public static void stopTimers(){
		stopTimers = true;
	}
	/**
	 * @return The direction the clock is proceeding.
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @return the label; for displaying.
	 */
	public JLabel getLabel() {
		return label;
	}

	/**
	 * Get the start time.
	 * @return The time the clock started at.
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * 
	 * @return The time on the clock (not
	 * formatted)
	 */
	public long getTime() {
		return time;
	}
	/**
	 * Reset the timer to its original settings
	 */
	public void reset(){
		lastUpdated = System.currentTimeMillis();
		time = startTime;
		updateDisplay();
	}
	/**
	 * Initialize the components of the timer.
	 */
	public void init() {
		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setOpaque(true);
		//getContentPane().add(label, BorderLayout.CENTER);
		nf = NumberFormat.getNumberInstance();
		nf.setMinimumIntegerDigits(2);
		timer = new Timer(1000, this);
		timer.setInitialDelay(0);
	}

	/**
	 * Restart the timer after resuming a saved game.
	 */
	public void restart() {
		timer = new Timer(1000, this);
		timer.setInitialDelay(0);
		lastUpdated = System.currentTimeMillis();
	}

	/**
	 * Set the direction of the clock; for undo.
	 * @param newDirection The new direction.
	 */
	public void setDirection(int newDirection) {
		direction = newDirection;
	}

	/**
	 * Set the time on the clock; for undo.
	 * @param newTime The new time to display.
	 */
	public void setTime(long newTime) {
		time = newTime == -1 ? startTime : newTime;
		timeSet = true;
	}
	/**
	 * Start the timer
	 */
	public abstract void start();

	/**
	 * Stop the timer.
	 */
	public abstract void stop();

	/**
	 * Respond to the time running out; shared code among all the 
	 * classes except NoTimer, which overrides this method.
	 */
	public void timeElapsed() {
		Result r = new Result(isBlack ? Result.WHITE_WIN : Result.BLACK_WIN);
		r.setText("Time has run out. " + r.winText() + "\n");
		PlayGame.endOfGame(r);
		timer.stop();

	}

	/**
	 * Updates the display in response to the time
	 * passing.
	 */
	protected void updateDisplay() {
		if(stopTimers){
			timer.stop();
			return;
		}
		long now = System.currentTimeMillis();
		long elapsed = now - lastUpdated;
		if (!delay) {
			time -= elapsed * direction; //to account for SimpleDelay
		} else {
			delay = false;
		}
		lastUpdated = now;

		int minutes = (int) (time / 60000);
		int seconds = (int) ((time % 60000) / 1000);
		label.setText(nf.format(minutes) + ":" + nf.format(seconds));
		if (time <= 0) {
			time = Math.abs(time);
			timeElapsed();

		}
	}

}
