package com.mikemybytes.junit5.formatted;

import org.junit.jupiter.api.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Defines method of processing raw arguments coming from the provided test case input string.
 */
interface RawArgumentsProcessor extends BiFunction<String, List<String>, List<Object>> {

    /**
     * Simple, pass through processor, that does nothing with the raw test case arguments.
     */
    static RawArgumentsProcessor passThrough() {
        return (rawInput, rawArguments) -> new ArrayList<>(rawArguments);
    }

    /**
     * Arguments processor wrapping the first argument of the test method with {@link Named} in order to customize
     * test case name. See the documentation of {@link FormattedSourceTest} for more information.
     */
    static RawArgumentsProcessor testCaseName() {
        return (rawInput, rawArguments) -> {
            Preconditions.require(!rawArguments.isEmpty());

            List<Object> namedArgs = new ArrayList<>(rawArguments);
            namedArgs.set(0, Named.of(rawInput, namedArgs.get(0)));
            return namedArgs;
        };
    }

}
