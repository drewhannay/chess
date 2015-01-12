package com.drewhannay.chesscrafter.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChessCoordinateTest {

    public static class EqualsAndHashCode {
        @Test
        public void satisfiesEqualsContract() {
            EqualsVerifier.forClass(ChessCoordinate.class).verify();
        }
    }

    public static class ToString {
        @Test
        public void printsFriendlyMessage() {
            assertEquals("ChessCoordinate{row=1, column=2, boardIndex=1}", ChessCoordinate.at(1, 2, 1).toString());
        }
    }
}