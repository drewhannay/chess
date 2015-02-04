package com.drewhannay.chesscrafter.rules.promotionmethods;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class PiecePromoter {

    private final Map<PieceType, Set<PieceType>> mPromotionMap;
    private final Map<Integer, Set<BoardCoordinate>> mPromotionCoordinateMap;
    private final Stack<Piece> mPromotionStack;

    public PiecePromoter(@NotNull Map<Integer, Set<BoardCoordinate>> promotionCoordinateMap,
                         @NotNull Map<PieceType, Set<PieceType>> promotionMap) {
        mPromotionCoordinateMap = ImmutableMap.copyOf(promotionCoordinateMap);
        mPromotionMap = ImmutableMap.copyOf(promotionMap);
        mPromotionStack = new Stack<>();
    }

    public static PiecePromoter createClassicPiecePromoter(int promotionRow, @NotNull PieceType pawnType) {
        Set<BoardCoordinate> promotionCoordinates = new HashSet<>();
        for (int i = 1; i <= BoardSize.CLASSIC_SIZE.width; i++) {
            promotionCoordinates.add(BoardCoordinate.at(i, promotionRow));
        }

        Map<Integer, Set<BoardCoordinate>> promotionCoordinateMap = ImmutableMap.of(0, promotionCoordinates);

        Map<PieceType, Set<PieceType>> promotionMap = Maps.newHashMap();
        promotionMap.put(pawnType, Sets.newHashSet(
                PieceTypeManager.getRookPieceType(),
                PieceTypeManager.getKnightPieceType(),
                PieceTypeManager.getBishopPieceType(),
                PieceTypeManager.getQueenPieceType()
        ));

        return new PiecePromoter(promotionCoordinateMap, promotionMap);
    }

    public boolean isPiecePromotable(int boardIndex, @NotNull BoardCoordinate coordinate, @NotNull Piece piece) {
        return getPieceTypeById(piece.getInternalId()) != null && mPromotionCoordinateMap.get(boardIndex).contains(coordinate);
    }

    @NotNull
    public Set<PieceType> getPromotionOptions(@NotNull Piece piece) {
        PieceType pieceType = getPieceTypeById(piece.getInternalId());

        Preconditions.checkState(pieceType != null);

        return mPromotionMap.get(pieceType);
    }

    @NotNull
    public Piece promotePiece(@NotNull Piece pieceToPromote, @NotNull PieceType promotedPieceType) {
        mPromotionStack.push(pieceToPromote);
        return new Piece(pieceToPromote.getTeamId(), promotedPieceType,
                pieceToPromote.isObjectivePiece(), pieceToPromote.getMoveCount());
    }

    @NotNull
    public Piece undoPromotion() {
        Preconditions.checkState(!mPromotionStack.isEmpty());
        return mPromotionStack.pop();
    }

    @Nullable
    private PieceType getPieceTypeById(@NotNull String internalId) {
        for (PieceType pieceType : mPromotionMap.keySet()) {
            if (internalId.equals(pieceType.getInternalId())) {
                return pieceType;
            }
        }
        return null;
    }
}
