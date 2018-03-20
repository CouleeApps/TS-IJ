package com.torquescript.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.highlighting.TSSyntaxHighlighter;
import com.torquescript.psi.TSClassExpr;
import com.torquescript.psi.TSFieldNameExpr;
import com.torquescript.reference.TSClassNameReference;
import com.torquescript.reference.TSFieldNameReference;
import org.jetbrains.annotations.NotNull;

/**
 * Highlights class names in object and datablock declarations. If the class name is actually an object name it will
 * highlight with the object attribute instead.
 */
public class TSClassFieldAnnotator extends TSAnnotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TSFieldNameExpr) {
            TSFieldNameExpr fieldName = (TSFieldNameExpr)element;

            //Check if the class name actually resolves to a real class. TSClassNameReference will check this
            // for us, we just have to see if it gives any results.
            boolean valid = false;
            TSFieldNameReference ref = (TSFieldNameReference)fieldName.getReference();
            if (ref != null) {
                //If there are any results at all then this is successful
                valid = ref.multiResolve(false).length > 0;
            }

            if (valid) {
                createSuccessAnnotation(fieldName, holder, TSSyntaxHighlighter.FIELD_NAME);
            }
        }
    }
}
