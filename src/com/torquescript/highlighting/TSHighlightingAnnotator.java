package com.torquescript.highlighting;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.psi.*;
import org.jetbrains.annotations.NotNull;

import static com.torquescript.highlighting.TSSyntaxHighlighter.*;

public class TSHighlightingAnnotator implements Annotator {
    //https://github.com/go-lang-plugin-org/go-lang-idea-plugin/blob/master/src/com/goide/highlighting/GoHighlightingAnnotator.java
    private static void setHighlighting(@NotNull PsiElement element, @NotNull AnnotationHolder holder, @NotNull TextAttributesKey key) {
        holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);
        String description = ApplicationManager.getApplication().isUnitTestMode() ? key.getExternalName() : null;
        holder.createInfoAnnotation(element, description).setTextAttributes(key);
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof  TSFnNameStmt) {
            TSFnNameStmt fn = (TSFnNameStmt) element;

            if (fn.isGlobal()) {
                PsiElement name = fn.getFirstChild();
                setHighlighting(name, holder, TSSyntaxHighlighter.FUNCTION);
            } else {
                PsiElement namespace = fn.getFirstChild();
                PsiElement name = namespace.getNextSibling().getNextSibling();

                setHighlighting(namespace, holder, TSSyntaxHighlighter.CLASSNAME);
                setHighlighting(name, holder, TSSyntaxHighlighter.FUNCTION);
            }
        }

        if (element instanceof TSCallExpr) {
            if (element instanceof TSCallGlobalExpr) {
                PsiElement name = element.getFirstChild();
                setHighlighting(name, holder, TSSyntaxHighlighter.FUNCTION_CALL);
            } else if (element instanceof TSCallNsGlobalExpr) {
                PsiElement namespace = element.getFirstChild();
                PsiElement name = namespace.getNextSibling().getNextSibling();

                setHighlighting(namespace, holder, TSSyntaxHighlighter.CLASSNAME);
                setHighlighting(name, holder, TSSyntaxHighlighter.FUNCTION_CALL);
            } else if (element instanceof  TSCallRefExpr) {
                PsiElement target = element.getFirstChild();
                PsiElement name = target.getNextSibling().getNextSibling();

                setHighlighting(name, holder, TSSyntaxHighlighter.FUNCTION_CALL);
            }
        }
    }
}
