package com.torquescript.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
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
import com.torquescript.psi.TSAssignExpr;
import com.torquescript.psi.TSVarExpr;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TSGlobalVariableCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        //All global variables
        Project project = parameters.getOriginalFile().getProject();
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSFileType.INSTANCE, GlobalSearchScope.allScope(project));

        for (VirtualFile virtualFile : virtualFiles) {
            TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (tsFile != null) {
                Collection<TSAssignExpr> assignments = PsiTreeUtil.findChildrenOfType(tsFile, TSAssignExpr.class);

                for (TSAssignExpr assignment : assignments) {
                    PsiElement first = assignment.getFirstChild();
                    if (!(first instanceof TSVarExpr))
                        continue;

                    if (((TSVarExpr)first).isLocal())
                        continue;

                    String name = ((TSVarExpr) first).getName();
                    result.addElement(
                            LookupElementBuilder.create(name)
                            .withPresentableText(first.getText())
                            .withCaseSensitivity(false)
                    );
                }
            }
        }
    }
}
