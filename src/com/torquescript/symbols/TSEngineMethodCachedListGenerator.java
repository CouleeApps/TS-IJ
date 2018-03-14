package com.torquescript.symbols;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.torquescript.TSFile;
import com.torquescript.TSFileType;
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.psi.TSPackageDecl;
import com.torquescript.symbolExporter.TSSymbolExporter;
import com.torquescript.symbolExporter.classDump.TSClassDumpFile;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpClassDecl;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpEngineMethod;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpGroup;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpNamespaceDecl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TSEngineMethodCachedListGenerator extends TSCachedListGenerator<TSClassDumpEngineMethod> {
    @Override
    public Set<TSClassDumpEngineMethod> generate(Project project) {
        Set<TSClassDumpEngineMethod> items = new HashSet<>();

        TSSymbolExporter exporter = TSSymbolExporter.getExporter(project);
        if (exporter != null) {
            TSClassDumpFile classDump = exporter.getGlobalClasses();
            if (classDump != null) {
                //Read global functions from the global dump
                TSClassDumpClassDecl[] classes = PsiTreeUtil.getChildrenOfType(classDump, TSClassDumpClassDecl.class);
                if (classes != null) {
                    for (TSClassDumpClassDecl classDecl : classes) {
                        //Get all functions in the class
                        TSClassDumpEngineMethod[] methods = PsiTreeUtil.getChildrenOfType(classDecl, TSClassDumpEngineMethod.class);
                        if (methods != null) {
                            Collections.addAll(items, methods);
                        }

                        TSClassDumpGroup[] groups = PsiTreeUtil.getChildrenOfType(classDecl, TSClassDumpGroup.class);
                        if (groups != null) {
                            for (TSClassDumpGroup group : groups) {
                                //Get all functions in the group
                                methods = PsiTreeUtil.getChildrenOfType(group, TSClassDumpEngineMethod.class);
                                if (methods != null) {
                                    Collections.addAll(items, methods);
                                }
                            }
                        }
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
                        TSClassDumpEngineMethod[] methods = PsiTreeUtil.getChildrenOfType(namespaceDecl, TSClassDumpEngineMethod.class);
                        if (methods != null) {
                            Collections.addAll(items, methods);
                        }

                        TSClassDumpGroup[] groups = PsiTreeUtil.getChildrenOfType(namespaceDecl, TSClassDumpGroup.class);
                        if (groups != null) {
                            for (TSClassDumpGroup group : groups) {
                                //Get all functions in the group
                                methods = PsiTreeUtil.getChildrenOfType(group, TSClassDumpEngineMethod.class);
                                if (methods != null) {
                                    Collections.addAll(items, methods);
                                }
                            }
                        }
                    }
                }
            }

        }

        return items;
    }
}
