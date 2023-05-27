package org.example.antlr.clazz;

import main.antlr4.org.example.antlr.Java8Lexer;
import main.antlr4.org.example.antlr.Java8Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.example.antlr.TransformationService;
import org.example.antlr.exceptions.ClassElementMissingException;

public class RenameClassService implements TransformationService<RenameClassDTO> {

    @Override
    public String transform(RenameClassDTO dto, String content) {
        Java8Lexer java8Lexer = new Java8Lexer(CharStreams.fromString(content));
        CommonTokenStream tokens = new CommonTokenStream(java8Lexer);
        Java8Parser java8Parser = new Java8Parser(tokens);
        ParseTree tree = java8Parser.compilationUnit();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        RenameClassListener classListener = new RenameClassListener(rewriter, dto);
        //when
        ParseTreeWalker.DEFAULT.walk(classListener, tree);

        if(!classListener.isSourceClassExist()) {
            throw new ClassElementMissingException("No source class: " + dto.getClassName() + " present");
        }

        return classListener.getRewrittenText();
    }
}
