package logic;

import gui.PlayGame;

import java.io.Serializable;
import java.util.List;

import timer.Word;

/**
 * Move.java
 * 
 * Class modeling a Move in a chess game.
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011
 * Phase 2
 * April 7, 2011
 */
public class Move implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -7581503689590078817L;

	/**
	 * Constant to indicate castling queen side
	 */
	public static final int CASTLE_QUEEN_SIDE = 89751354;

	/**
	 * Constant to indicate castling king side
	 */
	public static final int CASTLE_KING_SIDE = 678945613;

	/**
	 * The Board on which this Move is to be performed
	 */
	protected Board board;
	/**
	 * The Square the Move originates on
	 */
	protected Square origin;
	/**
	 * The Square the Move is going to
	 */
	private Square dest;
	/**
	 * The Class to which this Move is promoting a Piece, if any
	 */
	protected Class<?> promo;
	/**
	 * Boolean indicating whether this Move has been executed
	 */
	protected boolean executed;

	/**
	 * The piece being moved
	 */
	private Piece piece;
	/**
	 * The piece to which the moving piece was promoted
	 */
	protected Piece promotion;
	/**
	 * The piece which was captured this Move
	 */
	private Piece captured;
	/**
	 * The piece that was completely removed from the Board on this turn.
	 * Used by the AfterMove class
	 */
	private Piece removed;
	/**
	 * The enpassant column from the previous turn
	 */
	private int prevEnpassantCol;

	/**
	 * Whether this Move is check
	 */
	protected boolean check;
	/**
	 * Whether this Move is double check
	 */
	protected boolean doublecheck;
	/**
	 * Whether this move is checkmate
	 */
	protected boolean checkmate;

	/**
	 * Whether this move is stalemate
	 */
	protected boolean stalemate;
	/**
	 * Whether this move is a castleQueenside
	 */
	protected boolean castleQueenside;
	/**
	 * Whether this move is a castleKingside
	 */
	protected boolean castleKingside;
	/**
	 * The Move played before this move
	 */
	protected Move prev;
	/**
	 * Whether this Move has been verified as legal.
	 */
	private boolean verified;
	/**
	 * The Result of this move, if this move was the end of the game
	 */
	protected Result result;
	/**
	 * The old time from the white timer
	 */
	private long oldWhiteTime = -1;
	/**
	 * The old time from the black timer
	 */
	private long oldBlackTime = -1;
	/**
	 * The old direction of the white timer
	 */
	private int oldWhiteDirection = 1;
	/**
	 * The old direction of the black timer
	 */
	private int oldBlackDirection = 1;

	/**
	 * The uniqueness of the row and column of this move
	 */
	protected boolean[] unique = { false, false };//Row,Column

	/**
	 * Constructor
	 * @param board The Board on which this Move is to be performed
	 * @param castle The type of castling to be performed
	 * @throws Exception If this move was illegal
	 */
	public Move(Board board, int castle) throws Exception {
		this.board = board;
		switch (castle) {
		case CASTLE_QUEEN_SIDE:
			castleQueenside = true;
			break;
		case CASTLE_KING_SIDE:
			castleKingside = true;
			break;
		}
		if (!board.isLegalMove(this))
			throw new Exception("Illegal Move");
	}

	/**
	 * Constructor
	 * @param board The Board on which this Move is to be performed
	 * @param origin The origin Square for this Move
	 * @param dest The destination Square for this Move
	 * @throws Exception If this move was illegal
	 */
	public Move(Board board, Square origin, Square dest) throws Exception {
		this(board, origin, dest, null);
	}

	/**
	 * Constructor
	 * This constructor should only be explicitly called when reading in from Algebraic Chess
	 * Notation
	 * @param board The Board on which this Move is to be performed
	 * @param origin The origin Square for this Move
	 * @param dest The destination Square for this Move
	 * @param promo The Class to which the moving Piece will be promoted
	 * @throws Exception If this move was illegal
	 */
	public Move(Board board, Square origin, Square dest, Class<?> promo) throws Exception {
		this.board = board;
		this.origin = origin;
		this.setDest(dest);
		this.promo = promo;
		if (!board.isLegalMove(this))
			throw new Exception("Illegal Move");
	}

	/**
	 * Execute the constructed Move.
	 * @throws Exception If this move was illegal
	 */
	public void execute() throws Exception {

		prevEnpassantCol = board.getEnpassantCol();

		if (board.getGame().isClassicChess()) {
			if (castleKingside) {
				if (board.isBlackTurn()) {
					origin = board.getSquare(8, 5);
					setDest(board.getSquare(8, 7));
				} else {
					origin = board.getSquare(1, 5);
					setDest(board.getSquare(1, 7));
				}
			} else if (castleQueenside) {
				if (board.isBlackTurn()) {
					origin = board.getSquare(8, 5);
					setDest(board.getSquare(8, 3));
				} else {
					origin = board.getSquare(1, 5);
					setDest(board.getSquare(1, 3));
				}
			}
		}

		setPiece(origin.getPiece());

		if (!origin.isOccupied())
			throw new Exception("No Piece to Move");//If there is no Piece to Move, get angry.

		if (getPiece().isBlack() != board.isBlackTurn())
			throw new Exception("Not your turn");

		setCaptured(getDest().getPiece());

		if (board.getGame().isClassicChess()) {
			if (origin.getPiece() instanceof Pawn && getCaptured() == null && origin.getCol() != getDest().getCol()) {
				setCaptured(board.getSquare(origin.getRow(), getDest().getCol()).getPiece());
			} else {
				board.setEnpassantCol(Board.NO_ENPASSANT);
			}
		}

		//Take the captured Piece off the board
		if (getCaptured() != null) {
			getCaptured().setCaptured(true);
			Square capSquare = getCaptured().getSquare();
			capSquare.setPiece(null);
		}

		//Only do enpassant and castling for Classic chess.
		if (board.getGame().isClassicChess()) {
			//Mark enpassant on the board
			if (origin.getPiece().getClass() == Pawn.class && Math.abs(origin.getRow() - getDest().getRow()) == 2) {
				board.setEnpassantCol(origin.getCol());
			}

			//Castling
			if (origin.getPiece().getClass() == King.class
					&& origin.getPiece().getMoveCount() == 0) {

				Square rookOrigin;
				Square rookDest;
				//Long
				if (getDest().getCol() == 3) { //c
					rookOrigin = board.getSquare(origin.getRow(), 1); //a
					rookDest = board.getSquare(origin.getRow(), 4); //d
					rookDest.setPiece(rookOrigin.setPiece(null));
					rookDest.getPiece().setMoveCount(rookDest.getPiece().getMoveCount() + 1);
					castleQueenside = true;
				}
				//Short
				else if (getDest().getCol() == 7) { //g
					rookOrigin = board.getSquare(origin.getRow(), 8); //h
					rookDest = board.getSquare(origin.getRow(), 6); //f
					rookDest.setPiece(rookOrigin.setPiece(null));
					rookDest.getPiece().setMoveCount(rookDest.getPiece().getMoveCount() + 1);
					castleKingside = true;
				}
			}
		}

		unique = board.isDestUniqueForClass(getDest(), getPiece());

		getDest().setPiece(origin.setPiece(null));//Otherwise, move the Piece.
		//Clear the list of legal destinations for the piece.
		Piece p = getDest().getPiece();
		p.getLegalDests().clear();
		p.getGuardSquares().clear();
		p.setPinnedBy(null);
		p.setMoveCount(p.getMoveCount() + 1);

		List<Square> promoSquares = (board.isBlackTurn() ? board.getGame().getBlackRules() : board.getGame()
				.getWhiteRules()).getPromotionSquares(p);
		if (promoSquares != null && promoSquares.contains(getDest())) {
			promotion = (board.isBlackTurn() ? board.getGame().getBlackRules() : board.getGame().getWhiteRules())
					.promote(p, isVerified(), promo);
		}

		board.getGame().afterMove(this);

		board.getGame().nextTurn();
		prev = board.getGame().getLastMove();
		if (isVerified() && prev == null) {
			oldWhiteTime = oldBlackTime = -1;
		} else {
			oldWhiteTime = board.getGame().getWhiteTimer().getTime();
			oldBlackTime = board.getGame().getBlackTimer().getTime();
		}
		if (board.getGame().getWhiteTimer() instanceof Word) {
			oldWhiteDirection = board.getGame().getWhiteTimer().getDirection();
			oldBlackDirection = board.getGame().getBlackTimer().getDirection();
		}
		board.getGame().setLastMove(this);

		executed = true;

		board.getGame().setStaleLegalDests(true);

		if (!isVerified()) {
			board.getGame().genLegalDests();
		}

		setVerified(true);
		PlayGame.boardRefresh(board.getGame().getBoards());
	}

	/**
	 * @return the captured
	 */
	public Piece getCaptured() {
		return captured;
	}

	/**
	 * @return the dest
	 */
	public Square getDest() {
		return dest;
	}

	/**
	 * @return the piece
	 */
	public Piece getPiece() {
		return piece;
	}

	/**
	 * @return the removed
	 */
	public Piece getRemoved() {
		return removed;
	}

	/**
	 * Getter method for the Result of this Move
	 * @return The result of this Move
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * Check if this move is check
	 * @return If this move is check
	 */
	public boolean isCheck() {
		return check;
	}

	/**
	 * Check if this move is checkmate
	 * @return If this move is checkmate
	 */
	public boolean isCheckmate() {
		return checkmate;
	}

	/**
	 * Check if this move is double check
	 * @return If this move is double check
	 */
	public boolean isDoubleCheck() {
		return doublecheck;
	}

	/**
	 * Check if this move is the end of the Game
	 * @return If this move is the end of the Game
	 */
	public boolean isEndOfGame() {
		return (checkmate || stalemate || result != null || !result.isUndecided());
	}

	/**
	 * Check if this move is stalemate
	 * @return If this move is stalemate
	 */
	public boolean isStalemate() {
		return stalemate;
	}

	/**
	 * @return the verified
	 */
	public boolean isVerified() {
		return verified;
	}

	/**
	 * Convert a chess piece to it's corresponding character
	 * @param p The piece to convert
	 * @return The character representing this piece
	 */
	public char pieceToChar(Piece p) {
		if (p instanceof Knight)
			return 'N';
		else
			return p.getName().charAt(0);
	}

	/**
	 * @param captured the captured to set
	 */
	public void setCaptured(Piece captured) {
		this.captured = captured;
	}

	/**
	 * Setter method for check
	 * @param b Whether this move is check
	 */
	public void setCheck(boolean b) {
		check = b;
	}

	/**
	 * Setter method for checkmate
	 * @param b Whether this move is checkmate
	 */
	public void setCheckmate(boolean b) {
		checkmate = b;
		if (result == null && checkmate) {
			result = new Result(board.isBlackTurn() ? Result.BLACK_WIN : Result.WHITE_WIN);
		} else {
			result = null;
		}
	}

	/**
	 * @param dest the dest to set
	 */
	public void setDest(Square dest) {
		this.dest = dest;
	}

	/**
	 * Setter method for double check
	 * @param b Whether this move is double check
	 */
	public void setDoubleCheck(boolean b) {
		doublecheck = b;
		if (b) {
			check = b;
		}
	}

	/**
	 * @param piece the piece to set
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	/**
	 * @param removed the removed to set
	 */
	public void setRemoved(Piece removed) {
		this.removed = removed;
	}

	/**
	 * Setter method for the Result of this Move
	 * @param r The result of this Move
	 */
	public void setResult(Result r) {
		result = r;
	}

	/**
	 * Setter method for stalemate
	 * @param b Whether this move is stalemate
	 */
	public void setStalemate(boolean b) {
		stalemate = b;
		if (result == null && stalemate) {
			result = new Result(Result.DRAW);
		} else {
			result = null;
		}
	}

	/**
	 * @param verified the verified to set
	 */
	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	/**
	 * Create a String representing this move in Algebraic Chess Notation
	 * @return The String representation of this Move
	 */
	@Override
	public String toString() {
		String s = "";

		if (castleQueenside) {
			s = "O-O-O";
		} else if (castleKingside) {
			s = "O-O";
		} else {
			s = ((getPiece() != null && !(getPiece() instanceof Pawn)) ? (pieceToChar(getPiece())) + "" : " ")
					+ origin.toString(unique)
					+ ((getCaptured() != null) ? "x" : "")
					+ getDest().toString(new boolean[] { false, false });

			if (promotion != null) {
				s += "=" + pieceToChar(promotion);
			}

			if (checkmate) {
				s += "#";
			} else {
				if (check) {
					s += "+";
				}
				if (doublecheck) {
					s += "+";
				}
			}
		}

		return s;
	}

	/**
	 * Undo the execution of this move
	 */
	public void undo() {
		board.setEnpassantCol(prevEnpassantCol);

		if (board.getGame().isClassicChess()) {
			//Castling
			if (getPiece().getClass() == King.class && getPiece().getMoveCount() == 1) {
				Square rookOrigin;
				Square rookDest;
				//Long
				if (getDest().getCol() == 3) {//c
					rookOrigin = board.getSquare(origin.getRow(), 1);//a
					rookDest = board.getSquare(origin.getRow(), 4);//d
					rookOrigin.setPiece(rookDest.setPiece(null));
					rookOrigin.getPiece().setSquare(rookOrigin);
					rookOrigin.getPiece().setMoveCount(rookOrigin.getPiece().getMoveCount() - 1);
				}
				//Short
				else if (getDest().getCol() == 7) {//g
					rookOrigin = board.getSquare(origin.getRow(), 8);//h
					rookDest = board.getSquare(origin.getRow(), 6);//f
					rookOrigin.setPiece(rookDest.setPiece(null));
					rookOrigin.getPiece().setSquare(rookOrigin);
					rookOrigin.getPiece().setMoveCount(rookOrigin.getPiece().getMoveCount() - 1);
				}
			}
		}

		List<Square> promoSquares = (board.isBlackTurn() ? board.getGame().getBlackRules() : board.getGame()
				.getWhiteRules()).getPromotionSquares(getPiece());
		if (promoSquares != null && promoSquares.contains(getDest())) {
			(board.isBlackTurn() ? board.getGame().getBlackRules() : board.getGame().getWhiteRules())
					.undoPromote(promotion);
		}

		getDest().getPiece().setMoveCount(getDest().getPiece().getMoveCount() - 1);

		origin.setPiece(getDest().setPiece(null));
		getPiece().setSquare(origin);

		//Put any captured Pieces back on the board.
		if (getCaptured() != null) {
			getCaptured().setCaptured(false);
			getCaptured().getSquare().setPiece(getCaptured());
			getCaptured().getLegalDests().clear();
		}

		board.getGame().undoAfterMove(this);
		board.getGame().getWhiteTimer().setTime(oldWhiteTime);
		board.getGame().getBlackTimer().setTime(oldBlackTime);
		if (board.getGame().getWhiteTimer() instanceof Word) {
			board.getGame().getWhiteTimer().setDirection(oldWhiteDirection);
			board.getGame().getBlackTimer().setDirection(oldBlackDirection);
		}
		board.getGame().prevTurn();
		board.getGame().setLastMove(prev);

		executed = false;

		board.getGame().setStaleLegalDests(true);
		PlayGame.boardRefresh(board.getGame().getBoards());
	}
}
