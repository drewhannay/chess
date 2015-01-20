package com.drewhannay.chesscrafter.logic;

import com.drewhannay.chesscrafter.models.BoardCoordinate;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathMaker_GetPathToDestination_Should {
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

    @Test
    public void notReturn1_1Given1_1And8_1() {
        PathMaker target = new PathMaker(BoardCoordinate.at(1, 1), BoardCoordinate.at(8, 1));
        List<BoardCoordinate> moves = target.getPathToDestination();
        assertFalse(moves.contains(BoardCoordinate.at(1, 1)));
    }

    @Test
    public void returnOrderedWestPath() {
        PathMaker target = new PathMaker(BoardCoordinate.at(8, 6), BoardCoordinate.at(1, 6));
        List<BoardCoordinate> moves = target.getPathToDestination();
        assertEquals(BoardCoordinate.at(1, 6), moves.get(moves.size() - 1));
    }

    @Test
    public void returnOrderedEastPath() {
        PathMaker target = new PathMaker(BoardCoordinate.at(1, 6), BoardCoordinate.at(8, 6));
        List<BoardCoordinate> moves = target.getPathToDestination();
        assertEquals(BoardCoordinate.at(8, 6), moves.get(moves.size() - 1));
    }

    @Test
    public void returnOrderedNorthPath() {
        PathMaker target = new PathMaker(BoardCoordinate.at(4, 1), BoardCoordinate.at(4, 8));
        List<BoardCoordinate> moves = target.getPathToDestination();
        assertEquals(BoardCoordinate.at(4, 8), moves.get(moves.size() - 1));
    }

    @Test
    public void returnOrderedSouthPath() {
        PathMaker target = new PathMaker(BoardCoordinate.at(4, 8), BoardCoordinate.at(4, 1));
        List<BoardCoordinate> moves = target.getPathToDestination();
        assertEquals(BoardCoordinate.at(4, 1), moves.get(moves.size() - 1));
    }

    @Test
    public void returnOrderedNorthwestPath() {
        PathMaker target = new PathMaker(BoardCoordinate.at(8, 1), BoardCoordinate.at(1, 8));
        List<BoardCoordinate> moves = target.getPathToDestination();
        assertEquals(BoardCoordinate.at(1, 8), moves.get(moves.size() - 1));
    }

    @Test
    public void returnOrderedSoutheastPath() {
        PathMaker target = new PathMaker(BoardCoordinate.at(1, 8), BoardCoordinate.at(8, 1));
        List<BoardCoordinate> moves = target.getPathToDestination();
        assertEquals(BoardCoordinate.at(8, 1), moves.get(moves.size() - 1));
    }

    @Test
    public void returnOrderedNortheastPath() {
        PathMaker target = new PathMaker(BoardCoordinate.at(1, 1), BoardCoordinate.at(8, 8));
        List<BoardCoordinate> moves = target.getPathToDestination();
        assertEquals(BoardCoordinate.at(8, 8), moves.get(moves.size() - 1));
    }

    @Test
    public void returnOrderedSouthwestPath() {
        PathMaker target = new PathMaker(BoardCoordinate.at(8, 8), BoardCoordinate.at(1, 1));
        List<BoardCoordinate> moves = target.getPathToDestination();
        assertEquals(BoardCoordinate.at(1, 1), moves.get(moves.size() - 1));
    }
}
