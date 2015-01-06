package com.drewhannay.chesscrafter.board;

import org.junit.Test;

public class BoardConstructorShould {

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnNegativeBoardSize() {
        new Board(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnZeroBoardSize() {
        new Board(0);
    }
}