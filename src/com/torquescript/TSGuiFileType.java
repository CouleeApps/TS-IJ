package com.torquescript;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TSGuiFileType extends LanguageFileType {

    public static final TSGuiFileType INSTANCE = new TSGuiFileType();

    private TSGuiFileType() {
        super(TSLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "GUI File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "TorqueScript GUI file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "gui";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return TSIcons.FILE;
    }
}
