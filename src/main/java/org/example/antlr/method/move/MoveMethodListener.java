package org.example.antlr.method.move;

import lombok.Getter;
import main.antlr4.org.example.antlr.Java8Parser;
import main.antlr4.org.example.antlr.Java8ParserBaseListener;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.example.antlr.exceptions.MethodAlreadyDefineInTargetClassException;

import java.util.Objects;

@Getter
class MoveMethodListener extends Java8ParserBaseListener {
    private final TokenStreamRewriter rewriter;
    private boolean isInSourceClass;
    private boolean isInTargetClass;
    private boolean isMethodFound;
    private boolean isTargetClassExist;
    private boolean isSourceClassExist;
    private final MoveMethodDTO dto;
    private Java8Parser.MethodDeclarationContext methodBody;

    public MoveMethodListener(TokenStreamRewriter rewriter, MoveMethodDTO dto) {
        this.rewriter = rewriter;
        this.dto = dto;
        this.isInSourceClass = false;
        this.isInTargetClass = false;
        this.isMethodFound = false;
    }

    @Override
    public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        super.enterClassDeclaration(ctx);
        String className = ctx.normalClassDeclaration().Identifier().getText();
        if (className.equals(dto.getSourceClassName())) {
            this.isSourceClassExist = true;
            isInSourceClass = true;
        } else if (className.equals(dto.getTargetClassName())) {
            this.isTargetClassExist = true;
            isInTargetClass = true;
        }
    }

    @Override
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        String methodName = ctx.methodHeader().methodDeclarator().Identifier().getText();
        if(isInTargetClass && Objects.equals(methodName, this.dto.getMethodName())) {
            throw new MethodAlreadyDefineInTargetClassException("Method of name: " + dto.getMethodName() + " is already defined in target class: " + dto.getTargetClassName());
        }
        if (isInSourceClass && !isMethodFound) {
            if (methodName.equals(this.dto.getMethodName())) {
                isMethodFound = true;
                // Save the method declaration context
                this.methodBody = ctx;
                // Remove the method declaration from the source class
                rewriter.delete(ctx.start, ctx.stop);
            }
        }
    }

    @Override
    public void exitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        if (isInTargetClass && methodBody != null) {
            rewriter.insertAfter(ctx.stop.getTokenIndex() - 1, getFormattedMethodBody());
            methodBody = null;
        }
        this.isInSourceClass = false;
        this.isInTargetClass = false;
    }

    public String getRewrittenText() {
        return rewriter.getText();
    }

    private String getFormattedMethodBody() {
        var methodModifier = methodBody.methodModifier().get(0).getText();
        var methodReturnType = methodBody.methodHeader().result().getText();
        var methodName = methodBody.methodHeader().methodDeclarator().getText();
        var methodBodyContent = methodBody.methodBody().getText();
        return methodModifier + " " + methodReturnType + " " + methodName + " " + methodBodyContent + "\n";
    }

    public boolean isTargetClassMissing() {
        return isSourceClassExist && !isTargetClassExist;
    }

    public boolean isSourceClassMissing() {
        return !isSourceClassExist && isTargetClassExist;
    }

    public boolean isBothClassesMissing() {
        return !isSourceClassExist && !isTargetClassExist;
    }

    public boolean isMethodMissing() {
        return !isMethodFound;
    }
}
