package com.torquescript.symbolExporter.classDump;

import com.intellij.lang.Language;

public class TSClassDumpLanguage extends Language {
    public static final TSClassDumpLanguage INSTANCE = new TSClassDumpLanguage();

    private TSClassDumpLanguage() {
        super("TSClassDump");
    }
}
