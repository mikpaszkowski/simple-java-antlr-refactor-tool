package org.example.antlr.clazz;


import main.antlr4.org.example.antlr.Java8Parser;
import main.antlr4.org.example.antlr.Java8ParserBaseListener;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Objects;
import java.util.Optional;

class RenameClassListener extends Java8ParserBaseListener implements ParseTreeListener {

    private final TokenStreamRewriter rewriter;

    private final String className;
    private final String newClassName;

    public RenameClassListener(TokenStreamRewriter rewriter, RenameClassDTO dto) {
        this.rewriter = rewriter;
        this.newClassName = dto.getNewClassName();
        this.className = dto.getClassName();
    }

    @Override
    public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        super.enterClassDeclaration(ctx);
        TerminalNode node = ctx.normalClassDeclaration().Identifier();
        //get the current class name
        if (Objects.equals(className, node.getText())) {
            //replace the class name in the declaration and all references to it
            Token classNameToken = node.getSymbol();
            this.rewriter.replace(classNameToken, newClassName);
        }
    }

    @Override
    public void enterSingleTypeImportDeclaration(Java8Parser.SingleTypeImportDeclarationContext ctx) {
        super.enterSingleTypeImportDeclaration(ctx);
        //replace any imports that references the old class name with the new one
        var typeNameContext = ctx.typeName();
        var identifier = Optional.ofNullable(typeNameContext)
                .map(Java8Parser.TypeNameContext::Identifier)
                .orElse(null);
        if (identifier != null && Objects.equals(identifier.getText(), this.className)) {
            rewriter.replace(identifier.getSymbol(), newClassName);
        }
    }

    @Override
    public void enterImportDeclaration(Java8Parser.ImportDeclarationContext ctx) {
        super.enterImportDeclaration(ctx);
        //replace any imports that references the old class name with the new one
        var importNameToken = ctx.singleStaticImportDeclaration();
        var identifier = Optional.ofNullable(importNameToken)
                .map(Java8Parser.SingleStaticImportDeclarationContext::Identifier)
                .orElse(null);
        if (identifier != null && Objects.equals(identifier.getText(), this.className)) {
            rewriter.replace(identifier.getSymbol(), newClassName);
        }
    }

    @Override
    public void enterTypeImportOnDemandDeclaration(Java8Parser.TypeImportOnDemandDeclarationContext ctx) {
        super.enterTypeImportOnDemandDeclaration(ctx);
        var importClassNameToken = ctx.packageOrTypeName().Identifier().getSymbol();
        if(Objects.equals(importClassNameToken.getText(), this.className)){
            rewriter.replace(importClassNameToken, this.newClassName);
        }
    }

    @Override
    public void enterStaticImportOnDemandDeclaration(Java8Parser.StaticImportOnDemandDeclarationContext ctx) {
        super.enterStaticImportOnDemandDeclaration(ctx);
        var importClassNameToken = ctx.typeName().Identifier().getSymbol();
        if(Objects.equals(importClassNameToken.getText(), this.className)){
            rewriter.replace(importClassNameToken, this.newClassName);
        }
    }

    @Override
    public void enterSingleStaticImportDeclaration(Java8Parser.SingleStaticImportDeclarationContext ctx) {
        super.enterSingleStaticImportDeclaration(ctx);
    }

    @Override
    public void enterTypeDeclaration(Java8Parser.TypeDeclarationContext ctx) {
        super.enterTypeDeclaration(ctx);
        //replace any type declaration that references the old class name
        var typeDeclarationNameToken = ctx.classDeclaration().normalClassDeclaration().Identifier();
        if (Objects.equals(typeDeclarationNameToken.getText(), className)) {
            rewriter.replace(typeDeclarationNameToken.getSymbol(), newClassName);
        }
    }

    @Override
    public void enterConstructorDeclaration(Java8Parser.ConstructorDeclarationContext ctx) {
        super.enterConstructorDeclaration(ctx);
        //replace any imports that references the old class name with the new one
        var importNameToken = ctx.constructorDeclarator().simpleTypeName();
        var identifier = Optional.ofNullable(importNameToken)
                .map(Java8Parser.SimpleTypeNameContext::Identifier)
                .orElse(null);
        if (identifier != null && Objects.equals(identifier.getText(), this.className)) {
            rewriter.replace(identifier.getSymbol(), newClassName);
        }
    }

    @Override
    public void enterConstructorBody(Java8Parser.ConstructorBodyContext ctx) {
        super.enterConstructorBody(ctx);
        var blockStatementContextList = ctx.blockStatements().blockStatement();
        if(blockStatementContextList.isEmpty()) return;
        blockStatementContextList.forEach(this::replaceVariableType);
    }

    @Override
    public void enterFieldDeclaration(Java8Parser.FieldDeclarationContext ctx) {
        super.enterFieldDeclaration(ctx);
        //retrieve the field type and name in order to replace it with new class name if it is the one under refactoring
        var variableTypeContext = ctx.unannType();
        if (Objects.equals(variableTypeContext.getText(), this.className)) {
            rewriter.replace(variableTypeContext.getStart(), variableTypeContext.getStop(), this.newClassName);
        }
    }

    @Override
    public void enterExplicitConstructorInvocation(Java8Parser.ExplicitConstructorInvocationContext ctx) {
        super.enterExplicitConstructorInvocation(ctx);
    }

    @Override
    public void enterMethodBody(Java8Parser.MethodBodyContext ctx) {
        super.enterMethodBody(ctx);
        if (isMethodBlockStatementEmpty(ctx)) return;
        var blockStatementContextList = ctx.block().blockStatements().blockStatement();
        blockStatementContextList.forEach(this::replaceVariableType);
    }

    @Override
    public void enterClassInstanceCreationExpression(Java8Parser.ClassInstanceCreationExpressionContext ctx) {
        super.enterClassInstanceCreationExpression(ctx);
        var constructorNameNode = ctx.Identifier(0);
        if(Objects.isNull(constructorNameNode)) return;
        if(Objects.equals(constructorNameNode.getText(), this.className)){
            rewriter.replace(constructorNameNode.getSymbol(), this.newClassName);
        }
    }

    @Override
    public void exitClassInstanceCreationExpression_lfno_primary(Java8Parser.ClassInstanceCreationExpression_lfno_primaryContext ctx) {
        super.exitClassInstanceCreationExpression_lfno_primary(ctx);
        var constructorNameNode = ctx.Identifier(0);
        if(Objects.isNull(constructorNameNode)) return;
        if(Objects.equals(constructorNameNode.getText(), this.className)){
            rewriter.replace(constructorNameNode.getSymbol(), this.newClassName);
        }
    }

    @Override
    public void enterClassInstanceCreationExpression_lf_primary(Java8Parser.ClassInstanceCreationExpression_lf_primaryContext ctx) {
        super.enterClassInstanceCreationExpression_lf_primary(ctx);
        var constructorNameNode = ctx.Identifier();
        if(Objects.isNull(constructorNameNode)) return;
        if(Objects.equals(constructorNameNode.getText(), this.className)){
            rewriter.replace(constructorNameNode.getSymbol(), this.newClassName);
        }
    }

    @Override
    public void enterMethodInvocation(Java8Parser.MethodInvocationContext ctx) {
        super.enterMethodInvocation(ctx);
    }

    private void replaceVariableType(Java8Parser.BlockStatementContext blockStatementContext) {
        var localVarDeclaration = blockStatementContext.localVariableDeclarationStatement();
        if (Objects.isNull(localVarDeclaration)) return;
        var variableTypeContext = localVarDeclaration.localVariableDeclaration().unannType();
        if (Objects.equals(variableTypeContext.getText(), this.className)) {
            rewriter.replace(variableTypeContext.getStart(), variableTypeContext.getStop(), this.newClassName);
        }
    }

    private boolean isMethodBlockStatementEmpty(Java8Parser.MethodBodyContext ctx) {
        return ctx.block().blockStatements().blockStatement().isEmpty();
    }


    public String getRewrittenText() {
        return rewriter.getText();
    }
}
