# JUnit 5 FormattedSource User Guide

## The basics

_JUnit 5 FormattedSource_ extends [JUnit 5](https://github.com/junit-team/junit5) with a new way of writing 
[parameterized tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests).
Test cases are represented as strings (similarly to the [@CsvSource](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-sources-CsvSource)
annotation), following a user-defined format.

Let's start from an example:
```java
@FormattedSourceTest(               // FormattedSource annotation
    format = "{0} + {1} = {2}",     // format string
    lines = {                       // test cases definition
            "1 + 2 = 3",
            "3 + 4 = 7"
    }    
)
void calculatesSum(int a, int b, int sum) {
    Assertions.assertEquals(sum, a + b);    
}
```

The simplest use case contains three elements:
- FormattedSource annotation (`@FormattedSourceTest` or `@FormattedSource`),
- format string,
- test cases definition.

### FormattedSource annotations

`@FormattedSourceTest` turns our `calculatesSum` method into a FormattedSource parameterized test.
In fact, it's a shorthand for:
```java
@ParameterizedTest(name = "{0} + {1} = {2}")
@FormattedSource(               
    format = "{0} + {1} = {2}",
    lines = {
            "1 + 2 = 3",
            "3 + 4 = 7"
    }    
)
void calculatesSum(int a, int b, int sum) { ... }
```

There's no magic here. `@FormattedSource` is just another [JUnit 5 argument source](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-sources).
It not only save you from typing `@ParameterizedTest` every time, but also uses the same _format string_ for the
displayed test execution output:
```
calculatesSum(int, int, int) ✔
├─ 1 + 2 = 3 ✔
└─ 3 + 4 = 7 ✔
```

**Both `@FormattedSource` and `@FormattedSourceTest` share all their properties**. This means you can always turn one
into another, depending on the use case.

Unless you really need additional control (e.g. to further customize the displayed test execution output), 
`@FormattedSourceTest` should be preferred over `@FormattedSource`.

### Format string

**The _format string_ defines, how each test case definition has to look**. It instructs the FormattedSource 
engine where to find test method argument values. The format string has to be provided using a mandatory `format` 
parameter.

Let's analyze the format string of our example: `{0} + {1} = {2}`.

**By default, you can reference test method arguments using their indexes (starting from zero) surrounded with braces 
(curly brackets, `{` and `}`)**. This means `{0}` refers to the first argument, `{2}` to the third one, etc. 

**The parameters don't have to appear in the same order as the test method's arguments**. For our 
`calculatesSum(int a, int b, int sum)` method, an alternative format string like `{2} = {0} + {1}` could be used as
well.

### Test cases definition

**FormattedSource test cases are represented as text, where each line represents a separate test case**.

There are two alternative ways of defining test cases. **By using `lines`, we can specify them as an array of strings**:
```java
@FormattedSourceTest(
    format = "{0} + {1} = {2}",
    lines = {                       // that's an array!
            "1 + 2 = 3",
            "3 + 4 = 7"
    }    
)
void calculatesSum(int a, int b, int sum) { .. }
```

If you're running on Java 15+ (and you really should be!), you can use Text Block instead and pass it as the `textBlock`
param:
```java
@FormattedSourceTest(
    format = "{0} + {1} = {2}",
    textBlock = """
        1 + 2 = 3
        3 + 4 = 7
        """
)
void calculatesSum(int a, int b, int sum) { .. }
```

Both approaches (`lines` and `textBlock`) are functionally equivalent and mutually exclusive.

Similarly to other argument sources like `@ValueSource` or `@CsvSource`, standard [implicit argument conversions](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-argument-conversion)
are fully supported. This means you're not limited only to numbers and strings as the argument values:
```java
@FormattedSourceTest(
    format = "UUID: {0}, Date: {1}, URL: {2}",
    lines = { "UUID: f39f1c74-c0f8-4964-a5a0-7ce03c8aff4a, Date: 2023-04-23, URL: https://junit.org/" }    
)
void supportsImplicitArgumentConversions(UUID uuid, LocalDate date, URL url) { .. }
```

## Advanced features

### Using positional arguments syntax

Usually, the order of arguments in the format string follows the order of the test method's parameters. Using the 
default indexed syntax in the format string (e.g. `{0} -> {1}`) may then feel unnecessarily verbose. In cases like
these, a fixed placeholder string can be used instead.

Specifying the `argumentPlaceholder` parameter disables the default indexed syntax. Instead, the provided string
has to be used to represent all the arguments:
```java
@FormattedSourceTest(
    format = "? + ? = ?",
    argumentPlaceholder = "?",
    textBlock = """
        1 + 2 = 3
        3 + 4 = 7
        """
)
void calculatesSum(int a, int b, int sum) { .. }
```

Note that in this approach, the order of arguments in the format string has to always match the order of the test
method's parameters.

As the library does not currently support escaping braces in the format string, the positional argument syntax should be
considered a recommended alternative.

### Quoting argument values

Sometimes, test case readability could be improved by quoting specific argument values. By default, you can always use 
single quotes (`'`) in order to achieve that:

```java
@FormattedSourceTest(
    format = "this is a string: {0}, while this is not: {1}",
    textBlock = """
        this is a string: 'JUnit is great!', while this is not: 42
        this is a string: foo, while this is not: '11'
        """ 
)
void verifySomething(String str, int num) { .. }
```
As you can see, all types of parameters could be quoted - not only the string ones.

You can change the character used for quoting using `quoteCharacter` parameter:
```java
@FormattedSourceTest(
    format = "this is a string: {0}, while this is not: {1}",
    quoteCharacter = '*',
    textBlock = """
        this is a string: *JUnit is great!*, while this is not: 42
        this is a string: foo, while this is not: *11*
        """ 
)
void verifySomething(String str, int num) { .. }
```

### Additional whitespaces

By default, leading and trailing whitespaces will be removed from the argument value. This allows us to improve the readability by visually aligning multiple test cases:
```java
@FormattedSourceTest(
    format = "{0} + {1} = {2}",
    textBlock = """
          1 +  2 =   3
         11 + 98 = 109
        764 +  2 = 766
        """ 
)
void calculatesSum(int a, int b, int sum) { .. }
```

You can disable this behaviour entirely by passing `ignoreLeadingAndTrailingWhitespace = false`.

If you want to preserve leading/trailing whitespaces of a certain value, simply quote it:
```java
@FormattedSourceTest(
    format = "Does {0} starts with a whitespace? {1}",
    textBlock = """
        Does    JUnit starts with a whitespace? false
        Does ' JUnit' starts with a whitespace?  true
        """ 
)
void startsFromWhitespace(String str, boolean startsFromWhitespace) { .. }
```

### Null values

By default, unquoted empty argument values are converted to `null`:
```java
@FormattedSourceTest(
    format = "Is {0} a null? {1}",
    lines = {
            "Is null a null? false", // non-empty string, no special meaning
            "Is   '' a null? false", // quoted empty string is just an empty string
            "Is      a null? true"   // unquoted empty string resolves to null
    }
)
void isNull(String str, boolean expectedNull) { ... }
```

You can define your own set of "null values" that should be converted to `null` using `nullValues` parameter:
```java
@FormattedSourceTest(
    format = "Is {0} a null? {1}",
    nullValues = { "NULL", "nil" },  // array of strings
    lines = {
            "Is NULL a null? true",
            "Is  nil a null? true",
            "Is null a null? false", // case-sensitive match
    }
)
void isNull(String str, boolean expectedNull) { ... }
```

### Custom empty value

Similarly to the `@CsvSource`, quoted empty values could be substituted with a value passed as the `emptyValue`
parameter:
```java
@FormattedSourceTest(
    emptyValue = "EMPTY", 
    format = "empty: {0}",
    textBlock = """
        empty: ''
        """
) 
void supportsCustomEmptyValue(String str) {
    Assertions.assertEquals("EMPTY", str);
}
```

## Usage ideas (a.k.a the kitchen sink)

### Testing mappers and encoders

```java
@FormattedSourceTest(
    format = "{0} -> {1}", 
    textBlock = """
        'foo' -> 'bar'
        'junit' -> 'jupiter'
        """
)
void mapsOneValueToAnother(String input, String expectedValue) { ... }
```

```java
@FormattedSourceTest(
    format = "{0} maps to {1}", 
    textBlock = """
        'foo' maps to 'bar'
        'junit' maps to 'jupiter'
        """
)
void mapsOneValueToAnother(String input, String expectedValue) { ... }
```

```java
@FormattedSourceTest(
    format = "encodes {0} seconds as {1}", 
    lines = {
        "encodes 15 seconds as 'PT15S'",
        "encodes 180 seconds as 'PT3M'",
        "encodes 172800 seconds as 'PT48H'"
    }
)
void encodesDurationAsIso8601(long seconds, String expected) { ... }
```

### Testing validators

```java
@FormattedSourceTest(
    format = "{0} for {1}",
    lines = {
        "fails for 48",
        "succeeds for +48123456789"
    }
)
void validatesPhoneNumber(String expectedResult, String phoneNumber) {
    boolean expected = "succeeds".equals(expectedResult);
    Assertions.assertEquals(expected, validator.validate(phoneNumber));
}
```

### Testing HTTP APIs

```java
enum UserType { CUSTOMER, ADMIN }

@FormattedSourceTest(
    format = "{0} returns {1} for {2}",
    textBlock = """
        /api/items           returns 200 for CUSTOMER
        /api/items           returns 404 for ADMIN
        /api/admin/inventory returns 401 for CUSTOMER
        /api/admin/inventory returns 200 for ADMIN
        """
)
void protectsEndpointsBasedOnUserType(URI uri, int httpCode, UserType userType) {
    User user = TestUserFactory.authenticated(userType);
    testHttpClient.get(uri).returnsResponseCode(httpCode);
}
```
