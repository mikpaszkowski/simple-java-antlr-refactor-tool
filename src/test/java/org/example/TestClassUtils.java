package org.example;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class TestClassUtils {

    public static String CLASS_NAME = "SampleClass";
    public static String NEW_CLASS_NAME = "NewSampleClassName";
    public static String METHOD_NAME = "sampleMethod";
    public static String NEW_METHOD_NAME = "newSampleMethodName";

    public static CharStream getCharStreamFromClassFile(String pathResultPrefix, String className) throws IOException {
        String resourceName = "test-classes/" + pathResultPrefix + "/" + className + ".java";

        ClassLoader classLoader = TestClassUtils.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());
        String absolutePath = file.getAbsolutePath();
        return CharStreams.fromStream(new FileInputStream(absolutePath));
    }
}
