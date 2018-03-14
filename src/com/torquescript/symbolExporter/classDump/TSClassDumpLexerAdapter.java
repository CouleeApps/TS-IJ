package com.torquescript.symbolExporter.classDump;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class TSClassDumpLexerAdapter extends FlexAdapter {
    public TSClassDumpLexerAdapter() {
        super(new TSClassDumpLexer((Reader) null));
    }
}
