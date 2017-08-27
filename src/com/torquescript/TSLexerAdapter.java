package com.torquescript;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class TSLexerAdapter extends FlexAdapter {
    public TSLexerAdapter() {
        super(new TSLexer((Reader) null));
    }
}
