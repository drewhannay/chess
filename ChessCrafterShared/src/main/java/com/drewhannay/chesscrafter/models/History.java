package com.drewhannay.chesscrafter.models;

import java.util.List;

public final class History {
    public final String variantName;
    public final List<Move> moves;

    public History(String variantName, List<Move> moves) {
        this.variantName = variantName;
        this.moves = moves;
    }
}
