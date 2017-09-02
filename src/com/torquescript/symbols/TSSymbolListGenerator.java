package com.torquescript.symbols;

import com.intellij.openapi.project.Project;

import java.util.Set;

public abstract class TSSymbolListGenerator<T> {
    public TSSymbolListGenerator() {

    }
    public abstract Set<T> generate(Project project);
}
