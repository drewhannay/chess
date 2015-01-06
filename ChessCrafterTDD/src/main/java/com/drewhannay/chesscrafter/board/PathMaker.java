package com.drewhannay.chesscrafter.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathMaker {
    private final BoardCoordinate mOrigin;
    private final BoardCoordinate mDestination;

    public PathMaker(BoardCoordinate origin, BoardCoordinate destination) {
        mOrigin = origin;
        mDestination = destination;
    }

    public List<BoardCoordinate> getPathToDestination() {
        if (mOrigin.isOnSameHorizontalPathAs(mDestination)) {
            return getHorizontalPathSpaces();
        } else if (mOrigin.isOnSameVerticalPathAs(mDestination)) {
            return getVerticalPathSpaces();
        } else if (mOrigin.isOnSameDiagonalPathAs(mDestination)) {
            return getDiagonalPathSpaces();
        } else {
            return Collections.emptyList();
        }
    }

    private List<BoardCoordinate> getHorizontalPathSpaces() {
        int least = Math.min(mOrigin.x, mDestination.x);
        int most = Math.max(mOrigin.x, mDestination.x);

        List<BoardCoordinate> spaces = new ArrayList<>();
        for (int x = least + 1; x <= most; x++) {
            spaces.add(BoardCoordinate.at(x, mOrigin.y));
        }
        return spaces;
    }

    private List<BoardCoordinate> getVerticalPathSpaces() {
        int least = Math.min(mOrigin.y, mDestination.y);
        int most = Math.max(mOrigin.y, mDestination.y);

        List<BoardCoordinate> spaces = new ArrayList<>();
        for (int y = least + 1; y <= most; y++) {
            spaces.add(BoardCoordinate.at(mOrigin.x, y));
        }
        return spaces;
    }

    private List<BoardCoordinate> getDiagonalPathSpaces() {
        int absoluteDistance = Math.abs(mOrigin.x - mDestination.x);
        int xDirection = (mDestination.x - mOrigin.x) / absoluteDistance;
        int yDirection = (mDestination.y - mOrigin.y) / absoluteDistance;

        List<BoardCoordinate> spaces = new ArrayList<>();
        for (int index = 1; index <= absoluteDistance; index++) {
            spaces.add(BoardCoordinate.at(mOrigin.x + index * xDirection, mOrigin.y + index * yDirection));
        }
        return spaces;
    }
}
