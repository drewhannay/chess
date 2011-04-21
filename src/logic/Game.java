package logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.NetMove;
import rules.Rules;
import timer.ChessTimer;
import timer.NoTimer;

/**
 * Game.java Object to save Game state.
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas CSCI 335, Wheaton College,
 *         Spring 2011 Phase 2 April 7, 2011
 */
public class Game implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -4564453748576017108L;

	/**
	 * Array of Boards for this Game
	 */
	private Board[] boards;

	/**
	 * Rules for the white team
	 */
	private Rules whiteRules;
	/**
	 * Rules for the black team
	 */
	private Rules blackRules;

	/**
	 * The name of this Game type
	 */
	private String gameType;

	/**
	 * Timer for the white team
	 */
	private ChessTimer whiteTimer;
	/**
	 * Timer for the black team
	 */
	private ChessTimer blackTimer;
	/**
	 * Boolean indicating who's turn it is
	 */
	private boolean isBlackMove;
	/**
	 * List of white Pieces
	 */
	private List<Piece> whiteTeam;
	/**
	 * List of black Pieces
	 */
	private List<Piece> blackTeam;
	/**
	 * The objective piece for the black team
	 */
	private Piece blackObjectivePiece;
	/**
	 * The objective piece for the white team
	 */
	private Piece whiteObjectivePiece;
	/**
	 * Boolean indicating if the legal destinations need to be generated again.
	 */
	private boolean staleLegalDests = true;

	/**
	 * List of Moves played in this Game
	 */
	private List<Move> history;
	/**
	 * Reference to the last completed move.
	 */
	private Move lastMove = null;
	

	/**
	 * Constructor. Initializes and saves state of Game information.
	 * @param gameType Game type identifier.
	 * @param boards Reference to boards Array.
	 * @param whiteRules The rules for the white team
	 * @param blackRules The rules for the black team
	 */
	public Game(String gameType, Board[] boards, Rules whiteRules, Rules blackRules) {
		this.gameType = gameType;
		this.boards = boards;
		whiteTimer = new NoTimer();
		blackTimer = new NoTimer();
		this.setWhiteRules(whiteRules);
		whiteRules.setGame(this);
		this.setBlackRules(blackRules);
		blackRules.setGame(this);
		setBlackMove(false);
		for (Board b : this.boards) {
			b.setGame(this);
		}
		setHistory(new ArrayList<Move>());
	}

	/**
	 * Call the afterMove method in the correct rules object
	 * @param m The Move that has just been performed
	 */
	public void afterMove(Move m) {
		if (isBlackMove()) {
			getBlackRules().afterMove(m);
		} else {
			getWhiteRules().afterMove(m);
		}
	}

	/**
	 * Generate the legal destinations for every piece on the board.
	 */
	public void genLegalDests() {
		Piece[] threats = null;
		Piece movingObjectivePiece = null;
		Piece otherObjectivePiece = null;
		List<Piece> movingTeam = null;
		List<Piece> otherTeam = null;

		setStaleLegalDests(false);

		for (int i = 0; i < boards.length; i++) {
			for (int r = 0, c = 0; r < boards[i].getMaxRow(); r++) {
				for (c = 0; c < boards[i].getMaxCol(); c++) {
					if (boards[i].squares[r][c].getPiece() != null) {
						boards[i].squares[r][c].getPiece().genLegalDests(
								(isBlackMove() ? getBlackRules() : getWhiteRules())
										.getBoard(boards[i]));
					}
				}
			}
		}

		movingObjectivePiece = (isBlackMove()) ? blackObjectivePiece
				: whiteObjectivePiece;
		movingTeam = (isBlackMove()) ? getBlackTeam() : getWhiteTeam();
		otherObjectivePiece = (isBlackMove()) ? blackObjectivePiece
				: whiteObjectivePiece;
		otherTeam = (isBlackMove()) ? getWhiteTeam() : getBlackTeam();

		// Make sure the objective piece doesn't put himself in check
		if (movingObjectivePiece != null) {
			(movingObjectivePiece.isBlack() ? getBlackRules() : getWhiteRules())
					.cropLegalDests(movingObjectivePiece, movingObjectivePiece,
							movingTeam);
		}
		if (otherObjectivePiece != null) {
			(otherObjectivePiece.isBlack() ? getBlackRules() : getWhiteRules())
					.cropLegalDests(otherObjectivePiece, otherObjectivePiece,
							otherTeam);
		}

		if (movingObjectivePiece != null) {
			// Now see if any of the moves puts the objective piece in check and
			// are therefore illegal
			for (int i = 0; i < otherTeam.size(); i++) {
				if (otherTeam.equals(getWhiteTeam())) {
					getWhiteRules().cropLegalDests(movingObjectivePiece, otherTeam
							.get(i), movingTeam);
				} else {
					getBlackRules().cropLegalDests(movingObjectivePiece, otherTeam
							.get(i), movingTeam);
				}
			}
		}

		(isBlackMove() ? getBlackRules() : getWhiteRules()).adjustTeamDests(movingTeam);

		// If the King is in check, the legal moves list must be modified
		// accordingly.
		if (movingObjectivePiece != null && movingObjectivePiece.isInCheck()) {
			if (getLastMove() != null) {
				getLastMove().setCheck(true);
			}
			threats = getThreats(movingObjectivePiece);

			switch (threats.length) {
			case 1:
				// There's only one threat, so another Piece could block, or the
				// King could move.
				for (int i = 0; i < movingTeam.size(); i++) {
					movingTeam.get(i).genLegalDestsSaveKing(
							movingObjectivePiece, threats[0]);
				}
				break;
			case 2:
				// Since there's two threats, the objective piece is the only
				// one who can get himself out of check.
				for (int i = 0; i < movingTeam.size(); i++) {
					Piece p = (movingTeam.get(i));
					if (p != movingObjectivePiece) {
						p.getLegalDests().clear();
						p.getGuardSquares().clear();
					}
				}
				if (getLastMove() != null) {
					getLastMove().setDoubleCheck(true);
				}
				break;
			}
		}
	}

	/**
	 * @return the blackRules
	 */
	public Rules getBlackRules() {
		return blackRules;
	}

	/**
	 * @return the blackTeam
	 */
	public List<Piece> getBlackTeam() {
		return blackTeam;
	}

	/**
	 * Getter method for the Black timer
	 * @return The timer for the black team
	 */
	public ChessTimer getBlackTimer() {
		return blackTimer;
	}

	/**
	 * Getter method to retrieve boards.
	 * @return Reference to Array of Boards.
	 */
	public Board[] getBoards() {
		return boards;
	}

	/**
	 * Get an array of the captured Pieces for the specified team
	 * @param isBlack The team of which to find captured pieces
	 * @return The Piece[] of captured Pieces
	 */
	public Piece[] getCapturedPieces(boolean isBlack) {
		return getCaptures(isBlack, true);
	}

	/**
	 * Get the captured or not captured pieces from the specified team
	 * @param isBlack The team to get pieces from
	 * @param isCaptured Whether to collect captured or free pieces
	 * @return The Piece[] of matching pieces
	 */
	protected Piece[] getCaptures(boolean isBlack, boolean isCaptured) {
		Piece[] pows = null;
		List<Piece> team = (isBlack) ? getBlackTeam() : getWhiteTeam();
		int count = 0;
		Piece piece = null;

		for (int i = 0; i < team.size(); i++) {
			if (team.get(i).isCaptured() == isCaptured) {
				count++;
			}
		}

		if (count > 0) {
			pows = new Piece[count];
			count = 0;
			for (int i = 0; i < team.size(); i++) {
				piece = team.get(i);
				if (piece.isCaptured() == isCaptured) {
					pows[count++] = piece;
				}

			}
		}
		return pows;
	}

	/**
	 * Get the Pieces of the specified team guarding the specified Square
	 * @param sq The Square being guarded
	 * @param isBlack The team guarding the Square
	 * @return The Pieces guarding the Square
	 */
	public Piece[] getGuards(Square sq, boolean isBlack) {
		Iterator<Piece> team = (isBlack) ? getBlackTeam().iterator() : getWhiteTeam()
				.iterator();
		List<Piece> attackers = new ArrayList<Piece>();

		if (isStaleLegalDests()) {
			genLegalDests();
		}

		Piece piece = null;
		while (team.hasNext()) {
			piece = team.next();
			if (piece.isGuarding(sq)) {
				attackers.add(piece);
			}
		}

		Piece[] guards = null;
		if (attackers.size() > 0) {
			guards = new Piece[attackers.size()];
			guards = attackers.toArray(guards);
		}
		return guards;
	}

	/**
	 * @return the history
	 */
	public List<Move> getHistory() {
		return history;
	}

	/**
	 * @return the lastMove
	 */
	public Move getLastMove() {
		return lastMove;
	}

	/**
	 * Get a count of all legal moves for this turn.
	 * @return The number of legal moves this turn.
	 */
	public int getLegalMoveCount() {
		int count = 0;
		List<Piece> movingTeam = (isBlackMove()) ? getBlackTeam() : getWhiteTeam();

		if (isStaleLegalDests()) {
			genLegalDests();
		}

		for (int i = 0; i < movingTeam.size(); i++) {
			count += (movingTeam.get(i)).getLegalDests().size();
		}

		return count;
	}

	/**
	 * Get the specified objective piece
	 * @param isBlack The team from which to get the objective piece
	 * @return The objective piece, if found
	 */
	public Piece getOtherObjectivePiece(boolean isBlack) {
		return isBlack ? whiteObjectivePiece : blackObjectivePiece;
	}

	/**
	 * Get the rules for the specified team
	 * @param isBlack The team to get the rules for
	 * @return The rules for the specified team
	 */
	public Rules getRules(boolean isBlack) {
		return isBlack ? getBlackRules() : getWhiteRules();
	}

	/**
	 * Get the threats on the given piece
	 * @param piece The piece on which to assess threats
	 * @return The pieces threatening the specified piece
	 */
	public Piece[] getThreats(Piece piece) {
		return getThreats(piece.getSquare(), !piece.isBlack());
	}

	/**
	 * Get the threats on the given piece
	 * @param threatened The piece on which to assess threats
	 * @param attackerIsBlack The team of the attacker
	 * @return The pieces threatening the specified piece
	 */
	public Piece[] getThreats(Square threatened, boolean attackerIsBlack) {

		Iterator<Piece> team = (attackerIsBlack) ? getBlackTeam().iterator()
				: getWhiteTeam().iterator();
		List<Piece> attackers = new ArrayList<Piece>();

		if (isStaleLegalDests()) {
			genLegalDests();
		}

		Piece piece = null;
		while (team.hasNext()) {
			piece = team.next();
			if (piece.isLegalAttack(threatened)) {
				attackers.add(piece);
			}
		}

		Piece[] threats = null;
		if (attackers.size() > 0) {
			threats = new Piece[attackers.size()];
			threats = attackers.toArray(threats);
			return threats;
		}

		return threats;
	}

	/**
	 * @return the whiteRules
	 */
	public Rules getWhiteRules() {
		return whiteRules;
	}

	/**
	 * @return the whiteTeam
	 */
	public List<Piece> getWhiteTeam() {
		return whiteTeam;
	}

	/**
	 * Get the timer for the white team
	 * @return The timer for the white team
	 */
	public ChessTimer getWhiteTimer() {
		return whiteTimer;
	}

	/**
	 * @return the isBlackMove
	 */
	public boolean isBlackMove() {
		return isBlackMove;
	}

	/**
	 * Determines if a unit threatens the King-type unit.
	 * @return If king-type unit is threatened.
	 */
	public boolean isCheck() {
		boolean check = false;

		if (getLastMove() != null)
			return getLastMove().isCheck();

		if (isStaleLegalDests()) {
			genLegalDests();
		}

		check = (isBlackMove()) ? getBlackRules().objectivePiece(true).isInCheck()
				: getWhiteRules().objectivePiece(false).isInCheck();

		return check;
	}

	/**
	 * Determines if the King-type unit cannot escape check.
	 * @return If the King-type unit is in Check mate.
	 */
	public boolean isCheckmate() {
		if (getLastMove() != null)
			return getLastMove().isCheckmate();

		if (isStaleLegalDests()) {
			genLegalDests();
		}

		if (getLegalMoveCount() == 0 && isCheck())
			return true;
		else
			return false;
	}

	/**
	 * Check if this game is classic chess
	 * @return If this game is classic chess
	 */
	public boolean isClassicChess() {
		return gameType.equals("Classic");
	}

	/**
	 * Check if the given square is guarded by the given team
	 * @param sq The square to assess
	 * @param isBlack The team to assess
	 * @return If the square is guarded by the gived team
	 */
	public boolean isGuarded(Square sq, boolean isBlack) {
		return (getGuards(sq, isBlack) != null);
	}

	/**
	 * @return the staleLegalDests
	 */
	public boolean isStaleLegalDests() {
		return staleLegalDests;
	}

	/**
	 * Determines if a player is unable to move.
	 * @return If game is over by Stale mate.
	 */
	public boolean isStalemate() {
		if (getLastMove() != null)
			return getLastMove().isStalemate();

		if (isStaleLegalDests()) {
			genLegalDests();
		}

		if (getLegalMoveCount() == 0 && !isCheck())
			return true;
		else
			return false;
	}

	/**
	 * Check threats for the given piece
	 * @param piece The piece to assess threats on
	 * @return If the piece is threatened
	 */
	public boolean isThreatened(Piece piece) {
		return isThreatened(piece.getSquare(), !piece.isBlack());
	}

	/**
	 * Check threats for the given square
	 * @param sq The square to assess threats for
	 * @param isBlack The team to assess threats for
	 * @return If the square is threatened by the given team
	 */
	public boolean isThreatened(Square sq, boolean isBlack) {
		return (getThreats(sq, isBlack) != null);
	}

	/**
	 * Move to the next turn
	 */
	public void nextTurn() {
		//We only need to track turns once, so always use the whiteRules
		setBlackMove(getWhiteRules().nextTurn());
	}

	/**
	 * Play a move in the Game
	 * @param move The Move to play
	 * @throws Exception If the Move was illegal
	 */
	public void playMove(Move move) throws Exception {
		Move m = move;
		m.execute();
		if (getHistory().contains(m))
			return;
		getHistory().add(m);
		if (isBlackMove()) {
			getBlackRules().endOfGame(blackObjectivePiece);
		} else {
			getWhiteRules().endOfGame(whiteObjectivePiece);
		}
	}

	/**
	 * Revert to the previous turn
	 */
	public void prevTurn() {
		setBlackMove(getWhiteRules().prevTurn());
	}

	/**
	 * Saves game's state to file.
	 * @param dirName The directory in which to save the game
	 * @param fileName The name for the game file
	 * @param ACN Whether the game should be saved in ACN
	 */
	public void saveGame(String dirName, String fileName, boolean ACN) {
		try {
			new File(dirName).mkdir();

			if (ACN) {
				AlgebraicConverter.convert(getHistory(),
						(dirName + "/" + fileName + ".acn"));
			} else {
				FileOutputStream f_out = new FileOutputStream(new File(dirName
						+ "/" + fileName + ".var"));
				ObjectOutputStream out = new ObjectOutputStream(f_out);
				out.writeObject(this);
				out.close();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * @param isBlackMove the isBlackMove to set
	 */
	public void setBlackMove(boolean isBlackMove) {
		this.isBlackMove = isBlackMove;
	}

	/**
	 * @param blackRules the blackRules to set
	 */
	public void setBlackRules(Rules blackRules) {
		this.blackRules = blackRules;
	}

	/**
	 * Sets the ArrayList of Pieces for the black team.
	 * @param blackTeam ArrayList of black team pieces.
	 */
	public void setBlackTeam(List<Piece> blackTeam) {
		this.blackTeam = blackTeam;
		blackObjectivePiece = getBlackRules().objectivePiece(true);
	}

	/**
	 * @param history the history to set
	 */
	public void setHistory(List<Move> history) {
		this.history = history;
	}

	/**
	 * @param lastMove the lastMove to set
	 */
	public void setLastMove(Move lastMove) {
		this.lastMove = lastMove;
	}

	/**
	 * Set the Result for the Game
	 * @param result The Result to set.
	 */
	public void setResult(Result result) {
		Move m = getLastMove();
		m.setResult(result);
	}

	/**
	 * @param staleLegalDests the staleLegalDests to set
	 */
	public void setStaleLegalDests(boolean staleLegalDests) {
		this.staleLegalDests = staleLegalDests;
	}

	/**
	 * Set the timers for this game
	 * @param whiteTimer The timer for the white team
	 * @param blackTimer The timer for the black team
	 */
	public void setTimers(ChessTimer whiteTimer, ChessTimer blackTimer) {
		this.whiteTimer = whiteTimer;
		this.blackTimer = blackTimer;
	}

	/**
	 * @param whiteRules the whiteRules to set
	 */
	public void setWhiteRules(Rules whiteRules) {
		this.whiteRules = whiteRules;
	}

	/**
	 * Sets the ArrayList of Pieces for the white team.
	 * @param whiteTeam ArrayList of white team pieces.
	 */
	public void setWhiteTeam(List<Piece> whiteTeam) {
		this.whiteTeam = whiteTeam;
		whiteObjectivePiece = getWhiteRules().objectivePiece(false);
	}

	/**
	 * Undo the afterMove events
	 * @param m The Move to undo
	 */
	public void undoAfterMove(Move m) {
		if (isBlackMove()) {
			getBlackRules().undoAfterMove(m);
		} else {
			getWhiteRules().undoAfterMove(m);
		}
	}
	
	public Move fakeToRealMove(NetMove m) throws Exception {
		Board to = boards[m.boardNum];
		Board from = ((isBlackMove)?blackRules:whiteRules).getBoard(to);
		System.out.println(m.boardNum);
		return new Move(from,from.getSquare(m.originRow, m.originCol),to.getSquare(m.destRow, m.destCol),m.promoName);
	}
	
	public NetMove moveToFakeMove(Move m){
		int boardNum = (m.board == boards[0])?0:1;
		String promoName = null;
		if(history.get(history.size()-1).getPromoPiece()!=null){
			System.out.println("here");
			promoName = history.get(history.size()-1).getPromoPiece().getName();
		}
		return new NetMove(boardNum, m.origin.getRow(), m.origin.getCol(), m.getDest().getRow(), m.getDest().getCol(),promoName);
	}

}
