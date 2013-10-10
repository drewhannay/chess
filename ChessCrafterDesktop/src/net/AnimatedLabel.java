package net;

import javax.swing.JLabel;

/**
 * @author jmccormi Some code borrowed from
 * http://www.java2s.com/Code/Java/Swing-JFC/Animationlabel.htm Class to animate
 * a JLabel for the net loading screen
 */
public class AnimatedLabel extends JLabel implements Runnable
{
	public AnimatedLabel()
	{
		setText(Messages.getString("AnimatedLabel.noDot")); //$NON-NLS-1$
		new Thread(this).start();
	}

	/**
	 * Runs the thread which causes the net loading screen to have an animated
	 * "Waiting" JLabel
	 */
	@Override
	public void run()
	{
		m_isFinished = false;
		while (!m_isFinished)
		{
			mStringIndex++;
			if (mStringIndex == 1)
			{
				setText(Messages.getString("AnimatedLabel.oneDot")); //$NON-NLS-1$
			}
			else if (mStringIndex == 2)
			{
				setText(Messages.getString("AnimatedLabel.twoDots")); //$NON-NLS-1$
			}
			else
			{
				setText(Messages.getString("AnimatedLabel.threeDots")); //$NON-NLS-1$
				mStringIndex = 0;
			}
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception ex)
			{
			}
		}
	}

	private static final long serialVersionUID = 7373349083032786721L;

	public static boolean m_isFinished = false;

	protected String[] mStrings;
	protected int mStringIndex = 0;
}
