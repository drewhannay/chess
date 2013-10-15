package rules;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;

import logic.Board;
import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Square;
import rules.NextTurn.NextTurnOption;
import rules.ObjectivePiece.ObjectivePieceTypes;

public class Rules implements Serializable
{
	public Rules(boolean isBlack)
	{
		// Initialize everything to classic to ensure nothing can be null.
		mNextTurn = new NextTurn(NextTurnOption.CLASSIC, 1, 1, 0);

		mEndOfGame = EndOfGame.CLASSIC.init(3, Messages.getString("queen"), isBlack); //$NON-NLS-1$

		mCropLegalDests = EnumSet.of(CropLegalDestinations.CLASSIC);
		mObjectivePiece = new ObjectivePiece(ObjectivePieceTypes.CLASSIC);
		mAfterMoves = EnumSet.of(AfterMove.CLASSIC);
		mGetBoard = GetBoard.CLASSIC;
		mPromote = Promote.CLASSIC;
		mGetPromotionSquares = GetPromotionSquares.CLASSIC;
		mAdjustTeamLegalDestinations = AdjustTeamLegalDestinations.CLASSIC;
	}

	public void addAdjustTeamDestinations(AdjustTeamLegalDestinations adjustTeamDestinations)
	{
		mAdjustTeamLegalDestinations = adjustTeamDestinations;
	}

	public void addAfterMove(AfterMove afterMove)
	{
		mAfterMoves.add(afterMove);
	}

	public void addCropLegalDests(CropLegalDestinations cropLegalDests)
	{
		mCropLegalDests.add(cropLegalDests);
	}

	public void clearCropLegalDests()
	{
		mCropLegalDests.clear();
	}

	public void clearAfterMoves()
	{
		mAfterMoves.clear();
	}

	public void addEndOfGame(EndOfGame endOfGame)
	{
		mEndOfGame = endOfGame;
	}

	public EndOfGame getEndOfGame()
	{
		return mEndOfGame;
	}

	public void addGetPromotionSquares(GetPromotionSquares getPromotionSquares)
	{
		mGetPromotionSquares = getPromotionSquares;
	}

	public void addPromote(Promote promote)
	{
		mPromote = promote;
	}

	public void adjustTeamLegalDestinations(List<Piece> team)
	{
		mAdjustTeamLegalDestinations.adjustDestinations(team);
	}

	public void afterMove(Move move)
	{
		for (AfterMove rule : mAfterMoves)
			rule.performAfterMoveAction(move);
	}

	public void cropLegalDests(Piece movingObjectivePiece, Piece pieceToAdjust, List<Piece> enemyTeam)
	{
		for (CropLegalDestinations cropLegalDests : mCropLegalDests)
			cropLegalDests.cropLegalDestinations(movingObjectivePiece, pieceToAdjust, enemyTeam);
	}

	public void checkEndOfGame(Piece objectivePiece)
	{
		mEndOfGame.checkEndOfGame(objectivePiece);
	}

	public Board getBoard(Board startBoard)
	{
		return mGetBoard.getBoard(startBoard);
	}

	public List<Square> getPromotionSquares(Piece toPromote)
	{
		return mGetPromotionSquares.getPromotionSquares(toPromote);
	}

	public boolean nextTurn()
	{
		return mNextTurn.getNextTurn();
	}

	public boolean prevTurn()
	{
		return mNextTurn.undo();
	}

	public Piece objectivePiece(boolean isBlack)
	{
		return mObjectivePiece.getObjectivePiece(isBlack);
	}

	public Piece promote(Piece pieceToPromote, boolean pieceCanBePromoted, String pieceTypeToPromoteFrom)
	{
		return mPromote.promotePiece(pieceToPromote, pieceCanBePromoted, pieceTypeToPromoteFrom);
	}

	public void setGame(Game game)
	{
		mEndOfGame.setGame(game);
		mObjectivePiece.setGame(game);
		mGetBoard.setGame(game);
		// mGetPromotionSquares.setGame(game);
		mPromote.setGame(game);

		for (AfterMove rule : mAfterMoves)
			rule.setGame(game);
	}

	public void setGetBoard(GetBoard getBoard)
	{
		mGetBoard = getBoard;
	}

	public void setNextTurn(NextTurn nextTurn)
	{
		mNextTurn = nextTurn;
	}

	public NextTurn getNextTurn()
	{
		return mNextTurn;
	}

	public void setObjectivePiece(ObjectivePiece objectivePiece)
	{
		mObjectivePiece = objectivePiece;
	}

	public void undoAfterMove(Move move)
	{
		for (AfterMove rule : mAfterMoves)
			rule.undo(move);
	}

	public void undoEndOfGame()
	{
		mEndOfGame.undo();
	}

	public Piece undoPromote(Piece pieceToUnpromote)
	{
		return mPromote.undo(pieceToUnpromote);
	}

	public String getObjectiveName()
	{
		return mObjectivePiece.getObjectivePieceName();
	}

	public boolean rulesAreNetworkable()
	{
		return !(mAfterMoves.contains(AfterMove.CAPTURER_PLACES_CAPTURED) || mAfterMoves.contains(AfterMove.CAPTURER_STEALS_CAPTURED));
	}

	public boolean getCaptureMandatory()
	{
		return mAdjustTeamLegalDestinations.equals(AdjustTeamLegalDestinations.MUST_CAPTURE);
	}

	public boolean getObjectiveIsStationary()
	{
		return mCropLegalDests.contains(CropLegalDestinations.STATIONARY_OBJECTIVE);
	}

	public boolean getCapturedReturnToStart()
	{
		return mAfterMoves.contains(AfterMove.CAPTURED_PIECE_TO_ORIGIN);
	}

	public boolean getPiecesDrop()
	{
		return mAfterMoves.contains(AfterMove.CAPTURER_PLACES_CAPTURED);
	}

	public boolean getPiecesDropAndSwitch()
	{
		return mAfterMoves.contains(AfterMove.CAPTURER_STEALS_CAPTURED);
	}

	public boolean getNoAfterMovesSelected()
	{
		return (!getCapturedReturnToStart()) && (!getPiecesDrop()) && (!getPiecesDropAndSwitch());
	}

	public boolean isAtomic()
	{
		return mAfterMoves.contains(AfterMove.ATOMIC_CAPTURE);
	}

	public boolean switchBoards()
	{
		return mGetBoard.equals(GetBoard.OPPOSITE_BOARD);
	}

	private static final long serialVersionUID = -7895448383101471186L;

	private NextTurn mNextTurn;
	private EndOfGame mEndOfGame;
	private EnumSet<CropLegalDestinations> mCropLegalDests;
	private EnumSet<AfterMove> mAfterMoves;
	private ObjectivePiece mObjectivePiece;
	private GetBoard mGetBoard;
	private Promote mPromote;
	private GetPromotionSquares mGetPromotionSquares;
	private AdjustTeamLegalDestinations mAdjustTeamLegalDestinations;
}
