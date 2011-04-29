
import java.util.List;

public class AIDemo implements AIPlugin {

	@Override
	public FakeMove getMove(AIBoard[] boards) {
		for(int i = 0;i<boards[0].squares.length;i++){
			for(int j = 0;j<boards[0].squares[i].length;j++){
				if(boards[0].squares[i][j].getPiece()!=null && boards[0].squares[i][j].getPiece().isBlack()){
					List<AISquare> legalDests = boards[0].squares[i][j].getPiece().legalDests;
					for(AISquare s: legalDests){
						try{return new FakeMove(0, i+1, j+1, s.getRow(), s.getCol(), "Queen");
						}catch(Exception e){
							continue;
						}
						
					}
				}
					
			}
		}
		
		return null;
	}

}
