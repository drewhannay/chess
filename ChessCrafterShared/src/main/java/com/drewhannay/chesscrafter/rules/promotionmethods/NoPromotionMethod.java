package com.drewhannay.chesscrafter.rules.promotionmethods;

import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;

import java.util.Map;
import java.util.Set;

public final class NoPromotionMethod extends PromotionMethod {
    @Override
    public Piece promotePiece(Piece pieceToPromote, Map<PieceType, Set<PieceType>> promotionMap) {
        return pieceToPromote;
    }

    @Override
    public Piece undoPromotion(Piece pieceToDemote) {
        return pieceToDemote;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NoPromotionMethod;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
