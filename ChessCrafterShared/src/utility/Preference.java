
package utility;

public class Preference
{
	public Preference()
	{
		mShouldHighlightMoves = true;
		mShowPieceToolTips = true;
		s_defaultSaveLocation = DEFAULT_LOCATION;
		mSaveLocation = s_defaultSaveLocation;
	}

	public boolean isPathSet()
	{
		return !mPathSet;
	}

	public boolean isHighlightSet()
	{
		return !mHighlightSet;
	}

	public String getSaveLocation()
	{
		return mSaveLocation;
	}

	public void setSaveLocation(String saveLocation)
	{
		mPathSet = true;
		mSaveLocation = saveLocation;
	}

	public boolean isHighlightMoves()
	{
		return mShouldHighlightMoves;
	}

	public void setHighlightMoves(boolean highlightMoves)
	{
		mHighlightSet = true;
		mShouldHighlightMoves = highlightMoves;
	}

	public boolean showPieceToolTips()
	{
		return mShowPieceToolTips;
	}

	public void setShowPieceToolTips(boolean showPieceToolTips)
	{
		mShowPieceToolTips = showPieceToolTips;
	}

	private static String s_defaultSaveLocation;

	private static final String DEFAULT_LOCATION = "ya big dummy"; //$NON-NLS-1$
	
	private boolean mPathSet;
	private boolean mHighlightSet;
	private String mSaveLocation;
	private boolean mShouldHighlightMoves;
	private boolean mShowPieceToolTips;
}
