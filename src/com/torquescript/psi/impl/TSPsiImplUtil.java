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
        ASTNode nameNode;
        //Find which node contains our function name
        if (isGlobal(element)) {
            nameNode = element.getNode().findChildByType(TSTypes.ID);
        } else {
            ASTNode anchor = element.getNode().findChildByType(TSTypes.COLON_DOUBLE);
            nameNode = element.getNode().findChildByType(TSTypes.ID, anchor);
        }
        if (nameNode == null) {
            return null;
        }
        return nameNode.getText();
    }

    public static String getNamespace(TSFnDeclStmt element) {
        if (isGlobal(element)) {
            return null;
        }

        //Namespace should be the first
        ASTNode nsNode = element.getNode().findChildByType(TSTypes.ID);
        if (nsNode == null) {
            return null;
        }

        return nsNode.getText();
    }

    public static boolean isGlobal(TSFnDeclStmt element) {
        //If we have a double colon that counts as a namespace
        ASTNode doubleColon = element.getNode().findChildByType(TSTypes.FN_NAME_STMT);
        return doubleColon == null;
    }

    public static String getArgList(TSFnDeclStmt element) {
        ASTNode argNode = element.getNode().findChildByType(TSTypes.VAR_LIST);

        if (argNode != null) {
            return "(" + argNode.getText() + ")";
        }

        return null;
    }


    public static String getFunctionName(TSFnNameStmt element) {
        ASTNode nameNode;
        //Find which node contains our function name
        if (isGlobal(element)) {
            nameNode = element.getNode().findChildByType(TSTypes.ID);
        } else {
            ASTNode anchor = element.getNode().findChildByType(TSTypes.COLON_DOUBLE);
            nameNode = element.getNode().findChildByType(TSTypes.ID, anchor);
        }
        if (nameNode == null) {
            return null;
        }
        return nameNode.getText();
    }

    public static String getNamespace(TSFnNameStmt element) {
        if (isGlobal(element)) {
            return null;
        }

        //Namespace should be the first
        ASTNode nsNode = element.getNode().findChildByType(TSTypes.ID);
        if (nsNode == null) {
            return null;
        }

        return nsNode.getText();
    }

    public static boolean isGlobal(TSFnNameStmt element) {
        //If we have a double colon that counts as a namespace
        ASTNode doubleColon = element.getNode().findChildByType(TSTypes.FN_NAME_STMT);
        return doubleColon == null;
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
        return node.getText().substring(1);
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
