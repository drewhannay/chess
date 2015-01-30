package com.drewhannay.chesscrafter.dragNdrop;

import com.drewhannay.chesscrafter.label.SquareJLabel;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public final class SquareConfig {

    private final DropManager mDropManager;
    private final GlassPane mGlassPane;
    private final Supplier<List<SquareJLabel>> mHighlightSupplier;

    private boolean mHideIcon;

    public SquareConfig(DropManager dropManager, GlassPane glassPane) {
        this(dropManager, glassPane, null);
    }

    public SquareConfig(DropManager dropManager, GlassPane glassPane,
                        @Nullable Supplier<List<SquareJLabel>> highlightSupplier) {
        mDropManager = dropManager;
        mGlassPane = glassPane;
        mHighlightSupplier = highlightSupplier;

        mHideIcon = true;
    }

    public void setHideIcon(boolean hideIcon) {
        mHideIcon = hideIcon;
    }

    public void configureSquare(SquareJLabel square) {
        Preconditions.checkState(mHighlightSupplier != null);

        configure(square, mHighlightSupplier);
    }

    public void configureSquare(SquareJLabel square, Supplier<List<SquareJLabel>> highlightSupplier) {
        Preconditions.checkState(mHighlightSupplier == null);

        configure(square, highlightSupplier);
    }

    private void configure(SquareJLabel square, Supplier<List<SquareJLabel>> highlightSupplier) {
        square.addMouseMotionListener(new MotionAdapter(mGlassPane));
        square.addMouseListener(new SquareListener(mDropManager, mGlassPane, mHideIcon, highlightSupplier));
    }
}
