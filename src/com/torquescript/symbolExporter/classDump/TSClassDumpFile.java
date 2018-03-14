package com.torquescript.symbolExporter.classDump;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.torquescript.TSFileType;
import com.torquescript.TSLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TSClassDumpFile extends PsiFileBase {
    protected TSClassDumpFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, TSClassDumpLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return TSClassDumpFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Torque Class Dump File";
    }

    @Nullable
    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}
