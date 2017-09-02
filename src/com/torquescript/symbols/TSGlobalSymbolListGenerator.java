package com.torquescript.symbols;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.torquescript.TSFile;
import com.torquescript.TSFileType;
import com.torquescript.psi.TSAssignExpr;
import com.torquescript.psi.TSFnDeclStmt;
import com.torquescript.psi.TSPackageDecl;
import com.torquescript.psi.TSVarExpr;

import java.util.*;

public class TSGlobalSymbolListGenerator extends TSSymbolListGenerator<TSVarExpr> {
    @Override
    public Set<TSVarExpr> generate(Project project) {
        Set<TSVarExpr> items = new HashSet<>();
        //Search every file in the project
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (tsFile != null) {
                try {
                    Collection<TSAssignExpr> assignments = PsiTreeUtil.findChildrenOfType(tsFile, TSAssignExpr.class);
                    for (TSAssignExpr assignment : assignments) {
                        PsiElement first = assignment.getFirstChild();
                        if (!(first instanceof TSVarExpr))
                            continue;

                        if (((TSVarExpr)first).isLocal())
                            continue;

                        items.add((TSVarExpr) first);
                    }
                } catch (Exception ignored) {

                }
            }
        }
        return items;
    }
}
