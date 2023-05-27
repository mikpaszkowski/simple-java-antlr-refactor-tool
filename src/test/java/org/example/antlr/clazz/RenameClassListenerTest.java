package org.example.antlr.clazz;

import main.antlr4.org.example.antlr.Java8Lexer;
import main.antlr4.org.example.antlr.Java8Parser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.example.TestClassUtils;
import org.example.antlr.clazz.RenameClassDTO;
import org.example.antlr.clazz.RenameClassListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

public class RenameClassListenerTest {

    private static Stream<Arguments> refactorClassNameTestProvider() {
        return Stream.of(
                Arguments.of("TestRefactorClass1", "defined as nested class with constructor invocation"),
                Arguments.of("TestRefactorClass2", "defined empty class"),
                Arguments.of("TestRefactorClass3", "regularly imported with constructor invocation"),
                Arguments.of("TestRefactorClass4", "imported only nested classes with constructor invocation"),
                Arguments.of("TestRefactorClass5", "statically imported with constructor invocation")
        );
    }

    @ParameterizedTest(name = "should change class name when: {1}")
    @MethodSource("refactorClassNameTestProvider")
    public void shouldChangeClassNameAndAllItsReferences(String testFileName, String reason) throws IOException {
        //given
        Java8Lexer java8Lexer = new Java8Lexer(TestClassUtils.getCharStreamFromClassFile("before", testFileName));
        CommonTokenStream tokens = new CommonTokenStream(java8Lexer);
        Java8Parser java8Parser = new Java8Parser(tokens);
        ParseTree tree = java8Parser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        RenameClassListener classNameListener = new RenameClassListener(rewriter, RenameClassDTO.builder()
                .className(TestClassUtils.CLASS_NAME)
                .newClassName(TestClassUtils.NEW_CLASS_NAME)
                .build());
        //when
        walker.walk(classNameListener, tree);

        //then
        String modifiedText = classNameListener.getRewrittenText();
        Assertions.assertEquals(modifiedText, TestClassUtils.getCharStreamFromClassFile("after", testFileName).toString());
    }

}