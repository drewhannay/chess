package ai;

import java.io.Serializable;

public class FakeMove implements Serializable {
	
	private static final long serialVersionUID = -7511895104920021930L;
	
	public int boardNum;
	public int originRow;
	public int originCol;
	public int destRow;
	public int destCol;
	public String promoName;
	
	public FakeMove(int boardNum, int originRow, int originCol, int destRow, int destCol, String promoName){
		this.boardNum = boardNum;
		this.originRow = originRow;
		this.originCol = originCol;
		this.destRow = destRow;
		this.destCol = destCol;
		this.promoName = promoName;
	}
	
	public String toString(){
		return boardNum + " " + originRow + " " + originCol + " " + destRow + " " + destCol;
	}
	

}
