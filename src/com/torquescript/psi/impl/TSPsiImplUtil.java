package com.torquescript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.torquescript.psi.*;

public class TSPsiImplUtil {
    public static String getName(TSFnDeclStmt element) {
        return getFunctionName(element);
    }

    public static PsiElement setName(TSFnDeclStmt element, String newName) {
        ASTNode nameNode = element.getNode().findChildByType(TSTypes.ID);

        if (nameNode != null) {
            TSFnDeclStmt stmt = TSElementFactory.createFnDecl(element.getProject(), newName);
            ASTNode newNameNode = stmt.getNode().findChildByType(TSTypes.ID);
            if (newNameNode != null) {
                element.getNode().replaceChild(nameNode, newNameNode);
            }
        }

        return element;
    }

    public static PsiElement getNameIdentifier(TSFnDeclStmt element) {
        ASTNode nameNode = element.getNode().findChildByType(TSTypes.ID);

        if (nameNode != null) {
            return nameNode.getPsi();
        }

        return null;
    }

    public static String getFunctionName(TSFnDeclStmt element) {
        ASTNode nameNode = element.getNode().findChildByType(TSTypes.ID);

        if (nameNode != null) {
            return nameNode.getText();
        }

        return null;
    }

    public static String getName(TSVarExpr variable) {
        ASTNode node = variable.getNode();
        if (node == null) {
            return null;
        }
        node = node.getFirstChildNode();
        if (node == null) {
            return null;
        }
        return node.getText();
    }

    public static boolean isLocal(TSVarExpr variable) {
        ASTNode node = variable.getNode();
        if (node == null) {
            return false;
        }
        node = node.getFirstChildNode();
        if (node == null) {
            return false;
        }
        return (node.getElementType().equals(TSTypes.LOCALVAR));
    }
}
