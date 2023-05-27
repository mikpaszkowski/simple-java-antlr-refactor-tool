package org.example.antlr.method.move;

import org.example.TestClassUtils;
import org.example.antlr.exceptions.ClassElementMissingException;
import org.example.antlr.exceptions.MethodAlreadyDefineInTargetClassException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

class MoveMethodListenerTest {

    private static Stream<Arguments> moveMethodTransformProvider() {
        return Stream.of(
                Arguments.of("TestRefactorMoveMethod1", "only single void method in source class and target class empty"),
                Arguments.of("TestRefactorMoveMethod4", "only single non-void method in source class and target class empty"),
                Arguments.of("TestRefactorMoveMethod5", "source and target class has fields and other methods")
        );
    }

    @ParameterizedTest(name = "should move method when: {1}")
    @MethodSource("moveMethodTransformProvider")
    public void shouldMoveMethodToAnotherClass(String testFileName, String reason) throws IOException {
        //given
        var methodService = new MoveMethodService();
        var output = methodService.transform(MoveMethodDTO.builder()
                        .methodName("methodName")
                        .sourceClassName("SampleClass")
                        .targetClassName("AnotherClass")
                        .build(),
                TestClassUtils.getCharStreamFromClassFile("before", testFileName).toString());

        //when
        Assertions.assertEquals(output, TestClassUtils.getCharStreamFromClassFile("after", testFileName).toString());
    }

    private static Stream<Arguments> moveMethodExceptionTransformProvider() {
        return Stream.of(
                Arguments.of("TestRefactorMoveMethod3", "no target class present", "SampleClass", "RandomClass", "methodName"),
                Arguments.of("TestRefactorMoveMethod3", "no source class present", "RandomClass", "SampleClass", "randomName"),
                Arguments.of("TestRefactorMoveMethod3", "no source class present", "RandomClass2", "RandomClass", "methodName"),
                Arguments.of("TestRefactorMoveMethod1", "no method present", "SampleClass", "AnotherClass", "randomName")
        );
    }

    @ParameterizedTest(name = "should throw exception: {1}")
    @MethodSource("moveMethodExceptionTransformProvider")
    public void shouldThrowException(String testFileName, String reason, String sourceClass, String targetClass, String methodName) {
        //given
        var methodService = new MoveMethodService();
        Assertions.assertThrows(ClassElementMissingException.class, () -> methodService.transform(MoveMethodDTO.builder()
                        .methodName(methodName)
                        .sourceClassName(sourceClass)
                        .targetClassName(targetClass)
                        .build(),
                TestClassUtils.getCharStreamFromClassFile("before", testFileName).toString()));

    }

    private static Stream<Arguments> moveMethodExceptionOnAlreadyExistingMethodTransformProvider() {
        return Stream.of(
                Arguments.of("TestRefactorMoveMethod6", "method is already defined in target class", "SampleClass", "AnotherClass", "methodName"),
                Arguments.of("TestRefactorMoveMethod3", "source class is present but method already defined in target", "RandomClass", "SampleClass", "methodName")
        );
    }

    @ParameterizedTest(name = "should throw exception: {1}")
    @MethodSource("moveMethodExceptionOnAlreadyExistingMethodTransformProvider")
    public void shouldThrowExceptionWhenMethodExistsInTargetClass(String testFileName, String reason, String sourceClass, String targetClass, String methodName) {
        //given
        var methodService = new MoveMethodService();
        Assertions.assertThrows(MethodAlreadyDefineInTargetClassException.class, () -> methodService.transform(MoveMethodDTO.builder()
                        .methodName(methodName)
                        .sourceClassName(sourceClass)
                        .targetClassName(targetClass)
                        .build(),
                TestClassUtils.getCharStreamFromClassFile("before", testFileName).toString()));

    }
}
