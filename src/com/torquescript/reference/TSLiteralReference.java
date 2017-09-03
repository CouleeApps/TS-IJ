package com.torquescript.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.torquescript.TSIcons;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSLiteralExpr;
import com.torquescript.psi.TSObjectExpr;
import com.torquescript.psi.TSTypes;
import com.torquescript.psi.TSVarExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TSLiteralReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String name;

    private static TextRange getLiteralRange(TSLiteralExpr expr) {
        TextRange range = new TextRange(0, expr.getTextLength());
        if (expr.getFirstChild().getNode().getElementType().equals(TSTypes.STRATOM) && expr.getTextLength() > 1) {
            range = new TextRange(1, expr.getTextLength() - 1);
        }

        return range;
    }

    public TSLiteralReference(TSLiteralExpr expr) {
        super(expr, getLiteralRange(expr));
        name = expr.getName();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final List<TSObjectExpr> functions = TSUtil.findObject(project, name);
        List<ResolveResult> results = new ArrayList<>();
        for (TSObjectExpr function : functions) {
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
        Collection<TSObjectExpr> globals = TSUtil.getObjectList(project);
        List<LookupElement> variants = new ArrayList<>();

        for (final TSObjectExpr global : globals) {
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
