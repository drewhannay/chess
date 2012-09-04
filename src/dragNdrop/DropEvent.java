package dragNdrop;

import java.awt.Point;

import javax.swing.JComponent;

public class DropEvent
{
	public DropEvent(Point point, JComponent originComponent)
	{
		m_point = point;
		m_originComponent = originComponent;
	}

	public Point getDropLocation()
	{
		return m_point;
	}

	public JComponent getOriginComponent()
	{
		return m_originComponent;
	}

	private Point m_point;
	private JComponent m_originComponent;
}
