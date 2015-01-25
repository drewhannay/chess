package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.Result;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class History {
    public final String variantName;
    public final List<Move> moves;

    private Result mResult;

    public History(String variantName, List<Move> moves) {
        this.variantName = variantName;
        this.moves = moves;
    }

    @Nullable
    public Result getResult() {
        return mResult;
    }

    void setResult(@Nullable Result result) {
        mResult = result;
    }
}
