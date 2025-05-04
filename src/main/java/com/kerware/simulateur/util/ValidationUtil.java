package com.kerware.modelrefac.util;

public final class ValidationUtil {
    private ValidationUtil() {}

    public static void checkNonNegative(int value, String message) {
        if (value < 0) {throw new IllegalArgumentException(message);}
    }

    public static void checkNotNull(Object obj, String message) {
        if (obj == null) {throw new IllegalArgumentException(message);}
    }
}