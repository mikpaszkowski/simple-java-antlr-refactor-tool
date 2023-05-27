package org.example.antlr.method.move;

import lombok.Getter;
import main.antlr4.org.example.antlr.Java8Parser;
import main.antlr4.org.example.antlr.Java8ParserBaseListener;
import org.antlr.v4.runtime.TokenStreamRewriter;
@Getter
class MethodMoveListener extends Java8ParserBaseListener {
    private final TokenStreamRewriter rewriter;
    private boolean isInSourceClass;
    private boolean isInTargetClass;
    private boolean isMethodFound;
    private final MethodMoveDTO dto;
    private Java8Parser.MethodDeclarationContext methodBody;

    public MethodMoveListener(TokenStreamRewriter rewriter, MethodMoveDTO dto) {
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
            isInSourceClass = true;
        } else if (className.equals(dto.getTargetClassName())) {
            isInTargetClass = true;
        }
    }

    @Override
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        if (isInSourceClass && !isMethodFound) {
            String methodName = ctx.methodHeader().methodDeclarator().Identifier().getText();
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
        return isInSourceClass && !isInTargetClass;
    }

    public boolean isSourceClassMissing() {
        return !isInSourceClass && isInTargetClass;
    }

    public boolean isBothClassesMissing() {
        return !isInSourceClass && !isInTargetClass;
    }

    public boolean isMethodMissing() {
        return !isMethodFound;
    }
}
