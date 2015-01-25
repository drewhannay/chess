package com.drewhannay.chesscrafter.logic;

import java.util.EnumSet;

public enum Status {
    CONTINUE,
    CHECK,
    DOUBLE_CHECK,
    CHECKMATE,
    STALEMATE,
    DRAW;

    public static final EnumSet<Status> END_OF_GAME_STATUS = EnumSet.of(CHECKMATE, DRAW, STALEMATE);
    public static final EnumSet<Status> IN_CHECK_STATUS = EnumSet.of(CHECK, DOUBLE_CHECK);
}
