package rules;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;

import logic.Board;
import logic.Game;
import logic.Move;
import logic.Piece;
import logic.Square;

public class Rules implements Serializable
{
	public Rules(boolean isBlack)
	{
		// Initialize everything to classic to ensure nothing can be null.
		m_nextTurn = NextTurn.CLASSIC.init(1, 1, 0);

		m_endOfGame = EndOfGame.CLASSIC.init(3, "Queen", isBlack);

		m_cropLegalDests = EnumSet.of(CropLegalDestinations.CLASSIC);
		m_objectivePiece = ObjectivePiece.CLASSIC;
		m_afterMoves = EnumSet.of(AfterMove.CLASSIC);
		m_getBoard = GetBoard.CLASSIC;
		m_promote = Promote.CLASSIC;
		m_getPromotionSquares = GetPromotionSquares.CLASSIC;
		m_adjustTeamLegalDestinations = AdjustTeamLegalDestinations.CLASSIC;
	}

	public void addAdjustTeamDestinations(AdjustTeamLegalDestinations adjustTeamDestinations)
	{
		m_adjustTeamLegalDestinations = adjustTeamDestinations;
	}

	public void addAfterMove(AfterMove afterMove)
	{
		m_afterMoves.add(afterMove);
	}

	public void addCropLegalDests(CropLegalDestinations cropLegalDests)
	{
		m_cropLegalDests.add(cropLegalDests);
	}

	public void addEndOfGame(EndOfGame endOfGame)
	{
		m_endOfGame = endOfGame;
	}

	public EndOfGame getEndOfGame()
	{
		return m_endOfGame;
	}

	public void addGetPromotionSquares(GetPromotionSquares getPromotionSquares)
	{
		m_getPromotionSquares = getPromotionSquares;
	}

	public void addPromote(Promote promote)
	{
		m_promote = promote;
	}

	public void adjustTeamLegalDestinations(List<Piece> team)
	{
		m_adjustTeamLegalDestinations.adjustDestinations(team);
	}

	public void afterMove(Move move)
	{
		for (AfterMove rule : m_afterMoves)
			rule.performAfterMoveAction(move);
	}

	public void cropLegalDests(Piece movingObjectivePiece, Piece pieceToAdjust, List<Piece> enemyTeam)
	{
		for (CropLegalDestinations cropLegalDests : m_cropLegalDests)
			cropLegalDests.cropLegalDestinations(movingObjectivePiece, pieceToAdjust, enemyTeam);
	}

	public void checkEndOfGame(Piece objectivePiece)
	{
		m_endOfGame.checkEndOfGame(objectivePiece);
	}

	public Board getBoard(Board startBoard)
	{
		return m_getBoard.getBoard(startBoard);
	}

	public List<Square> getPromotionSquares(Piece toPromote)
	{
		return m_getPromotionSquares.getPromotionSquares(toPromote);
	}

	public boolean nextTurn()
	{
		return m_nextTurn.getNextTurn();
	}

	public boolean prevTurn()
	{
		return m_nextTurn.undo();
	}

	public Piece objectivePiece(boolean isBlack)
	{
		return m_objectivePiece.getObjectivePiece(isBlack);
	}

	public Piece promote(Piece pieceToPromote, boolean pieceCanBePromoted, String pieceTypeToPromoteFrom)
	{
		return m_promote.promotePiece(pieceToPromote, pieceCanBePromoted, pieceTypeToPromoteFrom);
	}

	public void setGame(Game game)
	{
		m_endOfGame.setGame(game);
		m_objectivePiece.setGame(game);
		m_getBoard.setGame(game);
		m_getPromotionSquares.setGame(game);
		m_promote.setGame(game);

		for (AfterMove rule : m_afterMoves)
			rule.setGame(game);
	}

	public void setGetBoard(GetBoard getBoard)
	{
		m_getBoard = getBoard;
	}

	public void setNextTurn(NextTurn nextTurn)
	{
		m_nextTurn = nextTurn;
	}

	public NextTurn getNextTurn()
	{
		return m_nextTurn;
	}

	public void setObjectivePiece(ObjectivePiece objectivePiece)
	{
		m_objectivePiece = objectivePiece;
	}

	public void undoAfterMove(Move move)
	{
		for (AfterMove rule : m_afterMoves)
			rule.undo(move);
	}

	public void undoEndOfGame()
	{
		m_endOfGame.undo();
	}

	public Piece undoPromote(Piece pieceToUnpromote)
	{
		return m_promote.undo(pieceToUnpromote);
	}

	public String getObjectiveName()
	{
		return m_objectivePiece.getObjectivePieceName();
	}

	public boolean rulesAreNetworkable()
	{
		return !(m_afterMoves.contains(AfterMove.CAPTURER_PLACES_CAPTURED) || m_afterMoves.contains(AfterMove.CAPTURER_STEALS_CAPTURED));
	}

	private static final long serialVersionUID = -7895448383101471186L;

	private NextTurn m_nextTurn;
	private EndOfGame m_endOfGame;
	private EnumSet<CropLegalDestinations> m_cropLegalDests;
	private EnumSet<AfterMove> m_afterMoves;
	private ObjectivePiece m_objectivePiece;
	private GetBoard m_getBoard;
	private Promote m_promote;
	private GetPromotionSquares m_getPromotionSquares;
	private AdjustTeamLegalDestinations m_adjustTeamLegalDestinations;
}
