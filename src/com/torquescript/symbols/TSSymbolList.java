package com.torquescript.symbols;

import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.model.TSClassList;
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.psi.TSObjectExpr;
import com.torquescript.psi.TSVarExpr;
import com.torquescript.symbolExporter.TSSymbolExporter;
import com.torquescript.symbolExporter.classDump.TSClassDumpFile;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpClassDecl;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpEngineMethod;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpGroup;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpNamespaceDecl;

import java.util.*;

/**
 * Created on 3/19/18.
 */
public class TSSymbolList {
    private Project project;
    private TSCachedList<TSFnDeclStmt> functions;
    private TSCachedList<TSVarExpr> globals;
    private TSCachedList<TSObjectExpr> objects;
    private TSClassList classList;

    private final String engineMethodLock = "Lock";
    private Collection<TSClassDumpEngineMethod> engineMethods;

    public TSSymbolList(Project project) {
        this.project = project;
        functions = new TSCachedList<>(project, new TSFunctionCachedListGenerator());
        globals = new TSCachedList<>(project, new TSGlobalCachedListGenerator());
        objects = new TSCachedList<>(project, new TSObjectCachedListGenerator());
        engineMethods = new HashSet<>();
        classList = new TSClassList();
    }

    /**
     * Find all function in the project.
     * @return A list of functions
     */
    public Collection<TSFnDeclStmt> getFunctionList() {
        return new ArrayList<>(functions.getList());
    }

    /**
     * Find a function in the project matching a given string.
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
     * @return A list of functions
     */
    public Collection<TSVarExpr> getGlobalList() {
        return new ArrayList<>(globals.getList());
    }

    /**
     * Find a function in the project matching a given string.
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
     * @return A list of functions
     */
    public Collection<TSObjectExpr> getObjectList() {
        return new ArrayList<>(objects.getList());
    }

    /**
     * Find a function in the project matching a given string.
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

    public TSClassList getClassList() {
        return classList;
    }


    /**
     * Find all engine function in the project.
     * @return A list of functions
     */
    public Collection<TSClassDumpEngineMethod> getEngineMethodList() {
        synchronized (engineMethodLock) {
            return engineMethods;
        }
    }

    /**
     * Find an engine function in the project matching a given string.
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
     * Build the engine method list from a project from a symbol export list
     */
    public void buildEngineMethodList(TSSymbolExporter exporter) {
        HashSet<TSClassDumpEngineMethod> methods = new HashSet<>();

        TSClassDumpFile classDump = exporter.getGlobalClasses();
        if (classDump != null) {
            //Read global functions from the global dump
            TSClassDumpClassDecl[] classes = PsiTreeUtil.getChildrenOfType(classDump, TSClassDumpClassDecl.class);
            if (classes != null) {
                for (TSClassDumpClassDecl classDecl : classes) {
                    //Get all functions in the class
                    TSClassDumpEngineMethod[] classMethods = PsiTreeUtil.getChildrenOfType(classDecl, TSClassDumpEngineMethod.class);
                    if (classMethods != null) {
                        Collections.addAll(methods, classMethods);
                    }

                    TSClassDumpGroup[] groups = PsiTreeUtil.getChildrenOfType(classDecl, TSClassDumpGroup.class);
                    addEngineMethodsFromGroups(methods, groups);
                }
            }
        }
        TSClassDumpFile functionDump = exporter.getGlobalFunctions();
        if (functionDump != null) {
            //Read global functions from the global dump
            TSClassDumpNamespaceDecl[] namespaces = PsiTreeUtil.getChildrenOfType(functionDump, TSClassDumpNamespaceDecl.class);
            if (namespaces != null) {
                for (TSClassDumpNamespaceDecl namespaceDecl : namespaces) {
                    //Get all functions in the class
                    TSClassDumpEngineMethod[] nsMethods = PsiTreeUtil.getChildrenOfType(namespaceDecl, TSClassDumpEngineMethod.class);
                    if (nsMethods != null) {
                        Collections.addAll(methods, nsMethods);
                    }

                    TSClassDumpGroup[] groups = PsiTreeUtil.getChildrenOfType(namespaceDecl, TSClassDumpGroup.class);
                    addEngineMethodsFromGroups(methods, groups);
                }
            }
        }

        synchronized (engineMethodLock) {
            engineMethods = methods;
        }
    }

    private void addEngineMethodsFromGroups(HashSet<TSClassDumpEngineMethod> methods, TSClassDumpGroup[] groups) {
        if (groups != null) {
            for (TSClassDumpGroup group : groups) {
                //Get all functions in the group
                TSClassDumpEngineMethod[] groupMethods = PsiTreeUtil.getChildrenOfType(group, TSClassDumpEngineMethod.class);
                if (groupMethods != null) {
                    Collections.addAll(methods, groupMethods);
                }
            }
        }
    }
}
