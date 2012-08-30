package rules;

import java.io.Serializable;
import java.util.List;

import logic.Board;
import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Square;

/**
 * Rules.java
 * 
 * Class to model the Rules of a variation of chess.
 * 
 * @author Drew Hannay & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class Rules implements Serializable
{
	/**
	 * @param isClassic Whether the game is classic chess.
	 * @param isBlack is this for the black or white player?
	 */
	public Rules(boolean isClassic, boolean isBlack)
	{
		// Initialize everything to classic to ensure nothing can be null.
		m_nextTurn = new NextTurn("classic", 1, 1, 0);
		m_endOfGame = new EndOfGame("classic", 3, "Queen", isBlack);
		m_cropLegalDests = new CropLegalDests();
		m_cropLegalDests.addMethod("classic");
		m_objectivePiece = new ObjectivePiece("classic", "");
		m_afterMove = new AfterMove();
		m_afterMove.addMethod("classic");
		m_getBoard = new GetBoard("classic");
		m_promote = new Promote("classic");
		m_getPromotionSquares = new GetPromotionSquares("classic");
		m_adjustTeamDests = new AdjustTeamDests("classic");
	}

	/**
	 * Setter for adjustTeamDests
	 * 
	 * @param dests the new adjustTeamDests.
	 */
	public void addAdjustTeamDests(AdjustTeamDests dests)
	{
		m_adjustTeamDests = dests;
	}

	/**
	 * Add a method to AfterMove.
	 * 
	 * @param string The name of the method to add.
	 */
	public void addAfterMove(String string)
	{
		if (m_afterMove == null)
		{
			m_afterMove = new AfterMove();
		}
		m_afterMove.addMethod(string);
	}

	/**
	 * Add a method to cropLegalDests
	 * 
	 * @param string The method to add.
	 */
	public void addCropLegalDests(String string)
	{
		if (m_cropLegalDests == null)
		{
			m_cropLegalDests = new CropLegalDests();
		}
		m_cropLegalDests.addMethod(string);
	}

	/**
	 * Setter for endOfGame
	 * 
	 * @param eog the new endOfGame.
	 */
	public void addEndOfGame(EndOfGame eog)
	{
		m_endOfGame = eog;
	}

	/**
	 * Getter for name of the game.
	 * 
	 * @return name of the game.
	 */
	public String theEndIsNigh()
	{
		return m_endOfGame.getMethodName();
	}

	/**
	 * Setter for getPromotionSquares
	 * 
	 * @param string The name of the method to implement
	 */
	public void addGetPromotionSquares(String string)
	{
		m_getPromotionSquares = new GetPromotionSquares(string);

	}

	/**
	 * Setter for promote
	 * 
	 * @param string The new promote.
	 */
	public void addPromote(String string)
	{
		m_promote = new Promote(string);
	}

	/**
	 * Adjust the dests of the entire team
	 * 
	 * @param team The team to adjust.
	 */
	public void adjustTeamDests(List<Piece> team)
	{
		m_adjustTeamDests.execute(team);
	}

	/**
	 * Do anything that needs to happen after a move.
	 * 
	 * @param m The move performed.
	 */
	public void afterMove(Move m)
	{
		m_afterMove.execute(m);
	}

	/**
	 * Crop the legal dests of a piece.
	 * 
	 * @param movingObjectivePiece The moving objective piece
	 * @param toAdjust The piece to adjust
	 * @param enemyTeam The enemy team.
	 */
	public void cropLegalDests(Piece movingObjectivePiece, Piece toAdjust, List<Piece> enemyTeam)
	{
		m_cropLegalDests.execute(movingObjectivePiece, toAdjust, enemyTeam);
	}

	/**
	 * Determine if the game is over.
	 * 
	 * @param objectivePiece Which piece, if any, is the objective?
	 */
	public void endOfGame(Piece objectivePiece)
	{
		m_endOfGame.execute(objectivePiece);
	}

	/**
	 * Get the board.
	 * 
	 * @param startBoard Which board the piece is on
	 * @return The board the piece should look at.
	 */
	public Board getBoard(Board startBoard)
	{
		return m_getBoard.execute(startBoard);
	}

	/**
	 * @param toPromote The piece to promote
	 * @return Where this piece can promote.
	 */
	public List<Square> getPromotionSquares(Piece toPromote)
	{
		return m_getPromotionSquares.execute(toPromote);
	}

	/**
	 * @return Getter for isClassic.
	 */
	public boolean isClassic()
	{
		return m_isClassic;
	}

	/**
	 * Determine whose turn it is.
	 * 
	 * @return Is it black's turn?
	 */
	public boolean nextTurn()
	{
		return m_nextTurn.execute();
	}

	/**
	 * Find the objective piece
	 * 
	 * @param isBlack Is the current piece black?
	 * @return The corresponding objective piece.
	 */
	public Piece objectivePiece(boolean isBlack)
	{
		return m_objectivePiece.execute(isBlack);
	}

	/**
	 * Undo nextTurn()
	 * 
	 * @return Whose turn it is.
	 */
	public boolean prevTurn()
	{
		return m_nextTurn.undo();
	}

	/**
	 * Promote a piece
	 * 
	 * @param toPromote The piece to promote
	 * @param verified Whether it's been verified that this piece can promote
	 * @param promo What it promotes from.
	 * @return The promoted piece.
	 */
	public Piece promote(Piece toPromote, boolean verified, String promo)
	{
		return m_promote.execute(toPromote, verified, promo);
	}

	/**
	 * Sets the game object in any instance variables requiring this
	 * information.
	 * 
	 * @param g The game to set
	 */
	public void setGame(Game g)
	{
		m_endOfGame.setGame(g);
		m_objectivePiece.setGame(g);
		m_afterMove.setGame(g);
		m_getBoard.setGame(g);
		m_getPromotionSquares.setGame(g);
		m_promote.setGame(g);
	}

	/**
	 * Setter for getBoard.
	 * 
	 * @param getBoard Set getBoard to this
	 */
	public void setGetBoard(GetBoard getBoard)
	{
		m_getBoard = getBoard;
	}

	/**
	 * Setter for nextTurn
	 * 
	 * @param n The new nextTurn object.
	 */
	public void setNextTurn(NextTurn n)
	{
		m_nextTurn = n;
	}

	/**
	 * Setter for nextTurn
	 * 
	 * @param n The new nextTurn object.
	 */
	public NextTurn getNextTurn()
	{
		return m_nextTurn;
	}

	/**
	 * Setter for objectivePiece
	 * 
	 * @param objectivePiece The new objectivePiece.
	 */
	public void setObjectivePiece(ObjectivePiece objectivePiece)
	{
		m_objectivePiece = objectivePiece;
	}

	/**
	 * Undo the effects of afterMove()
	 * 
	 * @param m The move to undo.
	 */
	public void undoAfterMove(Move m)
	{
		m_afterMove.undo(m);
	}

	/**
	 * undo endOfGame().
	 */
	public void undoEndOfGame()
	{
		m_endOfGame.undo();
	}

	/**
	 * Undo the promotion.
	 * 
	 * @param p The piece to unpromote.
	 * @return The unpromoted piece.
	 */
	public Piece undoPromote(Piece p)
	{
		return m_promote.undo(p);
	}

	/**
	 * Get the name of the objective piece
	 * 
	 * @return the name of the objective piece
	 */
	public String getObjectiveName()
	{
		return m_objectivePiece.getObjectivePieceName();
	}

	/**
	 * May these rules be played across a networked connection?
	 * 
	 * @return can this game be played across a network?
	 */
	public boolean networkable()
	{
		List<String> afterMoves = m_afterMove.getMethods();
		return !(afterMoves.contains("placeCaptured") || afterMoves.contains("placeCapturedSwitch"));
	}

	private static final long serialVersionUID = -7895448383101471186L;

	private boolean m_isClassic;
	private NextTurn m_nextTurn;
	private EndOfGame m_endOfGame;
	private CropLegalDests m_cropLegalDests;
	private ObjectivePiece m_objectivePiece;
	private AfterMove m_afterMove;
	private GetBoard m_getBoard;
	private Promote m_promote;
	private GetPromotionSquares m_getPromotionSquares;
	private AdjustTeamDests m_adjustTeamDests;
}
