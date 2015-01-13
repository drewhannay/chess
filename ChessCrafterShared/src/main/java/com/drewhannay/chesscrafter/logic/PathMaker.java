package com.drewhannay.chesscrafter.logic;

import com.drewhannay.chesscrafter.models.ChessCoordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathMaker {
    private final ChessCoordinate mOrigin;
    private final ChessCoordinate mDestination;

    public PathMaker(ChessCoordinate origin, ChessCoordinate destination) {
        mOrigin = origin;
        mDestination = destination;
    }

    public List<ChessCoordinate> getPathToDestination(int maxSteps) {
        if (mOrigin.isOnSameHorizontalPathAs(mDestination)) {
            return getHorizontalPathSpaces(maxSteps);
        } else if (mOrigin.isOnSameVerticalPathAs(mDestination)) {
            return getVerticalPathSpaces(maxSteps);
        } else if (mOrigin.isOnSameDiagonalPathAs(mDestination)) {
            return getDiagonalPathSpaces(maxSteps);
        } else {
            return Collections.emptyList();
        }
    }

    private List<ChessCoordinate> getHorizontalPathSpaces(int maxSteps) {
        int least = Math.min(mOrigin.x, mDestination.x);
        int most = Math.max(mOrigin.x, mDestination.x);

        List<ChessCoordinate> spaces = new ArrayList();
        for (int x = least + 1, steps = 1; x <= most && steps <= maxSteps; x++, steps++) {
            spaces.add(ChessCoordinate.at(x, mOrigin.y));
        }
        return spaces;
    }

    private List<ChessCoordinate> getVerticalPathSpaces(int maxSteps) {
        int least = Math.min(mOrigin.y, mDestination.y);
        int most = Math.max(mOrigin.y, mDestination.y);

        List<ChessCoordinate> spaces = new ArrayList();
        for (int y = least + 1, steps = 1; y <= most && steps <= maxSteps; y++, steps++) {
            spaces.add(ChessCoordinate.at(mOrigin.x, y));
        }
        return spaces;
    }

    private List<ChessCoordinate> getDiagonalPathSpaces(int maxSteps) {
        int absoluteDistance = Math.abs(mOrigin.x - mDestination.x);
        int xDirection = (mDestination.x - mOrigin.x) / absoluteDistance;
        int yDirection = (mDestination.y - mOrigin.y) / absoluteDistance;

        List<ChessCoordinate> spaces = new ArrayList();
        for (int index = 1; index <= absoluteDistance && index <= maxSteps; index++) {
            spaces.add(ChessCoordinate.at(mOrigin.x + index * xDirection, mOrigin.y + index * yDirection));
        }
        return spaces;
    }
}