package com.mikemybytes.junit5.formatted.test;

import com.mikemybytes.junit5.formatted.FormattedSourceTest;
import org.junit.jupiter.api.TestInfo;

import static org.assertj.core.api.Assertions.assertThat;

class FormattedSourceTestAnnotationTest {

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

    @FormattedSourceTest(format = "appending {0} to {1} gives {2}", quoteCharacter = '"', textBlock = """
            appending "foo" to "bar" gives "foobar"
            """)
    void supportsCustomQuoteCharacter(String a, String b, String c) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("foobar");
    }

}
