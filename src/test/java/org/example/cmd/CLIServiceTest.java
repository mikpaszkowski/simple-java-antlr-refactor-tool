package org.example.cmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.example.TestClassUtils.getCharStreamFromClassFile;
import static org.example.TestClassUtils.getContentOfFileOnPath;

class CLIServiceTest {

    @BeforeEach
    void beforeEach() throws IOException {
        var outputDirectoryPath = Path.of("transformation_result");
        if (Files.exists(outputDirectoryPath)) {
            Files
                    .walk(outputDirectoryPath) // Traverse the file tree in depth-first order
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            System.out.println("Deleting: " + path);
                            Files.delete(path);  //delete each file or directory
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private static Stream<Arguments> javaFileTransformationsProvider() {
        return Stream.of(
                Arguments.of(
                        new String[]{
                                "--transformType=changeClassName",
                                "--fileName=src/test/java/org/example/cmd/SampleClass.java",
                                "--source=SampleClass",
                                "--newClassName=NewSampleClass"
                        },
                        "TestClass1",
                        "change main class name",
                        "transformation_result/NewSampleClass.java"
                ),
                Arguments.of(
                        new String[]{
                                "--transformType=changeClassName",
                                "--fileName=src/test/java/org/example/cmd/SampleClass.java",
                                "--source=AnotherClass",
                                "--newClassName=NewAnotherClass"
                        },
                        "TestClass2",
                        "change private class name",
                        "transformation_result/SampleClass.java"
                ),
                Arguments.of(
                        new String[]{
                                "--transformType=changeMethodName",
                                "--fileName=src/test/java/org/example/cmd/SampleClass.java",
                                "--source=SampleClass",
                                "--methodName=sampleMethod",
                                "--newMethodName=newSampleMethodName"
                        },
                        "TestClass3",
                        "change method name",
                        "transformation_result/SampleClass.java"
                ),
                Arguments.of(
                        new String[]{
                                "--transformType=moveMethod",
                                "--fileName=src/test/java/org/example/cmd/SampleClass.java",
                                "--source=SampleClass",
                                "--target=AnotherClass",
                                "--methodName=sampleMethod",
                        },
                        "TestClass4",
                        "change method name",
                        "transformation_result/SampleClass.java"
                )
        );
    }

    @ParameterizedTest(name = "should correctly perform file transformation: {2}")
    @MethodSource("javaFileTransformationsProvider")
    void shouldPerformGivenTransformationsCorrectly(String[] args, String fileName, String action, String outputClassPath) throws IOException {
        //given
        CLIService.handleCmdArgs(args);
        Assertions.assertEquals(getCharStreamFromClassFile("cmd", fileName).toString(), getContentOfFileOnPath(outputClassPath));
    }
}