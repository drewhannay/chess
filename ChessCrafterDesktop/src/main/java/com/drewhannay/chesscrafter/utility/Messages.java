package com.drewhannay.chesscrafter.utility;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class Messages {
    @NonNls
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("GuiStrings");

    private Messages() {
    }

    public static String getString(@PropertyKey(resourceBundle = "GuiStrings") String key, Object... formatArgs) {
        String value = RESOURCE_BUNDLE.getString(key);

        if (formatArgs.length > 0) {
            return MessageFormat.format(value, formatArgs);
        }
        return value;
    }
}
