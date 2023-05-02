package com.mikemybytes.junit5.formatted;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Shared container for parameter values of both {@link FormattedSource} and {@link FormattedSourceTest} annotations.
 * Represents specific test configuration without any processing (raw values).
 */
class FormattedSourceData {

    /**
     * Format string of the related test.
     */
    private final String formatString;
    /**
     * Test input lines.
     */
    private final List<String> lines;
    /**
     * Character used for quoting test arguments.
     */
    private final char quoteCharacter;

    /**
     * Fixed placeholder string to be used instead of the default indexed syntax.
     */
    private final String argumentPlaceholder;

    /**
     * Defines whether to ignore leading and trailing whitespaces in argument values.
     */
    private final boolean ignoreWhitespaces;
    /**
     * A set of strings that should be interpreted as {@code null} references.
     */
    private final Set<String> nullValues;

    /**
     * A value used to substitute quoted empty strings.
     */
    private final String emptyValue;

    static FormattedSourceData from(FormattedSource annotation) {
        List<String> lines = extractLines(annotation.lines(), annotation.textBlock());
        return new FormattedSourceData(
                annotation.format(),
                lines,
                annotation.quoteCharacter(),
                annotation.argumentPlaceholder(),
                annotation.ignoreLeadingAndTrailingWhitespace(),
                toSet(annotation.nullValues()),
                annotation.emptyValue()
        );
    }

    static FormattedSourceData from(FormattedSourceTest annotation) {
        List<String> lines = extractLines(annotation.lines(), annotation.textBlock());
        return new FormattedSourceData(
                annotation.format(),
                lines,
                annotation.quoteCharacter(),
                annotation.argumentPlaceholder(),
                annotation.ignoreLeadingAndTrailingWhitespace(),
                toSet(annotation.nullValues()),
                annotation.emptyValue()
        );
    }

    private static List<String> extractLines(String[] lines, String textBlock) {
        if (!textBlock.isEmpty()) {
            return textBlock.lines().collect(Collectors.toList());
        } else {
            return toList(lines);
        }
    }

    private static List<String> toList(String[] array) {
        return array != null ? Arrays.asList(array) : Collections.emptyList();
    }

    private static Set<String> toSet(String[] array) {
        return Set.copyOf(toList(array));
    }

    private FormattedSourceData(
            String formatString,
            List<String> lines,
            char quoteCharacter,
            String argumentPlaceholder,
            boolean ignoreWhitespaces,
            Set<String> nullValues,
            String emptyValue) {
        this.formatString = formatString;
        this.lines = lines;
        this.quoteCharacter = quoteCharacter;
        this.argumentPlaceholder = argumentPlaceholder;
        this.ignoreWhitespaces = ignoreWhitespaces;
        this.nullValues = nullValues;
        this.emptyValue = emptyValue;
    }

    String getFormatString() {
        return formatString;
    }

    List<String> getLines() {
        return lines;
    }

    char getQuoteCharacter() {
        return quoteCharacter;
    }

    Optional<String> getArgumentPlaceholder() {
        if (argumentPlaceholder == null || argumentPlaceholder.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(argumentPlaceholder);
    }

    public boolean isIgnoreWhitespaces() {
        return ignoreWhitespaces;
    }

    public Set<String> getNullValues() {
        return nullValues;
    }

    public String getEmptyValue() {
        return emptyValue;
    }
}
