package com.drewhannay.chesscrafter.rules.promotionmethods;

import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;

import java.util.Map;
import java.util.Set;

public abstract class PromotionMethod {
    public abstract Piece promotePiece(Piece pieceToPromote, Map<PieceType, Set<PieceType>> promotionMap);

    public abstract Piece undoPromotion(Piece pieceToDemote);
}
