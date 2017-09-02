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
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.psi.TSObjectExpr;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class TSObjectNameCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        //All global functions
        Project project = parameters.getOriginalFile().getProject();
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSFileType.INSTANCE, GlobalSearchScope.allScope(project));

        for (VirtualFile virtualFile : virtualFiles) {
            TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (tsFile != null) {
                Collection<TSObjectExpr> objects = PsiTreeUtil.findChildrenOfType(tsFile, TSObjectExpr.class);
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
    }
}
