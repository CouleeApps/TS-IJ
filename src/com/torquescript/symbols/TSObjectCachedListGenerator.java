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
import com.torquescript.psi.TSDatablockDecl;
import com.torquescript.psi.TSObjectExpr;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TSObjectCachedListGenerator extends TSCachedListGenerator<TSObjectExpr> {
    @Override
    public Set<TSObjectExpr> generate(Project project) {
        Set<TSObjectExpr> items = new HashSet<>();
        //Search every file in the project
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (tsFile != null) {
                items.addAll(PsiTreeUtil.findChildrenOfType(tsFile, TSObjectExpr.class));
                items.addAll(PsiTreeUtil.findChildrenOfType(tsFile, TSDatablockDecl.class));
            }
            ProgressManager.progress("Loading Symbols");
        }
        return items;
    }
}
