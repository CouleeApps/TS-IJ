package com.torquescript;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TSFile extends PsiFileBase {
    protected TSFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, TSLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return TSFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "TorqueScript File";
    }

    @Nullable
    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}
