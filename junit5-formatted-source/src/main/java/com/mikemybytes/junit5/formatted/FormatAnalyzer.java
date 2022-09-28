package com.mikemybytes.junit5.formatted;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.mikemybytes.junit5.formatted.Preconditions.require;

/**
 * Analyzes given format string (defined within {@link FormattedSource#format} or {@link FormattedSourceTest#format}),
 * in order to build a {@link FormatSpecification} that could be then used to match specific test argument values.
 */
class FormatAnalyzer {

    private static final Pattern formatArgumentPlaceholderPattern = Pattern.compile("\\{(\\d+)}");

    FormatSpecification analyze(String formatString, int methodParameterCount) {
        List<MatchResult> matchResults = matchFormatArgumentPlaceholders(formatString);

        List<Integer> formatArgumentsOrder = extractTemplateArguments(matchResults, methodParameterCount);
        Pattern linePattern = prepareLinePattern(formatString, matchResults, formatArgumentsOrder);

        return new FormatSpecification(linePattern, formatArgumentsOrder);
    }

    private List<MatchResult> matchFormatArgumentPlaceholders(String formatString) {
        return formatArgumentPlaceholderPattern.matcher(formatString)
                .results()
                .collect(Collectors.toList());
    }

    private List<Integer> extractTemplateArguments(
            List<MatchResult> matchingFormatArgumentPlaceholders,
            int methodParameterCount) {
        List<Integer> templateArguments = matchingFormatArgumentPlaceholders.stream()
                .map(r -> {
                    require(r.groupCount() == 1);
                    return Integer.valueOf(r.group(1));
                })
                .collect(Collectors.toList());

        int formatParameterCount = templateArguments.size();

        require(
                methodParameterCount >= formatParameterCount,
                () -> "Number of method arguments is less than the number of format arguments"
        );

        List<Integer> expectedIndexes = IntStream.range(0, formatParameterCount)
                .boxed()
                .collect(Collectors.toList());

        boolean validArguments = new HashSet<>(templateArguments).containsAll(expectedIndexes)
                && templateArguments.size() == expectedIndexes.size();
        require(
                validArguments,
                () -> "Arguments provided in the format string are invalid: expected " + expectedIndexes
                        + " but got " + templateArguments.stream().sorted().collect(Collectors.toList()));

        return templateArguments;
    }

    private Pattern prepareLinePattern(
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

    private List<String> tokenize(String formatString, List<MatchResult> matchingFormatArgumentPlaceholders) {
        List<String> tokens = new ArrayList<>();

        int startIndex = 0;
        for (var placeholder : matchingFormatArgumentPlaceholders) {
            int endIndex = placeholder.start();
            if (startIndex == endIndex) {
                tokens.add("");
            } else if (startIndex < formatString.length()) {
                var part = formatString.substring(startIndex, endIndex).strip();
                tokens.add(part);
            }
            startIndex = placeholder.end();
        }
        if (startIndex == formatString.length()) {
            tokens.add("");
        }

        return tokens;
    }

}
