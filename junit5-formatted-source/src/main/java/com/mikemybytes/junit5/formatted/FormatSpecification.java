package com.mikemybytes.junit5.formatted;

import java.util.List;
import java.util.regex.Pattern;

class FormatSpecification {

    private final Pattern pattern;
    private final List<Integer> argumentsOrder;

    FormatSpecification(Pattern pattern, List<Integer> argumentsOrder) {
        this.pattern = pattern;
        this.argumentsOrder = argumentsOrder;
    }

    Pattern getPattern() {
        return pattern;
    }

    List<Integer> getArgumentsOrder() {
        return argumentsOrder;
    }

}
