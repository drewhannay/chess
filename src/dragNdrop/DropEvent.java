package dragNdrop;

import java.awt.Point;

public class DropEvent
{
	public DropEvent(String actionString, Point point)
	{
		m_actionString = actionString;
		m_point = point;
	}

	public String getAction()
	{
		return m_actionString;
	}

	public Point getDropLocation()
	{
		return m_point;
	}

	private Point m_point;
	private String m_actionString;
}
