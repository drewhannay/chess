package com.drewhannay.chesscrafter.models;

import com.google.common.base.Preconditions;

import java.util.Objects;

public final class BoardSize {
    public static final BoardSize CLASSIC_SIZE = BoardSize.withDimensions(8, 8);

    public final int width;
    public final int height;

    private BoardSize(int width, int height) {
        Preconditions.checkArgument(width > 0, "Width must be > 0");
        Preconditions.checkArgument(height > 0, "Height must be > 0");

        this.width = width;
        this.height = height;
    }

    public static BoardSize withDimensions(int width, int height) {
        return new BoardSize(width, height);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        BoardSize other = (BoardSize) obj;
        return width == other.width &&
                height == other.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public String toString() {
        return "BoardSize{width=" + width + ", height=" + height + "}";
    }
}
