package com.torquescript.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 3/19/18.
 */
public class TSClassList {

    private List<TSClass> rootClasses;

    public TSClassList() {
        this.rootClasses = new ArrayList<>();
    }

    public List<TSClass> childrenDepthFirst() {
        List<TSClass> allClasses = new ArrayList<>();

        for (TSClass rootClass : rootClasses) {
            allClasses.addAll(rootClass.childrenDepthFirst());
        }

        return allClasses;
    }

    public List<TSClass> childrenBreadthFirst() {
        List<TSClass> allClasses = new ArrayList<>(rootClasses);
        for (int i = allClasses.size() - 1; i < allClasses.size(); i ++) {
            TSClass current = allClasses.get(i);
            Collections.addAll(allClasses, current.getChildClasses());
        }

        return allClasses;
    }

    public TSClass findClass(String name) {
        List<TSClass> classes = childrenBreadthFirst();
        for (TSClass tsClass : classes) {
            if (tsClass.getName().equalsIgnoreCase(name)) {
                return tsClass;
            }
        }
        return null;
    }

    public void addClass(String name, String parentName) {
        TSClass parentClass = null;
        if (parentName != null) {
            parentClass = findClass(parentName);
            if (parentClass == null) {
                throw new RuntimeException("Cannot find parent class");
            }
        }

        TSClass newClass = new TSClass(name);
        newClass.setParentClass(parentClass);
        if (parentClass == null) {
            rootClasses.add(newClass);
        }
    }
}
