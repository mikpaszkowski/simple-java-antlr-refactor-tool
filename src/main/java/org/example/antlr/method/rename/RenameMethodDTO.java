package org.example.antlr.method.rename;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RenameMethodDTO {

    private String methodName;
    private String newMethodName;
    private String sourceClassName;
}
