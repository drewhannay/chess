package utility;

import java.io.Serializable;

public class Preference implements Serializable
{

	public Preference()
	{
		m_shouldHighlightMoves = true;
		s_defaultSaveLocation = FileUtility.getDefaultCompletedLocation();
		m_saveLocation = s_defaultSaveLocation;
	}

	public boolean isPathSet()
	{
		return !m_pathSet;
	}

	public boolean isHighlightSet()
	{
		return !m_highlightSet;
	}

	public String getSaveLocation()
	{
		return m_saveLocation;
	}

	public void setSaveLocation(String saveLocation)
	{
		m_pathSet = true;
		m_saveLocation = saveLocation;
	}

	public boolean isHighlightMoves()
	{
		return m_shouldHighlightMoves;
	}

	public void setHighlightMoves(boolean highlightMoves)
	{
		m_highlightSet = true;
		m_shouldHighlightMoves = highlightMoves;
	}

	private static String s_defaultSaveLocation;

	private boolean m_pathSet;
	private boolean m_highlightSet;
	private String m_saveLocation;
	private boolean m_shouldHighlightMoves;

	private static final long serialVersionUID = 6126269552471996685L;
}
