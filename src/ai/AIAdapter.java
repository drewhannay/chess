package ai;

import gui.PlayNetGame;

import java.util.List;

import logic.Board;
import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Square;

import com.google.common.collect.Lists;

/**
 * AIAdapter.java
 * 
 * Class to implement the Adapter Pattern and give AI plug ins an interface of
 * methods to call to get information about the Game being played.
 * 
 * @author Drew Hannay
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 3 April 11, 2011
 */
public class AIAdapter
{
	/**
	 * The Game being viewed by the AI plug in
	 */
	private Game m_game;

	/**
	 * The array of boards for reference.
	 */
	private AIBoard[] m_boards;

	/**
	 * Constructor
	 * 
	 * @param game The Game being played
	 */
	public AIAdapter(Game game)
	{
		m_game = game;
	}

	/**
	 * @return The game
	 */
	public Game getGame()
	{
		return m_game;
	}

	/**
	 * @param aiPlugin The AI logical analysis plugin for decision trees
	 */
	public void runGame(AIPlugin aiPlugin)
	{
		PlayNetGame.running = true;
		while (PlayNetGame.running)
		{
			while (m_game.isBlackMove())
			{
				try
				{
					Thread.sleep(200);
				}
				catch (InterruptedException e)
				{
				}
				AIBoard[] boards = getBoards();
				FakeMove fm = aiPlugin.getMove(boards);
				playMove(fm);
			}
			try
			{
				Thread.sleep(0);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param move The move that it wishes to try
	 * @return Whether it moved there or not.
	 */
	public boolean playMove(FakeMove move)
	{
		try
		{
			Board board = m_game.getBoards()[move.m_boardIndex];
			m_game.playMove(new Move(board, board.getSquare(move.m_originRow, move.m_originColumn), board.getSquare(
					move.m_destinationRow, move.m_destinationColumn), move.m_promotionPieceName));
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * @return The array of boards in terms of AIBoard type.
	 */
	public synchronized AIBoard[] getBoards()
	{
		if (m_game.isStaleLegalDests())
			m_game.genLegalDests();

		m_boards = new AIBoard[m_game.getBoards().length];
		for (int i = 0; i < m_boards.length; i++)
			m_boards[i] = new AIBoard(m_game.getBoards()[i]);
		return m_boards;
	}

	/**
	 * @author Drew Hannay The AIBoard class. This is the board the AI "sees"
	 * and acts upon, similar to networking games.
	 */
	public class AIBoard
	{
		/**
		 * Two dimensional Array of AISquares representing the AIBoard
		 */
		protected AISquare squares[][];

		/**
		 * If movement beyond the East and West edges of the board results in
		 * further relocation.
		 */
		private boolean wraparound;

		/**
		 * The max column for this AIBoard
		 */
		private int maxColumn;
		/**
		 * The max row for this AIBoard
		 */
		private int maxRow;

		/**
		 * @param b The AI board for reference to all of its components.
		 */
		public AIBoard(Board b)
		{
			wraparound = b.isWrapAround();
			squares = new AISquare[b.getMaxRow()][b.getMaxCol()];
			maxColumn = b.getMaxCol();
			maxRow = b.getMaxRow();
			for (int row = 1, col = 1; row <= b.getMaxRow(); row++)
			{
				for (col = 1; col <= b.getMaxCol(); col++)
				{
					// Initialize the AISquares.
					// ignore counting from zero.
					squares[row - 1][col - 1] = new AISquare((row), (col), b.getSquare(row, col).isHabitable(), null);
				}
			}
			for (int row = 1, col = 1; row <= b.getMaxRow(); row++)
			{
				for (col = 1; col <= b.getMaxCol(); col++)
				{
					// Initialize the AISquares.
					// ignore counting from zero.
					if (b.getSquare(row, col).getPiece() != null)
						squares[row - 1][col - 1].setPiece(new AIPiece(b.getSquare(row, col).getPiece(), this));
				}
			}
		}

		/**
		 * @return If the board is a wraparound board (if right and left edges
		 * connect)
		 */
		public boolean isWraparound()
		{
			return wraparound;
		}

		/**
		 * @return The max col for the AI Board
		 */
		public int maxCol()
		{
			return maxColumn;
		}

		/**
		 * @return The max row for the AI Board
		 */
		public int maxRow()
		{
			return maxRow;
		}

		/**
		 * Getter method for AISquares at specified position.
		 * 
		 * @param row Row number
		 * @param col Column number
		 * @return The Square at specified position.
		 */
		public AISquare getSquare(int row, int col)
		{
			// Use x-1 and y-1 so that we can maintain the illusion of counting
			// from
			// 1.
			return squares[row - 1][col - 1];
		}
	}

	/**
	 * @author Drew Hannay The class for AI squares.
	 */
	public class AISquare
	{

		/**
		 * Row Index of AISquare
		 */
		private int row;// File
		/**
		 * Column Index of AISquare
		 */
		private int col;// Rank
		/**
		 * If the AISquare is able to be occupied.
		 */
		private boolean isHabitable;

		/**
		 * The AIPiece occupying this AISquare
		 */
		private AIPiece piece;

		/**
		 * Constructor
		 * 
		 * @param row The row number
		 * @param col The col number
		 * @param isHabitable If it can hold a piece
		 * @param piece The piece on it, if there is one.
		 */
		public AISquare(int row, int col, boolean isHabitable, AIPiece piece)
		{
			this.row = row;
			this.col = col;
			this.isHabitable = isHabitable;
			this.piece = piece;
		}

		/**
		 * Getter method for index to Row
		 * 
		 * @return Index to Row
		 */
		public int getRow()
		{
			return row;
		}

		/**
		 * Setter for piece.
		 * 
		 * @param p The new piece.
		 */
		public void setPiece(AIPiece p)
		{
			piece = p;
		}

		/**
		 * Getter method for index to Column
		 * 
		 * @return Index to Column
		 */
		public int getCol()
		{
			return col;
		}

		/**
		 * Getter method for Piece occupying the Square
		 * 
		 * @return Piece occupying the Square.
		 */
		public AIPiece getPiece()
		{
			return piece;
		}

		/**
		 * Getter method for ability of AISquare to be occupied.
		 * 
		 * @return If AISquare can be occupied.
		 */
		public boolean isHabitable()
		{
			return isHabitable;
		}

	}

	/**
	 * @author Drew Hannay This is the AIPiece class to distinguish between
	 * normal and AI pieces.
	 */
	public class AIPiece
	{

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

		/**
		 * @param p The piece we are extracting data from
		 * @param b The board the AI piece is on
		 */
		public AIPiece(Piece p, AIBoard b)
		{
			name = p.getName();
			isBlack = p.isBlack();
			board = b;
			legalDests = transformLegalDests(p.getLegalDests());
		}

		/**
		 * @param legalDests The legal destinations of the piece in standard
		 * terms
		 * @return The legal destinations of the piece in terms of AI Squares
		 */
		private synchronized List<AISquare> transformLegalDests(List<Square> legalDests)
		{
			List<AISquare> toReturn = Lists.newArrayList();
			for (Square s : legalDests)
				if (board.getSquare(s.getRow(), s.getCol()) != null)
					toReturn.add(board.getSquare(s.getRow(), s.getCol()));
			return toReturn;
		}

		/**
		 * @return Name of piece
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * @return Color of piece
		 */
		public boolean isBlack()
		{
			return isBlack;
		}

		/**
		 * @return Board this piece is on
		 */
		public AIBoard getBoard()
		{
			return board;
		}

		/**
		 * @return Legal destinations for this piece
		 */
		public List<AISquare> getLegalDests()
		{
			return legalDests;
		}

	}
}
