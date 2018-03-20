package com.torquescript.symbols;

import com.torquescript.symbolExporter.classDump.psi.TSClassDumpMember;

/**
 * Created on 3/19/18.
 */
public class TSClassMember {
    private String name;
    private String type;
    private TSClassDumpMember element;

    public TSClassMember(String name, String type, TSClassDumpMember element) {
        this.name = name;
        this.type = type;
        this.element = element;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public TSClassDumpMember getElement() {
        return element;
    }
}
