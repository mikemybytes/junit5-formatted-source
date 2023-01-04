package com.mikemybytes.junit5.formatted;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

/**
 * {@code @FormattedSource} is an {@link ArgumentsSource} which reads test case arguments represented as {@link #lines}
 * or {@link #textBlock} in the user-defined {@link #format}.
 *
 * <p>The supplied values will be provided as arguments to the test method annotated with
 * {@link org.junit.jupiter.params.ParameterizedTest}.</p>
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(FormattedSourceArgumentsProvider.class)
public @interface FormattedSource {

    /**
     * The definition of the arguments format. Specific test method arguments must be referenced
     * by their position (starting from zero). E.g. {@code {2}} represents 3rd argument of the test method.
     */
    String format();

    /**
     * Test case input represented as lines in the defined {@link #format}. Each line represents a separate test case
     * of the {@link org.junit.jupiter.params.ParameterizedTest}. Lines must not contain newline characters like
     * {@code \n}.
     */
    String[] lines() default {};

    /**
     * Test case input represented as a single Java Text Block (available since Java 15). Each line represents a
     * separate test case of the {@link org.junit.jupiter.params.ParameterizedTest}. Lines must not contain newline
     * characters like {@code \n}.
     *
     * <p>When running on Java version less than 15, using {@link #lines} is recommended instead.</p>
     */
    String textBlock() default "";

    /**
     * The quote character that could be used to separate argument's value from the rest of the input.
     */
    char quoteCharacter() default '\'';

    /**
     * Allows to ignore (or not) leading and trailing whitespace characters identified in the argument values.
     */
    boolean ignoreLeadingAndTrailingWhitespace() default true;

    /**
     * A list of strings that should be interpreted as {@code null} references.
     *
     * <p>Provided values (e.g. {@code "null"}, {@code "N/A"}, {@code "NONE"}) will be converted to {@code null}
     * references, no matter if quoted ({@link #quoteCharacter()}) or not.</p>
     *
     * <p>Regardless of the value of this attribute, unquoted empty values will always be interpreted as
     * {@code null}.</p>
     */
    String[] nullValues() default {};

}
