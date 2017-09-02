package com.torquescript.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSFnDeclStmt;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TSMethodCallCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        PsiElement caller = parameters.getPosition().getPrevSibling().getPrevSibling();
        String ns = TSUtil.getElementNamespace(caller);

        //All global functions
        Project project = parameters.getOriginalFile().getProject();
        Collection<TSFnDeclStmt> functions = TSUtil.getFunctionList(project);
        for (TSFnDeclStmt function : functions) {
            if (function.isGlobal())
                continue;

            result.addElement(
                    LookupElementBuilder.create(function.getFunctionName())
                            .withCaseSensitivity(false)
                            .withPresentableText(function.getNamespace() + "." + function.getFunctionName())
                            .withBoldness(ns != null && function.getNamespace().equalsIgnoreCase(ns))
                            .withTailText(function.getArgList())
                            .withInsertHandler(TSCaseCorrectingInsertHandler.INSTANCE)
            );
        }
    }
}
