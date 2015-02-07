package com.drewhannay.chesscrafter.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardinalMovementTest {
    public static class Constructor {
        @Test(expected = IllegalArgumentException.class)
        public void throwsWhenPassedNegativeDistance() {
            CardinalMovement.with(Direction.EAST, -1);
        }

        @Test(expected = IllegalArgumentException.class)
        public void throwsWhenPassedNullDirection() {
            //noinspection ConstantConditions
            CardinalMovement.with(null, 1);
        }
    }

    public static class EqualsAndHashCode {
        @Test
        public void satisfiesEqualsContract() {
            EqualsVerifier.forClass(CardinalMovement.class).usingGetClass().verify();
        }
    }

    public static class ToString {
        @Test
        public void printsFriendlyMessage() {
            assertEquals("CardinalMovement{EAST:2}", CardinalMovement.with(Direction.EAST, 2).toString());
        }
    }
}
