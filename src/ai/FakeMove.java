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
	public FakeMove(int boardIndex, int originRow, int originColumn, int destinationRow, int destinationColumn, String promotionPieceName)
	{
		m_boardIndex = boardIndex;
		m_originRow = originRow;
		m_originColumn = originColumn;
		m_destinationRow = destinationRow;
		m_destinationColumn = destinationColumn;
		m_promotionPieceName = promotionPieceName;
	}

	/**
	 * Getter method for String representation of the FakeMove
	 * 
	 * @return String String representation of the FakeMove
	 */
	@Override
	public String toString()
	{
		return m_boardIndex + " " + m_originRow + " " + m_originColumn + " " + m_destinationRow + " " + m_destinationColumn;
	}

	private static final long serialVersionUID = -7511895104920021930L;

	public int m_boardIndex;
	public int m_originRow;
	public int m_originColumn;
	public int m_destinationRow;
	public int m_destinationColumn;
	public String m_promotionPieceName;
}
