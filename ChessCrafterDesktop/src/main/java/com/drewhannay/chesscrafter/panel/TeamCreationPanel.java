package com.drewhannay.chesscrafter.panel;

import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.dragNdrop.MotionAdapter;
import com.drewhannay.chesscrafter.dragNdrop.SquareListener;
import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class TeamCreationPanel extends ChessPanel {

    static class TeamInfo {
        int teamId;
        Color color;
        String teamName;

        SquareJLabel label;
    }

    private final List<TeamInfo> mTeamInfos;

    private PieceType mPieceType;

    public TeamCreationPanel(DropManager dropManager, GlassPane glassPane,
                             Supplier<List<SquareJLabel>> highlightCallback) {
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

        initComponents(dropManager, glassPane, highlightCallback);
    }

    public void setPieceType(PieceType pieceType) {
        mPieceType = pieceType;

        mTeamInfos.forEach(teamInfo -> teamInfo.label.setPiece(new Piece(teamInfo.teamId, mPieceType)));
    }

    private void initComponents(DropManager dropManager, GlassPane glassPane,
                                Supplier<List<SquareJLabel>> highlightCallback) {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        mTeamInfos.forEach(teamInfo -> {
            teamInfo.label = new SquareJLabel(BoardCoordinate.at(1, 1));
            teamInfo.label.addMouseListener(new SquareListener(dropManager, glassPane, highlightCallback));
            teamInfo.label.addMouseMotionListener(new MotionAdapter(glassPane));
            add(teamInfo.label);
        });
    }
}
