package timer;

import com.google.common.base.Preconditions;

public enum TimerTypes
{
	NO_TIMER, BRONSTEIN_DELAY, FISCHER, FISCHER_AFTER, HOUR_GLASS, SIMPLE_DELAY, WORD;

	@Override
	public String toString()
	{
		switch (this)
		{
		case NO_TIMER:
			return Messages.getString("noTimer"); //$NON-NLS-1$
		case BRONSTEIN_DELAY:
			return Messages.getString("bronsteinDelay"); //$NON-NLS-1$
		case FISCHER:
			return Messages.getString("fischer"); //$NON-NLS-1$
		case FISCHER_AFTER:
			return Messages.getString("fischerAfter"); //$NON-NLS-1$
		case HOUR_GLASS:
			return Messages.getString("hourGlass"); //$NON-NLS-1$
		case SIMPLE_DELAY:
			return Messages.getString("simpleDelay"); //$NON-NLS-1$
		case WORD:
			return Messages.getString("word"); //$NON-NLS-1$
		default:
			Preconditions.checkArgument(false);
			return ""; //$NON-NLS-1$
		}
	};
}
