package org.example.cmd;

import org.example.antlr.clazz.RenameClassDTO;
import org.example.antlr.clazz.RenameClassService;
import org.example.antlr.method.move.MoveMethodDTO;
import org.example.antlr.method.move.MoveMethodService;
import org.example.antlr.method.rename.RenameMethodDTO;
import org.example.antlr.method.rename.RenameMethodService;
import org.example.file.FileReader;

import java.io.IOException;
import java.util.Objects;

import static org.example.cmd.TransformType.*;

public class CmdService {

    public static void handleCmdArgs(String[] args) {
        TransformType transformType = null;
        String cliMode = null;
        String fileName = null;
        String sourceClass = null;
        String targetClass = null;
        String newMethodName = null;
        String methodName = null;
        String newClassName = null;

        // Parse named arguments
        for (String arg : args) {
            if (arg.startsWith(CommandArgument.CLI_INTERACTION_MODE.getValue())) {
                cliMode = "CLI";
            } else if (arg.startsWith(CommandArgument.TRANSFORM_TYPE.getValue())) {
                transformType = TransformType.transformTypeOfValue(arg.substring(16));
            } else if (arg.startsWith(CommandArgument.FILE_NAME.getValue())) {
                fileName = arg.substring(11);
            } else if (arg.startsWith(CommandArgument.SOURCE_CLASS.getValue())) {
                sourceClass = arg.substring(9);
            } else if (arg.startsWith(CommandArgument.TARGET_CLASS.getValue())) {
                targetClass = arg.substring(9);
            } else if (arg.startsWith(CommandArgument.METHOD_NAME.getValue())) {
                methodName = arg.substring(13);
            } else if (arg.startsWith(CommandArgument.NEW_CLASS_NAME.getValue())) {
                newClassName = arg.substring(15);
            } else if (arg.startsWith(CommandArgument.NEW_METHOD_NAME.getValue())) {
                newMethodName = arg.substring(16);
            }
        }
        if(Objects.nonNull(cliMode)) {
            var cliService = new CLIService();
            cliService.run();
        }

        if (fileName == null) {
            System.out.println("Missing input file. Provide --fileName");
            return;
        }

        switch (Objects.requireNonNull(transformType)) {
            case CHANGE_CLASS_NAME:
                if (sourceClass == null || newClassName == null) {
                    System.out.println("Missing arguments for transformation of type: classNameChange.");
                    System.out.println("Please provide --source, --newClassName");
                    return;
                }
                break;
            case CHANGE_METHOD_NAME:
                if (sourceClass == null || newMethodName == null || methodName == null) {
                    System.out.println("Missing arguments for transformation of type: changeMethodName.");
                    System.out.println("Please provide --source, --newMethodName");
                    return;
                }
                break;
            case MOVE_METHOD:
                if (sourceClass == null || targetClass == null || methodName == null) {
                    System.out.println("Missing arguments for transformation of type: moveMethod.");
                    System.out.println("Please provide --source, --target, --methodName");
                    return;
                }
                break;
            default:
                System.out.println("Action: " + transformType + " not supported");
        }

        try {
            // Read the file contents
            String inputContent = FileReader.readFile(fileName);
            String outputContent = null;

            if (transformType == MOVE_METHOD) {
                // Execute the method move
                MoveMethodService moveMethodService = new MoveMethodService();
                outputContent = moveMethodService.transform(MoveMethodDTO.builder()
                        .sourceClassName(sourceClass)
                        .targetClassName(targetClass)
                        .methodName(methodName)
                        .build(), inputContent);

                // Print the result
                System.out.println(outputContent);
            } else if (transformType == CHANGE_METHOD_NAME) {
                RenameMethodService renameMethodService = new RenameMethodService();
                outputContent = renameMethodService.transform(RenameMethodDTO.builder()
                        .sourceClassName(sourceClass)
                        .newMethodName(newMethodName)
                        .methodName(methodName)
                        .build(), inputContent);

                System.out.println(outputContent);
            } else if (transformType == CHANGE_CLASS_NAME) {
                RenameClassService renameClassService = new RenameClassService();
                outputContent = renameClassService.transform(RenameClassDTO.builder()
                        .className(sourceClass)
                        .newClassName(newClassName)
                        .build(), inputContent);

                System.out.println(outputContent);
            }
            var newFileName = transformType == CHANGE_CLASS_NAME ? fileName.replace(sourceClass, newClassName) : fileName;
            FileReader.verifyRefactoredFile(newFileName, outputContent);

        } catch (RuntimeException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
