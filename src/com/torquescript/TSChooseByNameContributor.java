package com.torquescript;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.torquescript.psi.TSFnDeclStmt;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TSChooseByNameContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        Collection<TSFnDeclStmt> functions = TSUtil.getFunctionList(project);
        List<String> names = new ArrayList<>(functions.size());
        for (TSFnDeclStmt function : functions) {
            if (function.getFunctionName() != null && function.getFunctionName().length() != 0) {
                names.add(function.getFunctionName());
            }
        }
        return names.toArray(new String[names.size()]);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        List<NavigationItem> navs = new ArrayList<>();
        navs.addAll(TSUtil.findFunction(project, name));
        navs.addAll(TSUtil.findEngineMethod(project, name));
        return navs.toArray(new NavigationItem[navs.size()]);
    }
}
