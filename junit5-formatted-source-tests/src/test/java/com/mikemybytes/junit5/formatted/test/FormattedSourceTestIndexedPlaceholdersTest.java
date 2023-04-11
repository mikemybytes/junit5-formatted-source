package com.mikemybytes.junit5.formatted.test;

import com.mikemybytes.junit5.formatted.FormattedSourceTest;
import org.junit.jupiter.api.TestInfo;

import static org.assertj.core.api.Assertions.assertThat;

class FormattedSourceTestIndexedPlaceholdersTest {

    @FormattedSourceTest(format = "{0} + {1} = {2}", lines = {
            "1 + 2 = 3",
            "3 + 4 = 7"
    })
    void supportsSimpleFormat(int a, int b, int c, TestInfo info) {
        assertThat(a + b).isEqualTo(c);

        assertThat(info.getDisplayName()).isEqualTo("%d + %d = %d".formatted(a, b, c));
    }

    @FormattedSourceTest(format = "{0} + {1} = {2}", textBlock = """
            1 + 2 = 3
            3 + 4 = 7
            """
    )
    void supportsSimpleTemplateViaTextBlock(int a, int b, int c, TestInfo info) {
        assertThat(a + b).isEqualTo(c);

        assertThat(info.getDisplayName()).isEqualTo("%d + %d = %d".formatted(a, b, c));
    }

    @FormattedSourceTest(format = "{0} maps to {1} and gives {2}", lines = {
            "'foo' maps to 'bar' and gives 'xyz'"
    })
    void supportsFullTextFormat(String a, String b, String c, TestInfo info) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("xyz");

        assertThat(info.getDisplayName()).isEqualTo("'foo' maps to 'bar' and gives 'xyz'");
    }

    @FormattedSourceTest(format = "example: {0} maps to {1} and gives {2}", lines = {
            "example: 'foo' maps to 'bar' and gives 'xyz'"
    })
    void supportsFullTextFormatStartingWithText(String a, String b, String c, TestInfo info) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("xyz");

        assertThat(info.getDisplayName()).isEqualTo("example: 'foo' maps to 'bar' and gives 'xyz'");
    }

    @FormattedSourceTest(format = "{0} maps to {1} and gives {2} (an example)", lines = {
            "'foo' maps to 'bar' and gives 'xyz' (an example)"
    })
    void supportsFullTextFormatEndingWithText(String a, String b, String c) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("xyz");
    }

    @FormattedSourceTest(format = "appending {0} to {1} gives {2}", quoteCharacter = '"', textBlock = """
            appending "foo" to "bar" gives "foobar"
            """)
    void supportsCustomQuoteCharacter(String a, String b, String c) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("foobar");
    }

    @FormattedSourceTest(format = "is {0} empty?", textBlock = """
            is '' empty?
            """)
    void supportsEmptyQuotedArguments(String argument) {
        assertThat(argument).isEmpty();
    }

    @FormattedSourceTest(format = "start {0} -> {1} => {2} > {3} end", textBlock = """
            start a  ->      'b'      =>  c >   '   d ' end
            """)
    void trimsLeadingAndTrailingWhitespacesWhenEnabled(String a, String b, String c, String d) {
        assertThat(a).isEqualTo("a");
        assertThat(b).isEqualTo("b");
        assertThat(c).isEqualTo("c");
        assertThat(d).isEqualTo("   d ");
    }

    @FormattedSourceTest(format = "start {0} -> {1} => {2} > {3} end",
            ignoreLeadingAndTrailingWhitespace = false, textBlock = """
            start a  ->      'b'      =>  c >   '   d ' end
            """)
    void doesNotTrimLeadingAndTrailingWhitespacesWhenDisabled(String a, String b, String c, String d) {
        assertThat(a).isEqualTo("a ");
        assertThat(b).isEqualTo("     'b'     ");
        assertThat(c).isEqualTo(" c");
        assertThat(d).isEqualTo("  '   d '");
    }

    @FormattedSourceTest(
            format = "this is null: {0} and this {1} is not!",
            lines = {"this is null:  and this '' is not!"})
    void recognizesEmptyUnquotedValueAsNull(String a, String b) {
        assertThat(a).isNull();
        assertThat(b).isEmpty();
    }

    @FormattedSourceTest(format = "a: {0}, b: {1}, c: {2}, d: {3}",
            nullValues = {"N/A", "null"},
            textBlock = """
            a: N/A, b: 'N/A', c: null, d: 'null'
            """)
    void supportsCustomNullValues(String a, String b, String c, String d) {
        assertThat(a).isNull();
        assertThat(b).isNull();
        assertThat(c).isNull();
        assertThat(d).isNull();
    }

    @FormattedSourceTest(emptyValue = "EMPTY", format = "a: {0}", lines = {"a: ''"})
    void supportsCustomEmptyValue(String a) {
        assertThat(a).isEqualTo("EMPTY");
    }

}
