package com.torquescript.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import com.intellij.util.indexing.FileBasedIndex;
import com.torquescript.TSFileType;
import com.torquescript.TSFunctionType;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpEngineMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TSGlobalNSCallCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        String namespace = parameters.getPosition().getPrevSibling().getPrevSibling().getText();

        Project project = parameters.getOriginalFile().getProject();

        Collection<TSClassDumpEngineMethod> engineMethods = TSUtil.getEngineMethodList(project);
        for (TSClassDumpEngineMethod method : engineMethods) {
            if (method.getFunctionType() == TSFunctionType.GLOBAL)
                continue;
            if (namespace != null && !method.getNamespace().equalsIgnoreCase(namespace))
                continue;
            result.addElement(
                    LookupElementBuilder.create(method.getFunctionName())
                            .withCaseSensitivity(false)
                            .withPresentableText(method.getNamespace() + "::" + method.getFunctionName())
                            .withTailText(method.getArgList())
                            .withInsertHandler(TSCaseCorrectingInsertHandler.INSTANCE)
            );
        }

        Collection<TSFnDeclStmt> functions = TSUtil.getFunctionList(project);
        for (TSFnDeclStmt function : functions) {
            if (function.getFunctionType() == TSFunctionType.GLOBAL)
                continue;
            if (namespace != null && !function.getNamespace().equalsIgnoreCase(namespace))
                continue;

            result.addElement(
                    LookupElementBuilder.create(function.getFunctionName())
                            .withCaseSensitivity(false)
                            .withPresentableText(function.getNamespace() + "::" + function.getFunctionName())
                            .withTailText(function.getArgList())
                            .withInsertHandler(TSCaseCorrectingInsertHandler.INSTANCE)
            );
        }
    }
}
