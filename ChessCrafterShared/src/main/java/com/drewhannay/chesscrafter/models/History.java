package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.logic.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class History {
    public final String internalGameId;
    public final List<Move> moves;

    private Result mResult;

    public History(@NotNull String internalGameId, @NotNull List<Move> moves) {
        this.internalGameId = internalGameId;
        this.moves = moves;
    }

    public boolean isComplete() {
        return mResult != null;
    }

    @Nullable
    public Result getResult() {
        return mResult;
    }

    void setResult(@Nullable Result result) {
        mResult = result;
    }
}
