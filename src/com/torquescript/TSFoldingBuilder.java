package com.torquescript;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.psi.TSPackageDecl;
import com.torquescript.psi.TSStmtBlock;
import com.torquescript.psi.TSTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TSFoldingBuilder extends FoldingBuilderEx {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        //Folding of block statements
        Collection<TSStmtBlock> blocks = PsiTreeUtil.findChildrenOfType(root, TSStmtBlock.class);
        for (final TSStmtBlock block : blocks) {
            if (!block.getFirstChild().getNode().getElementType().equals(TSTypes.BRACE_OPEN))
                continue;

            descriptors.add(new FoldingDescriptor(
                    block.getNode(),
                    new TextRange(block.getFirstChild().getTextOffset() + 1, block.getLastChild().getTextOffset()),
                    null
            ) {
                @Override
                public String getPlaceholderText() {
                    return "...";
                }
            });
        }

        //Function folding for the entire function
        Collection<TSFnDeclStmt> functions = PsiTreeUtil.findChildrenOfType(root, TSFnDeclStmt.class);
        for (final TSFnDeclStmt function : functions) {
            descriptors.add(new FoldingDescriptor(
                    function.getNode(),
                    //Entire function is the folded region, not just braces
                    new TextRange(function.getFirstChild().getTextOffset(), function.getLastChild().getTextOffset() + function.getLastChild().getTextLength()),
                    null
            ) {
                @Nullable
                @Override
                public String getPlaceholderText() {
                    switch (function.getFunctionType()) {
                        case GLOBAL:
                            return "function " +  function.getFunctionName();
                        case GLOBAL_NS:
                            return "function " +  function.getNamespace() + "::" + function.getFunctionName();
                        case METHOD:
                            return "function " +  function.getNamespace() + "::" + function.getFunctionName();
                    }
                    return "function " +  function.getFunctionName();
                }
            });
        }

        //Function folding for the entire function
        Collection<TSPackageDecl> packages = PsiTreeUtil.findChildrenOfType(root, TSPackageDecl.class);
        for (final TSPackageDecl pkg : packages) {
            descriptors.add(new FoldingDescriptor(
                    pkg.getNode(),
                    //Entire pkg is the folded region, not just braces
                    new TextRange(pkg.getFirstChild().getTextOffset(), pkg.getLastChild().getTextOffset() + pkg.getLastChild().getTextLength()),
                    null
            ) {
                @Nullable
                @Override
                public String getPlaceholderText() {
                    return "package " + pkg.getName();
                }
            });
        }



        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}
