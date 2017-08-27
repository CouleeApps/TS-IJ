package com.torquescript.psi;

import com.intellij.psi.tree.IElementType;
import com.torquescript.TSLanguage;
import org.jetbrains.annotations.NotNull;

public class TSElementType extends IElementType {
    public TSElementType(@NotNull String debugName) {
        super(debugName, TSLanguage.INSTANCE);
    }
}
