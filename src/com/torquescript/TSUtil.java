package com.torquescript;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.torquescript.psi.*;
import com.torquescript.symbols.TSFunctionSymbolListGenerator;
import com.torquescript.symbols.TSGlobalSymbolListGenerator;
import com.torquescript.symbols.TSSymbolList;

import java.util.*;

public class TSUtil {
    private static TSSymbolList<TSFnDeclStmt> FUNCTIONS = new TSSymbolList<>(new TSFunctionSymbolListGenerator());
    private static TSSymbolList<TSVarExpr> GLOBALS = new TSSymbolList<>(new TSGlobalSymbolListGenerator());

    /**
     * Find all function in the project.
     * @param project Containing project in which to search
     * @return A list of functions
     */
    public static List<TSFnDeclStmt> getFunctionList(Project project) {
        return new ArrayList<>(FUNCTIONS.getSymbolList(project));
    }

    /**
     * Find a function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSFnDeclStmt> findFunction(Project project, String key) {
        List<TSFnDeclStmt> result = null;
        Collection<TSFnDeclStmt> functions = new ArrayList<>(FUNCTIONS.getSymbolList(project));
        for (TSFnDeclStmt function : functions) {
            if (key.equalsIgnoreCase(function.getFunctionName())) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(function);
            }
        }
        return result == null ? Collections.emptyList() : result;
    }

    /**
     * Find all function in the project.
     * @param project Containing project in which to search
     * @return A list of functions
     */
    public static List<TSVarExpr> getGlobalList(Project project) {
        return new ArrayList<>(GLOBALS.getSymbolList(project));
    }

    /**
     * Find a function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSVarExpr> findGlobal(Project project, String key) {
        List<TSVarExpr> result = null;
        Collection<TSVarExpr> functions = new ArrayList<>(GLOBALS.getSymbolList(project));
        for (TSVarExpr function : functions) {
            if (key.equalsIgnoreCase(function.getName())) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(function);
            }
        }
        return result == null ? Collections.emptyList() : result;
    }

    public static String getElementNamespace(PsiElement element) {
        if (element.getNode().getElementType().equals(TSTypes.ID)) {
            return element.getText();
        }

        //TODO???
        return null;
    }
}
