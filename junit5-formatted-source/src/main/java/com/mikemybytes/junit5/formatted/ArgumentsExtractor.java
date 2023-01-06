package com.mikemybytes.junit5.formatted;

import org.junit.jupiter.params.provider.Arguments;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mikemybytes.junit5.formatted.Preconditions.require;

/**
 * Extracts test arguments from the provided input.
 */
class ArgumentsExtractor {

    private final FormattedSourceData sourceData;
    private final FormatSpecification formatSpecification;
    private final RawArgumentsProcessor rawArgumentsProcessor;

    ArgumentsExtractor(
            FormattedSourceData sourceData,
            FormatSpecification formatSpecification,
            RawArgumentsProcessor rawArgumentsProcessor) {
        this.sourceData = sourceData;
        this.formatSpecification = formatSpecification;
        this.rawArgumentsProcessor = rawArgumentsProcessor;
    }

    Arguments extract(String line) {
        var formatMatcher = formatSpecification.getPattern().matcher(line);

        require(formatMatcher.matches(), "Input does not match the expected format");

        List<String> args = formatSpecification.getArgumentsOrder().stream()
                .map(argIndex -> new ArgumentIndexMatcher(argIndex, new FormatArgumentMatcherGroup(argIndex)))
                .sorted(Comparator.comparing(ArgumentIndexMatcher::getIndex))
                .map(aim -> formatMatcher.group(aim.getMatcherGroup().getName()))
                .map(this::processArgumentValue)
                .collect(Collectors.toList());

        List<Object> processedArgs = rawArgumentsProcessor.apply(line, args);

        return Arguments.of(processedArgs.toArray());
    }

    private String processArgumentValue(String rawValue) {
        require(rawValue != null, "Argument's raw value can't be null");

        String value = rawValue;
        if (sourceData.isIgnoreWhitespaces()) {
            value = value.strip();
        }

        if (value.isEmpty()) {
            // interpreting unquoted empty value as null just like @CsvSource does
            return null;
        }

        String quote = "" + sourceData.getQuoteCharacter();
        if (value.startsWith(quote) && value.endsWith(quote)) {
            value = value.substring(1, value.length() - 1);
        }

        if (sourceData.getNullValues().contains(value)) {
            return null;
        }

        if(value.isEmpty()) {
            return sourceData.getEmptyValue();
        }

        return value;
    }

    /**
     * Represents a tuple containing argument's index (position, starting from zero) and it's respective
     * {@link FormatArgumentMatcherGroup}, to allow capturing its value.
     */
    private static class ArgumentIndexMatcher {
        private final int index;
        private final FormatArgumentMatcherGroup matcherGroup;

        public ArgumentIndexMatcher(int index, FormatArgumentMatcherGroup matcherGroup) {
            this.index = index;
            this.matcherGroup = matcherGroup;
        }

        public int getIndex() {
            return index;
        }

        public FormatArgumentMatcherGroup getMatcherGroup() {
            return matcherGroup;
        }

    }

}
