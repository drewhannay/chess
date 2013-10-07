package dragNdrop;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.SwingUtilities;


public class MotionAdapter extends MouseMotionAdapter
{
	public MotionAdapter(GlassPane glassPane)
	{
		mGlassPane = glassPane;
	}

	public void mouseDragged(MouseEvent event)
	{
		Component component = event.getComponent();

		Point point = (Point) event.getPoint().clone();
		SwingUtilities.convertPointToScreen(point, component);
		SwingUtilities.convertPointFromScreen(point, mGlassPane);
		mGlassPane.setPoint(point);

		mGlassPane.repaint();
	}

	private GlassPane mGlassPane;
}
