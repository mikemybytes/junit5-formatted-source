package com.mikemybytes.junit5.formatted;

import java.util.Arrays;
import java.util.List;
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
     * Defines whether to ignore leading and trailing whitespaces in argument values.
     */
    private final boolean ignoreWhitespaces;

    static FormattedSourceData from(FormattedSource annotation) {
        List<String> lines = extractLines(annotation.lines(), annotation.textBlock());
        return new FormattedSourceData(
                annotation.format(),
                lines,
                annotation.quoteCharacter(),
                annotation.ignoreLeadingAndTrailingWhitespace()
        );
    }

    static FormattedSourceData from(FormattedSourceTest annotation) {
        List<String> lines = extractLines(annotation.lines(), annotation.textBlock());
        return new FormattedSourceData(
                annotation.format(),
                lines,
                annotation.quoteCharacter(),
                annotation.ignoreLeadingAndTrailingWhitespace()
        );
    }

    private static List<String> extractLines(String[] lines, String textBlock) {
        if (!textBlock.isEmpty()) {
            return textBlock.lines().collect(Collectors.toList());
        } else {
            return Arrays.asList(lines);
        }
    }

    private FormattedSourceData(String formatString, List<String> lines, char quoteCharacter, boolean ignoreWhitespaces) {
        this.formatString = formatString;
        this.lines = lines;
        this.quoteCharacter = quoteCharacter;
        this.ignoreWhitespaces = ignoreWhitespaces;
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

    public boolean isIgnoreWhitespaces() {
        return ignoreWhitespaces;
    }
}
