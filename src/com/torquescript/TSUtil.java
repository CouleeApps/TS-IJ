package com.torquescript;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Time;
import com.intellij.util.indexing.FileBasedIndex;
import com.torquescript.psi.*;

import java.util.*;

public class TSUtil {
    private static List<TSFnDeclStmt> FUNCTIONS = null;
    private static long LAST_UPDATE;
    private static final String LOCK = "Probably slow";

    public static List<TSFnDeclStmt> findFunctions(Project project, String key) {
        List<TSFnDeclStmt> result = null;
        List<TSFnDeclStmt> functions = new ArrayList<>(findFunctions(project));
        for (TSFnDeclStmt function : functions) {
            if (key.equalsIgnoreCase(function.getFunctionName())) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(function);
            }
        }
        return result == null ? Collections.emptyList() : result;
    }

    public static List<TSFnDeclStmt> findFunctions(Project project) {
        synchronized (LOCK) {
            if (FUNCTIONS != null) {
                if (System.nanoTime() - LAST_UPDATE > /*1 sec*/ 1000000 * 15) {
                    FUNCTIONS = null;
                }
            }
            if (FUNCTIONS != null) {
                return FUNCTIONS;
            }

            LAST_UPDATE = System.nanoTime();

            FUNCTIONS = new ArrayList<>();
            Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSFileType.INSTANCE, GlobalSearchScope.allScope(project));

            for (VirtualFile virtualFile : virtualFiles) {
                TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
                if (tsFile != null) {
                    try {
                        TSFnDeclStmt[] functions = PsiTreeUtil.getChildrenOfType(tsFile, TSFnDeclStmt.class);
                        if (functions != null) {
                            Collections.addAll(FUNCTIONS, functions);
                        }

                        TSPackageDecl[] packages = PsiTreeUtil.getChildrenOfType(tsFile, TSPackageDecl.class);
                        if (packages != null) {
                            for (TSPackageDecl pack : packages) {
                                functions = PsiTreeUtil.getChildrenOfType(pack, TSFnDeclStmt.class);
                                if (functions != null) {
                                    for (TSFnDeclStmt function : functions) {
                                        Collections.addAll(FUNCTIONS, functions);
                                    }
                                }
                            }
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
            return FUNCTIONS;
        }
    }

    public static String getElementNamespace(PsiElement element) {
        if (element.getNode().getElementType().equals(TSTypes.ID)) {
            return element.getText();
        }

        //TODO???
        return null;
    }
}
