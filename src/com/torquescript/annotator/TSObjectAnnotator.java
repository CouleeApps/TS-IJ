package com.torquescript.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.highlighting.TSSyntaxHighlighter;
import com.torquescript.psi.TSClassNameExpr;
import com.torquescript.psi.TSLiteralExpr;
import com.torquescript.psi.TSObjectExpr;
import com.torquescript.psi.TSTypes;
import com.torquescript.reference.TSFunctionCallReference;
import com.torquescript.reference.TSLiteralReference;
import org.jetbrains.annotations.NotNull;

/**
 * Highlights text literals that are also declared objects
 */
public class TSObjectAnnotator extends TSAnnotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TSLiteralExpr) {
            boolean valid = false;
            TSLiteralReference ref = (TSLiteralReference)element.getReference();
            if (ref != null) {
                valid = ref.multiResolve(false).length > 0;
            }

            if (valid) {
                createSuccessAnnotation(element, holder, TSSyntaxHighlighter.OBJECT_NAME);
            }
        }
    }
}
