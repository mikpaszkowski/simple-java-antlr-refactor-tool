package org.example.antlr.method.move;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoveMethodDTO {

    private String methodName;
    private String targetClassName;
    private String sourceClassName;
}
