package dragNdrop;

import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class DropManagerDemo extends AbstractDropManager
{
	public DropManagerDemo(JComponent target/* ,DragnDropDemo context */)
	{
		super(target);
	}

	@Override
	public void dropped(DropEvent event)
	{
		String action = event.getAction();
		Point point = getTranslatedPoint(event.getDropLocation());
		if (isInTarget(point))
		{
			JOptionPane.showMessageDialog(m_component, "Action: " + action);
			// context.setImage(, row, col)
		}
	}

//	private JComponent m_target;
}
