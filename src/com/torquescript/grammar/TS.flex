package com.torquescript;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.torquescript.psi.TSTypes;
import com.intellij.psi.TokenType;

%%

%class TSLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

//https://stackoverflow.com/a/10786066/214063
STRATOM=\"([^\"\\]*(\\.[^\"\\]*)*)\"
TAGATOM=\'([^\'\\]*(\\.[^\'\\]*)*)\'

DIGIT    =[0-9]
INTEGER  ={DIGIT}+
FLOAT    =({INTEGER}\.{INTEGER})|({INTEGER}(\.{INTEGER})?[eE][+-]?{INTEGER})|(\.{INTEGER})|((\.{INTEGER})?[eE][+-]?{INTEGER})
LETTER   =[A-Za-z_]
VARMID   =[:A-Za-z0-9_]
IDTAIL   =[A-Za-z0-9_]
VARTAIL  ={VARMID}*{IDTAIL}
LOCALVAR ="%"{LETTER}{VARTAIL}*
GLOBALVAR="$"{LETTER}{VARTAIL}*
ID       ={LETTER}{IDTAIL}*
SPACE    =[ \t\v\f]
CRLF     =[\r\n]
COMMENT  ="//"[^\r\n]*

%state WAITING_VALUE

%%

<YYINITIAL> {
    {COMMENT}                                       { yybegin(YYINITIAL); return TSTypes.COMMENT; }

    "function"                                      { return TSTypes.FUNCTION; }
    "package"                                       { return TSTypes.PACKAGE; }
    "if"                                            { return TSTypes.IF; }
    "else"                                          { return TSTypes.ELSE; }
    "while"                                         { return TSTypes.WHILE; }
    "for"                                           { return TSTypes.FOR; }
    "switch"                                        { return TSTypes.SWITCH; }
    "switch$"                                       { return TSTypes.SWITCHSTR; }
    "case"                                          { return TSTypes.CASE; }
    "default"                                       { return TSTypes.DEFAULT; }
    "or"                                            { return TSTypes.CASEOR; }
    "break"                                         { return TSTypes.BREAK; }
    "continue"                                      { return TSTypes.CONTINUE; }
    "return"                                        { return TSTypes.RETURN; }
    "datablock"                                     { return TSTypes.DATABLOCK; }
    "new"                                           { return TSTypes.NEW; }
    "true"                                          { return TSTypes.TRUE; }
    "false"                                         { return TSTypes.FALSE; }

    "(" { return TSTypes.PAREN_OPEN; }
    ")" { return TSTypes.PAREN_CLOSE; }
    "[" { return TSTypes.BRACKET_OPEN; }
    "]" { return TSTypes.BRACKET_CLOSE; }
    "{" { return TSTypes.BRACE_OPEN; }
    "}" { return TSTypes.BRACE_CLOSE; }
    ":" { return TSTypes.COLON; }
    ";" { return TSTypes.SEMICOLON; }
    "," { return TSTypes.COMMA; }
    "." { return TSTypes.DOT; }
    "::" { return TSTypes.COLON_DOUBLE; }

    "=" { return TSTypes.ASSIGN; }
    "++" { return TSTypes.ASSIGN_INC; }
    "--" { return TSTypes.ASSIGN_DEC; }
    "+=" { return TSTypes.ASSIGN_ADD; }
    "-=" { return TSTypes.ASSIGN_SUB; }
    "*=" { return TSTypes.ASSIGN_MUL; }
    "/=" { return TSTypes.ASSIGN_DIV; }
    "%=" { return TSTypes.ASSIGN_MOD; }
    "&=" { return TSTypes.ASSIGN_AND; }
    "^=" { return TSTypes.ASSIGN_XOR; }
    "|=" { return TSTypes.ASSIGN_OR; }
    "<<=" { return TSTypes.ASSIGN_LSHIFT; }
    ">>=" { return TSTypes.ASSIGN_RSHIFT; }

    ">" { return TSTypes.COND_GT;}
    "<" { return TSTypes.COND_LT;}
    ">=" { return TSTypes.COND_GE;}
    "<=" { return TSTypes.COND_LE;}
    "==" { return TSTypes.COND_EQ;}
    "!=" { return TSTypes.COND_NE;}
    "||" { return TSTypes.COND_OR;}
    "<<" { return TSTypes.LSHIFT;}
    ">>" { return TSTypes.RSHIFT;}
    "&&" { return TSTypes.COND_AND;}
    "$=" { return TSTypes.COND_STREQ;}
    "!$=" { return TSTypes.COND_STRNE;}

    "^" { return TSTypes.OP_XOR; }
    "%" { return TSTypes.OP_MOD; }
    "&" { return TSTypes.OP_BIT_AND; }
    "|" { return TSTypes.OP_BIT_OR; }
    "+" { return TSTypes.OP_ADD; }
    "-" { return TSTypes.OP_SUB; }
    "*" { return TSTypes.OP_MUL; }
    "/" { return TSTypes.OP_DIV; }
    "?" { return TSTypes.TERNARY; }
    "@" { return TSTypes.OP_CAT; }
    "SPC" { return TSTypes.OP_CAT_SPC; }
    "TAB" { return TSTypes.OP_CAT_TAB; }
    "NL" { return TSTypes.OP_CAT_NL; }
    "!" { return TSTypes.OP_NOT; }
    "~" { return TSTypes.OP_BIT_NOT; }

    {ID}                                            { return TSTypes.ID; }
    {GLOBALVAR}                                     { return TSTypes.GLOBALVAR; }
    {LOCALVAR}                                      { return TSTypes.LOCALVAR; }
    {INTEGER}                                       { return TSTypes.INTEGER; }
    {FLOAT}                                         { return TSTypes.FLOAT; }
    {STRATOM}                                       { return TSTypes.STRATOM; }
    {TAGATOM}                                       { return TSTypes.TAGATOM; }
}
({SPACE}|{CRLF})+                                   { return TokenType.WHITE_SPACE; }
.                                                   { return TokenType.BAD_CHARACTER; }
