package logic;

import java.io.Serializable;

public enum Result implements Serializable
{
	UNDECIDED,
	DRAW,
	WHITE_WIN,
	BLACK_WIN;

	public String winText()
	{
		switch (this)
		{
		case DRAW:
			return "Draw!";
		case WHITE_WIN:
			return "White won!";
		case BLACK_WIN:
			return "Black won!";
		default:
			return "";
		}
	}

	public void setGuiText(String text)
	{
		m_GUIText = text;
	}

	public String getGUIText()
	{
		return m_GUIText;
	}

	@Override
	public String toString()
	{
		String acnResult = "";

		switch (this)
		{
		case UNDECIDED:
			acnResult = "?";
			break;
		case WHITE_WIN:
			acnResult = "1-0";
			break;
		case DRAW:
			acnResult = "1/2-1/2";
			break;
		case BLACK_WIN:
			acnResult = "0-1";
			break;
		default:
			acnResult = "?";
		}
		return acnResult;
	}

	private static final long serialVersionUID = -6844368741916902616L;

	private String m_GUIText;
}
