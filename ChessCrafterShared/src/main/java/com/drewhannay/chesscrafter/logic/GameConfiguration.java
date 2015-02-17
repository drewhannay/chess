package com.drewhannay.chesscrafter.logic;

public final class GameConfiguration {
    public static class TeamConfiguration {
        public int teamId;
        public int teamColor;
        public String teamName;
        public String[] conditionalMoveGenerators;
        public String[] moveFilters;
        public String[] postMoveActions;
        public PiecePromoterConfiguration piecePromoterConfiguration;
        public String endCondition;
    }

    public static class PiecePromoterConfiguration {
        public int promotionRow;
        public String pieceTypeId;
    }

    public static class BoardConfiguration {
        public int width;
        public int height;
        public PieceConfiguration[][] pieces;
    }

    public static class TurnKeeperConfiguration {
        public int[] teamIds;
        public int[] turnCounts;
        public int[] turnIncrements;
    }

    public String name;
    public String internalGameId;
    public BoardConfiguration[] boards;
    public TeamConfiguration[] teams;
    public TurnKeeperConfiguration turnKeeper;
}
