package com.mikemybytes.junit5.formatted;

class FormatAnalyzers {

    private FormatAnalyzers() {
        // static only
    }

    static FormatAnalyzer from(FormattedSourceData formattedSourceData) {
        if (formattedSourceData.getArgumentPlaceholder().isEmpty()) {
            return new IndexedArgumentPlaceholdersFormatAnalyzer(); // default
        } else {
            String argumentPlaceholder = formattedSourceData.getArgumentPlaceholder().get();
            return new PositionalArgumentPlaceholdersFormatAnalyzer(argumentPlaceholder);
        }
    }

}
