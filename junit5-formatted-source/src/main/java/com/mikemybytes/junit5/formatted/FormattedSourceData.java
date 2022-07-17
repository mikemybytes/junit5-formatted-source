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

    static FormattedSourceData from(FormattedSource annotation) {
        List<String> lines = extractLines(annotation.lines(), annotation.textBlock());
        return new FormattedSourceData(annotation.format(), lines, annotation.quoteCharacter());
    }

    static FormattedSourceData from(FormattedSourceTest annotation) {
        List<String> lines = extractLines(annotation.lines(), annotation.textBlock());
        return new FormattedSourceData(annotation.format(), lines, annotation.quoteCharacter());
    }

    private static List<String> extractLines(String[] lines, String textBlock) {
        if (!textBlock.isEmpty()) {
            return textBlock.lines().collect(Collectors.toList());
        } else {
            return Arrays.asList(lines);
        }
    }

    private FormattedSourceData(String formatString, List<String> lines, char quoteCharacter) {
        this.formatString = formatString;
        this.lines = lines;
        this.quoteCharacter = quoteCharacter;
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
}
