package com.mikemybytes.junit5.formatted;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

/**
 * {@code @FormattedSourceTest} combines {@link ParameterizedTest} annotation with the behaviour of {@link FormattedSource}
 * in order to reduce code verbosity. Additionally, it automatically sets the test case name (controlled via
 * {@link ParameterizedTest#name}) to the formatted input string.
 *
 * The following {@code @FormattedSourceTest} annotation:
 * <pre class="code">
 * {@literal @}FormattedSourceTest(format = "{0} + {1} = {2}", lines = {
 *     "3 + 4 = 7",
 *     "7 + 1 = 8"
 * })
 * void calculatesSum(int x, int y, int expectedSum) {
 *     // ...
 * }
 * </pre>
 * is equivalent to the following combination of {@link ParameterizedTest} and {@link FormattedSource}:
 * <pre class="code">
 * {@literal @}ParameterizedTest(name = "{0} + {1} = {2}")
 * {@literal @}FormattedSource(format = "{0} + {1} = {2}", lines = {
 *     "3 + 4 = 7",
 *     "7 + 1 = 8"
 * })
 * void calculatesSum(int x, int y, int expectedSum) {
 *     // ...
 * }
 * </pre>
 * Please note, that in both cases test cases names will be (respectively): {@code 3 + 4 = 7} and {@code 7 + 1 = 8}.
 *
 * <p>As JUnit 5 {@link ParameterizedTest} doesn't allow to wrap {@link org.junit.jupiter.params.provider.Arguments}
 * with {@link org.junit.jupiter.api.Named}, the value of the first argument (the only one that always has to be present)
 * is being wrapped with {@link org.junit.jupiter.api.Named} containing the whole formatted input string. Then, it is
 * being used as a test case name via {@code @ParameterizedTest(name = "{0}")}.</p>
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ParameterizedTest(name = "{0}")
@ArgumentsSource(FormattedSourceTestArgumentsProvider.class)
public @interface FormattedSourceTest {

    /**
     * The definition of the arguments format. By default, specific test method arguments must be referenced
     * by their position (starting from zero). E.g. {@code {2}} represents the 3rd argument of the test method.
     * <p>Setting {@link #argumentPlaceholder()} disables the default behavior, allowing to use a fixed placeholder
     * string instead. As there's no braces (curly brackets) escaping, switching to the fixed argument placeholder
     * allows using them in the format string.</p>
     */
    String format();

    /**
     * Test case input represented as lines in the defined {@link #format}. Each line represents a separate test case
     * of the {@link org.junit.jupiter.params.ParameterizedTest}. Lines must not contain newline characters like
     * {@code \n}.
     *
     * <p>Defaults to an empty string. Note: the test case input must be supplied either via {@link #lines()} or
     * {@link #textBlock()}.</p>
     */
    String[] lines() default {};

    /**
     * Test case input represented as a single Java Text Block (available since Java 15). Each line represents a
     * separate test case of the {@link org.junit.jupiter.params.ParameterizedTest}. Lines must not contain newline
     * characters like {@code \n}.
     *
     * <p>When running on Java version less than 15, using {@link #lines} is recommended instead.</p>
     *
     * <p>Defaults to an empty string. Note: the test case input must be supplied either via {@link #lines()} or
     * {@link #textBlock()}.</p>
     */
    String textBlock() default "";

    /**
     * The quote character that could be used to separate argument's value from the rest of the input.
     * As there's no escaping support, a different quote character should be chosen in case of a conflict.
     *
     * <p>Defaults to a single quote ({@code '}).</p>
     */
    char quoteCharacter() default '\'';

    /**
     * Specifies fixed argument placeholder string that should be used instead of the default indexed syntax.
     * Each placeholder's occurrence corresponds to the next argument of the annotated test method
     * (positional arguments).
     *
     * @since 1.0.0
     */
    String argumentPlaceholder() default "";

    /**
     * Allows to ignore (or not) leading and trailing whitespace characters identified in the argument values.
     *
     <p>Defaults to {@code true}.</p>
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
     *
     * <p>Defaults to {@code {}}.</p>
     */
    String[] nullValues() default {};

    /**
     * A value used to substitute quoted empty strings read from the input.
     *
     * <p>Defaults to empty string ({@code ""}).</p>
     */
    String emptyValue() default "";

}
