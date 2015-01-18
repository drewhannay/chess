package com.drewhannay.chesscrafter.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChessCoordinateTest {

    public static class EqualsAndHashCode {
        @Test
        public void satisfiesEqualsContract() {
            EqualsVerifier.forClass(ChessCoordinate.class).usingGetClass().verify();
        }
    }

    public static class ToString {
        @Test
        public void printsFriendlyMessage() {
            assertEquals("ChessCoordinate{x=1, y=2}", ChessCoordinate.at(1, 2).toString());
        }
    }
}
