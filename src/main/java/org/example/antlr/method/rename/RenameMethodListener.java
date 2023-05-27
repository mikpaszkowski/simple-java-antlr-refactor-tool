package org.example.antlr.method.rename;

import main.antlr4.org.example.antlr.Java8Parser;
import main.antlr4.org.example.antlr.Java8ParserBaseListener;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class RenameMethodListener extends Java8ParserBaseListener implements ParseTreeListener {

    private final TokenStreamRewriter rewriter;
    private final RenameMethodDTO refactorDTO;
    private boolean isParserInSourceClass;
    private boolean isSourceClassExist;
    private boolean isMethodExist;
    private final Map<String, String> classNameToInvokedMethodNameMap;

    public RenameMethodListener(TokenStreamRewriter rewriter, RenameMethodDTO dto) {
        this.rewriter = rewriter;
        this.refactorDTO = dto;
        this.isParserInSourceClass = false;
        this.classNameToInvokedMethodNameMap = new HashMap<>();
    }

    @Override
    public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        super.enterClassDeclaration(ctx);
        var classNameToken = ctx.normalClassDeclaration().Identifier().getSymbol();
        this.isParserInSourceClass = Objects.equals(classNameToken.getText(), this.refactorDTO.getSourceClassName());
        if(this.isParserInSourceClass) {
            this.isSourceClassExist = true;
        }
    }

    @Override
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        super.enterMethodDeclaration(ctx);
        var methodDeclarationToken = ctx.methodHeader().methodDeclarator().Identifier().getSymbol();
        if(Objects.equals(methodDeclarationToken.getText(), this.refactorDTO.getMethodName())){
            this.isMethodExist = true;
        }
        if (this.isParserInSourceClass) {
            changeNameOfToken(methodDeclarationToken);
        }
    }

    @Override
    public void enterMethodBody(Java8Parser.MethodBodyContext ctx) {
        super.enterMethodBody(ctx);
        ctx.block().blockStatements().blockStatement()
                .stream()
                .filter(this::isVariableDeclarationTypeOfSourceClassType)
                .map(Java8Parser.BlockStatementContext::localVariableDeclarationStatement)
                .map(Java8Parser.LocalVariableDeclarationStatementContext::localVariableDeclaration)
                .map(Java8Parser.LocalVariableDeclarationContext::variableDeclaratorList)
                .map(Java8Parser.VariableDeclaratorListContext::variableDeclarator)
                .map(list -> list.get(0))
                .map(Java8Parser.VariableDeclaratorContext::variableDeclaratorId)
                .map(Java8Parser.VariableDeclaratorIdContext::Identifier)
                .map(TerminalNode::getSymbol)
                .map(Token::getText)
                .findFirst().ifPresent(methodClassInstanceName -> this.classNameToInvokedMethodNameMap.put(this.refactorDTO.getSourceClassName(), methodClassInstanceName));
    }

    private boolean isVariableDeclarationTypeOfSourceClassType(Java8Parser.BlockStatementContext blockStatementContext) {
        var variableDeclarationStatementContext = blockStatementContext.localVariableDeclarationStatement();
        if (Objects.isNull(variableDeclarationStatementContext)) return false;
        if (variableDeclarationStatementContext.isEmpty()) return false;
        var className = variableDeclarationStatementContext.localVariableDeclaration().unannType().getText();
        return Objects.equals(className, this.refactorDTO.getSourceClassName());
    }

    @Override
    public void enterMethodInvocation(Java8Parser.MethodInvocationContext ctx) {
        super.enterMethodInvocation(ctx);
        var token = ctx.Identifier().getSymbol();
        if (this.isParserInSourceClass) {
            changeNameOfToken(token);
        } else {
            var typeNameCtx = ctx.typeName();
            if (typeNameCtx == null) return;
            var classInstanceNameOfInvokedMethod = typeNameCtx.Identifier().getSymbol().getText();
            if (!this.isParserInSourceClass && this.classNameToInvokedMethodNameMap.containsValue(classInstanceNameOfInvokedMethod)) {
                changeNameOfToken(token);
            }
        }

    }

    private void changeNameOfToken(Token token) {
        if (Objects.equals(token.getText(), this.refactorDTO.getMethodName())) {
            rewriter.replace(token, this.refactorDTO.getNewMethodName());
        }
    }

    public String getRewrittenText() {
        return rewriter.getText();
    }

    public boolean isSourceClassExist() {
        return isSourceClassExist;
    }

    public boolean isMethodExist() {
        return isMethodExist;
    }
}
