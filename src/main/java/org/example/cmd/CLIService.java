package org.example.cmd;

import org.example.antlr.clazz.RenameClassDTO;
import org.example.antlr.clazz.RenameClassService;
import org.example.antlr.method.move.MoveMethodDTO;
import org.example.antlr.method.move.MoveMethodService;
import org.example.antlr.method.rename.RenameMethodDTO;
import org.example.antlr.method.rename.RenameMethodService;
import org.example.file.FileReader;
import org.example.file.JavaFileCompilerValidator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CLIService {
    private final Scanner scanner;
    private String fileName;

    public CLIService() {
        scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Welcome to the File Refactoring Tool CLI!");

        System.out.println("Enter file name:");
        String fileName = scanner.nextLine();

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File does not exist. Exiting...");
            return;
        } else {
            JavaFileCompilerValidator.checkFileCompilability(fileName);
            this.fileName = fileName;
        }

        System.out.println("Select Transformation Type:");
        System.out.println("1. Rename Method");
        System.out.println("2. Rename Class");
        System.out.println("3. Move Method");

        int choice = getIntegerInput();

        switch (choice) {
            case 1:
                renameMethod();
                break;
            case 2:
                renameClass();
                break;
            case 3:
                moveMethod();
                break;
            default:
                System.out.println("Invalid choice. Exiting...");
                break;
        }
    }

    private void renameMethod() {
        System.out.println("Enter source class name:");
        String sourceClass = scanner.nextLine();

        System.out.println("Enter method name:");
        String methodName = scanner.nextLine();

        System.out.println("Enter new method name:");
        String newMethodName = scanner.nextLine();

        try {
            var content = FileReader.readFile(fileName);

            var renameMethodService = new RenameMethodService();
            var outputContent = renameMethodService.transform(RenameMethodDTO.builder()
                    .methodName(methodName)
                    .newMethodName(newMethodName)
                    .sourceClassName(sourceClass)
                    .build(), content);

            FileReader.verifyRefactoredFile(fileName, outputContent);
            System.out.println("Method renamed successfully!");
        } catch (RuntimeException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void renameClass() {
        System.out.println("Enter source class name:");
        String sourceClass = scanner.nextLine();

        System.out.println("Enter new class name:");
        String newClassName = scanner.nextLine();

        try {
            var content = FileReader.readFile(fileName);

            var renameClassService = new RenameClassService();
            var outputContent = renameClassService.transform(RenameClassDTO.builder()
                    .className(sourceClass)
                    .newClassName(newClassName)
                    .build(), content);

            FileReader.verifyRefactoredFile(fileName, outputContent);
            System.out.println("Class renamed successfully!");
        } catch (RuntimeException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void moveMethod() {
        System.out.println("Enter source class name:");
        String sourceClass = scanner.nextLine();

        System.out.println("Enter target class name:");
        String targetClass = scanner.nextLine();

        System.out.println("Enter method name:");
        String methodName = scanner.nextLine();

        try {
            var content = FileReader.readFile(fileName);

            var moveMethodService = new MoveMethodService();
            var outputContent = moveMethodService.transform(MoveMethodDTO.builder()
                    .methodName(methodName)
                    .sourceClassName(sourceClass)
                    .targetClassName(targetClass)
                    .build(), content);

            FileReader.verifyRefactoredFile(fileName, outputContent);
            System.out.println("Method moved successfully!");
        } catch (RuntimeException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private int getIntegerInput() {
        int choice = -1;

        while (choice == -1) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        return choice;
    }
}
