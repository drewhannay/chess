package com.drewhannay.chesscrafter.dragNdrop;

import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.utility.Pair;
import com.drewhannay.chesscrafter.utility.RunnableOfT;
import org.jetbrains.annotations.NotNull;

public class DropManager extends AbstractDropManager {

    private final Runnable mRefreshCallback;
    private final RunnableOfT<Pair<BoardCoordinate, BoardCoordinate>> mPlayMoveCallback;

    public DropManager(@NotNull Runnable refreshCallback,
                       @NotNull RunnableOfT<Pair<BoardCoordinate, BoardCoordinate>> playMoveCallback) {
        mRefreshCallback = refreshCallback;
        mPlayMoveCallback = playMoveCallback;
    }

    @Override
    public void dropped(DropEvent event, boolean fromDisplayBoard) {
        SquareJLabel originSquareLabel = (SquareJLabel) event.getOriginComponent();
        SquareJLabel destinationSquareLabel = (SquareJLabel) isInTarget(event.getDropLocation());

        clearComponentList();

        if (destinationSquareLabel == null) {
            mRefreshCallback.run();
            return;
        }

        BoardCoordinate origin = originSquareLabel.getCoordinates();
        BoardCoordinate destination = destinationSquareLabel.getCoordinates();

        mPlayMoveCallback.run(Pair.create(origin, destination));
    }
}
