package com.torquescript.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.torquescript.TSIcons;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSVarExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TSGlobalVariableReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String name;
    public TSGlobalVariableReference(TSVarExpr var) {
        super(var, new TextRange(0, var.getTextLength()));
        name = var.getName();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final List<TSVarExpr> functions = TSUtil.findGlobal(project, name);
        List<ResolveResult> results = new ArrayList<>();
        for (TSVarExpr function : functions) {
            results.add(new PsiElementResolveResult(function));
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
        Collection<TSVarExpr> globals = TSUtil.getGlobalList(project);
        List<LookupElement> variants = new ArrayList<>();

        for (final TSVarExpr global : globals) {
            if (global.getName() != null && global.getName().length() > 0) {
                variants.add(LookupElementBuilder.create(global)
                        .withIcon(TSIcons.FILE)
                        .withTypeText(global.getContainingFile().getName())
                        .withCaseSensitivity(false)

                );
            }
        }

        return variants.toArray();
    }
}
