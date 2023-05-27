package org.example.antlr.clazz;

import org.example.antlr.exceptions.ClassElementMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.example.TestClassUtils.getCharStreamFromClassFile;

class RenameClassServiceTest {

    @Test
    void shouldThrowExceptionWhenNoClassFound() throws IOException {
        var renameClassService = new RenameClassService();
        var content = getCharStreamFromClassFile("invalid", "SampleClass");
        Assertions.assertThrows(ClassElementMissingException.class, () -> renameClassService.transform(RenameClassDTO.builder()
                .className("NonExistingClass")
                .newClassName("NewClassName")
                .build(), content.toString()), "No source class: NonExistingClass present");
    }
}