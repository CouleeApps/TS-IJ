package com.torquescript;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.torquescript.psi.*;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpEngineMethod;
import com.torquescript.symbols.*;

import java.util.*;

public class TSUtil {
    private static Map<Project, TSSymbolList> symbolLists = new HashMap<>();

    public static TSSymbolList getSymbolList(Project project) {
        if (symbolLists.containsKey(project)) {
            return symbolLists.get(project);
        }

        TSSymbolList symbolList = new TSSymbolList(project);
        symbolLists.put(project, symbolList);
        return symbolList;
    }

    /**
     * Find all function in the project.
     * @param project Containing project in which to search
     * @return A list of functions
     */
    public static Collection<TSFnDeclStmt> getFunctionList(Project project) {
        return getSymbolList(project).getFunctionList();
    }

    /**
     * Find a function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSFnDeclStmt> findFunction(Project project, String key) {
        return getSymbolList(project).findFunction(key);
    }

    /**
     * Find all engine function in the project.
     * @param project Containing project in which to search
     * @return A list of functions
     */
    public static Collection<TSClassDumpEngineMethod> getEngineMethodList(Project project) {
        return getSymbolList(project).getEngineMethodList();
    }

    /**
     * Find an engine function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSClassDumpEngineMethod> findEngineMethod(Project project, String key) {
        return getSymbolList(project).findEngineMethod(key);
    }

    /**
     * Find all function in the project.
     * @param project Containing project in which to search
     * @return A list of functions
     */
    public static Collection<TSVarExpr> getGlobalList(Project project) {
        return getSymbolList(project).getGlobalList();
    }

    /**
     * Find a function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSVarExpr> findGlobal(Project project, String key) {
        return getSymbolList(project).findGlobal(key);
    }

    /**
     * Find all function in the project.
     * @param project Containing project in which to search
     * @return A list of functions
     */
    public static Collection<TSObjectExpr> getObjectList(Project project) {
        return getSymbolList(project).getObjectList();
    }

    /**
     * Find a function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSObjectExpr> findObject(Project project, String key) {
        return getSymbolList(project).findObject(key);
    }

    /**
     * Find all function in the project.
     * @param project Containing project in which to search
     * @return A list of functions
     */
    public static Collection<TSClass> getScriptClassList(Project project) {
        return getSymbolList(project).getScriptClassList();
    }

    /**
     * Find a function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSClass> findScriptClass(Project project, String key) {
        return getSymbolList(project).findScriptClass(key);
    }

    public static String getElementNamespace(PsiElement element) {
        if (element.getNode().getElementType().equals(TSTypes.ID)) {
            return element.getText();
        }

        //TODO???
        return null;
    }

    public static TextRange getRangeOfChild(PsiElement parent, PsiElement child) {
        TextRange range = new TextRange(child.getStartOffsetInParent(), child.getStartOffsetInParent() + child.getTextLength());
        while (child.getParent() != parent) {
            child = child.getParent();
            if (child == null) {
                return null;
            }
            range = new TextRange(range.getStartOffset() + child.getStartOffsetInParent(), range.getEndOffset() + child.getStartOffsetInParent());
        }
        return range;
    }
}
