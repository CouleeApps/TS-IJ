package com.torquescript.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.torquescript.highlighting.TSSyntaxHighlighter;
import com.torquescript.psi.TSLiteralExpr;
import com.torquescript.psi.TSObjectNameExpr;
import com.torquescript.reference.TSObjectNameReference;
import org.jetbrains.annotations.NotNull;

/**
 * Highlights text literals that are also declared objects
 */
public class TSObjectNameAnnotator extends TSAnnotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TSLiteralExpr || element instanceof TSObjectNameExpr) {
            boolean valid = false;
            TSObjectNameReference ref = (TSObjectNameReference)element.getReference();
            if (ref != null) {
                valid = ref.multiResolve(false).length > 0;
            }

            if (valid) {
                createSuccessAnnotation(element, holder, TSSyntaxHighlighter.OBJECT_NAME);
            }
        }
    }
}
