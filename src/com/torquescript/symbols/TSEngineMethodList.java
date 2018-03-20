package com.torquescript.symbols;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.symbolExporter.TSSymbolExporter;
import com.torquescript.symbolExporter.classDump.TSClassDumpFile;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpClassDecl;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpEngineMethod;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpGroup;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpNamespaceDecl;

import java.util.Collections;
import java.util.HashSet;

/**
 * Created on 3/19/18.
 */
public class TSEngineMethodList {
    private final String engineMethodLock = "Lock";
    private HashSet<TSClassDumpEngineMethod> engineMethods;

    public TSEngineMethodList() {
        engineMethods = new HashSet<>();
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

    public HashSet<TSClassDumpEngineMethod> getList() {
        synchronized (engineMethodLock) {
            return engineMethods;
        }
    }
}
