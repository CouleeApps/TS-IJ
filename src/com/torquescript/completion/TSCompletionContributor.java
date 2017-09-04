package com.torquescript.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.indexing.FileBasedIndex;
import com.torquescript.TSFile;
import com.torquescript.TSFileType;
import com.torquescript.TSLanguage;
import com.torquescript.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.Element;
import java.util.Collection;

import static com.intellij.patterns.PlatformPatterns.*;

/**
 * Base class that provides autocomplete suggestions
 */
public class TSCompletionContributor extends CompletionContributor {
    public TSCompletionContributor() {
        extend(CompletionType.BASIC, isKeywordable(), new TSKeywordCompletionContributor());
        extend(CompletionType.BASIC, inGlobalCall(), new TSGlobalCallCompletionContributor());
        extend(CompletionType.BASIC, inGlobalNSCall(), new TSGlobalNSCallCompletionContributor());
        extend(CompletionType.BASIC, inMethodCall(), new TSMethodCallCompletionContributor());
        extend(CompletionType.BASIC, inLocalVariable(), new TSLocalVariableCompletionContributor());
        extend(CompletionType.BASIC, inGlobalVariable(), new TSGlobalVariableCompletionContributor());
        extend(CompletionType.BASIC, inObjectName(), new TSObjectNameCompletionContributor());
    }

    @Override
    public boolean invokeAutoPopup(@NotNull PsiElement position, char typeChar) {
        //Second colon on a :: should pop it
        if (typeChar == ':' && position.getNode().getElementType().equals(TSTypes.COLON)) {
            return true;
        }
        //Variable prefixes
        if (typeChar == '%' || typeChar == '$') {
            return true;
        }
        return super.invokeAutoPopup(position, typeChar);
    }

    private static ElementPattern<? extends PsiElement> isKeywordable() {
        return psiElement(TSTypes.ID).withLanguage(TSLanguage.INSTANCE)
                .andNot(psiElement().afterSibling(psiElement(TSTypes.DOT)))
                .andNot(psiElement().afterSibling(psiElement(TSTypes.COLON_DOUBLE)));
    }

    private static ElementPattern<? extends PsiElement> inGlobalCall() {
        return isKeywordable();
    }

    private static ElementPattern<? extends PsiElement> inGlobalNSCall() {
        return psiElement(TSTypes.ID).withLanguage(TSLanguage.INSTANCE).afterSibling(psiElement(TSTypes.COLON_DOUBLE));
    }

    private static ElementPattern<? extends PsiElement> inMethodCall() {
        return psiElement(TSTypes.ID).withLanguage(TSLanguage.INSTANCE).afterSibling(psiElement(TSTypes.DOT));
    }

    private static ElementPattern<? extends PsiElement> inLocalVariable() {
        return StandardPatterns.or(
                psiElement(TSTypes.ID)
                .andNot(psiElement().afterSibling(psiElement(TSTypes.DOT)))
                .andNot(psiElement().afterSibling(psiElement(TSTypes.COLON_DOUBLE))),
                psiElement(TSTypes.LOCALVAR));
    }

    private static ElementPattern<? extends PsiElement> inGlobalVariable() {
        return StandardPatterns.or(
                psiElement(TSTypes.ID)
                        .andNot(psiElement().afterSibling(psiElement(TSTypes.DOT)))
                        .andNot(psiElement().afterSibling(psiElement(TSTypes.COLON_DOUBLE))),
                psiElement(TSTypes.GLOBALVAR));
    }

    private static ElementPattern<? extends PsiElement> inObjectName() {
        return isKeywordable();
    }
}
