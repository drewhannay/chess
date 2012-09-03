package dragNdrop;

import java.awt.event.MouseAdapter;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class DropAdapter extends MouseAdapter
{
	public DropAdapter(GlassPane glassPane, String actionString)
	{
		m_glassPane = glassPane;
		m_actionString = actionString;
		m_listeners = Lists.newArrayList();
	}

	public void addDropListener(DropListener listener)
	{
		if (listener != null)
			m_listeners.add(listener);
	}

	public void removeGhostDropListener(DropListener listener)
	{
		if (listener != null)
			m_listeners.remove(listener);
	}

	protected void fireDropEvent(DropEvent event)
	{
		Iterator<DropListener> iterator = m_listeners.iterator();
		while (iterator.hasNext())
			((DropListener) iterator.next()).dropped(event);
	}

	protected GlassPane m_glassPane;
	protected String m_actionString;

	private List<DropListener> m_listeners;
}
