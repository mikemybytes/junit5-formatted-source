package com.mikemybytes.junit5.formatted.test;

import com.mikemybytes.junit5.formatted.FormattedSource;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class FormattedSourceAnnotationTest {

    @ParameterizedTest
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

    @ParameterizedTest
    @FormattedSource(format = "appending {0} to {1} gives {2}", quoteCharacter = '"', textBlock = """
            appending "foo" to "bar" gives "foobar"
            """)
    void supportsCustomQuoteCharacter(String a, String b, String c) {
        assertThat(a).isEqualTo("foo");
        assertThat(b).isEqualTo("bar");
        assertThat(c).isEqualTo("foobar");
    }

}
