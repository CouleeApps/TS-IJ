package com.torquescript.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.torquescript.TSFunctionType;
import com.torquescript.reference.TSFunctionCallReference;
import com.torquescript.highlighting.TSSyntaxHighlighter;
import com.torquescript.psi.*;
import org.jetbrains.annotations.NotNull;

public class TSMethodCallAnnotator extends TSAnnotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TSFnNameStmt) {
            TSFnNameStmt fn = (TSFnNameStmt) element;

            if (fn.getFunctionType() == TSFunctionType.GLOBAL) {
                PsiElement name = fn.getFirstChild();
                createSuccessAnnotation(name, holder, TSSyntaxHighlighter.FUNCTION);
            } else {
                PsiElement namespace = fn.getFirstChild();
                PsiElement name = namespace.getNextSibling().getNextSibling();

                createSuccessAnnotation(namespace, holder, TSSyntaxHighlighter.CLASSNAME);
                createSuccessAnnotation(name, holder, TSSyntaxHighlighter.FUNCTION);
            }
        }

        if (element instanceof TSCallExpr) {
            boolean valid = false;
            TSFunctionCallReference ref = (TSFunctionCallReference)element.getReference();
            PsiElement name = null;
            if (ref != null) {
                valid = ref.multiResolve(false).length > 0;
            }
            if (element instanceof TSCallGlobalExpr) {
                name = element.getFirstChild();
            } else if (element instanceof TSCallGlobalNsExpr) {
                PsiElement namespace = element.getFirstChild();
                name = namespace.getNextSibling().getNextSibling();

                if (!((TSCallGlobalNsExpr) element).isParentCall()) {
                    createSuccessAnnotation(namespace, holder, TSSyntaxHighlighter.CLASSNAME);
                }
            } else if (element instanceof TSCallMethodExpr) {
                PsiElement target = element.getFirstChild();
                name = target.getNextSibling().getNextSibling();
            }
            if (name != null) {
                if (valid) {
                    createSuccessAnnotation(name, holder, TSSyntaxHighlighter.FUNCTION_CALL);
                } else {
                    createWarnAnnotation(name, holder);
                }
            }
        }
    }
}
