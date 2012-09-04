package timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import utility.RunnableOfT;

public abstract class ChessTimer implements ActionListener, Serializable
{
	public void init(RunnableOfT<Boolean> timeElapsedCallback)
	{
		m_displayLabel = new JLabel();
		m_displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		m_displayLabel.setOpaque(true);
		m_numberFormat = NumberFormat.getNumberInstance();
		m_numberFormat.setMinimumIntegerDigits(2);
		m_timer = new Timer(1000, this);
		m_timer.setInitialDelay(0);
		m_timeElapsedCallback = timeElapsedCallback;
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		updateDisplay();
	}

	public static void stopTimers()
	{
		m_isStopped = true;
	}

	public int getClockDirection()
	{
		return m_clockDirection;
	}

	public JLabel getDisplayLabel()
	{
		return m_displayLabel;
	}

	public long getStartTime()
	{
		return m_initialStartTime;
	}

	public long getRawTime()
	{
		return m_currentTime;
	}

	/**
	 * Reset the timer to the original settings
	 */
	public void reset()
	{
		m_clockLastUpdatedTime = System.currentTimeMillis();
		m_currentTime = m_initialStartTime;
		updateDisplay();
	}

	/**
	 * Restart the timer after resuming a saved game.
	 */
	public void restart()
	{
		m_timer = new Timer(1000, this);
		m_timer.setInitialDelay(0);
		m_clockLastUpdatedTime = System.currentTimeMillis();
	}

	public void setClockDirection(int newDirection)
	{
		m_clockDirection = newDirection;
	}

	public void setClockTime(long newTime)
	{
		m_currentTime = newTime == -1 ? m_initialStartTime : newTime;
		m_timeWasRecentlyReset = true;
	}

	public abstract void startTimer();

	public abstract void stopTimer();

	public void timeElapsed()
	{
		m_timeElapsedCallback.run(m_isBlackTeamTimer);
		m_timer.stop();
	}

	protected void updateDisplay()
	{
		if (m_isStopped)
		{
			m_timer.stop();
			return;
		}
		long now = System.currentTimeMillis();
		long elapsed = now - m_clockLastUpdatedTime;

		if (!m_isDelayedTimer)
			m_currentTime -= elapsed * m_clockDirection;
		else
			m_isDelayedTimer = false;

		m_clockLastUpdatedTime = now;

		int minutes = (int) (m_currentTime / 60000);
		int seconds = (int) ((m_currentTime % 60000) / 1000);
		m_displayLabel.setText(m_numberFormat.format(minutes) + ":" + m_numberFormat.format(seconds));
		if (m_currentTime <= 0)
		{
			m_currentTime = Math.abs(m_currentTime);
			timeElapsed();
		}
	}

	private static final long serialVersionUID = -1195203886987180343L;

	protected long m_currentTime;
	protected long m_clockLastUpdatedTime;
	protected JLabel m_displayLabel;
	protected NumberFormat m_numberFormat;
	protected Timer m_timer;
	protected boolean m_isDelayedTimer;
	protected boolean m_isBlackTeamTimer;
	protected boolean m_timeWasRecentlyReset;
	protected long m_initialStartTime;
	// should be 1 or -1 to account for timer counting up instead. -1 to count
	// up since it gets subtracted
	protected int m_clockDirection = 1;
	protected static boolean m_isStopped;
	protected RunnableOfT<Boolean> m_timeElapsedCallback;
}
