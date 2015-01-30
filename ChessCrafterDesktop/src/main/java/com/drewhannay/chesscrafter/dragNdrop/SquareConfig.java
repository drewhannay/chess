package com.drewhannay.chesscrafter.dragNdrop;

import com.drewhannay.chesscrafter.label.SquareJLabel;

import java.util.List;
import java.util.function.Supplier;

public final class SquareConfig {

    private final DropManager mDropManager;
    private final GlassPane mGlassPane;

    public SquareConfig(DropManager dropManager, GlassPane glassPane) {
        mDropManager = dropManager;
        mGlassPane = glassPane;
    }

    public void configureSquare(SquareJLabel square, Supplier<List<SquareJLabel>> highlightSupplier) {
        square.addMouseMotionListener(new MotionAdapter(mGlassPane));
        square.addMouseListener(new SquareListener(mDropManager, mGlassPane, highlightSupplier));
    }
}
