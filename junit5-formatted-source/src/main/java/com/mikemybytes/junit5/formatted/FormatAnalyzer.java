package com.mikemybytes.junit5.formatted;

/**
 * Analyzes given format string (defined within {@link FormattedSource#format} or {@link FormattedSourceTest#format}),
 * in order to build a {@link FormatSpecification} that could be then used to match specific test argument values.
 */
interface FormatAnalyzer {

    FormatSpecification analyze(String formatString, int methodParameterCount);

}
