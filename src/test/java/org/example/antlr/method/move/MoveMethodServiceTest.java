package org.example.antlr.method.move;

import org.example.antlr.exceptions.ClassElementMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.example.TestClassUtils.getCharStreamFromClassFile;

class MoveMethodServiceTest {

    private static Stream<Arguments> moveMethodServiceThrowExceptionProvider() {
        return Stream.of(
                Arguments.of("NonExistingClass", "AnotherNonExistingClass", "randomMethod", "No given class found."),
                Arguments.of("SampleClass", "NonExistingClass" , "randomMethod", "No target class: NonExistingClass found."),
                Arguments.of("NonExistingClass", "SampleClass" , "randomMethod", "No source class: NonExistingClass found."),
                Arguments.of("SampleClass", "AnotherSampleClass" , "nonExistingMethod", "No method: nonExistingMethod found.")
        );
    }

    @ParameterizedTest(name = "should throw exception when: {3}")
    @MethodSource("moveMethodServiceThrowExceptionProvider")
    void shouldThrowExceptionWhenCertainTransformationObjectsNotFound(String sourceClassName, String targetClassName, String methodName, String exceptionMsg) throws IOException {
        var moveMethodService = new MoveMethodService();
        var content = getCharStreamFromClassFile("invalid", "SampleClass");
        Assertions.assertThrows(ClassElementMissingException.class, () -> moveMethodService.transform(MoveMethodDTO.builder()
                        .sourceClassName(sourceClassName)
                        .targetClassName(targetClassName)
                        .methodName(methodName)
                .build(), content.toString()), exceptionMsg);
    }

}