package com.drewhannay.chesscrafter.utility;

@FunctionalInterface
public interface RunnableOfT<T> {
    void run(T t);
}
