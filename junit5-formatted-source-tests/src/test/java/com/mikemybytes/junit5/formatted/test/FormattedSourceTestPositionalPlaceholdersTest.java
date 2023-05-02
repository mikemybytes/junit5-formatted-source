package com.mikemybytes.junit5.formatted.test;

import com.mikemybytes.junit5.formatted.FormattedSourceTest;
import org.junit.jupiter.api.TestInfo;

import static org.assertj.core.api.Assertions.assertThat;

class FormattedSourceTestPositionalPlaceholdersTest {

    @FormattedSourceTest(format = "? + ? = ?", argumentPlaceholder = "?", lines = {
            "1 + 2 = 3",
            "3 + 4 = 7"
    })
    void supportsSimpleFormat(int a, int b, int c, TestInfo info) {
        assertThat(a + b).isEqualTo(c);

        assertThat(info.getDisplayName()).isEqualTo("%d + %d = %d".formatted(a, b, c));
    }

    @FormattedSourceTest(format = "<X> + <X> = <X>", argumentPlaceholder = "<X>", textBlock = """
            1 + 2 = 3
            3 + 4 = 7
            """
    )
    void supportsSimpleTemplateViaTextBlock(int a, int b, int c, TestInfo info) {
        assertThat(a + b).isEqualTo(c);

        assertThat(info.getDisplayName()).isEqualTo("%d + %d = %d".formatted(a, b, c));
    }

    @FormattedSourceTest(format = "? maps to ? and gives ?", argumentPlaceholder = "?", lines = {
            "'foo' maps to 'bar' and gives 'xyz'"
    })
    void supportsFullTextFormat(String a, String b, String c, TestInfo info) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("xyz");

        assertThat(info.getDisplayName()).isEqualTo("'foo' maps to 'bar' and gives 'xyz'");
    }

    @FormattedSourceTest(format = "example: ? maps to ? and gives ?", argumentPlaceholder = "?", lines = {
            "example: 'foo' maps to 'bar' and gives 'xyz'"
    })
    void supportsFullTextFormatStartingWithText(String a, String b, String c, TestInfo info) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("xyz");

        assertThat(info.getDisplayName()).isEqualTo("example: 'foo' maps to 'bar' and gives 'xyz'");
    }

    @FormattedSourceTest(format = "? maps to ? and gives ? (an example)", argumentPlaceholder = "?", lines = {
            "'foo' maps to 'bar' and gives 'xyz' (an example)"
    })
    void supportsFullTextFormatEndingWithText(String a, String b, String c) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("xyz");
    }

    @FormattedSourceTest(format = "appending ? to ? gives ?", argumentPlaceholder = "?", quoteCharacter = '"', textBlock = """
            appending "foo" to "bar" gives "foobar"
            """)
    void supportsCustomQuoteCharacter(String a, String b, String c) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("foobar");
    }

    @FormattedSourceTest(format = "is ??? empty?", argumentPlaceholder = "???", textBlock = """
            is '' empty?
            """)
    void supportsEmptyQuotedArguments(String argument) {
        assertThat(argument).isEmpty();
    }

    @FormattedSourceTest(format = "start ? -> ? => ? > ? end", argumentPlaceholder = "?", textBlock = """
            start a  ->      'b'      =>  c >   '   d ' end
            """)
    void trimsLeadingAndTrailingWhitespacesWhenEnabled(String a, String b, String c, String d) {
        assertThat(a).isEqualTo("a");
        assertThat(b).isEqualTo("b");
        assertThat(c).isEqualTo("c");
        assertThat(d).isEqualTo("   d ");
    }

    @FormattedSourceTest(format = "start ? -> ? => ? > ? end", argumentPlaceholder = "?",
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
            format = "this is null: {} and this {} is not!", argumentPlaceholder = "{}",
            lines = {"this is null:  and this '' is not!"})
    void recognizesEmptyUnquotedValueAsNull(String a, String b) {
        assertThat(a).isNull();
        assertThat(b).isEmpty();
    }

    @FormattedSourceTest(format = "a: {}, b: {}, c: {}, d: {}", argumentPlaceholder = "{}",
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

    @FormattedSourceTest(emptyValue = "EMPTY", format = "a: ?", argumentPlaceholder = "?", lines = {"a: ''"})
    void supportsCustomEmptyValue(String a) {
        assertThat(a).isEqualTo("EMPTY");
    }

}
