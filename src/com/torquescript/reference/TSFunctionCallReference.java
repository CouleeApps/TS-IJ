package com.torquescript.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.TSFunctionType;
import com.torquescript.TSIcons;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSCallExpr;
import com.torquescript.psi.TSFnDeclStmt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TSFunctionCallReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String name;
    private TSCallExpr call;
    private TSFunctionType type;

    public TSFunctionCallReference(TSCallExpr call, PsiElement nameNode) {
        super(call, TSUtil.getRangeOfChild(call, nameNode));
        this.call = call;
        this.type = this.call.getFunctionType();
        name = nameNode.getText();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final List<TSFnDeclStmt> functions = TSUtil.findFunction(project, name);
        List<ResolveResult> results = new ArrayList<>();
        for (TSFnDeclStmt function : functions) {
            //Because you can GLOBAL_NS call a METHOD but not a GLOBAL unless parent
            if ((function.getFunctionType() == TSFunctionType.GLOBAL) == (type == TSFunctionType.GLOBAL)) {
                results.add(new PsiElementResolveResult(function));
            }
        }
        return results.toArray(new ResolveResult[results.size()]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length == 1 ? results[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        Project project = myElement.getProject();
        Collection<TSFnDeclStmt> functions = TSUtil.getFunctionList(project);
        List<LookupElement> variants = new ArrayList<>();

        for (final TSFnDeclStmt function : functions) {
            if (function.getFunctionName() != null && function.getFunctionName().length() > 0) {
                variants.add(LookupElementBuilder.create(function)
                        .withIcon(TSIcons.FILE)
                        .withTypeText(function.getContainingFile().getName())
                        .withCaseSensitivity(false)

                );
            }
        }

        return variants.toArray();
    }
}
