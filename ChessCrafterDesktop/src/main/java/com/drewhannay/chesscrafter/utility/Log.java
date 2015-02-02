package com.drewhannay.chesscrafter.utility;

public final class Log {
    private Log() {
    }

    private static enum Priority {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        ASSERT
    }

    public static void d(String tag, String message) {
        print(Priority.DEBUG, tag, message);
    }

    public static void e(String tag, String message) {
        print(Priority.ERROR, tag, message);
    }

    private static void print(Priority priority, String tag, String msg) {
        switch (priority) {
            case ERROR:
            case ASSERT:
                System.err.println(tag + ": " + msg);
                break;
            default:
                System.out.println(tag + ": " + msg);
                break;
        }
    }
}
