package com.torquescript.symbols;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.ID;
import com.torquescript.TSFile;
import com.torquescript.TSFileType;
import com.torquescript.TSGuiFileType;
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
        virtualFiles.addAll(FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSGuiFileType.INSTANCE, GlobalSearchScope.projectScope(project)));
        for (VirtualFile virtualFile : virtualFiles) {
            TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (tsFile != null) {
                try {
                    Collection<TSObjectExpr> objects = PsiTreeUtil.findChildrenOfType(tsFile, TSObjectExpr.class);
                    items.addAll(objects);
                } catch (Exception ignored) {

                }
            }
        }
        return items;
    }
}
