package com.drewhannay.chesscrafter.board;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PathMakerGetPathToDestinationShould {

    @Test
    public void return7Spaces() {
        PathMaker target = new PathMaker(BoardCoordinate.at(1, 1), BoardCoordinate.at(8, 1));

        assertEquals(7, target.getPathToDestination().size());
    }

    @Test
    public void returnEmptyListFor1_1And2_3() {
        PathMaker target = new PathMaker(BoardCoordinate.at(1, 1), BoardCoordinate.at(2, 3));

        assertTrue(target.getPathToDestination().isEmpty());
    }
}