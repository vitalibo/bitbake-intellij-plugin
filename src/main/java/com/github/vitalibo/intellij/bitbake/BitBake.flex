package com.github.vitalibo.intellij.bitbake;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.github.vitalibo.intellij.bitbake.psi.BitBakeTypes;
import com.intellij.psi.TokenType;

%%

%class BitBakeLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=[\ \n\t\f]
STR_START_CHAR="\""
VALUE_CHARACTER=[^\n\f\\\"] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#")[^\r\n]*
ASSIGNMENT_OPERATOR=("="|"?="|"??="|":="|"+="|"=+"|".="|"=.")
KEY_CHARACTER=[^?=:+.\ \n\t\f\\\(\)] | "\\ "

%state WAITING_VALUE

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                                   { yybegin(YYINITIAL); return BitBakeTypes.COMMENT; }
<YYINITIAL> {KEY_CHARACTER}+                                        { yybegin(YYINITIAL); return BitBakeTypes.KEY; }
<YYINITIAL> {ASSIGNMENT_OPERATOR}                                   { yybegin(WAITING_VALUE); return BitBakeTypes.OPERATOR; }
<WAITING_VALUE> {CRLF}({CRLF}|{WHITE_SPACE})+                       { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
<WAITING_VALUE> {WHITE_SPACE}+                                      { yybegin(WAITING_VALUE); return TokenType.WHITE_SPACE; }
<WAITING_VALUE> {STR_START_CHAR}{VALUE_CHARACTER}*{STR_START_CHAR}  { yybegin(YYINITIAL); return BitBakeTypes.VALUE; }
({CRLF}|{WHITE_SPACE})+                                             { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
[^]                                                                 { return TokenType.BAD_CHARACTER; }
