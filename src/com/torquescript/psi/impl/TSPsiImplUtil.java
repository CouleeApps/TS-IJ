package com.torquescript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.TSIcons;
import com.torquescript.psi.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
        TSFnNameStmt name = PsiTreeUtil.getChildOfType(element, TSFnNameStmt.class);
        if (name == null) {
            return null;
        }
        return name.getFunctionName();
    }

    public static String getNamespace(TSFnDeclStmt element) {
        TSFnNameStmt name = PsiTreeUtil.getChildOfType(element, TSFnNameStmt.class);
        if (name == null) {
            return null;
        }
        return name.getNamespace();
    }

    public static boolean isGlobal(TSFnDeclStmt element) {
        TSFnNameStmt name = PsiTreeUtil.getChildOfType(element, TSFnNameStmt.class);
        if (name == null) {
            return false;
        }
        return name.isGlobal();
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

    public static ItemPresentation getPresentation(final TSFnDeclStmt function) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                if (function.isGlobal()) {
                    return function.getFunctionName();
                } else {
                    return function.getNamespace() + "::" + function.getFunctionName();
                }
            }

            @Nullable
            @Override
            public String getLocationString() {
                return function.getContainingFile().getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return TSIcons.FILE;
            }
        };
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
        ASTNode doubleColon = element.getNode().findChildByType(TSTypes.COLON_DOUBLE);
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

    public static String getName(TSObjectExpr obj) {
        //Should be the first element after the open paren
        ASTNode node = obj.getNode();
        if (node == null) {
            return null;
        }
        ASTNode openParen = node.findChildByType(TSTypes.PAREN_OPEN);
        if (openParen == null) {
            return null;
        }
        PsiElement nameNode = openParen.getPsi().getNextSibling();
        if (nameNode == null) {
            return null;
        }
        if (!(nameNode instanceof TSLiteralExpr)) {
            return null;
        }
        return nameNode.getFirstChild().getText();
    }

    public static String getClassName(TSObjectExpr obj) {
        return null;
    }

    public static String getParentName(TSObjectExpr obj) {
        return null;
    }
}
