package com.torquescript.annotator;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.TSFunctionType;
import com.torquescript.highlighting.TSSyntaxHighlighter;
import com.torquescript.psi.*;
import com.torquescript.reference.TSFunctionCallReference;
import org.jetbrains.annotations.NotNull;

/**
 * Highlights class names in object and datablock declarations. If the class name is actually an object name it will
 * highlight with the object attribute instead.
 */
public class TSClassNameAnnotator extends TSAnnotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        PsiElement classElement = null;
        if (element instanceof  TSDatablockDecl) {
            TSDatablockDecl db = (TSDatablockDecl) element;

            //Find the first id node, this is kinda wonky since we have to account for whitespace nodes
            //datablock ClassName(...)
            ASTNode node = db.getNode();
            if (node == null) {
                return;
            }
            node = node.findChildByType(TSTypes.ID);
            if (node == null) {
                return;
            }
            classElement = node.getPsi();
        } else if (element instanceof TSObjectExpr) {
            TSObjectExpr obj = (TSObjectExpr) element;

            //Class name should be the second thing in the element:
            // new ClassName(...)
            classElement = PsiTreeUtil.getChildOfType(obj, TSClassNameExpr.class);

            if (classElement == null) {
                return;
            }
            classElement = classElement.getFirstChild();
        }
        if (classElement == null) {
            return;
        }

        //Only annotate if it's an id, can't really tell if it's an expr
        if (classElement.getNode().getElementType().equals(TSTypes.ID)) {
            createSuccessAnnotation(classElement, holder, TSSyntaxHighlighter.CLASSNAME);
        }
    }
}
