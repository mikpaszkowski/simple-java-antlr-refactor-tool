package org.example.antlr.clazz;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RenameClassDTO {

    private String className;
    private String newClassName;
}
