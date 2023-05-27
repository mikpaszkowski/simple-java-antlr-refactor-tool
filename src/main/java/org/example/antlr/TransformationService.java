package org.example.antlr;

public interface TransformationService<T> {

    String transform(T dto, String content);
}
