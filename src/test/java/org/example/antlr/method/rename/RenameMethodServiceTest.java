package org.example.antlr.method.rename;

import org.example.antlr.exceptions.ClassElementMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.example.TestClassUtils.getCharStreamFromClassFile;

class RenameMethodServiceTest {

    private static Stream<Arguments> moveMethodServiceThrowExceptionProvider() {
        return Stream.of(
                Arguments.of("NonExistingClass", "methodName", "No source class: NonExistingClass present."),
                Arguments.of("SampleClass", "nonExistingMethodName", "No method: nonExistingMethodName found.")
        );
    }

    @ParameterizedTest(name = "should throw exception when: {2}")
    @MethodSource("moveMethodServiceThrowExceptionProvider")
    void shouldThrowExceptionWhenCertainTransformationObjectsNotFound(String sourceClassName, String methodName, String exceptionMsg) throws IOException {
        var renameMethodService = new RenameMethodService();
        var content = getCharStreamFromClassFile("invalid", "SampleClass");
        Assertions.assertThrows(ClassElementMissingException.class, () -> renameMethodService.transform(RenameMethodDTO.builder()
                .sourceClassName(sourceClassName)
                .methodName(methodName)
                .newMethodName("newMethodName")
                .build(), content.toString()), exceptionMsg);
    }

}