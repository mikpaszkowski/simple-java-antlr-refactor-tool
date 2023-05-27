package org.example.antlr.method.move;

import main.antlr4.org.example.antlr.Java8Lexer;
import main.antlr4.org.example.antlr.Java8Parser;
import org.example.antlr.exceptions.ThrowingErrorListener;
import org.example.antlr.exceptions.ClassElementMissingException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class MethodMoveService {

    public static String moveMethod(MethodMoveDTO dto, String sourceCode) {
        // Create the lexer and parser
        Java8Lexer lexer = new Java8Lexer(CharStreams.fromString(sourceCode));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);

        // Enable parsing error listener to throw exception on syntax errors
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowingErrorListener());

        // Get the parse tree
        Java8Parser.CompilationUnitContext parseTree;
        try {
            parseTree = parser.compilationUnit();
        } catch (ParseCancellationException ex) {
            throw new IllegalArgumentException("Invalid Java source code: " + ex.getMessage());
        }

        // Create the token stream rewriter
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        // Create the listener
        MethodMoveListener listener = new MethodMoveListener(rewriter, dto);

        // Traverse the parse tree with the listener
        ParseTreeWalker.DEFAULT.walk(listener, parseTree);

        if (listener.isTargetClassMissing()) {
            throw new ClassElementMissingException("No target class: " + dto.getTargetClassName() + " present");
        } else if (listener.isSourceClassMissing()) {
            throw new ClassElementMissingException("No source class: " + dto.getSourceClassName() + " present");
        } else if (listener.isBothClassesMissing()) {
            throw new ClassElementMissingException("No given classes found");
        } else if(listener.isMethodMissing()){
            throw new ClassElementMissingException("No method: " + dto.getMethodName() + " found");
        }

        // Return the modified source code
        return rewriter.getText();
    }
}
