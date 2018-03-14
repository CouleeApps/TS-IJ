package com.torquescript.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.util.ProcessingContext;
import com.torquescript.TSFunctionType;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpEngineMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TSGlobalCallCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        //All global functions
        Project project = parameters.getOriginalFile().getProject();
        Collection<TSFnDeclStmt> functions = TSUtil.getFunctionList(project);
        for (TSFnDeclStmt function : functions) {
            if (function.getFunctionType() != TSFunctionType.GLOBAL)
                continue;

            result.addElement(
                    LookupElementBuilder.create(function)
                            .withCaseSensitivity(false)
                            .withTailText(function.getArgList())
                            .withInsertHandler(TSCaseCorrectingInsertHandler.INSTANCE)
            );
        }
        Collection<TSClassDumpEngineMethod> engineMethods = TSUtil.getEngineMethodList(project);
        for (TSClassDumpEngineMethod method : engineMethods) {
            if (method.getFunctionType() != TSFunctionType.GLOBAL)
                continue;
            result.addElement(
                    LookupElementBuilder.create(method)
                            .withCaseSensitivity(false)
                            .withTailText(method.getArgList())
                            .withInsertHandler(TSCaseCorrectingInsertHandler.INSTANCE)
            );
        }
    }
}
