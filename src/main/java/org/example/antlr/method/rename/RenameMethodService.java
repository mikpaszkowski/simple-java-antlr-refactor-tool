package org.example.antlr.method.rename;

import main.antlr4.org.example.antlr.Java8Lexer;
import main.antlr4.org.example.antlr.Java8Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class RenameMethodService {

    public static String renameMethod(RenameMethodDTO dto, String sourceCode) {
        Java8Lexer java8Lexer = new Java8Lexer(CharStreams.fromString(sourceCode));
        CommonTokenStream tokens = new CommonTokenStream(java8Lexer);
        Java8Parser java8Parser = new Java8Parser(tokens);
        ParseTree tree = java8Parser.compilationUnit();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        RenameMethodListener methodNameListener = new RenameMethodListener(rewriter, dto);
        //when
        ParseTreeWalker.DEFAULT.walk(methodNameListener, tree);

        return methodNameListener.getRewrittenText();
    }
}
