package dragNdrop;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

public class ComponentAdapter extends DropAdapter
{
	public ComponentAdapter(GlassPane glassPane, String action)
	{
		super(glassPane, action);
	}

	public void mousePressed(MouseEvent event)
	{
		Component component  = event.getComponent();

		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = image.getGraphics();
		component.paint(graphics);

		m_glassPane.setVisible(true);

		Point point = (Point) event.getPoint().clone();
		SwingUtilities.convertPointToScreen(point, component);
		SwingUtilities.convertPointFromScreen(point, m_glassPane);

		m_glassPane.setPoint(point);
		m_glassPane.setImage(image);
		m_glassPane.repaint();
	}

	public void mouseReleased(MouseEvent event)
	{
		Component component = event.getComponent();

		Point point = (Point) event.getPoint().clone();
		SwingUtilities.convertPointToScreen(point, component);

		Point eventPoint = (Point) point.clone();
		SwingUtilities.convertPointFromScreen(point, m_glassPane);

		m_glassPane.setPoint(point);
		m_glassPane.setVisible(false);
		m_glassPane.setImage(null);

		fireDropEvent(new DropEvent(m_actionString, eventPoint));
	}
}
