package com.torquescript.highlighting;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.torquescript.TSLexerAdapter;
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
    public static final TextAttributesKey OPERATOR = createTextAttributesKey("TS_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey DOT = createTextAttributesKey("TS_DOT", DefaultLanguageHighlighterColors.DOT);
    public static final TextAttributesKey COMMA = createTextAttributesKey("TS_COMMA", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey SEMICOLON = createTextAttributesKey("TS_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey PARENTHESES = createTextAttributesKey("TS_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey BRACES = createTextAttributesKey("TS_BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey BRACKETS = createTextAttributesKey("TS_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey FUNCTION = createTextAttributesKey("TS_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    public static final TextAttributesKey CLASSNAME = createTextAttributesKey("TS_CLASSNAME", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey FIELD_NAME = createTextAttributesKey("TS_FIELD_NAME", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
    public static final TextAttributesKey FUNCTION_CALL = createTextAttributesKey("TS_FUNCTION_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL);
    public static final TextAttributesKey OBJECT_NAME = createTextAttributesKey("TS_OBJECT_NAME", DefaultLanguageHighlighterColors.IDENTIFIER);

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
        KEYS.put(TSTypes.SINGLETON, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.NEW, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.TRUE, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.FALSE, new TextAttributesKey[]{KEYWORD});
        KEYS.put(TSTypes.PARENT, new TextAttributesKey[]{KEYWORD});

        KEYS.put(TSTypes.PAREN_OPEN, new TextAttributesKey[]{PARENTHESES});
        KEYS.put(TSTypes.PAREN_CLOSE, new TextAttributesKey[]{PARENTHESES});
        KEYS.put(TSTypes.BRACKET_OPEN, new TextAttributesKey[]{BRACKETS});
        KEYS.put(TSTypes.BRACKET_CLOSE, new TextAttributesKey[]{BRACKETS});
        KEYS.put(TSTypes.BRACE_OPEN, new TextAttributesKey[]{BRACES});
        KEYS.put(TSTypes.BRACE_CLOSE, new TextAttributesKey[]{BRACES});
        KEYS.put(TSTypes.COLON, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COLON_DOUBLE, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_INC, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_DEC, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_ADD, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_SUB, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_MUL, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_DIV, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_MOD, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_AND, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_XOR, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_OR, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_LSHIFT, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.ASSIGN_RSHIFT, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COND_GT, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COND_LT, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COND_GE, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COND_LE, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COND_EQ, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COND_NE, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COND_OR, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.LSHIFT, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.RSHIFT, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COND_AND, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COND_STREQ, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.COND_STRNE, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_XOR, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_MOD, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_BIT_AND, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_BIT_OR, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_ADD, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_SUB, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_MUL, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_DIV, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.TERNARY, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_CAT, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_CAT_SPC, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_CAT_TAB, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_CAT_NL, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_NOT, new TextAttributesKey[]{OPERATOR});
        KEYS.put(TSTypes.OP_BIT_NOT, new TextAttributesKey[]{OPERATOR});

        KEYS.put(TSTypes.SEMICOLON, new TextAttributesKey[]{SEMICOLON});
        KEYS.put(TSTypes.COMMA, new TextAttributesKey[]{COMMA});
        KEYS.put(TSTypes.DOT, new TextAttributesKey[]{DOT});
        KEYS.put(TSTypes.CALL_METHOD_EXPR, new TextAttributesKey[]{FUNCTION_CALL});
        KEYS.put(TSTypes.CALL_GLOBAL_EXPR, new TextAttributesKey[]{FUNCTION_CALL});
        KEYS.put(TSTypes.CALL_GLOBAL_NS_EXPR, new TextAttributesKey[]{FUNCTION_CALL});
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
