package com.torquescript.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.torquescript.TSFunctionType;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpEngineMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TSMethodCallCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        PsiElement caller = parameters.getPosition().getPrevSibling().getPrevSibling();
        String ns = TSUtil.getElementNamespace(caller);
        Project project = parameters.getOriginalFile().getProject();

        Collection<TSClassDumpEngineMethod> engineMethods = TSUtil.getEngineMethodList(project);
        for (TSClassDumpEngineMethod method : engineMethods) {
            if (method.getFunctionType() == TSFunctionType.GLOBAL)
                continue;

            result.addElement(
                    LookupElementBuilder.create(method.getFunctionName())
                            .withCaseSensitivity(false)
                            .withPresentableText(method.getNamespace() + "." + method.getFunctionName())
                            .withBoldness(ns != null && method.getNamespace().equalsIgnoreCase(ns))
                            .withTailText(method.getArgList())
                            .withInsertHandler(TSCaseCorrectingInsertHandler.INSTANCE)
            );
        }

        Collection<TSFnDeclStmt> functions = TSUtil.getFunctionList(project);
        for (TSFnDeclStmt function : functions) {
            if (function.getFunctionType() == TSFunctionType.GLOBAL)
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
