package com.torquescript.symbols;

import com.intellij.openapi.project.Project;
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.psi.TSObjectExpr;
import com.torquescript.psi.TSVarExpr;
import com.torquescript.symbolExporter.TSSymbolExporter;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpEngineMethod;

import java.util.*;

/**
 * Created on 3/19/18.
 */
public class TSSymbolList {
    private Project project;
    private TSCachedList<TSFnDeclStmt> functions;
    private TSCachedList<TSVarExpr> globals;
    private TSCachedList<TSObjectExpr> objects;
    private TSClassList scriptClassList;
    private TSEngineMethodList engineMethodList;

    public TSSymbolList(Project project) {
        this.project = project;
        functions = new TSCachedList<>(project, new TSFunctionCachedListGenerator());
        globals = new TSCachedList<>(project, new TSGlobalCachedListGenerator());
        objects = new TSCachedList<>(project, new TSObjectCachedListGenerator());
        engineMethodList = new TSEngineMethodList();
        scriptClassList = new TSClassList();
    }

    /**
     * Find all function in the project.
     *
     * @return A list of functions
     */
    public Collection<TSFnDeclStmt> getFunctionList() {
        return new ArrayList<>(functions.getList());
    }

    /**
     * Find a function in the project matching a given string.
     *
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public List<TSFnDeclStmt> findFunction(String key) {
        List<TSFnDeclStmt> result = null;
        Collection<TSFnDeclStmt> functions = getFunctionList();
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
     *
     * @return A list of functions
     */
    public Collection<TSVarExpr> getGlobalList() {
        return new ArrayList<>(globals.getList());
    }

    /**
     * Find a function in the project matching a given string.
     *
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public List<TSVarExpr> findGlobal(String key) {
        List<TSVarExpr> result = null;
        Collection<TSVarExpr> globals = getGlobalList();
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
     *
     * @return A list of functions
     */
    public Collection<TSObjectExpr> getObjectList() {
        return new ArrayList<>(objects.getList());
    }

    /**
     * Find a function in the project matching a given string.
     *
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public List<TSObjectExpr> findObject(String key) {
        List<TSObjectExpr> result = null;
        Collection<TSObjectExpr> objects = getObjectList();
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

    /**
     * Find all script classes in the project.
     *
     * @return A list of functions
     */
    public Collection<TSClass> getScriptClassList() {
        return scriptClassList.listBreadthFirst();
    }

    /**
     * Find a script class in the project matching a given string.
     *
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public List<TSClass> findScriptClass(String key) {
        List<TSClass> result = null;
        Collection<TSClass> methods = getScriptClassList();
        for (TSClass method : methods) {
            if (key.equalsIgnoreCase(method.getName())) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(method);
            }
        }
        return result == null ? Collections.emptyList() : result;
    }

    /**
     * Find all engine function in the project.
     *
     * @return A list of functions
     */
    public Collection<TSClassDumpEngineMethod> getEngineMethodList() {
        return engineMethodList.getList();
    }

    /**
     * Find an engine function in the project matching a given string.
     *
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public List<TSClassDumpEngineMethod> findEngineMethod(String key) {
        List<TSClassDumpEngineMethod> result = null;
        Collection<TSClassDumpEngineMethod> methods = getEngineMethodList();
        for (TSClassDumpEngineMethod method : methods) {
            if (key.equalsIgnoreCase(method.getFunctionName())) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(method);
            }
        }
        return result == null ? Collections.emptyList() : result;
    }

    /**
     * Build the script class list from a symbol export list
     */
    public void buildScriptClassList(TSSymbolExporter exporter) {
        scriptClassList.buildClassList(exporter);
    }

    /**
     * Build the engine method list from a symbol export list
     */
    public void buildEngineMethodList(TSSymbolExporter exporter) {
        engineMethodList.buildEngineMethodList(exporter);
    }
}
