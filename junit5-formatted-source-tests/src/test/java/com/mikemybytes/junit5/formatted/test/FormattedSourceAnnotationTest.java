package com.mikemybytes.junit5.formatted.test;

import com.mikemybytes.junit5.formatted.FormattedSource;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class FormattedSourceAnnotationTest {

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @FormattedSource(format = "{0} + {1} = {2}", lines = {
            "1 + 2 = 3",
            "3 + 4 = 7"
    })
    void supportsSimpleFormat(int a, int b, int c) {
        assertThat(a + b).isEqualTo(c);
    }

    @ParameterizedTest(name = "{0} plus {1} is equal to {2}")
    @FormattedSource(format = "{0} + {1} = {2}", lines = {
            "1 + 2 = 3",
            "3 + 4 = 7"
    })
    void supportsSimpleFormatWithDisplayName(int a, int b, int c, TestInfo info) {
        assertThat(a + b).isEqualTo(c);
        assertThat(info.getDisplayName()).isEqualTo("%d plus %d is equal to %d".formatted(a, b, c));
    }

    @ParameterizedTest
    @FormattedSource(format = "{0} + {1} = {2}", textBlock = """
            1 + 2 = 3
            3 + 4 = 7
            """
    )
    void supportsSimpleTemplateViaTextBlock(int a, int b, int c) {
        assertThat(a + b).isEqualTo(c);
    }

    @ParameterizedTest(name = "{0} maps to {1} and gives {2}")
    @FormattedSource(format = "{0} maps to {1} and gives {2}", lines = {
            "'foo' maps to 'bar' and gives 'xyz'"
    })
    void supportsFullTextFormat(String a, String b, String c) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("xyz");
    }

    @ParameterizedTest(name = "example: {0} maps to {1} and gives {2}")
    @FormattedSource(format = "example: {0} maps to {1} and gives {2}", lines = {
            "example: 'foo' maps to 'bar' and gives 'xyz'"
    })
    void supportsFullTextFormatStartingWithText(String a, String b, String c) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("xyz");
    }

    @ParameterizedTest(name = "{0} maps to {1} and gives {2} (an example)")
    @FormattedSource(format = "{0} maps to {1} and gives {2} (an example)", lines = {
            "'foo' maps to 'bar' and gives 'xyz' (an example)"
    })
    void supportsFullTextFormatEndingWithText(String a, String b, String c) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("xyz");
    }

    @ParameterizedTest(name = "appending {0} to {1} gives {2}")
    @FormattedSource(format = "appending {0} to {1} gives {2}", quoteCharacter = '"', textBlock = """
            appending "foo" to "bar" gives "foobar"
            """)
    void supportsCustomQuoteCharacter(String a, String b, String c) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("foobar");
    }

    @ParameterizedTest(name = "is {0} empty?")
    @FormattedSource(format = "is {0} empty?", textBlock = """
            is '' empty?
            """)
    void supportsEmptyQuotedArguments(String argument) {
        assertThat(argument).isEmpty();
    }

    @ParameterizedTest(name = "start {0} -> {1} => {2} > {3} end")
    @FormattedSource(format = "start {0} -> {1} => {2} > {3} end", textBlock = """
            start a  ->      'b'      =>  c >   '   d ' end
            """)
    void trimsLeadingAndTrailingWhitespacesWhenEnabled(String a, String b, String c, String d) {
        assertThat(a).isEqualTo("a");
        assertThat(b).isEqualTo("b");
        assertThat(c).isEqualTo("c");
        assertThat(d).isEqualTo("   d ");
    }

    @ParameterizedTest(name = "start {0} -> {1} => {2} > {3} end")
    @FormattedSource(format = "start {0} -> {1} => {2} > {3} end",
            ignoreLeadingAndTrailingWhitespace = false, textBlock = """
            start a  ->      'b'      =>  c >   '   d ' end
            """)
    void doesNotTrimLeadingAndTrailingWhitespacesWhenDisabled(String a, String b, String c, String d) {
        assertThat(a).isEqualTo("a ");
        assertThat(b).isEqualTo("     'b'     ");
        assertThat(c).isEqualTo(" c");
        assertThat(d).isEqualTo("  '   d '");
    }

}
