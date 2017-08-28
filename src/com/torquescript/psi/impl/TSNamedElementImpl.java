package com.torquescript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.torquescript.psi.TSNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class TSNamedElementImpl extends ASTWrapperPsiElement implements TSNamedElement {
    public TSNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}
