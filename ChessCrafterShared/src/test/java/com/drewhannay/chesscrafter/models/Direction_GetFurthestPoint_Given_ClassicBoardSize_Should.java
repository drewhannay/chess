package com.drewhannay.chesscrafter.models;

import org.junit.Before;
import org.junit.Test;

import static com.drewhannay.chesscrafter.models.Direction.*;
import static org.junit.Assert.assertEquals;

public class Direction_GetFurthestPoint_Given_ClassicBoardSize_Should {

    BoardSize mBoardSize;

    @Before
    public void setUp() {
        mBoardSize = BoardSize.withDimensions(8, 8);
    }

    public static class North extends Direction_GetFurthestPoint_Given_ClassicBoardSize_Should {
        @Test
        public void returns4_8Given4_8() {
            ChessCoordinate point = NORTH.getFurthestPoint(ChessCoordinate.at(4, 8), mBoardSize);
            assertEquals(ChessCoordinate.at(4, 8), point);
        }

        @Test
        public void returns2_8Given2_2() {
            ChessCoordinate point = NORTH.getFurthestPoint(ChessCoordinate.at(2, 2), mBoardSize);
            assertEquals(ChessCoordinate.at(2, 8), point);
        }
    }

    public static class South extends Direction_GetFurthestPoint_Given_ClassicBoardSize_Should {
        @Test
        public void returns4_1Given4_1() {
            ChessCoordinate point = SOUTH.getFurthestPoint(ChessCoordinate.at(4, 1), mBoardSize);
            assertEquals(ChessCoordinate.at(4, 1), point);
        }

        @Test
        public void returns2_1Given2_2() {
            ChessCoordinate point = SOUTH.getFurthestPoint(ChessCoordinate.at(2, 2), mBoardSize);
            assertEquals(ChessCoordinate.at(2, 1), point);
        }
    }

    public static class East extends Direction_GetFurthestPoint_Given_ClassicBoardSize_Should {
        @Test
        public void returns8_4Given8_4() {
            ChessCoordinate point = EAST.getFurthestPoint(ChessCoordinate.at(8, 4), mBoardSize);
            assertEquals(ChessCoordinate.at(8, 4), point);
        }

        @Test
        public void returns8_2Given2_2() {
            ChessCoordinate point = EAST.getFurthestPoint(ChessCoordinate.at(2, 2), mBoardSize);
            assertEquals(ChessCoordinate.at(8, 2), point);
        }
    }

    public static class West extends Direction_GetFurthestPoint_Given_ClassicBoardSize_Should {
        @Test
        public void returns1_4Given1_4() {
            ChessCoordinate point = WEST.getFurthestPoint(ChessCoordinate.at(1, 4), mBoardSize);
            assertEquals(ChessCoordinate.at(1, 4), point);
        }

        @Test
        public void returns1_2Given2_2() {
            ChessCoordinate point = WEST.getFurthestPoint(ChessCoordinate.at(2, 2), mBoardSize);
            assertEquals(ChessCoordinate.at(1, 2), point);
        }
    }

    public static class Northeast extends Direction_GetFurthestPoint_Given_ClassicBoardSize_Should {
        @Test
        public void return6_8Given1_3() {
            ChessCoordinate point = NORTHEAST.getFurthestPoint(ChessCoordinate.at(1, 3), mBoardSize);
            assertEquals(ChessCoordinate.at(6, 8), point);
        }

        @Test
        public void return8_6Given3_1() {
            ChessCoordinate point = NORTHEAST.getFurthestPoint(ChessCoordinate.at(3, 1), mBoardSize);
            assertEquals(ChessCoordinate.at(8, 6), point);
        }

        @Test
        public void return8_8Given8_8() {
            ChessCoordinate point = NORTHEAST.getFurthestPoint(ChessCoordinate.at(8, 8), mBoardSize);
            assertEquals(ChessCoordinate.at(8, 8), point);
        }
    }

    public static class Northwest extends Direction_GetFurthestPoint_Given_ClassicBoardSize_Should {
        @Test
        public void return1_8Given5_4() {
            ChessCoordinate point = NORTHWEST.getFurthestPoint(ChessCoordinate.at(5, 4), mBoardSize);
            assertEquals(ChessCoordinate.at(1, 8), point);
        }

        @Test
        public void return1_5Given3_3() {
            ChessCoordinate point = NORTHWEST.getFurthestPoint(ChessCoordinate.at(3, 3), mBoardSize);
            assertEquals(ChessCoordinate.at(1, 5), point);
        }

        @Test
        public void return6_8Given7_7() {
            ChessCoordinate point = NORTHWEST.getFurthestPoint(ChessCoordinate.at(7, 7), mBoardSize);
            assertEquals(ChessCoordinate.at(6, 8), point);
        }
    }

    public static class Southeast extends Direction_GetFurthestPoint_Given_ClassicBoardSize_Should {
        @Test
        public void return8_8Given8_8() {
            ChessCoordinate point = SOUTHEAST.getFurthestPoint(ChessCoordinate.at(8, 8), mBoardSize);
            assertEquals(ChessCoordinate.at(8, 8), point);
        }

        @Test
        public void return1_1Given2_3() {
            ChessCoordinate point = SOUTHEAST.getFurthestPoint(ChessCoordinate.at(2, 3), mBoardSize);
            assertEquals(ChessCoordinate.at(4, 1), point);
        }

        @Test
        public void return8_6Given7_7() {
            ChessCoordinate point = SOUTHEAST.getFurthestPoint(ChessCoordinate.at(7, 7), mBoardSize);
            assertEquals(ChessCoordinate.at(8, 6), point);
        }
    }

    public static class Southwest extends Direction_GetFurthestPoint_Given_ClassicBoardSize_Should {
        @Test
        public void return1_1Given1_1() {
            ChessCoordinate point = SOUTHWEST.getFurthestPoint(ChessCoordinate.at(1, 1), mBoardSize);
            assertEquals(ChessCoordinate.at(1, 1), point);
        }

        @Test
        public void return2_1Given4_3() {
            ChessCoordinate point = SOUTHWEST.getFurthestPoint(ChessCoordinate.at(4, 3), mBoardSize);
            assertEquals(ChessCoordinate.at(2, 1), point);
        }

        @Test
        public void return1_2Given2_3() {
            ChessCoordinate point = SOUTHWEST.getFurthestPoint(ChessCoordinate.at(2, 3), mBoardSize);
            assertEquals(ChessCoordinate.at(1, 2), point);
        }
    }
}