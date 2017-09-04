package com.torquescript;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import com.torquescript.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TSFindUsagesProvider implements FindUsagesProvider {
    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(
                new TSLexerAdapter(),
                TokenSet.create(TSTypes.ID),
                TokenSet.create(TSTypes.COMMENT),
                TokenSet.EMPTY
        );
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        if (psiElement instanceof PsiNamedElement) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof TSFnNameStmt) {
            return "Function";
        } else if (element instanceof TSFnDeclStmt) {
            return "Function";
        } else if (element instanceof TSVarExpr) {
            return "Variable";
        } else if (element instanceof TSObjectExpr) {
            return "Object";
        }

        return "";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof TSFnNameStmt) {
            return ((TSFnNameStmt) element).getFunctionName();
        } else if (element instanceof TSFnDeclStmt) {
            return ((TSFnDeclStmt) element).getFunctionName();
        } else if (element instanceof TSVarExpr) {
            return ((TSVarExpr) element).getName();
        } else if (element instanceof TSObjectExpr) {
            return ((TSObjectExpr) element).getName();
        }

        return "";
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return element.getText();
    }
}
