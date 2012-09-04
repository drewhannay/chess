package gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.SwingUtilities;

import dragNdrop.GlassPane;

public class MotionAdapter extends MouseMotionAdapter
{
	public MotionAdapter(GlassPane glassPane)
	{
		m_glassPane = glassPane;
	}

	public void mouseDragged(MouseEvent event)
	{
		Component component = event.getComponent();

		Point point = (Point) event.getPoint().clone();
		SwingUtilities.convertPointToScreen(point, component);
		SwingUtilities.convertPointFromScreen(point, m_glassPane);
		m_glassPane.setPoint(point);

		m_glassPane.repaint();
	}

	private GlassPane m_glassPane;
}
