package com.mikemybytes.junit5.formatted;

import java.util.function.Supplier;

/**
 * Utility class for expressing various preconditions in the code.
 */
final class Preconditions {

    private Preconditions() {
        // static only
    }

    /**
     * Requires a given boolean condition to be true. Throws {@link IllegalArgumentException} otherwise.
     *
     * @param condition Evaluated boolean condition.
     */
    static void require(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("Unexpected precondition check failure");
        }
    }

    /**
     * Requires a given boolean condition to be true. Throws {@link IllegalArgumentException} otherwise.
     *
     * @param condition Evaluated boolean condition.
     * @param message   Error message to be presented when condition is not met.
     */
    static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Requires a given boolean condition to be true. Throws {@link IllegalArgumentException} otherwise.
     *
     * @param condition Evaluated boolean condition.
     * @param message   Lazy error message to be presented when condition is not met.
     */
    static void require(boolean condition, Supplier<String> message) {
        if (!condition) {
            throw new IllegalArgumentException(message.get());
        }
    }

}
