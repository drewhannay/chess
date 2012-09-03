package dragNdrop;

import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public abstract class AbstractDropManager implements DropListener
{
	public AbstractDropManager()
	{
		this(null);
	}

	public AbstractDropManager(JComponent component)
	{
		m_component = component;
	}

	protected Point getTranslatedPoint(Point point)
	{
		Point clonedPoint = (Point) point.clone();
		SwingUtilities.convertPointFromScreen(clonedPoint, m_component);
		return clonedPoint;
	}

	protected boolean isInTarget(Point point)
	{
		return m_component.getBounds().contains(point);
	}

	public void dropped(DropEvent event)
	{
	}

	protected JComponent m_component;
}
