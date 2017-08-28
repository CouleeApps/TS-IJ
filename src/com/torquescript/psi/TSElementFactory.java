package com.torquescript.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.torquescript.TSFile;
import com.torquescript.TSFileType;
import org.jetbrains.annotations.NotNull;

public class TSElementFactory {
    public static TSFnDeclStmt createFnDecl(Project project, String name) {
        final TSFile file = createFile(project, "function " + name + "(){}");
        return (TSFnDeclStmt) file.getFirstChild();
    }

    @NotNull
    public static TSFile createFile(Project project, String text) {
        String fileName = "dummy.cs";
        return (TSFile) PsiFileFactory.getInstance(project).createFileFromText(fileName, TSFileType.INSTANCE, text);
    }
}
