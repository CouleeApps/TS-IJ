package com.torquescript.symbols;

import com.intellij.openapi.project.Project;
import java.util.Collection;

public abstract class TSCachedListGenerator<T> {
    public abstract Collection<T> generate(Project project);
}
