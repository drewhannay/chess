package logic;

import java.util.ArrayList;
import java.util.List;

import net.FakeMove;

/**
 * AIAdapter.java
 * 
 * Class to implement the Adapter Pattern and give AI
 * plug ins an interface of methods to call to get information about 
 * the Game being played.
 * 
 * @author Drew Hannay
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 3
 * April 11, 2011
 */
public class AIAdapter {

	/**
	 * The Game being viewed by the AI plug in
	 */
	private Game g;

	private AIBoard[] boards;

	/**
	 * Constructor
	 * @param g The Game being played
	 */
	public AIAdapter(Game g){
		this.g = g;
	}
	
	public Game getGame(){
		return g;
	}

	public void runGame(AIPlugin ai){
		while(true){
//			System.out.println("my turn");
			while(g.isBlackMove()){
				System.out.println("my turn");
				FakeMove fm = ai.getMove(getBoards());
				System.out.println(fm);
				System.out.println("Sucess? " + playMove(fm));
			}
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public boolean playMove(FakeMove m){
		Board b = g.getBoards()[m.boardNum];
		try{
			g.playMove(new Move(b, b.getSquare(m.originRow, m.originCol), b.getSquare(m.destRow, m.destCol)));
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public AIBoard[] getBoards(){
		boards = new AIBoard[g.getBoards().length];
		for(int i = 0;i<boards.length;i++)
			boards[i] = new AIBoard(g.getBoards()[i]);
		return boards;
	}

	public class AIBoard{

		/**
		 * Two dimensional Array of AISquares representing the AIBoard
		 */
		protected AISquare squares[][];

		/**
		 * If movement beyond the East and West edges of the board results in
		 * further relocation.
		 */
		private boolean wraparound;

		public AIBoard(Board b) {
			this.wraparound = b.isWraparound();
			squares = new AISquare[b.getMaxRow()][b.getMaxCol()];

			for (int row = 0, col = 0; row < b.getMaxRow(); row++) {
				for (col = 0; col < b.getMaxCol(); col++) {
					// Initialize the AISquares. Add one to the row and column to
					// ignore counting from zero.
					if(b.getSquare(row+1, col+1).getPiece()!=null)
						squares[row][col] = new AISquare((row + 1), (col + 1),b.getSquare(row+1, col+1).isHabitable(),
								new AIPiece(b.getSquare(row+1, col+1).getPiece(),this));
					else
						squares[row][col] = new AISquare((row + 1), (col + 1),b.getSquare(row+1, col+1).isHabitable(),null);
				}
			}
		}

		public boolean isWraparound(){
			return wraparound;
		}

		/**
		 * Getter method for AISquares at specified position.
		 * @param row Row number
		 * @param col Column number
		 * @return The Square at specified position.
		 */
		public AISquare getSquare(int row, int col) {
			// Use x-1 and y-1 so that we can maintain the illusion of counting from
			// 1.
			return squares[row - 1][col - 1];
		}
	}

	public class AISquare{

		/**
		 * Row Index of AISquare
		 */
		private int row;//File
		/**
		 * Column Index of AISquare
		 */
		private int col;//Rank
		/**
		 * If the AISquare is able to be occupied.
		 */
		private boolean isHabitable;

		/**
		 * The AIPiece occupying this AISquare
		 */
		private AIPiece piece;

		public AISquare(int row, int col, boolean isHabitable, AIPiece piece) {
			this.row = row;
			this.col = col;
			this.isHabitable = isHabitable;
			this.piece = piece;
		}

		/**
		 * Getter method for index to Row
		 * @return Index to Row
		 */
		public int getRow() {
			return row;
		}

		/**
		 * Getter method for index to Column
		 * @return Index to Column
		 */
		public int getCol() {
			return col;
		}

		/**
		 * Getter method for Piece occupying the Square
		 * @return Piece occupying the Square.
		 */
		public AIPiece getPiece() {
			return piece;
		}

		/**
		 * Getter method for ability of AISquare to be occupied.
		 * @return If AISquare can be occupied.
		 */
		public boolean isHabitable(){
			return isHabitable;
		}

	}

	public class AIPiece{

		/**
		 * The name of this AIPiece
		 */
		private String name;

		/**
		 * The color of this AIPiece
		 */
		private boolean isBlack;

		/**
		 * The Board this AIPiece is on
		 */
		private AIBoard board;

		/**
		 * List of legal AISquares for this AIPiece to move to
		 */
		protected List<AISquare> legalDests;

		public AIPiece(Piece p,AIBoard b) {
			this.name = p.getName();
			this.isBlack = p.isBlack();
			this.board = b;
			this.legalDests = transformLegalDests(p.getLegalDests());
		}

		private List<AISquare> transformLegalDests(List<Square> legalDests){
			List<AISquare> toReturn = new ArrayList<AISquare>();
			for(Square s:legalDests)
				toReturn.add(board.getSquare(s.getRow(), s.getCol()));
			return toReturn;
		}

		public String getName(){
			return name;
		}

		public boolean isBlack(){
			return isBlack;
		}

		public AIBoard getBoard(){
			return board;
		}

		public List<AISquare> getLegalDests(){
			return legalDests;
		}

	}
}
