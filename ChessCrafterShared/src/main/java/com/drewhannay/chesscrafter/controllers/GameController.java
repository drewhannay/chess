package com.drewhannay.chesscrafter.controllers;

import com.drewhannay.chesscrafter.models.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GameController {
    private GameController() {
    }

    public static void setGame(Game game) {
        sGame = game;
    }

    private static void verifyGameIsSet() {
        Preconditions.checkArgument(sGame != null);
    }

    public static Game getGame() {
        verifyGameIsSet();

        return sGame;
    }

    public static Set<ChessCoordinate> getLegalDestinations(Piece piece) {
        //computeLegalDestinations();
        //int teamId = piece.getTeamId(sGame);
        //return mDataMap.get(teamId).getLegalDests(piece);
        int boardIndex = piece.getCoordinates().boardIndex;
        BoardSize size = getGame().getBoards()[boardIndex].getBoardSize();
        mLegalDestinations = Sets.newHashSet(piece.getPieceType().getMovesFrom(piece.getCoordinates(), size));
        return mLegalDestinations;
    }

    /**
     * Get the Pieces of the specified team guarding the specified Square
     *
     * @param coordinates    The Square being guarded
     * @param guardTeamIndex The team guarding the Square
     * @return The Pieces guarding the Square
     */
    public static List<Piece> getGuards(ChessCoordinate coordinates, int guardTeamIndex) {
        List<Piece> pieces = sGame.getTeams()[guardTeamIndex].getPieces();
        List<Piece> guards = Lists.newArrayList();

        /*for (Piece piece : pieces) {
            // TODO: use updated methods
            if (mDataMap.get(guardTeamIndex).isGuarding(piece, coordinates))
                guards.add(piece);
        }*/

        return guards;
    }

    /**
     * Get a count of all legal moves for this turn.
     *
     * @return The number of legal moves this turn.
     */
    public static int getLegalMoveCount() {
        //int teamIndex = sGame.getTurnKeeper().getCurrentTeamIndex();
       // List<Piece> movingTeam = sGame.getTeams()[teamIndex].getPieces();

        //int count = 0;
        //for (Piece piece : movingTeam)
        //    count += mDataMap.get(teamIndex).getLegalDests(piece).size();

        return mLegalDestinations.size();
    }

    private static List<Piece> getThreats(ChessCoordinate threatened, int attackerTeamIndex) {
        List<Piece> pieces = sGame.getTeams()[attackerTeamIndex].getPieces();
        List<Piece> attackers = Lists.newArrayList();

        /*for (Piece piece : pieces) {
            @TODO Use updated methods
            if (mDataMap.get(attackerTeamIndex).canLegallyAttack(piece, threatened))
                attackers.add(piece);
        }*/

        return attackers;
    }

    public static boolean isGuarded(ChessCoordinate coordinates, int teamIndex) {
        return getGuards(coordinates, teamIndex) != null;
    }

    public static boolean isThreatened(ChessCoordinate coordinates, int teamIndex) {
        return getThreats(coordinates, teamIndex) != null;
    }

    public static boolean isLegalMove(Piece piece, ChessCoordinate destination) {
        //int teamIndex = sGame.getTurnKeeper().getCurrentTeamIndex();

        //return mDataMap.get(teamIndex).getLegalDests(piece).contains(destination);
        return mLegalDestinations.contains(destination);
    }

    /**
     * Play a move in the Game
     *
     * @param move The Move to play
     * @throws Exception If the Move was illegal
     */
    public static void playMove(Move move) throws Exception {
        MoveController.execute(move);

        // TODO: seems odd that this check would be necessary
//		if (sGame.getHistory().contains(move))
//			return;

        sGame.getHistory().add(move);

        // TODO: probably need to stop somehow when we hit a valid end condition
        for (Team team : sGame.getTeams())
            team.getRules().checkEndCondition();

        // TODO: We're just using this for it's side effect of changing the team index. Do we ever need to store it?
        sGame.getTurnKeeper().getTeamIndexForNextTurn();
    }

    private static Game sGame;
    private static Set<ChessCoordinate> mLegalDestinations;
}
