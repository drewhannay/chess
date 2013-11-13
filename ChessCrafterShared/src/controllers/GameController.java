
package controllers;

import java.util.List;
import models.ChessCoordinates;
import models.Game;
import models.Move;
import models.Piece;
import models.Team;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public final class GameController
{
	private GameController()
	{
	}

	public static void setGame(Game game)
	{
		sGame = game;
		computeLegalDestinations();
	}

	private static void verifyGameIsSet()
	{
		Preconditions.checkArgument(sGame != null);
	}

	public static Game getGame()
	{
		verifyGameIsSet();

		return sGame;
	}
	
	public static void computeLegalDestinations()
	{
		// Piece[] threats = null;
		// Piece movingObjectivePiece = null;
		// Piece otherObjectivePiece = null;
		// List<Piece> movingTeam = null;
		// List<Piece> otherTeam = null;

			Team team = sGame.getTeams()[sGame.getTurnKeeper().getCurrentTeamIndex()];
			mData = new ComputedPieceData(sGame.getTurnKeeper().getCurrentTeamIndex());
			for (Piece piece : team.getPieces())
			{
				int boardIndex = team.getRules() == null ? piece.getCoordinates().boardIndex : team.getRules().getDestinationBoardIndex(piece.getCoordinates().boardIndex);
				mData.computeLegalDestinations(piece, boardIndex);
		}

		// TODO: deal with all this stuff
		// movingObjectivePiece = (isBlackMove()) ? mBlackRules.objectivePiece(true) : mWhiteRules.objectivePiece(false);
		// movingTeam = (isBlackMove()) ? getBlackTeam() : getWhiteTeam();
		// otherObjectivePiece = (isBlackMove()) ? mBlackRules.objectivePiece(true) : mWhiteRules.objectivePiece(false);
		// otherTeam = (isBlackMove()) ? getWhiteTeam() : getBlackTeam();
		//
		// // Make sure the objective piece doesn't put himself in check
		// if (movingObjectivePiece != null)
		// (movingObjectivePiece.isBlack() ? getBlackRules() : getWhiteRules()).cropLegalDests(movingObjectivePiece, movingObjectivePiece, movingTeam);
		// if (otherObjectivePiece != null)
		// (otherObjectivePiece.isBlack() ? getBlackRules() : getWhiteRules()).cropLegalDests(otherObjectivePiece, otherObjectivePiece, otherTeam);
		//
		// if (movingObjectivePiece != null)
		// {
		// // Now see if any of the moves puts the objective piece in check and
		// // are therefore illegal
		// for (int i = 0; i < otherTeam.size(); i++)
		// {
		// if (otherTeam.equals(getWhiteTeam()))
		// getWhiteRules().cropLegalDests(movingObjectivePiece, otherTeam.get(i), movingTeam);
		// else
		// getBlackRules().cropLegalDests(movingObjectivePiece, otherTeam.get(i), movingTeam);
		// }
		// }
		//
		// (isBlackMove() ? getBlackRules() : getWhiteRules()).adjustTeamLegalDestinations(movingTeam);
		//
		// // if the objective piece is in check, the legal moves list must be
		// // modified accordingly
		// if (movingObjectivePiece != null && movingObjectivePiece.isInCheck())
		// {
		// if (getLastMove() != null)
		// getLastMove().setCheck(true);
		//
		// threats = getThreats(movingObjectivePiece);
		//
		// switch (threats.length)
		// {
		// case 1:
		// // there is only one threat, so another Piece could block, or
		// // the King could move
		// for (int i = 0; i < movingTeam.size(); i++)
		// movingTeam.get(i).computeLegalDestsToSaveObjective(movingObjectivePiece, threats[0]);
		//
		// break;
		// case 2:
		// // since there is two threats, the objective piece is the only
		// // one who can get himself out of check.
		// for (int i = 0; i < movingTeam.size(); i++)
		// {
		// ComputedPieceData p = (movingTeam.get(i));
		// if (p != movingObjectivePiece)
		// {
		// p.getLegalDests().clear();
		// p.getGuardSquares().clear();
		// }
		// }
		//
		// if (getLastMove() != null)
		// getLastMove().setDoubleCheck(true);
		//
		// break;
		// }
		// }
	}

	/**
	 * Get the Pieces of the specified team guarding the specified Square
	 * 
	 * @param square
	 *            The Square being guarded
	 * @param isBlack
	 *            The team guarding the Square
	 * @return The Pieces guarding the Square
	 */
	public static List<Piece> getGuards(ChessCoordinates coordinates, int guardTeamIndex)
	{
		List<Piece> pieces = sGame.getTeams()[guardTeamIndex].getPieces();
		List<Piece> guards = Lists.newArrayList();

		for (Piece piece : pieces)
		{
			// TODO: use real ComputedPieceData
			if (mData.isGuarding(piece, coordinates))
				guards.add(piece);
		}

		return guards;
	}

	/**
	 * Get a count of all legal moves for this turn.
	 * 
	 * @return The number of legal moves this turn.
	 */
	public int getLegalMoveCount()
	{
		List<Piece> movingTeam = sGame.getTeams()[sGame.getTurnKeeper().getCurrentTeamIndex()].getPieces();

		int count = 0;
		for (Piece piece : movingTeam)
			count += mData.getLegalDests(piece).size();

		return count;
	}

	private static List<Piece> getThreats(ChessCoordinates threatened, int attackerTeamIndex)
	{
		List<Piece> pieces = sGame.getTeams()[attackerTeamIndex].getPieces();
		List<Piece> attackers = Lists.newArrayList();

		for (Piece piece : pieces)
		{
			if (mData.canLegallyAttack(piece, threatened))
				attackers.add(piece);
		}

		return attackers;
	}

	public static boolean isGuarded(ChessCoordinates coordinates, int teamIndex)
	{
		return getGuards(coordinates, teamIndex) != null;
	}

	public static boolean isThreatened(ChessCoordinates coordinates, int teamIndex)
	{
		return getThreats(coordinates, teamIndex) != null;
	}
	
	public static boolean isLegalMove(Piece piece, ChessCoordinates destination)
	{
		return mData.getLegalDests(piece).contains(destination);
	}

	/**
	 * Play a move in the Game
	 * 
	 * @param move
	 *            The Move to play
	 * @throws Exception
	 *             If the Move was illegal
	 */
	public static void playMove(Move move) throws Exception
	{
		MoveController.execute(move);

		// TODO: seems odd that this check would be necessary
		if (sGame.getHistory().contains(move))
			return;

		sGame.getHistory().add(move);

		// TODO: probably need to stop somehow when we hit a valid end condition
		for (Team team : sGame.getTeams())
			team.getRules().checkEndCondition();
		
		computeLegalDestinations();
	}

	private static Game sGame;
	private static ComputedPieceData mData;
}
