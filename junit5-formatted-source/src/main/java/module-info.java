/**
 * JUnit 5 Formatted Source introduces new way of writing parameterized tests. It allows defining test case arguments in
 * a human-readable way, following a user-defined format.
 */
module com.mikemybytes.junit5.formatted {

    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;

    exports com.mikemybytes.junit5.formatted;

    // reflective access required while executing tests
    opens com.mikemybytes.junit5.formatted to org.junit.platform.commons;

}