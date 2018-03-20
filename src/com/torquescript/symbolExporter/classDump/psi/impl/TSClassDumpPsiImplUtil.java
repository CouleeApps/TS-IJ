package com.torquescript.symbolExporter.classDump.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.torquescript.TSFunctionType;
import com.torquescript.TSIcons;
import com.torquescript.symbolExporter.classDump.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TSClassDumpPsiImplUtil {

    public static String getName(TSClassDumpClassDecl element) {
        TSClassDumpIdentifier[] idents = PsiTreeUtil.getChildrenOfType(element, TSClassDumpIdentifier.class);
        if (idents == null || idents.length == 0) {
            return null;
        }
        return idents[0].getText();
    }

    public static PsiElement setName(TSClassDumpClassDecl element, @NotNull String name) throws IncorrectOperationException {
        return element;
    }

    public static PsiElement getNameIdentifier(TSClassDumpClassDecl element) {
        TSClassDumpIdentifier[] idents = PsiTreeUtil.getChildrenOfType(element, TSClassDumpIdentifier.class);
        if (idents == null || idents.length == 0) {
            return null;
        }
        return idents[0];
    }

    public static ItemPresentation getPresentation(TSClassDumpClassDecl element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return "Engine Method";
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return TSIcons.FILE;
            }
        };
    }

    public static String getParentName(TSClassDumpClassDecl element) {
        //See if we have a parent
        TSClassDumpIdentifier[] idents = PsiTreeUtil.getChildrenOfType(element, TSClassDumpIdentifier.class);
        if (idents == null) {
            return null;
        }
        if (idents.length > 1) {
            return idents[1].getText();
        }
        return null;
    }

    public static String getName(TSClassDumpNamespaceDecl element) {
        TSClassDumpIdentifier[] idents = PsiTreeUtil.getChildrenOfType(element, TSClassDumpIdentifier.class);
        if (idents == null || idents.length == 0) {
            return null;
        }
        return idents[0].getText();
    }

    public static PsiElement setName(TSClassDumpNamespaceDecl element, @NotNull String name) throws IncorrectOperationException {
        return element;
    }

    public static PsiElement getNameIdentifier(TSClassDumpNamespaceDecl element) {
        TSClassDumpIdentifier[] idents = PsiTreeUtil.getChildrenOfType(element, TSClassDumpIdentifier.class);
        if (idents == null || idents.length == 0) {
            return null;
        }
        return idents[0];
    }

    public static ItemPresentation getPresentation(TSClassDumpNamespaceDecl element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return "Engine Method";
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return TSIcons.FILE;
            }
        };
    }

    public static String getName(TSClassDumpEngineMethod element) {
        return getFunctionName(element);
    }

    public static PsiElement setName(TSClassDumpEngineMethod element, @NotNull String name) throws IncorrectOperationException {
        return element;
    }

    public static PsiElement getNameIdentifier(TSClassDumpEngineMethod element) {
        TSClassDumpIdentifier[] idents = PsiTreeUtil.getChildrenOfType(element, TSClassDumpIdentifier.class);
        if (idents == null || idents.length == 0) {
            return null;
        }
        return idents[0];
    }

    public static String getFunctionName(TSClassDumpEngineMethod element) {
        TSClassDumpIdentifier[] idents = PsiTreeUtil.getChildrenOfType(element, TSClassDumpIdentifier.class);
        if (idents == null || idents.length == 0) {
            return null;
        }
        return idents[0].getText();
    }

    public static String getNamespace(TSClassDumpEngineMethod element) {
        TSClassDumpClassDecl parent = PsiTreeUtil.getTopmostParentOfType(element, TSClassDumpClassDecl.class);
        if (parent == null) {
            TSClassDumpNamespaceDecl ns = PsiTreeUtil.getTopmostParentOfType(element, TSClassDumpNamespaceDecl.class);
            if (ns == null) {
                return null;
            }
            return ns.getName();
        }
        return parent.getName();
    }

    public static TSFunctionType getFunctionType(TSClassDumpEngineMethod element) {
        //Are we in a namespace or a class
        TSClassDumpClassDecl parent = PsiTreeUtil.getTopmostParentOfType(element, TSClassDumpClassDecl.class);
        if (parent == null) {
            return TSFunctionType.GLOBAL;
        }
        return TSFunctionType.METHOD;
    }

    public static String getArgList(TSClassDumpEngineMethod element) {
        ASTNode argNode = element.getNode().findChildByType(TSClassDumpTypes.ARG_LIST);

        if (argNode != null) {
            return "(" + argNode.getText() + ")";
        } else {
            return "()";
        }
    }

    public static ItemPresentation getPresentation(final TSClassDumpEngineMethod function) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return function.getNamespace() + "::" + function.getFunctionName();
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
}
