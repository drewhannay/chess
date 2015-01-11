package com.drewhannay.chesscrafter.controllers;

import com.drewhannay.chesscrafter.models.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GameController {
    private GameController() {
    }

    public static void setGame(Game game) {
        sGame = game;
        mDataMap = Maps.newConcurrentMap();
        computeLegalDestinations();
    }

    private static void verifyGameIsSet() {
        Preconditions.checkArgument(sGame != null);
    }

    public static Game getGame() {
        verifyGameIsSet();

        return sGame;
    }

    public static Set<ChessCoordinates> getLegalDestinations(Piece piece) {
        computeLegalDestinations();
        int teamId = piece.getTeamId(sGame);
        return mDataMap.get(teamId).getLegalDests(piece);
    }


    public static void computeLegalDestinations() {
        // Piece[] threats = null;
        // Piece movingObjectivePiece = null;
        // Piece otherObjectivePiece = null;
        // List<Piece> movingTeam = null;
        // List<Piece> otherTeam = null;

        Team[] teams = sGame.getTeams();

        for (int teamIndex = 0; teamIndex < teams.length; teamIndex++) {
            mDataMap.put(teamIndex, new ComputedPieceData(teamIndex));
            for (Piece piece : teams[teamIndex].getPieces()) {
                int boardIndex =
                        teams[teamIndex].getRules() == null ? piece.getCoordinates().boardIndex : teams[teamIndex].getRules().getDestinationBoardIndex(
                                piece.getCoordinates().boardIndex);
                mDataMap.get(teamIndex).computeLegalDestinations(piece, boardIndex);
            }

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
     * @param coordinates    The Square being guarded
     * @param guardTeamIndex The team guarding the Square
     * @return The Pieces guarding the Square
     */
    public static List<Piece> getGuards(ChessCoordinates coordinates, int guardTeamIndex) {
        List<Piece> pieces = sGame.getTeams()[guardTeamIndex].getPieces();
        List<Piece> guards = Lists.newArrayList();

        for (Piece piece : pieces) {
            // TODO: use real ComputedPieceData
            if (mDataMap.get(guardTeamIndex).isGuarding(piece, coordinates))
                guards.add(piece);
        }

        return guards;
    }

    /**
     * Get a count of all legal moves for this turn.
     *
     * @return The number of legal moves this turn.
     */
    public static int getLegalMoveCount() {
        int teamIndex = sGame.getTurnKeeper().getCurrentTeamIndex();
        List<Piece> movingTeam = sGame.getTeams()[teamIndex].getPieces();

        int count = 0;
        for (Piece piece : movingTeam)
            count += mDataMap.get(teamIndex).getLegalDests(piece).size();

        return count;
    }

    private static List<Piece> getThreats(ChessCoordinates threatened, int attackerTeamIndex) {
        List<Piece> pieces = sGame.getTeams()[attackerTeamIndex].getPieces();
        List<Piece> attackers = Lists.newArrayList();

        for (Piece piece : pieces) {
            if (mDataMap.get(attackerTeamIndex).canLegallyAttack(piece, threatened))
                attackers.add(piece);
        }

        return attackers;
    }

    public static boolean isGuarded(ChessCoordinates coordinates, int teamIndex) {
        return getGuards(coordinates, teamIndex) != null;
    }

    public static boolean isThreatened(ChessCoordinates coordinates, int teamIndex) {
        return getThreats(coordinates, teamIndex) != null;
    }

    public static boolean isLegalMove(Piece piece, ChessCoordinates destination) {
        int teamIndex = sGame.getTurnKeeper().getCurrentTeamIndex();

        return mDataMap.get(teamIndex).getLegalDests(piece).contains(destination);
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

        computeLegalDestinations();
    }

    private static Game sGame;
    private static Map<Integer, ComputedPieceData> mDataMap;
}
