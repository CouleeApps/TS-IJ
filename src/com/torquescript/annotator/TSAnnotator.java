package com.torquescript.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Annotator class that provides highlighting for function calls and objects references in the source.
 */
public class TSAnnotator implements Annotator {
    private static final ThreadLocal<TSAnnotator[]> annotators = ThreadLocal.withInitial(() -> new TSAnnotator[]{
            new TSMethodCallAnnotator(),
            new TSClassNameAnnotator(),
            new TSObjectNameAnnotator(),
            new TSClassFieldAnnotator()
    });

    //https://github.com/go-lang-plugin-org/go-lang-idea-plugin/blob/master/src/com/goide/highlighting/GoHighlightingAnnotator.java
    void createSuccessAnnotation(@NotNull PsiElement element, @NotNull AnnotationHolder holder, @NotNull TextAttributesKey key) {
        holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);
        String description = ApplicationManager.getApplication().isUnitTestMode() ? key.getExternalName() : null;
        holder.createInfoAnnotation(element, description).setTextAttributes(key);
    }

    void createWarnAnnotation(@NotNull PsiElement element, @NotNull AnnotationHolder holder, @NotNull String message) {
        holder.createWarningAnnotation(element, message);
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        for (TSAnnotator annotator : annotators.get()) {
            annotator.annotate(element, holder);
        }
    }
}
