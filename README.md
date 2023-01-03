# JUnit 5 FormattedSource

![](https://img.shields.io/github/license/mikemybytes/junit5-formatted-source)
![](https://img.shields.io/github/v/release/mikemybytes/junit5-formatted-source)
![](https://img.shields.io/maven-central/v/com.mikemybytes/junit5-formatted-source)
![](https://img.shields.io/github/actions/workflow/status/mikemybytes/junit5-formatted-source/build.yml)

This library extends [JUnit 5](https://github.com/junit-team/junit5) with a new way of writing [parameterized tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests).
It allows defining test case arguments in a human-readable way, following a user-defined format. Additionally, it automatically
takes care of the test case name, so the input definition is also what will be presented in the test execution output.

```java
class CalculatorTest {
    
    private final Calculator calculator = new Calculator();

    @FormattedSourceTest(format = "{0} + {1} = {2}", lines = {
            "1 + 2 = 3",
            "3 + 4 = 7"
    })
    void calculatesSum(int a, int b, int expectedSum) {
        Assertions.assertEquals(expectedSum, calculator.sum(a, b));
    }
    
}
```

Output:
```
calculatesSum(int, int, int) ✔
├─ 1 + 2 = 3 ✔
└─ 3 + 4 = 7 ✔
```

Of course, _JUnit 5 FormattedSource_ can do even more!

## Installing

Requirements:
- Java 11+
- JUnit 5.8.0+

_Note: This library does not introduce any dependencies other than the JUnit 5._

### Maven

```xml
    <dependency>
        <groupId>com.mikemybytes</groupId>
        <artifactId>junit5-formatted-source</artifactId>
        <version>0.1.2</version>
        <scope>test</scope>
    </dependency>
```

### Gradle

```groovy
    testImplementation "com.mikemybytes:junit5-formatted-source:0.1.2"
```

## Yet another argument source?

The project has been inspired by the build-in [`@CsvSource` annotation](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-sources-CsvSource),
which allows not only [writing data table tests](https://mikemybytes.com/2021/10/19/parameterize-like-a-pro-with-junit-5-csvsource/)
but also specification-like test case definitions:

```java
class CsvSourceSpecificationTest {
    
    @ParameterizedTest(name = "{0} maps to {1}")
    @CsvSource(delimiterString = "maps to", textBlock = """
        'foo' maps to 'bar'
        'junit' maps to 'jupiter'
        """)
    void mapsOneValueToAnother(String input, String expectedValue) {
        // ...
    }
    
}
```

Yet, `@CsvSource` limits the user to only one `delimiterString`, which effectively means supporting 
only two arguments at a time. Additionally, selected delimiter must be repeated within the `@ParameterizedTest`'s `name`
parameter in order to appear in the test execution output.

Using `@FormattedSource` allows to forget about these limitations and write less code:

```java
class FormattedSourceSpecificationTest {
    
    @FormattedSourceTest(format = "{0} maps to {1} using rule {2}", textBlock = """
        'foo' maps to 'bar' using rule 486
        'junit' maps to 'jupiter' using rule 44
        """)
    void mapsOneValueToAnother(String input, String expectedValue, int expectedRuleId) {
        // ...
    }
    
}
```

Test case names are automatically generated based on provided specification:
```
mapsOneValueToAnother(String, String, int) ✔
├─ 'foo' maps to 'bar' using rule 486 ✔
└─ 'junit' maps to 'jupiter' using rule 44 ✔
```

## `@FormattedSourceTest` vs `@FormattedSource`

The library comes with two annotations. `@FormattedSource` is just a standard JUnit 5 argument source that has to be
combined with `@ParameterizedTest`. It also does not influence generated test case names automatically:

```java
class DurationEncodingTest {
    
    @ParameterizedTest(name = "encodes {0} seconds as {1}")
    @FormattedSource(format = "encodes {0} seconds as {1}", lines = {
            "encodes 15 seconds as 'PT15S'",
            "encodes 180 seconds as 'PT3M'",
            "encodes 172800 seconds as 'PT48H'"
    })
    void encodesDurationAsIso8601(long seconds, String expected) {
        // ...
    }
    
}
```

The equivalent `@FormattedSourceTest` simply results in a less verbose code:
```java
class DurationEncodingShorterTest {
    
    // @ParameterizedTest already included (with test case name!)
    @FormattedSourceTest(format = "encodes {0} seconds as {1}", lines = {
            "encodes 15 seconds as 'PT15S'",
            "encodes 180 seconds as 'PT3M'",
            "encodes 172800 seconds as 'PT48H'"
    })
    void encodesDurationAsIso8601(long seconds, String expected) {
        // ...
    }
    
}
```

## Features

Both `@FormattedSourceTest` and `@FormattedSource` share the same set of configuration parameters.

Format string - defined via `format` parameter - can reference test case argument values by their position (index) using
surrounded with braces. For example, `{3}` means "the value of the 4th argument".

Test case definition (input) can be defined as a list (array to be precise) of `lines` or using a single Java 15+ 
`textBlock`. In both cases, each line represents a separate test case.

Optionally, argument values can be surrounded by the `quoteCharacter` (default: single quote) to improve readability.

By default, leading and trailing whitespaces will be removed from the argument value (unless they're a part of the 
quoted value). Yet, you can easily disable this behavior by setting `ignoreLeadingAndTrailingWhitespace = false`.

You can combine `@FormattedSourceTest` and `@FormattedSource` with other standard JUnit 5 annotations like 
`@DisplayName` or `@Order`. Also, both annotations support the [implicit argument conversion](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-argument-conversion).

## Limitations

As the project is still in a relatively early stage, not all the customizations known from `@CsvSource` are already
supported. See the [project's issue list](https://github.com/mikemybytes/junit5-formatted-source/issues) for more details.
