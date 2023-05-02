package com.mikemybytes.junit5.formatted;

import java.util.HashSet;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.mikemybytes.junit5.formatted.Preconditions.require;

/**
 * Creates {@link FormatSpecification} for indexed argument placeholders. Every argument is represented as {@code {x}},
 * where {@code x} is its index (counting from zero). For example, {@code {0}} represents the first argument, while
 * {@code {3}} represents the fourth one.
 * <p>
 * Inspired by the <a href="https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-display-names">JUnit 5 convention used for customizing display names</a>.
 * </p>
 */
class IndexedArgumentPlaceholdersFormatAnalyzer implements FormatAnalyzer {

    private static final Pattern formatArgumentPlaceholderPattern = Pattern.compile("\\{(\\d+)}");

    @Override
    public FormatSpecification analyze(String formatString, int methodParameterCount) {
        List<MatchResult> matchResults = matchFormatArgumentPlaceholders(formatString);

        List<Integer> formatArgumentsOrder = extractTemplateArguments(matchResults, methodParameterCount);
        Pattern linePattern = LinePatternFactory.create(formatString, matchResults, formatArgumentsOrder);

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
                        + " but got " + templateArguments.stream().sorted().collect(Collectors.toList())
        );

        return templateArguments;
    }

}
