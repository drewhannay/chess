package com.drewhannay.chesscrafter.models;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTHEAST,
    NORTHWEST,
    SOUTHEAST,
    SOUTHWEST;

    public static final EnumSet<Direction> ADJACENT_DIRECTIONS = EnumSet.of(NORTH, SOUTH, EAST, WEST);
    public static final EnumSet<Direction> DIAGONAL_DIRECTIONS = EnumSet.of(NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST);

    public BoardCoordinate getFurthestPoint(@NotNull BoardCoordinate start, @NotNull BoardSize boardSize) {
        int x = start.x;
        int y = start.y;

        switch (this) {
            case NORTH:
                return BoardCoordinate.at(x, boardSize.height);
            case SOUTH:
                return BoardCoordinate.at(x, 1);
            case EAST:
                return BoardCoordinate.at(boardSize.width, y);
            case WEST:
                return BoardCoordinate.at(1, y);
            case NORTHEAST: {
                int xDistanceToEdge = boardSize.width - x;
                int yDistanceToEdge = boardSize.height - y;
                int changeInPosition = Math.min(xDistanceToEdge, yDistanceToEdge);
                return BoardCoordinate.at(x + changeInPosition, y + changeInPosition);
            }
            case NORTHWEST: {
                int xDistanceToEdge = x - 1;
                int yDistanceToEdge = boardSize.height - y;
                int changeInPosition = Math.min(xDistanceToEdge, yDistanceToEdge);
                return BoardCoordinate.at(x - changeInPosition, y + changeInPosition);
            }
            case SOUTHEAST: {
                int xDistanceToEdge = boardSize.width - x;
                int yDistanceToEdge = y - 1;
                int changeInPosition = Math.min(xDistanceToEdge, yDistanceToEdge);
                return BoardCoordinate.at(x + changeInPosition, y - changeInPosition);
            }
            case SOUTHWEST: {
                int xDistanceToEdge = x - 1;
                int yDistanceToEdge = y - 1;
                int changeInPosition = Math.min(xDistanceToEdge, yDistanceToEdge);
                return BoardCoordinate.at(x - changeInPosition, y - changeInPosition);
            }
            default:
                throw new IllegalArgumentException("Unknown direction type");
        }
    }
}
