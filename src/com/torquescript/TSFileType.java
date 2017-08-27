package com.torquescript;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TSFileType extends LanguageFileType {

    public static final TSFileType INSTANCE = new TSFileType();

    private TSFileType() {
        super(TSLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "TS File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "TorqueScript file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "cs";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return TSIcons.FILE;
    }
}
