package models;

import logic.Result;

public final class Move
{
	public Move(ChessCoordinates origin, ChessCoordinates destination)
	{
		this.mOrigin = origin;
		this.mDestination = destination;
	}

	public ChessCoordinates getOrigin()
	{
		return mOrigin;
	}

	public ChessCoordinates getDestination()
	{
		return mDestination;
	}

	private final ChessCoordinates mOrigin;
	private final ChessCoordinates mDestination;
	private PieceType promotionType;

	/**
	 * Constant to indicate castling queen side
	 */
	public static final int CASTLE_QUEEN_SIDE = 89751354;

	/**
	 * Constant to indicate castling king side
	 */
	public static final int CASTLE_KING_SIDE = 678945613;

	/**
	 * The Move played before this move
	 */
	protected Move prev;

	/**
	 * The old position of the square, if placed by opponent.
	 */
	private ChessCoordinates oldPos;
	/**
	 * The pieces caught up in a potential atomic explosion.
	 */
	private Piece[] exploded;

	/**
	 * The piece being moved
	 */
	private Piece piece;
	/**
	 * The piece to which the moving piece was promoted
	 */
	private Piece promotion;
	/**
	 * The piece which was captured this Move
	 */
	private Piece captured;
	/**
	 * The piece that was completely removed from the Board on this turn. Used
	 * by the AfterMove class
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
	 * Whether this Move has been verified as legal.
	 */
	private boolean verified;
	/**
	 * The Result of this move, if this move was the end of the game
	 */
	public Result result;
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
	protected boolean[] unique = { false, false };// Row,Column

	@Override
	public boolean equals(Object other)
	{
		// TODO: implement this method
		return true;
	}

	@Override
	public int hashCode()
	{
		// TODO implement this method
		return super.hashCode();
	}
}
