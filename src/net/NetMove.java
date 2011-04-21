package net;

public class NetMove {
	
	public int boardNum;
	public int originRow;
	public int originCol;
	public int destRow;
	public int destCol;
	
	public NetMove(int boardNum, int originRow, int originCol, int destRow, int destCol){
		this.boardNum = boardNum;
		this.originRow = originRow;
		this.originCol = originCol;
		this.destRow = destRow;
		this.destCol = destCol;
	}
	
	public String toString(){
		return boardNum + " " + originRow + " " + originCol;
	}
	

}
