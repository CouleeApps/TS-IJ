package com.torquescript.psi;

import com.intellij.psi.tree.IElementType;
import com.torquescript.TSLanguage;
import org.jetbrains.annotations.NotNull;

public class TSTokenType extends IElementType {
    public TSTokenType(@NotNull String debugName) {
        super(debugName, TSLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "TSTokenType." + super.toString();
    }
}
