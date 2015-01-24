package com.drewhannay.chesscrafter.rules.promotionmethods;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PiecePromoter_Given_ClassicRules_Should {

    PiecePromoter mWhiteTarget;
    PiecePromoter mBlackTarget;
    Piece mWhitePawn;
    Piece mBlackPawn;

    @Before
    public void setUp() {
        mWhiteTarget = PiecePromoter.createClassicPiecePromoter(8, PieceTypeManager.getNorthFacingPawnPieceType());
        mWhitePawn = Piece.newNorthFacingPawn(Piece.TEAM_ONE);

        mBlackTarget = PiecePromoter.createClassicPiecePromoter(1, PieceTypeManager.getSouthFacingPawnPieceType());
        mBlackPawn = Piece.newSouthFacingPawn(Piece.TEAM_TWO);
    }

    public static class IsPiecePromotable extends PiecePromoter_Given_ClassicRules_Should {
        @Test
        public void allowWhitePawnPromotionAt1_8() {
            assertTrue(mWhiteTarget.isPiecePromotable(0, BoardCoordinate.at(1, 8), mWhitePawn));
        }

        @Test
        public void allowWhitePawnPromotionAt8_8() {
            assertTrue(mWhiteTarget.isPiecePromotable(0, BoardCoordinate.at(8, 8), mWhitePawn));
        }

        @Test
        public void notAllowWhitePawnPromotionAt1_7() {
            assertFalse(mWhiteTarget.isPiecePromotable(0, BoardCoordinate.at(1, 7), mWhitePawn));
        }

        @Test
        public void notAllowKnightPromotion() {
            assertFalse(mWhiteTarget.isPiecePromotable(0, BoardCoordinate.at(1, 8), Piece.newKnight(Piece.TEAM_ONE)));
        }

        @Test
        public void allowBlackPawnPromotionAt8_1() {
            assertTrue(mBlackTarget.isPiecePromotable(0, BoardCoordinate.at(8, 1), mBlackPawn));
        }

        @Test
        public void allowBlackPawnPromotionAt1_1() {
            assertTrue(mBlackTarget.isPiecePromotable(0, BoardCoordinate.at(1, 1), mBlackPawn));
        }

        @Test
        public void notAllowBlackPawnPromotionAt1_8() {
            assertFalse(mBlackTarget.isPiecePromotable(0, BoardCoordinate.at(1, 8), mBlackPawn));
        }
    }

    public static class GetPromotionOptions extends PiecePromoter_Given_ClassicRules_Should {
        @Test
        public void notReturnKingAsPromotionOption() {
            assertFalse(mWhiteTarget.getPromotionOptions(mWhitePawn).contains(PieceTypeManager.getKingPieceType()));
        }

        @Test
        public void returnQueenAsPromotionOption() {
            assertTrue(mWhiteTarget.getPromotionOptions(mWhitePawn).contains(PieceTypeManager.getQueenPieceType()));
        }

        @Test
        public void returnFourPromotionOptions() {
            assertEquals(4, mWhiteTarget.getPromotionOptions(mWhitePawn).size());
        }

        @Test(expected = IllegalStateException.class)
        public void throwExceptionWhenPassedInvalidPiece() {
            mWhiteTarget.getPromotionOptions(Piece.newKnight(Piece.TEAM_ONE));
        }
    }

    public static class PromotePiece extends PiecePromoter_Given_ClassicRules_Should {
        @Test
        public void returnQueenWhenPromotingToQueen() {
            Piece queen = mWhiteTarget.promotePiece(mWhitePawn, PieceTypeManager.getQueenPieceType());
            assertEquals(PieceTypeManager.getQueenPieceType().getName(), queen.getName());
        }

        @Test
        public void returnPromotedPieceWithSameMoveCountAsOriginalPiece() {
            mWhitePawn.incrementMoveCount();
            mWhitePawn.incrementMoveCount();
            Piece queen = mWhiteTarget.promotePiece(mWhitePawn, PieceTypeManager.getQueenPieceType());
            assertEquals(mWhitePawn.getMoveCount(), queen.getMoveCount());
        }
    }

    public static class UndoPromotion extends PiecePromoter_Given_ClassicRules_Should {
        @Test
        public void returnOriginalPieceAfterUndoingPromotion() {
            mWhiteTarget.promotePiece(mWhitePawn, PieceTypeManager.getQueenPieceType());
            Piece demotedPiece = mWhiteTarget.undoPromotion();
            assertEquals(mWhitePawn, demotedPiece);
        }

        @Test(expected = IllegalStateException.class)
        public void throwExceptionWhenUndoIsCalledBeforePromoting() {
            mWhiteTarget.undoPromotion();
        }
    }
}
