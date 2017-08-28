package com.torquescript;

import com.android.tools.sherpa.drawing.decorator.ColorTheme;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.indexing.FileBasedIndex;
import com.torquescript.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TSCompletionContributor extends CompletionContributor {
    public TSCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(TSTypes.ID).withLanguage(TSLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                        for (String keyword : keywords) {
                            result.addElement(LookupElementBuilder.create(keyword));
                        }

                        //All global functions
                        Project project = parameters.getOriginalFile().getProject();
                        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSFileType.INSTANCE, GlobalSearchScope.allScope(project));

                        for (VirtualFile virtualFile : virtualFiles) {
                            TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
                            if (tsFile != null) {
                                TSFnDeclStmt[] functions = PsiTreeUtil.getChildrenOfType(tsFile, TSFnDeclStmt.class);
                                if (functions != null) {
                                    for (TSFnDeclStmt function : functions) {
                                        result.addElement(LookupElementBuilder.createWithSmartPointer(function.getFunctionName(), function));
                                    }
                                }
                            }
                        }
                    }
                });
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(TSTypes.GLOBALVAR).withLanguage(TSLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                        //All global variables
                        Project project = parameters.getOriginalFile().getProject();
                        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TSFileType.INSTANCE, GlobalSearchScope.allScope(project));

                        for (VirtualFile virtualFile : virtualFiles) {
                            TSFile tsFile = (TSFile) PsiManager.getInstance(project).findFile(virtualFile);
                            if (tsFile != null) {
                                TSVarExpr[] variables = PsiTreeUtil.getChildrenOfType(tsFile, TSVarExpr.class);
                                if (variables != null) {
                                    for (TSVarExpr variable : variables) {
                                        if (variable.isLocal()) {
                                            continue;
                                        }
                                        result.addElement(LookupElementBuilder.create(variable.getName()));
                                    }
                                }
                            }
                        }
                    }
                });
    }

    String keywords[] = new String[] {
            "function",
            "package",
            "if",
            "else",
            "while",
            "for",
            "switch",
            "switch$",
            "case",
            "default",
            "or",
            "break",
            "continue",
            "return",
            "datablock",
            "new"
    };

}
