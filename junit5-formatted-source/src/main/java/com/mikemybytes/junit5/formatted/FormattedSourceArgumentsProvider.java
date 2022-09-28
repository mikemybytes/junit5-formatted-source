package com.mikemybytes.junit5.formatted;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.stream.Stream;

import static com.mikemybytes.junit5.formatted.Preconditions.require;

/**
 * {@code FormattedSourceArgumentsProvider} is an {@link ArgumentsProvider} implementation capable of extracting
 * argument values from {@link FormattedSource} annotation.
 */
class FormattedSourceArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<FormattedSource> {

    private FormattedSourceData sourceData;

    @Override
    public void accept(FormattedSource annotation) {
        sourceData = FormattedSourceData.from(annotation);
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        require(sourceData != null);

        int expectedParameterCount = context.getRequiredTestMethod().getParameterCount();
        FormatSpecification specification = new FormatAnalyzer().analyze(sourceData.getFormatString(), expectedParameterCount);
        var argumentsExtractor = new ArgumentsExtractor(sourceData, specification, RawArgumentsProcessor.passThrough());
        return sourceData.getLines().stream()
                .map(argumentsExtractor::extract);
    }

}
