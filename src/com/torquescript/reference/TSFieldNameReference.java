package com.torquescript.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.TSIcons;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSClassExpr;
import com.torquescript.psi.TSFieldNameExpr;
import com.torquescript.psi.TSObjectExpr;
import com.torquescript.psi.TSTypes;
import com.torquescript.symbols.TSClass;
import com.torquescript.symbols.TSClassMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TSFieldNameReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String name;
    private TSObjectExpr object;
    private String objectClassName;

    public TSFieldNameReference(TSFieldNameExpr expr) {
        super(expr, expr.getTextRange());
        name = expr.getName();
        object = PsiTreeUtil.getParentOfType(expr, TSObjectExpr.class);
        if (object != null) {
            objectClassName = object.getClassName();
        }
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final List<TSClass> tsClasses = TSUtil.findScriptClass(project, objectClassName);
        List<ResolveResult> results = new ArrayList<>();
        for (TSClass tsClass : tsClasses) {
            //Add references for the class's fields
            List<TSClassMember> members = tsClass.getMembers();
            if (members != null) {
                for (TSClassMember member : members) {
                    if (member.getName().equalsIgnoreCase(name)) {
                        results.add(new PsiElementResolveResult(member.getElement()));
                    }
                }
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
        final List<TSClass> tsClasses = TSUtil.findScriptClass(project, objectClassName);
        List<LookupElement> variants = new ArrayList<>();

        for (final TSClass tsClass : tsClasses) {
            //Add variants for the class's fields
            List<TSClassMember> members = tsClass.getMembers();
            if (members != null) {
                for (TSClassMember member : members) {
                    if (member.getName() != null && member.getName().length() > 0) {
                        variants.add(LookupElementBuilder.create(member)
                                .withIcon(TSIcons.FILE)
                                .withCaseSensitivity(false)
                        );
                    }
                }
            }
        }

        return variants.toArray();
    }
}
