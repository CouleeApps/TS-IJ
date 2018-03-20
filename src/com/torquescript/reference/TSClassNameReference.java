package com.torquescript.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.torquescript.TSIcons;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSClassExpr;
import com.torquescript.psi.TSTypes;
import com.torquescript.symbols.TSClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TSClassNameReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String name;

    private static TextRange getLiteralRange(TSClassExpr expr) {
        TextRange range = new TextRange(0, expr.getTextLength());
        if (expr.getFirstChild().getNode().getElementType().equals(TSTypes.STRATOM) && expr.getTextLength() > 1) {
            range = new TextRange(1, expr.getTextLength() - 1);
        }

        return range;
    }

    public TSClassNameReference(TSClassExpr expr) {
        super(expr, getLiteralRange(expr));
        name = expr.getName();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final List<TSClass> tsClasses = TSUtil.findScriptClass(project, name);
        List<ResolveResult> results = new ArrayList<>();
        for (TSClass tsClass : tsClasses) {
            results.add(new PsiElementResolveResult(tsClass.getElement()));
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
        Collection<TSClass> tsClasses = TSUtil.getScriptClassList(project);
        List<LookupElement> variants = new ArrayList<>();

        for (final TSClass tsClass : tsClasses) {
            if (tsClass.getName() != null && tsClass.getName().length() > 0) {
                variants.add(LookupElementBuilder.create(tsClass)
                        .withIcon(TSIcons.FILE)
                        .withCaseSensitivity(false)
                );
            }
        }

        return variants.toArray();
    }
}
