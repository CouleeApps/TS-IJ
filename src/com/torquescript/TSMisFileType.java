package com.torquescript;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TSMisFileType extends LanguageFileType {

    public static final TSMisFileType INSTANCE = new TSMisFileType();

    private TSMisFileType() {
        super(TSLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Mission File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "TorqueScript Mission file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "mis";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return TSIcons.FILE;
    }
}
