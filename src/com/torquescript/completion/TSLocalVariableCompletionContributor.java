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
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.psi.TSTypes;
import com.torquescript.psi.TSVarExpr;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TSLocalVariableCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        List<TSVarExpr> variables = new ArrayList<>();
        for (PsiElement current = parameters.getPosition().getPrevSibling(); current != null; current = current.getPrevSibling()) {
            if (current.getNode().getElementType().equals(TSTypes.FUNCTION))
                break;

            if (current.getNode().getElementType().equals(TSTypes.LOCALVAR)) {
                String prefixless = current.getText().substring(1);
                result.addElement(
                        LookupElementBuilder.create(prefixless)
                        .withPresentableText(current.getText())
                        .withCaseSensitivity(false)
                );
            }
        }
    }
}
