package com.torquescript.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.indexing.FileBasedIndex;
import com.torquescript.TSFile;
import com.torquescript.TSFileType;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.psi.TSObjectExpr;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class TSObjectNameCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        Collection<TSObjectExpr> objects = TSUtil.getObjectList(parameters.getPosition().getProject());
        for (TSObjectExpr object : objects) {
            String name = object.getName();
            if (name != null) {
                result.addElement(
                        LookupElementBuilder.create(name)
                        .withCaseSensitivity(false)
                        .withInsertHandler(TSCaseCorrectingInsertHandler.INSTANCE)
                );
            }
        }
    }
}
