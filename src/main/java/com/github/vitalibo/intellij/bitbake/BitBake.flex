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
STR_START_CHAR=\"|\'
VALUE_CHARACTER=[^\n\f\\\"] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT= (("#")[^\r\n]*)
ASSIGNMENT_OPERATOR=("="|"?="|"??="|":="|"+="|"=+"|".="|"=.")
KEY_CHARACTER=[^?=:+.\ \n\t\f\\\(\)] | "\\ "

%state WAITING_VALUE FUNCTION_NAME PY_FUNCTION_NAME PY_FUNCTION FUNCTION_VALUE INCLUDE_VALUE STATEMENT_VALUE

%%

<YYINITIAL> "inherit" { yybegin(INCLUDE_VALUE); return BitBakeTypes.INHERIT; }
<YYINITIAL> "include" { yybegin(INCLUDE_VALUE); return BitBakeTypes.INCLUDE; }
<YYINITIAL> "require" { yybegin(INCLUDE_VALUE); return BitBakeTypes.REQUIRE; }
<YYINITIAL> "export" { return BitBakeTypes.EXPORT; }
<YYINITIAL> "EXPORT_FUNCTIONS" { yybegin(INCLUDE_VALUE); return BitBakeTypes.EXPORT; }
<YYINITIAL> addtask|addhandler|after|before { yybegin(STATEMENT_VALUE); return BitBakeTypes.STATEMENT; }

<INCLUDE_VALUE> .+ { yybegin(YYINITIAL); return BitBakeTypes.INCLUDE_REST; }
<STATEMENT_VALUE> {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
<STATEMENT_VALUE> [^\ \n\t\f]+ { yybegin(YYINITIAL); return BitBakeTypes.STATEMENT_REST; }

<YYINITIAL> ^((python|fakeroot)\s*)*([\w\.\-\+\{\}\$]+)?\s*\(\s*\)\s*\{$  { yypushback(yylength()); yybegin(FUNCTION_NAME); }
<YYINITIAL> ^(def\s+)([0-9A-Za-z_-]+)(\s*\(.*\)\s*):\s*  { yypushback(yylength()); yybegin(PY_FUNCTION_NAME); }

<FUNCTION_NAME> {
  "python" { return BitBakeTypes.PYTHON; }
  "(" { return BitBakeTypes.LB; }
  ")" { return BitBakeTypes.RB; }
  "{" { yybegin(FUNCTION_VALUE); return BitBakeTypes.LBB; }
  [\w\.\-\+\{\}\$]+ { return BitBakeTypes.BB_FUNCTION_NAME; }
  {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
}

<PY_FUNCTION_NAME> {
  "def" { return BitBakeTypes.DEF; }
  "(" { return BitBakeTypes.LB; }
  ")" { return BitBakeTypes.RB; }
  ":" { yybegin(PY_FUNCTION); return BitBakeTypes.LBB; }
  ","  { return BitBakeTypes.COMMA; }
  [\w\.\-\+\{\}\$]+ { return BitBakeTypes.BB_FUNCTION_NAME; }
  {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
}

<PY_FUNCTION> {
  \n\n+ { yybegin(YYINITIAL); return BitBakeTypes.RBB; }
  [^] { return BitBakeTypes.FB; }
}

<FUNCTION_VALUE> {
  ^\} { yybegin(YYINITIAL); return BitBakeTypes.RBB; }
  [^] { return BitBakeTypes.FB; }
}

<YYINITIAL> {KEY_CHARACTER}+ { return BitBakeTypes.KEY; }
<YYINITIAL> {ASSIGNMENT_OPERATOR} { yybegin(WAITING_VALUE); return BitBakeTypes.OPERATOR; }
<WAITING_VALUE> {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
<WAITING_VALUE> {STR_START_CHAR}{VALUE_CHARACTER}*{STR_START_CHAR} { yybegin(YYINITIAL); return BitBakeTypes.VALUE; }

<YYINITIAL> {
  ^#$ { yybegin(YYINITIAL); return BitBakeTypes.COMMENT; }
  {END_OF_LINE_COMMENT} { yybegin(YYINITIAL); return BitBakeTypes.COMMENT; }
}

<YYINITIAL> {END_OF_LINE_COMMENT} { yybegin(YYINITIAL); return BitBakeTypes.COMMENT; }
({CRLF}|{WHITE_SPACE})+ { return TokenType.WHITE_SPACE; }
[^] { return TokenType.BAD_CHARACTER; }
