package org.example.file;

import org.example.antlr.exceptions.FileFormatException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {
    public static String readFile(String fileName) {
        try {
            Path filePath = Path.of(fileName);
            String fileContent = Files.readString(filePath);
            String fileExtension = getFileExtension(fileName);
            if(!fileExtension.equals("java")) {
                throw new FileFormatException("Wrong extension of input file. Should be *.java");
            }
            JavaFileCompilerValidator.checkFileCompilability(filePath.toString());
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while accessing file: " + fileName);
        }
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return ""; // No extension found or it's an empty extension
        }
        return fileName.substring(dotIndex + 1);
    }

    public static void saveFile(String content, String sourceFileName)
            throws IOException {
        String className = sourceFileName.substring(0, sourceFileName.lastIndexOf("."));
        String newFileName = className + "_refactored.java";
        BufferedWriter writer = new BufferedWriter(new FileWriter(newFileName));
        writer.write(content);

        writer.close();
    }

    public static void removeFile(String fileName) throws IOException {
        Path filePath = Path.of(fileName);
        Files.delete(filePath);
    }
}
