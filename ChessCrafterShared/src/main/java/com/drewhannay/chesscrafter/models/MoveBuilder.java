package com.drewhannay.chesscrafter.models;

import com.drewhannay.chesscrafter.rules.promotionmethods.PiecePromoter;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class MoveBuilder {

    private final Team mTeam;
    private final Board mBoard;

    private final BoardCoordinate mOrigin;
    private final BoardCoordinate mDestination;

    private PieceType mPromotionType;

    @SuppressWarnings("ConstantConditions")
    MoveBuilder(@NotNull Team team, @NotNull Board board,
                @NotNull BoardCoordinate origin, @NotNull BoardCoordinate destination) {
        Preconditions.checkArgument(origin != null);
        Preconditions.checkArgument(destination != null);

        mTeam = team;
        mBoard = board;
        mOrigin = origin;
        mDestination = destination;
    }

    public MoveBuilder setPromotionType(@Nullable PieceType promotionType) {
        mPromotionType = promotionType;
        return this;
    }

    public boolean hasPromotionType() {
        return mPromotionType != null;
    }

    public boolean needsPromotion() {
        Piece piece = mBoard.getPiece(mOrigin);
        return mTeam.getPiecePromoter().isPiecePromotable(0, mDestination, piece);
    }

    public Set<PieceType> getPromotionOptions() {
        Piece piece = mBoard.getPiece(mOrigin);
        return mTeam.getPiecePromoter().getPromotionOptions(piece);
    }

    public Move build() {
        Piece piece = mBoard.getPiece(mOrigin);

        Preconditions.checkState(mBoard.doesPieceExistAt(mOrigin), "No piece at origin");
        Preconditions.checkState(piece.getTeamId() == mTeam.getTeamId(), "Moving piece belongs to another team");

        PiecePromoter piecePromoter = mTeam.getPiecePromoter();
        if (needsPromotion()) {
            Preconditions.checkState(mPromotionType != null, "No promotion type set");
            Preconditions.checkState(piecePromoter.getPromotionOptions(piece).contains(mPromotionType), "Invalid promotion type");
        }

        return Move.from(mOrigin, mDestination, mPromotionType);
    }
}
