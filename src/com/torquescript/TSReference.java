package com.torquescript;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.torquescript.psi.TSFnDeclStmt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TSReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String name;

    public TSReference(PsiElement element, TextRange rangeInElement) {
        super(element, rangeInElement);
        name = element.getText().substring(rangeInElement.getStartOffset(), rangeInElement.getEndOffset());
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final List<TSFnDeclStmt> functions = TSUtil.findFunctions(project, name);
        List<ResolveResult> results = new ArrayList<>();
        for (TSFnDeclStmt function : functions) {
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
        List<TSFnDeclStmt> functions = TSUtil.findFunctions(project);
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
