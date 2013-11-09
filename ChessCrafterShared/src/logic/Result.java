
package logic;

public enum Result
{
	UNDECIDED,
	DRAW,
	WHITE_WIN,
	BLACK_WIN;

	public String winText()
	{
		// TODO: the clients needs to manage what the win text is
		switch (this)
		{
		case DRAW:
			return null; // Messages.getString("drawExc"); //$NON-NLS-1$
		case WHITE_WIN:
			return null; // Messages.getString("whiteWonExc"); //$NON-NLS-1$
		case BLACK_WIN:
			return null; // Messages.getString("blackWonExc"); //$NON-NLS-1$
		default:
			return ""; //$NON-NLS-1$
		}
	}

	public void setGuiText(String text)
	{
		mGuiText = text;
	}

	public String getGuiText()
	{
		return mGuiText;
	}

	@Override
	public String toString()
	{
		String acnResult = ""; //$NON-NLS-1$

		switch (this)
		{
		case UNDECIDED:
			acnResult = "?"; //$NON-NLS-1$
			break;
		case WHITE_WIN:
			acnResult = "1-0"; //$NON-NLS-1$
			break;
		case DRAW:
			acnResult = "1/2-1/2"; //$NON-NLS-1$
			break;
		case BLACK_WIN:
			acnResult = "0-1"; //$NON-NLS-1$
			break;
		default:
			acnResult = "?"; //$NON-NLS-1$
		}
		return acnResult;
	}

	private String mGuiText;
}
