package com.drewhannay.chesscrafter.rules.endconditions;

import com.drewhannay.chesscrafter.logic.Status;
import com.drewhannay.chesscrafter.models.Game;
import org.jetbrains.annotations.NotNull;

public abstract class EndCondition {
    public abstract Status checkEndCondition(@NotNull Game game);

    public abstract void undo();

    public static EndCondition from(@NotNull String name, int teamId) {
        switch (name) {
            case CaptureObjectiveEndCondition.NAME:
                return new CaptureObjectiveEndCondition(teamId);
        }

        throw new IllegalArgumentException("Unknown EndCondition name:" + name);
    }
}
