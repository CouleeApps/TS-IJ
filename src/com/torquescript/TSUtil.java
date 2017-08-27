package com.torquescript;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.torquescript.psi.TSFnDeclStmt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TSUtil {
    public static List<TSFnDeclStmt> findFunctions(Project project, String key) {
        List<TSFnDeclStmt> result = null;
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSFileType.INSTANCE, GlobalSearchScope.allScope(project));

        for (VirtualFile virtualFile : virtualFiles) {
            TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (tsFile != null) {
                TSFnDeclStmt[] functions = PsiTreeUtil.getChildrenOfType(tsFile, TSFnDeclStmt.class);
                if (functions != null) {
                    for (TSFnDeclStmt function : functions) {
                        if (key.equals(function.getFunctionName())) {
                            if (result == null) {
                                result = new ArrayList<>();
                            }
                            result.add(function);
                        }
                    }
                }
            }
        }

        return result == null ? Collections.emptyList() : result;
    }

    public static List<TSFnDeclStmt> findFunctions(Project project) {
        List<TSFnDeclStmt> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSFileType.INSTANCE, GlobalSearchScope.allScope(project));

        for (VirtualFile virtualFile : virtualFiles) {
            TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (tsFile != null) {
                TSFnDeclStmt[] functions = PsiTreeUtil.getChildrenOfType(tsFile, TSFnDeclStmt.class);
                if (functions != null) {
                    Collections.addAll(result, functions);
                }
            }
        }

        return result;
    }
}
