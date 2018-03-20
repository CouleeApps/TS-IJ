package com.torquescript.symbols;

import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSClass {
    private String name;
    private TSClass parentClass;
    private List<TSClass> childClasses;
    private List<TSClassMember> members;
    private PsiElement element;

    public TSClass(String name, PsiElement element) {
        this.name = name;
        this.element = element;
        this.parentClass = null;
        this.childClasses = new ArrayList<>();
        this.members = new ArrayList<>();
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

    public PsiElement getElement() {
        return element;
    }

    public List<TSClassMember> getMembers() {
        List<TSClassMember> members = new ArrayList<>();
        if (parentClass != null) {
            members.addAll(parentClass.getMembers());
        }
        members.addAll(getOwnMembers());
        return members;
    }

    public List<TSClassMember> getOwnMembers() {
        return members;
    }

    public void addMember(TSClassMember member) {
        members.add(member);
    }

    public void removeMember(TSClassMember member) {
        members.remove(member);
    }

}
