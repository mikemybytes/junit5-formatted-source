package com.mikemybytes.junit5.formatted;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PositionalArgumentPlaceholdersFormatAnalyzerTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "?   | ?                    |   1 | 5",
            "?   | prefix ? ? suffix    |   2 | prefix ABC DEF suffix",
            "<X> | <X> <X> <X> <X> <X>  |   5 | Lorem ipsum dolor sit amet",
            "?   | ''                   |   0 | '' "
    })
    void formatMatchesInput(String argumentPlaceholder, String format, int parameterCount, String matchingInput) {
        FormatSpecification specification = analyzer(argumentPlaceholder).analyze(format, parameterCount);
        assertTrue(specification.getPattern().matcher(matchingInput).matches());
    }

    @Test
    void failsWhenNotEnoughMethodParameters() {
        // given
        String format = "??? ??? ??? ??? ???";
        int parameterCount = 3;
        // when & then
        assertThrows(
                IllegalArgumentException.class,
                () -> analyzer("???").analyze(format, parameterCount)
        );
    }

    /**
     * This ensures that additional special parameters like {@link org.junit.jupiter.api.TestInfo} can still be provided.
     */
    @Test
    void supportsMoreMethodParametersThanArgumentPlaceholders() {
        // given
        String format = "??? ??? ???";
        int parameterCount = 5;
        String input = "a b c";
        // when
        FormatSpecification specification = analyzer("???").analyze(format, parameterCount);
        // then
        assertTrue(specification.getPattern().matcher(input).matches());
    }

    private PositionalArgumentPlaceholdersFormatAnalyzer analyzer(String argumentPlaceholder) {
        return new PositionalArgumentPlaceholdersFormatAnalyzer(argumentPlaceholder);
    }

}