package com.torquescript;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.torquescript.psi.TSTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class TSSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("TS_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
    public static final TextAttributesKey LINE_COMMENT = createTextAttributesKey("TS_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey LOCAL_VARIABLE = createTextAttributesKey("TS_LOCAL_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
    public static final TextAttributesKey GLOBAL_VARIABLE = createTextAttributesKey("TS_GLOBAL_VARIABLE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE);
    public static final TextAttributesKey ID = createTextAttributesKey("TS_ID", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey NUMBER = createTextAttributesKey("TS_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey STRING = createTextAttributesKey("TS_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey KEYWORD = createTextAttributesKey("TS_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey FUNCTION = createTextAttributesKey("TS_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    public static final TextAttributesKey CLASSNAME = createTextAttributesKey("TS_CLASSNAME", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey FUNCTION_CALL = createTextAttributesKey("TS_FUNCTION_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL);

    public static final Map<IElementType, TextAttributesKey[]> KEYS = new HashMap<>();
    public static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
    static {
        KEYS.put(TokenType.BAD_CHARACTER, new TextAttributesKey[]{BAD_CHARACTER});
        KEYS.put(TSTypes.COMMENT, new TextAttributesKey[]{LINE_COMMENT});
        KEYS.put(TSTypes.LOCALVAR, new TextAttributesKey[]{LOCAL_VARIABLE});
        KEYS.put(TSTypes.GLOBALVAR, new TextAttributesKey[]{GLOBAL_VARIABLE});
        KEYS.put(TSTypes.ID, new TextAttributesKey[]{ID});
        KEYS.put(TSTypes.INTEGER, new TextAttributesKey[]{NUMBER});
        KEYS.put(TSTypes.FLOAT, new TextAttributesKey[]{NUMBER});
        KEYS.put(TSTypes.STRATOM, new TextAttributesKey[]{STRING});
        KEYS.put(TSTypes.TAGATOM, new TextAttributesKey[]{STRING});
        KEYS.put(TSTypes.FUNCTION, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.PACKAGE, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.IF, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.ELSE, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.WHILE, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.FOR, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.SWITCH, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.SWITCHSTR, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.CASE, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.DEFAULT, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.CASEOR, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.BREAK, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.CONTINUE, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.RETURN, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.DATABLOCK, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.NEW, new TextAttributesKey[]{KEYWORD});

        KEYS.put(TSTypes.FN_DECL_STMT, new TextAttributesKey[]{FUNCTION});
        KEYS.put(TSTypes.CALL_REF_EXPR, new TextAttributesKey[]{FUNCTION_CALL});
        KEYS.put(TSTypes.CALL_GLOBAL_EXPR, new TextAttributesKey[]{FUNCTION_CALL});
        KEYS.put(TSTypes.CALL_NS_GLOBAL_EXPR, new TextAttributesKey[]{FUNCTION_CALL});
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new TSLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (KEYS.containsKey(tokenType)) {
            return KEYS.get(tokenType);
        }
        return EMPTY_KEYS;
    }
}
