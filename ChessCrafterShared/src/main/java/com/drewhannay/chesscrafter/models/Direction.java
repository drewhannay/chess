package com.drewhannay.chesscrafter.models;

import org.jetbrains.annotations.NotNull;

public enum Direction {
    NORTH('n'),
    SOUTH('s'),
    EAST('e'),
    WEST('w'),
    NORTHWEST('f'),
    NORTHEAST('g'),
    SOUTHWEST('a'),
    SOUTHEAST('d');

    private final char mDirection;

    private Direction(char direction) {
        mDirection = direction;
    }

    public char getDirectionChar() {
        return mDirection;
    }

    public ChessCoordinate getFurthestPoint(@NotNull ChessCoordinate start, @NotNull BoardSize boardSize) {
        int x = start.x;
        int y = start.y;

        switch (this) {
            case NORTH:
                return ChessCoordinate.at(x, boardSize.height);
            case SOUTH:
                return ChessCoordinate.at(x, 1);
            case EAST:
                return ChessCoordinate.at(boardSize.width, y);
            case WEST:
                return ChessCoordinate.at(1, y);
            case NORTHEAST: {
                int xDistanceToEdge = boardSize.width - x;
                int yDistanceToEdge = boardSize.height - y;
                int changeInPosition = Math.min(xDistanceToEdge, yDistanceToEdge);
                return ChessCoordinate.at(x + changeInPosition, y + changeInPosition);
            }
            case NORTHWEST: {
                int xDistanceToEdge = x - 1;
                int yDistanceToEdge = boardSize.height - y;
                int changeInPosition = Math.min(xDistanceToEdge, yDistanceToEdge);
                return ChessCoordinate.at(x - changeInPosition, y + changeInPosition);
            }
            case SOUTHEAST: {
                int xDistanceToEdge = boardSize.width - x;
                int yDistanceToEdge = y - 1;
                int changeInPosition = Math.min(xDistanceToEdge, yDistanceToEdge);
                return ChessCoordinate.at(x + changeInPosition, y - changeInPosition);
            }
            case SOUTHWEST: {
                int xDistanceToEdge = x - 1;
                int yDistanceToEdge = y - 1;
                int changeInPosition = Math.min(xDistanceToEdge, yDistanceToEdge);
                return ChessCoordinate.at(x - changeInPosition, y - changeInPosition);
            }
            default:
                throw new IllegalArgumentException("Unknown direction type");
        }
    }
}
