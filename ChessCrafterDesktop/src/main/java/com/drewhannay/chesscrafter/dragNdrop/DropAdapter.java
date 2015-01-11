
package dragNdrop;
import java.awt.event.MouseAdapter;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;

public class DropAdapter extends MouseAdapter
{
	public DropAdapter(GlassPane glassPane)
	{
		mGlassPane = glassPane;
		mListeners = Lists.newArrayList();
	}

	public void addDropListener(DropListener listener)
	{
		if (listener != null)
			mListeners.add(listener);
	}

	public void removeDropListener(DropListener listener)
	{
		if (listener != null)
			mListeners.remove(listener);
	}

	protected void fireDropEvent(DropEvent event, boolean fromDisplayBoard)
	{
		Iterator<DropListener> iterator = mListeners.iterator();
		while (iterator.hasNext())
			((DropListener) iterator.next()).dropped(event, fromDisplayBoard);
	}

	protected GlassPane mGlassPane;

	private List<DropListener> mListeners;
}
