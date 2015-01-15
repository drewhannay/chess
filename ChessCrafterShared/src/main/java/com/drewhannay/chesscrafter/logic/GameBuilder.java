package com.drewhannay.chesscrafter.logic;

import com.drewhannay.chesscrafter.models.*;
import com.drewhannay.chesscrafter.models.turnkeeper.TurnKeeper;
import com.drewhannay.chesscrafter.rules.Rules;
import com.drewhannay.chesscrafter.rules.endconditions.CaptureObjectiveEndCondition;
import com.drewhannay.chesscrafter.rules.legaldestinationcropper.ClassicLegalDestinationCropper;
import com.drewhannay.chesscrafter.rules.legaldestinationcropper.LegalDestinationCropper;
import com.drewhannay.chesscrafter.rules.postmoveaction.PostMoveAction;
import com.drewhannay.chesscrafter.rules.promotionmethods.ClassicPromotionMethod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameBuilder {
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final int BOTH = 3;

    // TODO: remove this eventually
    public static void main(String[] args) {
        buildClassic();
    }

    /**
     * Constructor
     *
     * @param name The name of this Game type
     */
    public GameBuilder(String name) {
        mName = name;
        mWhiteTeam = Lists.newArrayList();
        mBlackTeam = Lists.newArrayList();

        mWhitePromotionMap = Maps.newHashMap();
        mBlackPromotionMap = Maps.newHashMap();
    }

    public GameBuilder(String name, Board[] boards, List<Piece> whiteTeam, List<Piece> blackTeam, Rules whiteRules, Rules blackRules) {
        mName = name;
        mBoards = boards;

        mWhiteTeam = whiteTeam;
        mBlackTeam = blackTeam;
        mWhitePromotionMap = Maps.newHashMap();
        mBlackPromotionMap = Maps.newHashMap();

        mWhiteRules = whiteRules;
        mBlackRules = blackRules;
    }

    public static void setupClassicPawns(Board target, int row, int teamId) {
        for (int x = 1; x <= BoardSize.CLASSIC_SIZE.width; x++)
            target.addPiece(Piece.newPawn(teamId), ChessCoordinate.at(x, row));
    }

    public static void setupClassicPieces(Board target, int row, int teamId) {
        target.addPiece(Piece.newRook(teamId), ChessCoordinate.at(1, row));
        target.addPiece(Piece.newRook(teamId), ChessCoordinate.at(8, row));

        target.addPiece(Piece.newKnight(teamId), ChessCoordinate.at(2, row));
        target.addPiece(Piece.newKnight(teamId), ChessCoordinate.at(7, row));

        target.addPiece(Piece.newBishop(teamId), ChessCoordinate.at(3, row));
        target.addPiece(Piece.newBishop(teamId), ChessCoordinate.at(6, row));

        target.addPiece(Piece.newQueen(teamId), ChessCoordinate.at(4, row));
        target.addPiece(Piece.newKing(teamId), ChessCoordinate.at(5, row));
    }

    @NotNull
    public static Game buildClassic() {
        List<ChessCoordinate> whitePromotionCoordinateList = Lists.newArrayList();
        List<ChessCoordinate> blackPromotionCoordinateList = Lists.newArrayList();
        for (int i = 1; i < 9; i++) {
            whitePromotionCoordinateList.add(ChessCoordinate.at(1, i, 0));
            blackPromotionCoordinateList.add(ChessCoordinate.at(8, i, 0));
        }

        Map<PieceType, Set<PieceType>> promotionMap = Maps.newHashMap();
        promotionMap.put(PieceType.getPawnPieceType(), Sets.newHashSet(
                PieceType.getRookPieceType(),
                PieceType.getKnightPieceType(),
                PieceType.getBishopPieceType(),
                PieceType.getQueenPieceType()
        ));

        Rules whiteRules = new Rules(
                PieceType.getKingPieceType(),
                whitePromotionCoordinateList,
                Rules.DESTINATION_SAME_BOARD,
                Lists.<LegalDestinationCropper>newArrayList(new ClassicLegalDestinationCropper()),
                promotionMap,
                new ClassicPromotionMethod(),
                Collections.<PostMoveAction>emptyList(),
                new CaptureObjectiveEndCondition()
        );
        Rules blackRules = new Rules(
                PieceType.getKingPieceType(),
                blackPromotionCoordinateList,
                Rules.DESTINATION_SAME_BOARD,
                Lists.<LegalDestinationCropper>newArrayList(new ClassicLegalDestinationCropper()),
                promotionMap,
                new ClassicPromotionMethod(),
                Collections.<PostMoveAction>emptyList(),
                new CaptureObjectiveEndCondition()
        );

        Team[] teams = new Team[2];
        teams[0] = new Team(whiteRules);
        teams[1] = new Team(blackRules);

        Board[] boards = new Board[]{new Board(BoardSize.withDimensions(8, 8))};

        return new Game("Classic", boards, teams, TurnKeeper.createClassic(Piece.TEAM_ONE, Piece.TEAM_TWO));
    }

    public Board[] getBoards() {
        return mBoards;
    }

    public void setBoards(Board[] boards) {
        mBoards = boards;
    }

    public void setName(String name) {
        mName = name;
    }

    public void addToPromotionMap(PieceType key, List<PieceType> value, int colorCode) {
        if (colorCode == WHITE || colorCode == BOTH)
            mWhitePromotionMap.put(key, value);
        if (colorCode == BLACK || colorCode == BOTH)
            mBlackPromotionMap.put(key, value);
    }

    public void setWhitePromotionMap(Map<PieceType, List<PieceType>> promotionMap) {
        mWhitePromotionMap.clear();
        mWhitePromotionMap.putAll(promotionMap);
    }

    public void setBlackPromotionMap(Map<PieceType, List<PieceType>> promotionMap) {
        mBlackPromotionMap.clear();
        mBlackPromotionMap.putAll(promotionMap);
    }

    public Map<PieceType, List<PieceType>> getWhitePromotionMap() {
        return mWhitePromotionMap;
    }

    public Map<PieceType, List<PieceType>> getBlackPromotionMap() {
        return mBlackPromotionMap;
    }

    public Rules getWhiteRules() {
        return mWhiteRules;
    }

    public void setWhiteRules(Rules whiteRules) {
        mWhiteRules = whiteRules;
    }

    public Rules getBlackRules() {
        return mBlackRules;
    }

    public void setBlackRules(Rules blackRules) {
        mBlackRules = blackRules;
    }

    public String getName() {
        return mName;
    }

    public List<Piece> getWhiteTeam() {
        return mWhiteTeam;
    }

    public void setWhiteTeam(List<Piece> whiteTeam) {
        mWhiteTeam.clear();
        mWhiteTeam.addAll(whiteTeam);
    }

    public List<Piece> getBlackTeam() {
        return mBlackTeam;
    }

    public void setBlackTeam(List<Piece> blackTeam) {
        mBlackTeam.clear();
        mBlackTeam.addAll(blackTeam);
    }

    private final List<Piece> mWhiteTeam;
    private final List<Piece> mBlackTeam;
    private final Map<PieceType, List<PieceType>> mWhitePromotionMap;
    private final Map<PieceType, List<PieceType>> mBlackPromotionMap;

    private String mName;
    private Board[] mBoards;
    private Rules mWhiteRules;
    private Rules mBlackRules;
}
