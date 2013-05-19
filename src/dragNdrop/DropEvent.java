package dragNdrop;

import java.awt.Point;

import javax.swing.JComponent;

public class DropEvent
{
	public DropEvent(Point point, JComponent originComponent)
	{
		mPoint = point;
		mOriginComponent = originComponent;
	}

	public Point getDropLocation()
	{
		return mPoint;
	}

	public JComponent getOriginComponent()
	{
		return mOriginComponent;
	}

	private Point mPoint;
	private JComponent mOriginComponent;
}
