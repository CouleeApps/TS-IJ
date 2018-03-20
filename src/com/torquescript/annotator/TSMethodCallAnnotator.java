package com.torquescript.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.torquescript.TSFunctionType;
import com.torquescript.TSUtil;
import com.torquescript.reference.TSFunctionCallReference;
import com.torquescript.highlighting.TSSyntaxHighlighter;
import com.torquescript.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Highlights any function calls and function declarations. Will mark missing function calls with a warning.
 */
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

                //Check if this namespace is actually an object namespace
                List<TSObjectExpr> objects = TSUtil.findObject(element.getProject(), namespace.getText());
                if (objects.size() > 0) {
                    //Object method
                    createSuccessAnnotation(namespace, holder, TSSyntaxHighlighter.OBJECT_NAME);
                } else {
                    //Class method
                    createSuccessAnnotation(namespace, holder, TSSyntaxHighlighter.CLASSNAME);
                }

                createSuccessAnnotation(name, holder, TSSyntaxHighlighter.FUNCTION);
            }
        }

        if (element instanceof TSCallExpr) {
            //Check if the function call actually resolves to a real function. TSFunctionCallReference will check this
            // for us, we just have to see if it gives any results.
            boolean valid = false;
            TSFunctionCallReference ref = (TSFunctionCallReference)element.getReference();
            if (ref != null) {
                //If there are any results at all then this is successful
                valid = ref.multiResolve(false).length > 0;
            }

            PsiElement name = null;
            if (element instanceof TSCallGlobalExpr) {
                //Call is method() so just the first is what we want
                name = element.getFirstChild();
            } else if (element instanceof TSCallGlobalNsExpr) {
                //Call is NS::method so we want the third as well
                PsiElement namespace = element.getFirstChild();
                name = namespace.getNextSibling().getNextSibling();

                //Highlight the namespace as long as it's not Parent::method
                if (!((TSCallGlobalNsExpr) element).isParentCall()) {
                    //Check if this namespace is actually an object namespace
                    List<TSObjectExpr> objects = TSUtil.findObject(element.getProject(), namespace.getText());
                    if (objects.size() > 0) {
                        //Object method
                        createSuccessAnnotation(namespace, holder, TSSyntaxHighlighter.OBJECT_NAME);
                    } else {
                        //Class method
                        createSuccessAnnotation(namespace, holder, TSSyntaxHighlighter.CLASSNAME);
                    }
                }
            } else if (element instanceof TSCallMethodExpr) {
                //Call is %target.method so we want the third element
                PsiElement target = element.getFirstChild();
                name = target.getNextSibling().getNextSibling();
            }

            //Mark missing functions with a warning (not an error because you can dynamically define functions)
            if (name != null) {
                if (valid) {
                    createSuccessAnnotation(name, holder, TSSyntaxHighlighter.FUNCTION_CALL);
                } else {
                    createWarnAnnotation(name, holder, "Can't find function");
                }
            }
        }
    }
}
