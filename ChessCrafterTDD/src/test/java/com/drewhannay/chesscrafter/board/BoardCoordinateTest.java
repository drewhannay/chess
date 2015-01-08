package com.drewhannay.chesscrafter.board;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BoardCoordinateTest {

    public static class IsCoordinateValidForBoardSize {

        @Test
        public void returnsFalseWhenComparedWithString() {
            BoardCoordinate coordinate = BoardCoordinate.at(1, 1);
            Object obj = "Not a coordinate";
            assertFalse(coordinate.equals(obj));
        }

        @Test
        public void returnsFalseForXLessThanZero() {
            BoardCoordinate coordinate = BoardCoordinate.at(-12, 2);
            assertFalse(coordinate.isCoordinateValidForBoardSize(5));
        }

        @Test
        public void returnsFalseForYLessThanZero() {
            BoardCoordinate coordinate = BoardCoordinate.at(1, -23);
            assertFalse(coordinate.isCoordinateValidForBoardSize(Board.DEFAULT_BOARD_SIZE));
        }
    }

    public static class EqualsAndHashcode {
        @Test
        public void satisfiesEqualsContract() {
            EqualsVerifier.forClass(BoardCoordinate.class).verify();
        }
    }

    public static class ToString {
        @Test
        public void printsFriendlyMessage() {
            assertEquals("BoardCoordinate{x=1, y=2}", BoardCoordinate.at(1, 2).toString());
        }
    }
}