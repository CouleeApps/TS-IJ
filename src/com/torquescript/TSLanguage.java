package com.torquescript;

import com.intellij.lang.Language;

public class TSLanguage extends Language {
    public static final TSLanguage INSTANCE = new TSLanguage();

    private TSLanguage() {
        super("TS");
    }
}
