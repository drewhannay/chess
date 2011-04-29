package rules;

import java.io.Serializable;
import java.util.ArrayList;
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
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class Rules implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -7895448383101471186L;

	//Variables Declaration
	/**
	 * Whether it is classic chess or not.
	 */
	private boolean isClassic;
	/**
	 * An object to determine whose turn it is.
	 */
	private NextTurn nextTurn;
	/**
	 * An object to determine if the game is over.
	 */
	private EndOfGame endOfGame;
	/**
	 * An object to crop the legal dests of a piece
	 */
	private CropLegalDests cropLegalDests;
	/**
	 * An object to determine which piece is the objective.
	 */
	private ObjectivePiece objectivePiece;
	/**
	 * An object to determine what happens after
	 * a move.
	 */
	private AfterMove afterMove;
	/**
	 * An object to get the board.
	 */
	private GetBoard getBoard;
	/**
	 * An object to promote a piece.
	 */
	private Promote promote;
	/**
	 * An object to get the promotion squares of a piece.
	 */
	private GetPromotionSquares getPromotionSquares;
	/**
	 * An object to adjust the team's dests.
	 */
	private AdjustTeamDests adjustTeamDests;

	//End Variables Declaration

	/**
	 * @param isClassic Whether the game is classic chess.
	 */
	public Rules(boolean isClassic,boolean isBlack) {
		//Initialize everything to classic to ensure nothing can be null.
		nextTurn = new NextTurn("classic", 1, 1, 0);
		endOfGame = new EndOfGame("classic", false, 3, "Queen",isBlack);
		cropLegalDests = new CropLegalDests();
		cropLegalDests.addMethod("classic");
		objectivePiece = new ObjectivePiece("classic", ""); //TODO error handling, in GUI
		afterMove = new AfterMove();
		afterMove.addMethod("classic");
		getBoard = new GetBoard("classic");
		promote = new Promote("classic");
		getPromotionSquares = new GetPromotionSquares("classic");
		adjustTeamDests = new AdjustTeamDests("classic");

	}

	/**
	 * Setter for adjustTeamDests
	 * @param dests the new adjustTeamDests.
	 */
	public void addAdjustTeamDests(AdjustTeamDests dests) {
		adjustTeamDests = dests;
	}

	/**
	 * Add a method to AfterMove.
	 * @param string The name of the method to add.
	 */
	public void addAfterMove(String string) {
		if (afterMove == null) {
			afterMove = new AfterMove();
		}
		afterMove.addMethod(string);

	}

	/**
	 * Add a method to cropLegalDests
	 * @param string The method to add.
	 */
	public void addCropLegalDests(String string) {
		if (cropLegalDests == null) {
			cropLegalDests = new CropLegalDests();
		}
		cropLegalDests.addMethod(string);

	}

	/**
	 * Setter for endOfGame
	 * @param eog the new endOfGame.
	 */
	public void addEndOfGame(EndOfGame eog) {
		endOfGame = eog;
	}

	/**
	 * Getter for name of the game.
	 * @return name of the game.
	 */
	public String theEndIsNigh(){
		return endOfGame.getName();
	}
	
	
	/**
	 * Setter for getPromotionSquares
	 * @param string The name of the method to implement
	 */
	public void addGetPromotionSquares(String string) {
		getPromotionSquares = new GetPromotionSquares(string);

	}

	/**
	 * Setter for promote
	 * @param string The new promote.
	 */
	public void addPromote(String string) {
		promote = new Promote(string);

	}

	/**
	 * Adjust the dests of the entire team
	 * @param team The team to adjust.
	 */
	public void adjustTeamDests(List<Piece> team) {
		adjustTeamDests.execute(team);
	}

	/**
	 * Do anything that needs to happen after a move.
	 * @param m The move performed.
	 */
	public void afterMove(Move m) {
		afterMove.execute(m);
	}

	/**
	 * Crop the legal dests of a piece.
	 * @param movingObjectivePiece The moving objective piece
	 * @param toAdjust The piece to adjust
	 * @param enemyTeam The enemy team.
	 */
	public void cropLegalDests(Piece movingObjectivePiece, Piece toAdjust, List<Piece> enemyTeam) {
		cropLegalDests.execute(movingObjectivePiece, toAdjust, enemyTeam);
	}

	/**
	 * Determine if the game is over.
	 * @param objectivePiece Which piece, if any,
	 * is the objective?
	 */
	public void endOfGame(Piece objectivePiece) {
		endOfGame.execute(objectivePiece);
	}

	/**
	 * Get the board.
	 * @param startBoard Which board the piece is on
	 * @return The board the piece should look at.
	 */
	public Board getBoard(Board startBoard) {
		return getBoard.execute(startBoard);

	}

	/**
	 * @param toPromote The piece to promote
	 * @return Where this piece can promote.
	 */
	public List<Square> getPromotionSquares(Piece toPromote) {
		return getPromotionSquares.execute(toPromote);
	}

	/**
	 * @return Getter for isClassic.
	 */
	public boolean isClassic() {
		return isClassic;
	}

	/**
	 * Determine whose turn it is.
	 * @return Is it black's turn?
	 */
	public boolean nextTurn() {
		return nextTurn.execute();

	}

	/**
	 * Find the objective piece
	 * @param isBlack Is the current piece black?
	 * @return The corresponding objective piece.
	 */
	public Piece objectivePiece(boolean isBlack) {
		return objectivePiece.execute(isBlack);

	}

	/**
	 * Undo nextTurn()
	 * @return Whose turn it is.
	 */
	public boolean prevTurn() {
		return nextTurn.undo();
	}

	/**
	 * Promote a piece
	 * @param toPromote The piece to promote
	 * @param verified Whether it's been verified that
	 * this piece can promote
	 * @param promo What it promotes from.
	 * @return The promoted piece.
	 */
	public Piece promote(Piece toPromote, boolean verified, String promo) {
		return promote.execute(toPromote, verified, promo);
	}

	/**
	 * Sets the game object in any instance
	 * variables requiring this information.
	 * @param g The game to set
	 */
	public void setGame(Game g) {
		endOfGame.setGame(g);
		objectivePiece.setGame(g);
		afterMove.setGame(g);
		getBoard.setGame(g);
		getPromotionSquares.setGame(g);
		promote.setGame(g);
	}

	/** 
	 * Setter for getBoard.
	 * @param getBoard Set getBoard to this
	 */
	public void setGetBoard(GetBoard getBoard) {
		this.getBoard = getBoard;

	}

	/**
	 * Setter for nextTurn
	 * @param n The new nextTurn object.
	 */
	public void setNextTurn(NextTurn n) {
		this.nextTurn = n;
	}

	/**
	 * Setter for objectivePiece
	 * @param objectivePiece The new objectivePiece.
	 */
	public void setObjectivePiece(ObjectivePiece objectivePiece) {
		this.objectivePiece = objectivePiece;

	}

	/**
	 * Undo the effects of afterMove()
	 * @param m The move to undo.
	 */
	public void undoAfterMove(Move m) {
		afterMove.undo(m);
	}

	/**
	 * undo endOfGame().
	 */
	public void undoEndOfGame() {
		endOfGame.undo();
	}

	/**
	 * Undo the promotion.
	 * @param p The piece to unpromote.
	 * @return The unpromoted piece.
	 */
	public Piece undoPromote(Piece p) {
		return promote.undo(p);
	}
	
	public String getObjectiveName(){
		return objectivePiece.getObjectiveName();
	}

	/**
	 * May these rules be played across a networked connection?
	 * @return
	 */
	public boolean networkable() {
		List<String> afterMoves = afterMove.getMethods();
		return !(afterMoves.contains("placeCaptured")||afterMoves.contains("placeCapturedSwitch"));
	}
}
