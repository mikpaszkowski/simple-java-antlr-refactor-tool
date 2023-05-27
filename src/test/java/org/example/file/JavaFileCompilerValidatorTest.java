package org.example.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class JavaFileCompilerValidatorTest {

    @Test
    void shouldValidateCompilableJavaClassWithoutExceptionBeingThrown() {
        Assertions.assertDoesNotThrow(() -> JavaFileCompilerValidator.checkFileCompilability("src/test/resources/test-classes/compilability-check-classes/CompilableClass.java"));
    }

    @Test
    void shouldThrowExceptionForNonCompilableJavaClass() {
        Assertions.assertThrows(RuntimeException.class, () -> JavaFileCompilerValidator.checkFileCompilability("src/test/resources/test-classes/compilability-check-classes/NonCompilableClass.java"));
    }
}