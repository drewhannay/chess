package timer;

import com.google.common.base.Preconditions;

public enum TimerTypes
{
	NO_TIMER,
	BRONSTEIN_DELAY,
	FISCHER,
	FISCHER_AFTER,
	HOUR_GLASS,
	SIMPLE_DELAY,
	WORD;

	@Override
	public String toString()
	{
		switch (this)
		{
		case NO_TIMER:
			return "No Timer";
		case BRONSTEIN_DELAY:
			return "Bronstein Delay";
		case FISCHER:
			return "Fischer";
		case FISCHER_AFTER:
			return "Fischer After";
		case HOUR_GLASS:
			return "Hour Glass";
		case SIMPLE_DELAY:
			return "Simple Delay";
		case WORD:
			return "Word";
		default:
			Preconditions.checkArgument(false);
			return "";
		}
	};
}
