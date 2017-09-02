package com.torquescript;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.torquescript.psi.*;
import com.torquescript.symbols.TSFunctionCachedListGenerator;
import com.torquescript.symbols.TSGlobalCachedListGenerator;
import com.torquescript.symbols.TSObjectCachedListGenerator;
import com.torquescript.symbols.TSCachedList;

import java.util.*;

public class TSUtil {
    private static TSCachedList<TSFnDeclStmt> FUNCTIONS = new TSCachedList<>(new TSFunctionCachedListGenerator());
    private static TSCachedList<TSVarExpr> GLOBALS = new TSCachedList<>(new TSGlobalCachedListGenerator());
    private static TSCachedList<TSObjectExpr> OBJECTS = new TSCachedList<>(new TSObjectCachedListGenerator());

    /**
     * Find all function in the project.
     * @param project Containing project in which to search
     * @return A list of functions
     */
    public static List<TSFnDeclStmt> getFunctionList(Project project) {
        return new ArrayList<>(FUNCTIONS.getList(project));
    }

    /**
     * Find a function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSFnDeclStmt> findFunction(Project project, String key) {
        List<TSFnDeclStmt> result = null;
        Collection<TSFnDeclStmt> functions = new ArrayList<>(FUNCTIONS.getList(project));
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
        return new ArrayList<>(GLOBALS.getList(project));
    }

    /**
     * Find a function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSVarExpr> findGlobal(Project project, String key) {
        List<TSVarExpr> result = null;
        Collection<TSVarExpr> globals = new ArrayList<>(GLOBALS.getList(project));
        for (TSVarExpr global : globals) {
            if (key.equalsIgnoreCase(global.getName())) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(global);
            }
        }
        return result == null ? Collections.emptyList() : result;
    }

    /**
     * Find all function in the project.
     * @param project Containing project in which to search
     * @return A list of functions
     */
    public static List<TSObjectExpr> getObjectList(Project project) {
        return new ArrayList<>(OBJECTS.getList(project));
    }

    /**
     * Find a function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSObjectExpr> findObject(Project project, String key) {
        List<TSObjectExpr> result = null;
        Collection<TSObjectExpr> objects = new ArrayList<>(OBJECTS.getList(project));
        for (TSObjectExpr object : objects) {
            if (key.equalsIgnoreCase(object.getName())) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(object);
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
