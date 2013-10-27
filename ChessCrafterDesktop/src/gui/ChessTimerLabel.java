package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import timer.ChessTimer;
import timer.ChessTimer.ChessTimerListener;

import com.google.common.base.Preconditions;

public final class ChessTimerLabel extends JLabel
{
	private static final long serialVersionUID = -373763961178221657L;

	public ChessTimerLabel(ChessTimer timer)
	{
		Preconditions.checkArgument(timer != null);

		mChessTimer = timer;
		mChessTimer.setChessTimerListener(mChessTimerListener);

		mTimer = new Timer(1000, mTimerActionListener);
		mTimer.setInitialDelay(0);

		setHorizontalAlignment(SwingConstants.CENTER);
		setOpaque(true);
	}

	private final ActionListener mTimerActionListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			mChessTimer.updateDisplay();
		}
	};

	private final ChessTimerListener mChessTimerListener = new ChessTimerListener()
	{
		@Override
		public void onDisplayUpdated(String displayText)
		{
			setText(displayText);
		}

		@Override
		public void onTimerStart()
		{
			mTimer.start();
		}

		@Override
		public void onTimerStop()
		{
			mTimer.stop();
		}

		@Override
		public void setInitialDelay(int initialDelay)
		{
			mTimer.setInitialDelay(initialDelay);
		}

		@Override
		public void onTimerRestart()
		{
			mTimer = new Timer(1000, mTimerActionListener);
			mTimer.setInitialDelay(0);
		}
	};

	private ChessTimer mChessTimer;
	private Timer mTimer;
}
