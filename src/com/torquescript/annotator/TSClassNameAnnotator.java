package com.torquescript.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.TSFunctionType;
import com.torquescript.highlighting.TSSyntaxHighlighter;
import com.torquescript.psi.*;
import com.torquescript.reference.TSFunctionCallReference;
import org.jetbrains.annotations.NotNull;

public class TSClassNameAnnotator extends TSAnnotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TSObjectExpr) {
            TSObjectExpr fn = (TSObjectExpr) element;

            //Class name should be the second thing in the element
            PsiElement classElement = PsiTreeUtil.getChildOfType(fn, TSClassNameExpr.class);
            if (classElement == null) {
                return;
            }
            classElement = classElement.getFirstChild();
            if (classElement == null) {
                return;
            }

            //Only annotate if it's an id, can't really tell if it's an expr
            if (classElement.getNode().getElementType().equals(TSTypes.ID)) {
                createSuccessAnnotation(classElement, holder, TSSyntaxHighlighter.CLASSNAME);
            }
        }
    }
}
