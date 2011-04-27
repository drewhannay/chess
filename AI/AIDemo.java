package logic;

import java.util.List;

import logic.AIAdapter.AIBoard;
import logic.AIAdapter.AISquare;
import net.FakeMove;

public class AIDemo implements AIPlugin {

	@Override
	public FakeMove getMove(AIBoard[] boards) {
		
		for(int i = 0;i<boards[0].squares.length;i++){
			for(int j = 0;j<boards[0].squares[i].length;j++){
				if(boards[0].squares[i][j].getPiece()!=null && boards[0].squares[i][j].getPiece().isBlack()){
					List<AISquare> legalDests = boards[0].squares[i][j].getPiece().legalDests;
					if(legalDests.size()>0)
						return new FakeMove(0, i+1, j+1, legalDests.get(0).getRow(), legalDests.get(0).getCol(), null);
				}
					
			}
		}
		
		return null;
	}

}
