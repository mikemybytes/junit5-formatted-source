package com.mikemybytes.junit5.formatted;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.mikemybytes.junit5.formatted.Preconditions.require;

/**
 * Creates {@link FormatSpecification} for positional argument placeholders. The order of arguments is determined
 * based on their order of appearance. The same (provided) placeholder string is used to represent them.
 */
class PositionalArgumentPlaceholdersFormatAnalyzer implements FormatAnalyzer {

    private final String argumentPlaceholder;

    PositionalArgumentPlaceholdersFormatAnalyzer(String argumentPlaceholder) {
        this.argumentPlaceholder = argumentPlaceholder;
    }

    @Override
    public FormatSpecification analyze(String formatString, int methodParameterCount) {
        List<MatchResult> matchResults = matchFormatArgumentPlaceholders(formatString);

        int formatParameterCount = matchResults.size();
        require(
                methodParameterCount >= formatParameterCount,
                () -> "Number of method arguments is less than the number of format arguments"
        );

        List<Integer> formatArgumentsOrder = IntStream.range(0, formatParameterCount)
                .boxed()
                .collect(Collectors.toList());

        Pattern linePattern = LinePatternFactory.create(formatString, matchResults, formatArgumentsOrder);

        return new FormatSpecification(linePattern, formatArgumentsOrder);
    }

    private List<MatchResult> matchFormatArgumentPlaceholders(String formatString) {
        return Pattern.compile(Pattern.quote(argumentPlaceholder))
                .matcher(formatString)
                .results()
                .collect(Collectors.toList());
    }

}
