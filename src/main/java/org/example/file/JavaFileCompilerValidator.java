package org.example.file;

import javax.tools.*;
import java.io.IOException;
import java.util.Collections;

public class JavaFileCompilerValidator {

    public static void checkFileCompilability(String filePath) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Collections.singletonList(filePath));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, null, null, compilationUnits);

            if (!task.call()) {
                StringBuilder errorMessage = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticCollector.getDiagnostics()) {
                    errorMessage.append(diagnostic.toString()).append("\n");
                }
                throw new RuntimeException("File is not compilable:\n" + errorMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while accessing file: " + filePath);
        }
        System.out.println("File is compilable.");
    }
}
