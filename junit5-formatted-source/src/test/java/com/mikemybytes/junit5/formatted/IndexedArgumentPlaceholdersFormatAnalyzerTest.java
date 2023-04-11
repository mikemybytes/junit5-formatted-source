package com.mikemybytes.junit5.formatted;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class IndexedArgumentPlaceholdersFormatAnalyzerTest {

    private final IndexedArgumentPlaceholdersFormatAnalyzer analyzer = new IndexedArgumentPlaceholdersFormatAnalyzer();

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            // format              | params | matchingInput
            "{0}                   |      1 | 1",
            "prefix {0} {1} suffix |      2 | prefix ABC DEF suffix",
            "{0} {1} {2} {3} {4}   |      5 | Lorem ipsum dolor sit amet",
            "''                    |      0 | '' "
    })
    void formatMatchesInput(String format, int parameterCount, String matchingInput) {
        FormatSpecification specification = analyzer.analyze(format, parameterCount);
        assertTrue(specification.getPattern().matcher(matchingInput).matches());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            // format    | params | argumentsOrder
            "{0}         |      1 | [0]",
            "{0} {1} {2} |      3 | [0,1,2]",
            "{2} {0} {1} |      3 | [2,0,1]"
    })
    void recognizesArgumentsOrder(
            String format,
            int parameterCount,
            @ConvertWith(ListIntArgumentConverter.class) List<Integer> argumentsOrder) {
        FormatSpecification specification = analyzer.analyze(format, parameterCount);
        assertEquals(argumentsOrder, specification.getArgumentsOrder());
    }

    @Test
    void failsWhenNotEnoughMethodParameters() {
        // given
        String format = "{0} {1} {2} {3} {4}";
        int parameterCount = 3;
        // when & then
        assertThrows(IllegalArgumentException.class, () -> analyzer.analyze(format, parameterCount));
    }

    /**
     * This ensures that additional special parameters like {@link org.junit.jupiter.api.TestInfo} can still be provided.
     */
    @Test
    void supportsMoreMethodParametersThanArgumentPlaceholders() {
        // given
        String format = "{0} {1} {2}";
        int parameterCount = 5;
        String input = "a b c";
        // when
        FormatSpecification specification = analyzer.analyze(format, parameterCount);
        // then
        assertTrue(specification.getPattern().matcher(input).matches());
    }

    @Test
    void failsOnInvalidArgumentPlaceholder() {
        // given
        String format = "{4}";
        int parameterCount = 1;
        // when & then
        assertThrows(IllegalArgumentException.class, () -> analyzer.analyze(format, parameterCount));
    }

    static class ListIntArgumentConverter extends SimpleArgumentConverter {

        @Override
        protected Object convert(Object source, Class<?> targetType) {
            assertEquals(List.class, targetType, "Can only convert to List");
            var noBrackets = String.valueOf(source).replaceAll("[\\[\\]]", "");
            return Arrays.stream(noBrackets.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        }

    }

}