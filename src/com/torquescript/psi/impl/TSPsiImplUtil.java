package com.torquescript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.TSFunctionType;
import com.torquescript.TSIcons;
import com.torquescript.reference.TSClassNameReference;
import com.torquescript.reference.TSFunctionCallReference;
import com.torquescript.psi.*;
import com.torquescript.reference.TSGlobalVariableReference;
import com.torquescript.reference.TSLiteralReference;
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

    public static TSFunctionType getFunctionType(TSFnDeclStmt element) {
        TSFnNameStmt name = PsiTreeUtil.getChildOfType(element, TSFnNameStmt.class);
        if (name == null) {
            return null;
        }
        return name.getFunctionType();
    }

    public static String getArgList(TSFnDeclStmt element) {
        ASTNode argNode = element.getNode().findChildByType(TSTypes.VAR_LIST);

        if (argNode != null) {
            return "(" + argNode.getText() + ")";
        } else {
            return "()";
        }
    }

    public static String getFunctionName(TSFnNameStmt element) {
        ASTNode nameNode = null;
        //Find which node contains our function name
        switch (getFunctionType(element)) {
            case GLOBAL:
                nameNode = element.getNode().findChildByType(TSTypes.ID);
                break;
            case GLOBAL_NS:
            case METHOD:
                ASTNode anchor = element.getNode().findChildByType(TSTypes.COLON_DOUBLE);
                nameNode = element.getNode().findChildByType(TSTypes.ID, anchor);
                break;
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
                if (function.getFunctionType() == TSFunctionType.GLOBAL) {
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
        if (element.getFunctionType() == TSFunctionType.GLOBAL) {
            return null;
        }

        //Namespace should be the first
        ASTNode nsNode = element.getNode().findChildByType(TSTypes.ID);
        if (nsNode == null) {
            return null;
        }

        return nsNode.getText();
    }

    public static TSFunctionType getFunctionType(TSFnNameStmt element) {
        //If we have a double colon that counts as a namespace
        ASTNode doubleColon = element.getNode().findChildByType(TSTypes.COLON_DOUBLE);
        if (doubleColon == null) {
            return TSFunctionType.GLOBAL;
        }

        //In general, methods have a first arg of %this
        TSVarList varList = PsiTreeUtil.getNextSiblingOfType(element, TSVarList.class);

        //No var list? Must be global ns then
        if (varList == null) {
            return TSFunctionType.GLOBAL_NS;
        }

        //Not %this first? Global ns
        TSVarExpr firstVar = PsiTreeUtil.getChildOfType(varList, TSVarExpr.class);
        if (firstVar == null) {
            //No first var means no %this so global ns
            return TSFunctionType.GLOBAL_NS;
        }
        if (firstVar.getName().equalsIgnoreCase("this")) {
            return TSFunctionType.METHOD;
        }

        //Probably global ns
        return TSFunctionType.GLOBAL_NS;
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

    public static PsiReference getReference(TSVarExpr var) {
        if (var.isLocal()) {
            return null;
        }
        return new TSGlobalVariableReference(var);
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
        if (nameNode instanceof TSLiteralExpr) {
            return ((TSLiteralExpr) nameNode).getName();
        }
        if (nameNode.getNode().getElementType().equals(TSTypes.ID)) {
            return nameNode.getText();
        }
        return null;
    }

    public static String getClassName(TSObjectExpr obj) {
        return null;
    }

    public static String getParentName(TSObjectExpr obj) {
        return null;
    }

    public static PsiReference getReference(TSCallExpr call) {
        PsiElement nameNode = getNameNode(call);
        if (nameNode == null) {
            return null;
        }
        return new TSFunctionCallReference(call, nameNode);
    }

    public static String getFunctionName(TSCallExpr call) {
        PsiElement nameNode = getNameNode(call);
        if (nameNode == null) {
            return null;
        }
        return nameNode.getText();
    }

    public static PsiElement getNameNode(TSCallExpr call) {
        PsiElement nameElement = null;

        if (call.isParentCall()) {
            //Third
            nameElement = call.getFirstChild();
            if (nameElement != null) {
                nameElement = nameElement.getNextSibling();
            }
            if (nameElement != null) {
                nameElement = nameElement.getNextSibling();
            }
        } else {
            if (call.getFunctionType() == TSFunctionType.GLOBAL) {
                //First element is the function name
                nameElement = call.getFirstChild();
            } else {
                //Third
                nameElement = call.getFirstChild();
                if (nameElement != null) {
                    nameElement = nameElement.getNextSibling();
                }
                if (nameElement != null) {
                    nameElement = nameElement.getNextSibling();
                }
            }
        }
        if (nameElement == null) {
            return null;
        }
        return nameElement;
    }

    public static TSFunctionType getFunctionType(TSCallExpr call) {
        if (call instanceof TSCallGlobalExpr) {
            return TSFunctionType.GLOBAL;
        } else if (call instanceof TSCallGlobalNsExpr) {
            //What about parent?
            if (call.isParentCall()) {
                //We are whatever our function's type is
                TSFnDeclStmt fn = call.getParentFunction();
                if (fn == null) {
                    //Well then I dunno
                    return TSFunctionType.GLOBAL_NS;
                }
                return fn.getFunctionType();
            }

            return TSFunctionType.GLOBAL_NS;
        } else if (call instanceof TSCallMethodExpr) {
            return TSFunctionType.METHOD;
        } else {
            //????
            return null;
        }
    }

    public static boolean isParentCall(TSCallExpr call) {
        PsiElement first = call.getFirstChild();
        return (first.getNode().getElementType().equals(TSTypes.PARENT));
    }

    public static TSFnDeclStmt getParentFunction(TSCallExpr call) {
        return PsiTreeUtil.getParentOfType(call, TSFnDeclStmt.class);
    }

    public static String getName(TSLiteralExpr expr) {
        if (expr.getFirstChild().getNode().getElementType().equals(TSTypes.STRATOM)) {
            String text = expr.getText();
            if (text.length() > 1) {
                return text.substring(1, text.length() - 1);
            }
        }
        return expr.getText();
    }

    public static PsiReference getReference(TSLiteralExpr expr) {
        return new TSLiteralReference(expr);
    }

    public static String getName(TSClassExpr expr) {
        if (expr.getFirstChild().getNode().getElementType().equals(TSTypes.STRATOM)) {
            String text = expr.getText();
            if (text.length() > 1) {
                return text.substring(1, text.length() - 1);
            }
        }
        return expr.getText();
    }

    public static PsiReference getReference(TSClassExpr expr) {
        return new TSClassNameReference(expr);
    }

    public static String getName(TSPackageDecl pkg) {
        //First id token is the name
        ASTNode nameNode = pkg.getNode().findChildByType(TSTypes.ID);
        if (nameNode == null) {
            return null;
        }

        return nameNode.getText();
    }

    public static PsiElement getNameIdentifier(TSPackageDecl element) {
        ASTNode nameNode = element.getNode().findChildByType(TSTypes.ID);

        if (nameNode != null) {
            return nameNode.getPsi();
        }

        return null;
    }


    public static PsiElement setName(TSPackageDecl element, String newName) {
        ASTNode nameNode = element.getNode().findChildByType(TSTypes.ID);

        if (nameNode != null) {
            TSPackageDecl stmt = TSElementFactory.createPackageDecl(element.getProject(), newName);
            ASTNode newNameNode = stmt.getNode().findChildByType(TSTypes.ID);
            if (newNameNode != null) {
                element.getNode().replaceChild(nameNode, newNameNode);
            }
        }

        return element;
    }

}
