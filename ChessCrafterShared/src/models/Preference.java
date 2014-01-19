
package models;

import utility.GsonUtility;
import com.google.common.base.Objects;

public class Preference
{
	public Preference()
	{
		mShouldHighlightMoves = true;
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

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Preference))
			return false;

		Preference otherPreference = (Preference) other;

		return Objects.equal(mSaveLocation, otherPreference.mSaveLocation)
				&& Objects.equal(mShouldHighlightMoves, otherPreference.mShouldHighlightMoves)
				&& Objects.equal(mShowPieceToolTips, otherPreference.mShowPieceToolTips);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(mSaveLocation, mShouldHighlightMoves, mShowPieceToolTips);
	}
	
	public static Preference loadPreference(String jsonString)
	{
		return GsonUtility.getGson().fromJson(jsonString, Preference.class);
	}
	
	public String getJsonString()
	{
		return GsonUtility.getGson().toJson(this);
	}
	
	private static String s_defaultSaveLocation;

	private static final String DEFAULT_LOCATION = "default"; //$NON-NLS-1$
	
	private boolean mPathSet;
	private boolean mHighlightSet;
	private String mSaveLocation;
	private boolean mShouldHighlightMoves;
	private boolean mShowPieceToolTips;
}
