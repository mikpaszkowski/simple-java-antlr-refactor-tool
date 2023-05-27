package org.example.antlr.method.rename;

import main.antlr4.org.example.antlr.Java8Lexer;
import main.antlr4.org.example.antlr.Java8Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.example.antlr.TransformationService;
import org.example.antlr.exceptions.ClassElementMissingException;

public class RenameMethodService implements TransformationService<RenameMethodDTO> {

    @Override
    public String transform(RenameMethodDTO dto, String content) {
        Java8Lexer java8Lexer = new Java8Lexer(CharStreams.fromString(content));
        CommonTokenStream tokens = new CommonTokenStream(java8Lexer);
        Java8Parser java8Parser = new Java8Parser(tokens);
        ParseTree tree = java8Parser.compilationUnit();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        RenameMethodListener methodNameListener = new RenameMethodListener(rewriter, dto);
        //when
        ParseTreeWalker.DEFAULT.walk(methodNameListener, tree);

        if(!methodNameListener.isSourceClassExist()) {
            throw new ClassElementMissingException("No source class: " + dto.getSourceClassName() + " present.");
        } else if (!methodNameListener.isMethodExist()){
            throw new ClassElementMissingException("No method: " + dto.getMethodName() + " found.");
        }

        return methodNameListener.getRewrittenText();
    }
}
