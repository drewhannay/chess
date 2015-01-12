package com.drewhannay.chesscrafter.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardSizeTest {
    public static class Constructor {
        @Test(expected = IllegalArgumentException.class)
        public void throwsWhenPassedZeroWidth() {
            BoardSize.withDimensions(0, 8);
        }

        @Test(expected = IllegalArgumentException.class)
        public void throwsWhenPassedZeroHeight() {
            BoardSize.withDimensions(8, 0);
        }

        @Test(expected = IllegalArgumentException.class)
        public void throwsWhenPassedNegativeWidth() {
            BoardSize.withDimensions(-1, 8);
        }

        @Test(expected = IllegalArgumentException.class)
        public void throwsWhenPassedNegativeHeight() {
            BoardSize.withDimensions(8, -1);
        }
    }

    public static class EqualsAndHashCode {
        @Test
        public void satisfiesEqualsContract() {
            EqualsVerifier.forClass(BoardSize.class).verify();
        }
    }

    public static class ToString {
        @Test
        public void printsFriendlyMessage() {
            assertEquals("BoardSize{width=8, height=9}", BoardSize.withDimensions(8, 9).toString());
        }
    }
}