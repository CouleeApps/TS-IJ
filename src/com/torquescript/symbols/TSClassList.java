package com.torquescript.symbols;

import com.intellij.psi.util.PsiTreeUtil;
import com.torquescript.symbolExporter.TSSymbolExporter;
import com.torquescript.symbolExporter.classDump.TSClassDumpFile;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpClassDecl;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpGroup;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created on 3/19/18.
 */
public class TSClassList {

    private List<TSClass> rootClasses;
    private final String rootClassesLock = "RCLock";

    public TSClassList() {
        this.rootClasses = new ArrayList<>();
    }

    /**
     * Get all root classes
     * @return
     */
    public List<TSClass> getRootClasses() {
        synchronized (rootClassesLock) {
            return rootClasses;
        }
    }

    /**
     * Get a list of all classes in depth-first traversal order (one root and its children/subs at a time)
     * @return A list of classes in the order specified
     */
    public List<TSClass> listDepthFirst() {
        List<TSClass> allClasses = new ArrayList<>();

        for (TSClass rootClass : getRootClasses()) {
            allClasses.addAll(rootClass.childrenDepthFirst());
        }

        return allClasses;
    }

    /**
     * Get a list of all classes in breadth-first traversal order (roots first, then their immediate children, etc)
     * @return A list of classes in the order specified
     */
    public List<TSClass> listBreadthFirst() {
        List<TSClass> allClasses = new ArrayList<>(getRootClasses());
        for (int i = 0; i < allClasses.size(); i ++) {
            TSClass current = allClasses.get(i);
            Collections.addAll(allClasses, current.getChildClasses());
        }

        return allClasses;
    }

    /**
     * Build the list of classes from a symbol export
     * @param exporter Symbol exporter with global classes to use for the new class list
     */
    public void buildClassList(TSSymbolExporter exporter) {
        List<TSClass> allClasses = new ArrayList<>();
        //Keep parents in a map String->String because we don't have all the classes
        // until we've gone through the list and they are probably not in class traversal order
        HashMap<String, String> parentClasses = new HashMap<>();
        //For efficiency
        HashMap<String, TSClass> nameToClass = new HashMap<>();

        TSClassDumpFile classDump = exporter.getGlobalClasses();
        if (classDump != null) {
            //Read global classes from the global dump
            TSClassDumpClassDecl[] classes = PsiTreeUtil.getChildrenOfType(classDump, TSClassDumpClassDecl.class);
            if (classes != null) {
                for (TSClassDumpClassDecl classDecl : classes) {
                    String className = classDecl.getName();
                    String parentName = classDecl.getParentName();

                    if (className == null) {
                        continue;
                    }
                    if (parentName != null) {
                        parentClasses.put(className, parentName);
                    }
                    TSClass newClass = new TSClass(className, classDecl);

                    TSClassDumpMember[] members = PsiTreeUtil.getChildrenOfType(classDecl, TSClassDumpMember.class);
                    if (members != null) {
                        for (TSClassDumpMember member : members) {
                            TSClassMember classMember = new TSClassMember(member.getName(), member.getTypeString(), member);
                            newClass.addMember(classMember);
                        }
                    }
                    TSClassDumpGroup[] groups = PsiTreeUtil.getChildrenOfType(classDecl, TSClassDumpGroup.class);
                    if (groups != null) {
                        for (TSClassDumpGroup group : groups) {
                            members = PsiTreeUtil.getChildrenOfType(group, TSClassDumpMember.class);
                            if (members != null) {
                                for (TSClassDumpMember member : members) {
                                    TSClassMember classMember = new TSClassMember(member.getName(), member.getTypeString(), member);
                                    newClass.addMember(classMember);
                                }
                            }
                        }
                    }

                    allClasses.add(newClass);
                    nameToClass.put(className, newClass);
                }
            }
        }

        //Assign parent classes
        for (TSClass tsClass : allClasses) {
            if (parentClasses.containsKey(tsClass.getName())) {
                String parentName = parentClasses.get(tsClass.getName());
                if (nameToClass.containsKey(parentName)) {
                    TSClass parentClass = nameToClass.get(parentName);
                    tsClass.setParentClass(parentClass);
                } else {
                    //Unknown who the parent class is, just make a dummy
                    TSClass parentClass = new TSClass(parentName, null);
                    tsClass.setParentClass(parentClass);
                    allClasses.add(parentClass);
                    nameToClass.put(parentName, parentClass);
                }
            }
        }

        //Collect all root classes
        List<TSClass> newRoots = new ArrayList<>();
        for (TSClass tsClass : allClasses) {
            if (tsClass.getParentClass() == null) {
                newRoots.add(tsClass);
            }
        }

        //And use the new list
        synchronized (rootClassesLock) {
            rootClasses = newRoots;
        }
    }
}
