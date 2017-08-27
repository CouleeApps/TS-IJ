package com.torquescript.psi.impl;

import com.intellij.lang.ASTNode;
import com.torquescript.psi.*;

public class TSPsiImplUtil {

    public static String getFunctionName(TSFnDeclStmt element) {
        ASTNode nameNode = element.getNode().findChildByType(TSTypes.ID);

        if (nameNode != null) {
            return nameNode.getText();
        }

        return null;
    }

}
