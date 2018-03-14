package com.torquescript.symbolExporter.classDump.psi;

import com.intellij.psi.tree.IElementType;
import com.torquescript.TSLanguage;
import com.torquescript.symbolExporter.classDump.TSClassDumpLanguage;
import org.jetbrains.annotations.NotNull;

public class TSClassDumpElementType extends IElementType {
    public TSClassDumpElementType(@NotNull String debugName) {
        super(debugName, TSClassDumpLanguage.INSTANCE);
    }
}
