package ai;

import java.io.Serializable;

/**
 * @author Drew Hannay The move to be sent across the network for trying to move
 */
public class FakeMove implements Serializable
{
	/**
	 * @param boardIndex Which board in the board[]
	 * @param originRow Which row it was/is on
	 * @param originColumn Which col it was/is on
	 * @param destinationRow Which row it wants to be on
	 * @param destinationColumn Which col it wants to be on
	 * @param promotionPieceName The name of the promotion piece
	 */
	public FakeMove(int boardIndex, int originRow, int originColumn, int destinationRow, int destinationColumn,
			String promotionPieceName)
	{
		mBoardIndex = boardIndex;
		mOriginRow = originRow;
		mOriginColumn = originColumn;
		mDestinationRow = destinationRow;
		mDestinationColumn = destinationColumn;
		mPromotionPieceName = promotionPieceName;
	}

	/**
	 * Getter method for String representation of the FakeMove
	 * 
	 * @return String String representation of the FakeMove
	 */
	@Override
	public String toString()
	{
		String space = " "; //$NON-NLS-1$
		return mBoardIndex + space + mOriginRow + space + mOriginColumn + space + mDestinationRow + space + mDestinationColumn;
	}

	private static final long serialVersionUID = -7511895104920021930L;

	public int mBoardIndex;
	public int mOriginRow;
	public int mOriginColumn;
	public int mDestinationRow;
	public int mDestinationColumn;
	public String mPromotionPieceName;
}
