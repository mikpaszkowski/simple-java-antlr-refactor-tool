package org.example.cmd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum TransformType {

    MOVE_METHOD("moveMethod"),
    CHANGE_METHOD_NAME("changeMethodName"),
    CHANGE_CLASS_NAME("changeClassName");

    @Getter
    private final String value;

    public static TransformType transformTypeOfValue(String label) {
        for (TransformType e : values()) {
            if (e.value.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
