package org.example.file;

import org.example.antlr.exceptions.FileFormatException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;

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

    public static String saveFile(String content, String sourceFileName)
            throws IOException {
        String currentDirectory = System.getProperty("user.dir");
        String directoryPath = currentDirectory + File.separator + "transformation_result";


        if(Files.exists(Path.of(directoryPath))){
            Files
                    .walk(Path.of(directoryPath)) // Traverse the file tree in depth-first order
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
        File directory = new File(directoryPath);
        if (directory.mkdirs()) {
            System.out.println("Directory created successfully.");
        } else {
            System.out.println("Failed to create the output directory.");
            return sourceFileName;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(directoryPath, extractJavaFileName(sourceFileName))));
        writer.write(content);

        writer.close();
        return directoryPath + "/" + extractJavaFileName(sourceFileName);
    }

    public static void removeFile(String fileName) throws IOException {
        Path filePath = Path.of(fileName);
        Files.delete(filePath);
    }

    public static String extractJavaFileName(String filePath) {
        int lastSeparatorIndex = filePath.lastIndexOf(File.separator);
        int extensionIndex = filePath.lastIndexOf(".java");

        if (lastSeparatorIndex == -1 || extensionIndex == -1 || extensionIndex <= lastSeparatorIndex) {
            throw new IllegalArgumentException("Invalid file path: " + filePath);
        }

        return filePath.substring(lastSeparatorIndex + 1, extensionIndex + 5);
    }

    public static void verifyRefactoredFile(String fileName, String outputContent) throws IOException {
        if (!Objects.isNull(outputContent)) {
            System.out.println("File has been refactored successfully!");
            String newFilePath = FileReader.saveFile(outputContent, fileName);
            System.out.println("Refactored content has been saved under the root in directory named: transformation_result.");
            System.out.println("Verifying whether file is compilable ...");

            try {
                JavaFileCompilerValidator.checkFileCompilability(Path.of(newFilePath).toString());
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
                FileReader.removeFile(newFilePath);
            }
        }
    }
}
