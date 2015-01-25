package com.drewhannay.chesscrafter.logic;

public final class Result {

    public final Status status;
    public final Integer winningTeamId;

    public Result(Status status, Integer winningTeamId) {
        this.winningTeamId = winningTeamId;
        this.status = status;
    }
}
