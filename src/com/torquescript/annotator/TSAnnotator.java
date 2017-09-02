package com.torquescript.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class TSAnnotator implements Annotator {
    private static TSAnnotator[] annotators = new TSAnnotator[]{
            new TSMethodCallAnnotator(),
            new TSClassNameAnnotator()
    };

    //https://github.com/go-lang-plugin-org/go-lang-idea-plugin/blob/master/src/com/goide/highlighting/GoHighlightingAnnotator.java
    void createSuccessAnnotation(@NotNull PsiElement element, @NotNull AnnotationHolder holder, @NotNull TextAttributesKey key) {
        holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);
        String description = ApplicationManager.getApplication().isUnitTestMode() ? key.getExternalName() : null;
        holder.createInfoAnnotation(element, description).setTextAttributes(key);
    }

    void createWarnAnnotation(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        holder.createWarningAnnotation(element, "Can't find function");
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        for (TSAnnotator annotator : annotators) {
            annotator.annotate(element, holder);
        }
    }
}
