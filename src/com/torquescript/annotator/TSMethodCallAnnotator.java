package com.torquescript.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.TSReference;
import com.torquescript.highlighting.TSSyntaxHighlighter;
import com.torquescript.psi.*;
import org.jetbrains.annotations.NotNull;

import static com.torquescript.highlighting.TSSyntaxHighlighter.*;

public class TSMethodCallAnnotator implements Annotator {
    //https://github.com/go-lang-plugin-org/go-lang-idea-plugin/blob/master/src/com/goide/highlighting/GoHighlightingAnnotator.java
    private static void createSuccessAnnotation(@NotNull PsiElement element, @NotNull AnnotationHolder holder, @NotNull TextAttributesKey key) {
        holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);
        String description = ApplicationManager.getApplication().isUnitTestMode() ? key.getExternalName() : null;
        holder.createInfoAnnotation(element, description).setTextAttributes(key);
    }

    private static void createWarnAnnotation(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        holder.createWarningAnnotation(element, "Can't find function");
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TSFnNameStmt) {
            TSFnNameStmt fn = (TSFnNameStmt) element;

            if (fn.isGlobal()) {
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
            TSReference ref = (TSReference)element.getReference();
            PsiElement name = null;
            if (ref != null) {
                valid = ref.multiResolve(false).length > 0;
            }
            if (element instanceof TSCallGlobalExpr) {
                name = element.getFirstChild();
            } else if (element instanceof TSCallGlobalNsExpr) {
                PsiElement namespace = element.getFirstChild();
                name = namespace.getNextSibling().getNextSibling();

                createSuccessAnnotation(namespace, holder, TSSyntaxHighlighter.CLASSNAME);
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
