package com.torquescript.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TSClass {
    private String name;
    private TSClass parentClass;
    private List<TSClass> childClasses;

    public TSClass(String name) {
        this.name = name;
        this.parentClass = null;
        this.childClasses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public TSClass getParentClass() {
        return parentClass;
    }

    public void setParentClass(TSClass parent) {
        if (parentClass != null) {
            parentClass.removeChildClass(this);
        }
        parentClass = parent;
        if (parent != null) {
            parent.addChildClass(this);
        }
    }

    public TSClass[] getChildClasses() {
        return childClasses.toArray(new TSClass[childClasses.size()]);
    }

    public void addChildClass(TSClass child) {
        childClasses.add(child);
    }

    public void removeChildClass(TSClass child) {
        childClasses.remove(child);
    }

    public List<TSClass> childrenDepthFirst() {
        List<TSClass> allClasses = new ArrayList<>();

        for (TSClass child : childClasses) {
            allClasses.add(child);
            allClasses.addAll(child.childrenDepthFirst());
        }

        return allClasses;
    }

    public List<TSClass> childrenBreadthFirst() {
        List<TSClass> allClasses = new ArrayList<>(childClasses);

        for (int i = 0; i < allClasses.size(); i ++) {
            TSClass current = allClasses.get(i);
            Collections.addAll(allClasses, current.getChildClasses());
        }

        return allClasses;
    }
}
