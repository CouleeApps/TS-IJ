package com.torquescript.symbolExporter.classDump;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.torquescript.TSIcons;
import com.torquescript.TSLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TSClassDumpFileType extends LanguageFileType {

    public static final TSClassDumpFileType INSTANCE = new TSClassDumpFileType();

    private TSClassDumpFileType() {
        super(TSClassDumpLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "TS Class Dump File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Torque class dump file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "tscd";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return TSIcons.FILE;
    }
}
