package com.torquescript;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.torquescript.psi.*;

import java.util.*;

public class TSUtil {
    private static Set<TSFnDeclStmt> FUNCTIONS = null;
    private static long LAST_UPDATE;
    private static final long CACHE_LIFETIME = 15 * /* ns */ 1000000;
    private static final String LOCK = "Probably slow";

    /**
     * Find a function in the project matching a given string.
     * @param project Containing project in which to search
     * @param key Search string compare functions with
     * @return A function declaration, or null if none is found
     */
    public static List<TSFnDeclStmt> findFunction(Project project, String key) {
        List<TSFnDeclStmt> result = null;
        Collection<TSFnDeclStmt> functions = new ArrayList<>(getFunctionList(project));
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

    /**
     * Get a list of all function declarations in the project. This list is cached and updated every few seconds
     * so you don't have to find all the functions for every function call.
     * @param project Containing project in which to search
     * @return A list of all function declarations
     */
    public static Collection<TSFnDeclStmt> getFunctionList(Project project) {
        //Need to synchronize this in case we update cache while something is accessing the functions
        synchronized (LOCK) {
            //If the cache has existed for long enough we should probably regenerate it
            if (FUNCTIONS != null) {
                if (System.nanoTime() - LAST_UPDATE > CACHE_LIFETIME) {
                    FUNCTIONS = null;
                }
            }
            //Cache is still warm, use it instead of searching
            if (FUNCTIONS != null) {
                return FUNCTIONS;
            }

            //Need to regenerate cache
            LAST_UPDATE = System.nanoTime();
            FUNCTIONS = new HashSet<>();

            //Search every file in the project
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
