package org.example.cmd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum CommandArgument {
    TRANSFORM_TYPE("--transformType="),
    FILE_NAME("--fileName="),
    SOURCE_CLASS("--source="),
    TARGET_CLASS("--target="),
    METHOD_NAME("--methodName="),
    NEW_METHOD_NAME("--newMethodName="),
    NEW_CLASS_NAME("--newClassName="),
    GUI_INTERACTION_MODE("--GUI");

    @Getter
    private final String value;

}
