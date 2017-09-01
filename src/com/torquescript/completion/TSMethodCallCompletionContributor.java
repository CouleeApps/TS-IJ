package com.torquescript.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementWeigher;
import com.intellij.openapi.project.Project;
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
import com.torquescript.psi.TSFnDeclStmt;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Completion;
import java.util.Collection;

public class TSMethodCallCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        PsiElement caller = parameters.getPosition().getPrevSibling().getPrevSibling();
        String ns = TSUtil.getElementNamespace(caller);

        //All global functions
        Project project = parameters.getOriginalFile().getProject();
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (tsFile != null) {
                Collection<TSFnDeclStmt> functions = PsiTreeUtil.findChildrenOfType(tsFile, TSFnDeclStmt.class);
                for (TSFnDeclStmt function : functions) {
                    if (function.isGlobal())
                        continue;

                    result.addElement(
                            LookupElementBuilder.create(function.getFunctionName())
                                    .withCaseSensitivity(false)
                                    .withPresentableText(function.getNamespace() + "." + function.getFunctionName())
                                    .withBoldness(ns != null && function.getNamespace().equalsIgnoreCase(ns))
                                    .withTailText(function.getArgList())
                    );
                }
            }
        }
    }
}
