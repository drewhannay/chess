package com.drewhannay.chesscrafter.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TwoHopMovementTest {

    public static class Constructor {
        @Test(expected = IllegalArgumentException.class)
        public void throwsWhenPassedNegativeX() {
            TwoHopMovement.with(-1, 2);
        }

        @Test(expected = IllegalArgumentException.class)
        public void throwsWhenPassedNegativeY() {
            TwoHopMovement.with(2, -1);
        }
    }

    public static class EqualsAndHashCode {
        @Test
        public void satisfiesEqualsContract() {
            EqualsVerifier.forClass(TwoHopMovement.class).usingGetClass().verify();
        }

        @Test
        public void returnsTrueForReversedXYOrder() {
            TwoHopMovement movement = TwoHopMovement.with(1, 2);
            TwoHopMovement reverse = TwoHopMovement.with(2, 1);
            assertEquals(movement, reverse);
        }
    }

    public static class ToString {
        @Test
        public void printsFriendlyMessage() {
            assertEquals("TwoHopMovement{x=1, y=2}", TwoHopMovement.with(1, 2).toString());
        }
    }
}
