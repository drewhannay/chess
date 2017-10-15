package com.drewhannay.chesscrafter.utility;

import org.jetbrains.annotations.Contract;

public final class Log {
    private Log() {
    }

    private enum Priority {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        ASSERT
    }

    public static void v(String tag, String message) {
        print(Priority.VERBOSE, tag, message, null);
    }

    public static void d(String tag, String message) {
        print(Priority.DEBUG, tag, message, null);
    }

    public static void i(String tag, String message) {
        print(Priority.INFO, tag, message, null);
    }

    public static void w(String tag, String message) {
        print(Priority.WARN, tag, message, null);
    }

    public static void e(String tag, String message) {
        print(Priority.ERROR, tag, message, null);
    }

    public static void e(String tag, String message, Throwable e) {
        print(Priority.ERROR, tag, message, e);
    }

    @Contract(value = "_, _ -> fail")
    public static void wtf(String tag, String message) {
        print(Priority.ASSERT, tag, message, null);
    }

    @Contract(value = "_, _, _ -> fail")
    public static void wtf(String tag, String message, Throwable e) {
        print(Priority.ASSERT, tag, message, e);
    }

    private static void print(Priority priority, String tag, String msg, Throwable e) {
        switch (priority) {
            case ERROR:
                System.err.println(tag + ": " + msg);
                if (e != null) {
                    e.printStackTrace();
                }
                break;
            case ASSERT:
                System.err.println(tag + ": " + msg);
                if (e != null) {
                    e.printStackTrace();
                }
                System.exit(-1);
                break;
            default:
                System.out.println(tag + ": " + msg);
                break;
        }
    }
}
