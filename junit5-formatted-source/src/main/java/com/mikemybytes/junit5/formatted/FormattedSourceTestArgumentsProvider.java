package com.mikemybytes.junit5.formatted;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.stream.Stream;

import static com.mikemybytes.junit5.formatted.Preconditions.require;

/**
 * {@code FormattedSourceArgumentsProvider} is an {@link ArgumentsProvider} implementation capable of extracting
 * argument values from {@link FormattedSourceTest} annotation.
 */
class FormattedSourceTestArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<FormattedSourceTest> {

    private FormattedSourceData sourceData;

    @Override
    public void accept(FormattedSourceTest annotation) {
        sourceData = FormattedSourceData.from(annotation);
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        require(sourceData != null);

        FormatSpecification specification = new FormatAnalyzer().analyze(context, sourceData.getFormatString());
        var processor = new ArgumentsExtractor(sourceData, specification, RawArgumentsProcessor.testCaseName());
        return sourceData.getLines().stream()
                .map(processor::extract);
    }
}
