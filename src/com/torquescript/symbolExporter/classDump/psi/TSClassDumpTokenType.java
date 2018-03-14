package com.torquescript.symbolExporter.classDump.psi;

import com.intellij.psi.tree.IElementType;
import com.torquescript.TSLanguage;
import com.torquescript.symbolExporter.classDump.TSClassDumpLanguage;
import org.jetbrains.annotations.NotNull;

public class TSClassDumpTokenType extends IElementType {
    public TSClassDumpTokenType(@NotNull String debugName) {
        super(debugName, TSClassDumpLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "TSClassDumpTokenType." + super.toString();
    }
}
