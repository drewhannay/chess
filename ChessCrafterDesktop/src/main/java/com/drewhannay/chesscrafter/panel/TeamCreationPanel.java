package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.SquareConfig;
import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeamCreationPanel extends ChessPanel {

    static class TeamInfo {
        int teamId;
        Color color;
        String teamName;

        SquareJLabel label;
    }

    private final List<TeamInfo> mTeamInfos;

    private PieceType mPieceType;

    public TeamCreationPanel(SquareConfig squareConfig) {
        super(false);

        mTeamInfos = new ArrayList<>(2);

        TeamInfo white = new TeamInfo();
        white.teamId = Piece.TEAM_ONE;
        white.color = Color.WHITE;
        white.teamName = "White";

        TeamInfo black = new TeamInfo();
        black.teamId = Piece.TEAM_TWO;
        black.color = Color.BLACK;
        black.teamName = "Black";

        mTeamInfos.add(white);
        mTeamInfos.add(black);

        initComponents(squareConfig);
    }

    public void setPieceType(PieceType pieceType) {
        mPieceType = pieceType;

        mTeamInfos.forEach(teamInfo -> teamInfo.label.setPiece(new Piece(teamInfo.teamId, mPieceType), teamInfo.color));
    }

    private void initComponents(SquareConfig squareConfig) {
        mTeamInfos.forEach(teamInfo -> {
            teamInfo.label = new SquareJLabel(BoardCoordinate.at(1, teamInfo.teamId));
            squareConfig.configureSquare(teamInfo.label);
            add(teamInfo.label);
        });
    }
}
