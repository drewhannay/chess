package com.drewhannay.chesscrafter.utility;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GsonUtilityTest {

    public static class GetGson {
        @Test
        public void doesNotReturnNull() {
            assertNotNull(GsonUtility.getGson());
        }
    }
}
