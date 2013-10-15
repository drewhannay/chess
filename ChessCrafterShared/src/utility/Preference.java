package utility;

import java.io.Serializable;

public class Preference implements Serializable{

	public Preference()
	{
		m_shouldHighlightMoves = true;
		s_defaultSaveLocation = FileUtility.getDefaultCompletedLocation();
	}
	
	public boolean isDefaultPreferences() {
		return m_saveLocation.equals(s_defaultSaveLocation) && isHighlightMoves();
	}

	public String getSaveLocation() {
		return m_saveLocation;
	}

	public void setSaveLocation(String saveLocation) {
		m_saveLocation = saveLocation;
	}

	public boolean isHighlightMoves() {
		return m_shouldHighlightMoves;
	}

	public void setHighlightMoves(boolean highlightMoves) {		
		m_shouldHighlightMoves = highlightMoves;
	}
	
	private static String s_defaultSaveLocation;
	
	private String m_saveLocation;
	private boolean m_shouldHighlightMoves;
	
	private static final long serialVersionUID = 6126269552471996685L;
}
