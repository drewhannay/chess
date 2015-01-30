package com.drewhannay.chesscrafter.dragNdrop;

import com.drewhannay.chesscrafter.utility.Pair;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Optional;
import java.util.function.Consumer;

public class DropManager extends AbstractDropManager {

    private final Runnable mDropCanceledCallback;
    private final Consumer<Pair<JComponent, JComponent>> mDragDropConsumer;

    public DropManager(@NotNull Runnable refreshCallback,
                       @NotNull Consumer<Pair<JComponent, JComponent>> dragDropConsumer) {
        mDropCanceledCallback = refreshCallback;
        mDragDropConsumer = dragDropConsumer;
    }

    @Override
    public void dropped(DropEvent event, boolean fromDisplayBoard) {
        Optional<? extends JComponent> optional = isInTarget(event.getDropLocation());

        if (optional.isPresent()) {
            JComponent origin = event.getOriginComponent();
            JComponent destination = optional.get();

            mDragDropConsumer.accept(Pair.create(origin, destination));
        } else {
            mDropCanceledCallback.run();
        }

        clearComponentList();
    }
}
