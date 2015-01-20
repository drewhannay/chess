package com.drewhannay.chesscrafter.rules.endconditions;

import com.drewhannay.chesscrafter.logic.Result;
import com.drewhannay.chesscrafter.models.Game;
import org.jetbrains.annotations.NotNull;

public interface EndCondition {
    public Result checkEndCondition(@NotNull Game game);

    public void undo();
}
