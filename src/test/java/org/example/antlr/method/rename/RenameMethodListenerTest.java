package org.example.antlr.method.rename;

import main.antlr4.org.example.antlr.Java8Lexer;
import main.antlr4.org.example.antlr.Java8Parser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.example.antlr.method.rename.RenameMethodDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.example.TestClassUtils.*;

class RenameMethodListenerTest {

    private static Stream<Arguments> refactorMethodNameTestProvider() {
        return Stream.of(
                Arguments.of("TestRefactorMethod1", "defined in the class body without further references"),
                Arguments.of("TestRefactorMethod2", "defined in the class body and invoked by other method"),
                Arguments.of("TestRefactorMethod3", "defined in the class body and invoked in scope of another class"),
                Arguments.of("TestRefactorMethod4", "defined in the class body, invoked in another class with already existing method of identical name")
        );
    }
//TODO check compilation of input/output
    @ParameterizedTest(name = "should change method name when: {1}")
    @MethodSource("refactorMethodNameTestProvider")
    public void shouldChangeClassNameAndAllItsReferences(String testFileName, String reason) throws IOException {
        //given
        Java8Lexer java8Lexer = new Java8Lexer(getCharStreamFromClassFile("before", testFileName));
        CommonTokenStream tokens = new CommonTokenStream(java8Lexer);
        Java8Parser java8Parser = new Java8Parser(tokens);
        ParseTree tree = java8Parser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        RenameMethodListener methodNameListener = new RenameMethodListener(rewriter, RenameMethodDTO.builder()
                .methodName(METHOD_NAME)
                .newMethodName(NEW_METHOD_NAME)
                .sourceClassName(CLASS_NAME)
                .build());
        //when
        walker.walk(methodNameListener, tree);

        //then
        String modifiedText = methodNameListener.getRewrittenText();
        Assertions.assertEquals(modifiedText, getCharStreamFromClassFile("after", testFileName).toString());
    }
}