package com.mikemybytes.junit5.formatted;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

class LinePatternFactory {

    private LinePatternFactory() {
        // static only
    }

    /**
     * Creates {@link Pattern} that could be used to extract argument values out of the given input line.
     *
     * @param formatString                       Format string as defined in one of the annotations
     * @param matchingFormatArgumentPlaceholders {@link MatchResult} of the argument placeholders in the format string
     * @param formatArgumentsOrder               the order of arguments represented as the order of their indexes
     */
    static Pattern create(
            String formatString,
            List<MatchResult> matchingFormatArgumentPlaceholders,
            List<Integer> formatArgumentsOrder) {

        List<String> textParts = tokenize(formatString, matchingFormatArgumentPlaceholders);

        StringBuilder lineRegex = new StringBuilder();
        for (int i = 0; i < textParts.size(); i++) {
            if (!textParts.get(i).isEmpty()) {
                lineRegex.append(Pattern.quote(textParts.get(i)));
            }
            if (i < formatArgumentsOrder.size()) {
                var group = new FormatArgumentMatcherGroup(formatArgumentsOrder.get(i));
                lineRegex.append(group.getRegex());
            }
        }

        return Pattern.compile(lineRegex.toString());
    }

    private static List<String> tokenize(String formatString, List<MatchResult> matchingFormatArgumentPlaceholders) {
        List<String> tokens = new ArrayList<>();

        int startIndex = 0;
        for (var placeholder : matchingFormatArgumentPlaceholders) {
            int endIndex = placeholder.start();
            if (startIndex == endIndex) {
                tokens.add("");
            } else if (startIndex < formatString.length()) {
                var part = formatString.substring(startIndex, endIndex);
                tokens.add(part);
            }
            startIndex = placeholder.end();
        }
        if (startIndex == formatString.length()) {
            tokens.add("");
        } else {
            tokens.add(formatString.substring(startIndex));
        }

        return tokens;
    }

}
