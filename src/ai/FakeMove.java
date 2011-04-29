package ai;

import java.io.Serializable;

/**
 * @author Drew Hannay
 * The move to be sent across the network for trying to move
 */
public class FakeMove implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -7511895104920021930L;
	
	/**
	 * Which board number in the board[]
	 */
	public int boardNum;
	/**
	 * Which row it is coming from
	 */
	public int originRow;
	/**
	 * Which col it is coming from
	 */
	public int originCol;
	/**
	 * Which row it wants to go to
	 */
	public int destRow;
	/**
	 * Which col it wants to go to
	 */
	public int destCol;
	/**
	 * The name of the promotion piece
	 */
	public String promoName;
	
	/**
	 * @param boardNum Which board in the board[]
	 * @param originRow Which row it was/is on
	 * @param originCol Which col it was/is on
	 * @param destRow Which row it wants to be on
	 * @param destCol Which col it wants to be on
	 * @param promoName The name of the promotion piece
	 */
	public FakeMove(int boardNum, int originRow, int originCol, int destRow, int destCol, String promoName){
		this.boardNum = boardNum;
		this.originRow = originRow;
		this.originCol = originCol;
		this.destRow = destRow;
		this.destCol = destCol;
		this.promoName = promoName;
	}
	
	/**
	 * Getter method for String representation of the FakeMove
	 * @return String String representation of the FakeMove  
	 */
	public String toString(){
		return boardNum + " " + originRow + " " + originCol + " " + destRow + " " + destCol;
	}
	

}
