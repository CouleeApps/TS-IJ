package com.torquescript.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.indexing.FileBasedIndex;
import com.torquescript.TSFile;
import com.torquescript.TSFileType;
import com.torquescript.TSUtil;
import com.torquescript.psi.TSAssignExpr;
import com.torquescript.psi.TSVarExpr;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TSGlobalVariableCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        //All global variables
        Collection<TSVarExpr> variables = TSUtil.getGlobalList(parameters.getPosition().getProject());

        for (TSVarExpr var : variables) {
            String name = var.getText();
            result.addElement(
                    LookupElementBuilder.create(name)
                    .withPresentableText(name)
                    .withCaseSensitivity(false)
                    .withInsertHandler(TSCaseCorrectingInsertHandler.INSTANCE)
            );
        }
    }
}
