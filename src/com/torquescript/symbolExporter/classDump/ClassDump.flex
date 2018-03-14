package com.torquescript.symbolExporter.classDump;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.torquescript.symbolExporter.classDump.psi.TSClassDumpTypes;
import com.intellij.psi.TokenType;

%%

%class TSClassDumpLexer
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
INTEGER  ="-"?{DIGIT}+
FLOAT    =({INTEGER}\.{INTEGER})|({INTEGER}(\.{INTEGER})?[eE][+-]?{INTEGER})|(\.{INTEGER})|((\.{INTEGER})?[eE][+-]?{INTEGER})
LETTER   =[A-Za-z_]
IDTAIL   =[A-Za-z0-9_]
ID       ={LETTER}{IDTAIL}*
SPACE    =[ \t\f]
CRLF     =[\r\n]

GROUP_HEADER_START="/*! @name"
GROUP_HEADER_SEPARATOR="/*! */"
GROUP_FOLD_START="@{ */"
GROUP_FOLD_END="/// @}"
GROUP_NAME_WORD=[A-Za-z0-9_/:]*

DOC_COMMENT=("///"[^\n\r]*[\n\r]+)+
COMMENT=("//"[^\n\r]*[\n\r]+)+

BLOCK_START="/*"
BLOCK_END="*/"
BLOCK_INNER="!"([^*]|(\*[^/])|[\n\r])+

%state WAITING_VALUE

%%

<YYINITIAL> {
    "class"                   { return TSClassDumpTypes.CLASS; }
    "public"                  { return TSClassDumpTypes.PUBLIC; }
    "public:"                 { return TSClassDumpTypes.PUBLIC_COLON; }
    "virtual"                 { return TSClassDumpTypes.VIRTUAL; }
    "const"                   { return TSClassDumpTypes.CONST; }
    "true"                    { return TSClassDumpTypes.TRUE; }
    "false"                   { return TSClassDumpTypes.FALSE; }
    ":"                       { return TSClassDumpTypes.COLON; }
    ";"                       { return TSClassDumpTypes.SEMICOLON; }
    ","                       { return TSClassDumpTypes.COMMA; }
    "="                       { return TSClassDumpTypes.EQUALS; }
    "*"                       { return TSClassDumpTypes.ASTERISK; }
    "("                       { return TSClassDumpTypes.PAREN_OPEN; }
    ")"                       { return TSClassDumpTypes.PAREN_CLOSE; }
    "["                       { return TSClassDumpTypes.BRACKET_OPEN; }
    "]"                       { return TSClassDumpTypes.BRACKET_CLOSE; }
    "{"                       { return TSClassDumpTypes.BRACE_OPEN; }
    "}"                       { return TSClassDumpTypes.BRACE_CLOSE; }
    "..."                     { return TSClassDumpTypes.ELLIPSIS; }

    {INTEGER}                 { return TSClassDumpTypes.INTEGER; }
    {FLOAT}                   { return TSClassDumpTypes.FLOAT; }
    {ID}                      { return TSClassDumpTypes.ID; }
    {DOC_COMMENT}             { return TSClassDumpTypes.DOC_COMMENT; }
    {GROUP_HEADER_START}      { return TSClassDumpTypes.GROUP_HEADER_START; }
    {GROUP_HEADER_SEPARATOR}  { return TSClassDumpTypes.GROUP_HEADER_SEPARATOR; }
    {GROUP_FOLD_START}        { return TSClassDumpTypes.GROUP_FOLD_START; }
    {GROUP_FOLD_END}          { return TSClassDumpTypes.GROUP_FOLD_END; }
    {GROUP_NAME_WORD}         { return TSClassDumpTypes.GROUP_NAME_WORD; }
    {BLOCK_START}             { return TSClassDumpTypes.BLOCK_START; }
    {BLOCK_END}               { return TSClassDumpTypes.BLOCK_END; }
    {BLOCK_INNER}             { return TSClassDumpTypes.BLOCK_INNER; }
    {STRATOM}                 { return TSClassDumpTypes.STRATOM; }
}
({SPACE}|{CRLF})+                                   { return TokenType.WHITE_SPACE; }
.                                                   { return TokenType.BAD_CHARACTER; }
