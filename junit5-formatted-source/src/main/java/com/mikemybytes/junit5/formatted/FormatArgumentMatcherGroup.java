package com.mikemybytes.junit5.formatted;

/**
 * Object representing {@link java.util.regex.Pattern} "named capturing group" of the specific argument. Allows building
 * regular expressions matching specific argument's value.
 */
class FormatArgumentMatcherGroup {

    /**
     * Prefix of the {@link java.util.regex.Pattern} "named capturing group" that will be used for all arguments (in
     * order to contain something more than just a number).
     */
    private static final String GROUP_NAME_PREFIX = "a";

    /**
     * Index of the related argument (starting from zero).
     */
    private final int index;

    FormatArgumentMatcherGroup(int index) {
        this.index = index;
    }

    /**
     * Returns name of the "named capturing group" for the associated argument, allowing its value to be matched.
     */
    String getName() {
        return GROUP_NAME_PREFIX + index;
    }

    /**
     * Returns regular expression representing argument's "named capturing group".
     */
    String getRegex() {
        return "(?<a" + index + ">.*)";
    }

}
