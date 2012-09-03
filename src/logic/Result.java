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

	public void setText(String text)
	{
		m_text = text;
	}

	@Override
	public String toString()
	{
		// TODO: I think this should be used for ACN files, but it must be broken now
//		String s = "";
//
//		switch (this)
//		{
//		case UNDECIDED:
//			s = "?";
//			break;
//		case WHITE_WIN:
//			s = "1-0";
//			break;
//		case DRAW:
//			s = "1/2-1/2";
//			break;
//		case BLACK_WIN:
//			s = "0-1";
//			break;
//		default:
//			s = "?";
//		}
		return m_text;
	}

	private static final long serialVersionUID = -6844368741916902616L;

	private String m_text;
}
