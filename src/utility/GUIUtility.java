package utility;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public final class GUIUtility
{
	private GUIUtility()
	{
	}

	public static void requestFocus(final JComponent component)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				component.requestFocus();
			}
		});
	}
}
