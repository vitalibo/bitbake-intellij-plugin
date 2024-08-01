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
VALUE_CHARACTER=[^\n\f\\\"] | "\\"{CRLF} | "\\".
COMMENT= (("#")[^\r\n]*)
ASSIGNMENT_OPERATOR=("="|"?="|"??="|":="|"+="|"=+"|".="|"=.")
KEY_CHARACTER=[^?=:+.\ \n\t\f\\\(\)] | "\\ "
OVERRIDE=(":" {KEY_CHARACTER}+)
BROKEN_OVERRIDE=(":"+ {KEY_CHARACTER}+)
FN_NAME=([\w\.\-\+\{\}\$]+)
FN_TOKEN=({FN_NAME} {BROKEN_OVERRIDE}*)
VALUE=(("'" {VALUE_CHARACTER}* "'") | (\" {VALUE_CHARACTER}* \"))

%state WAITING_VALUE FUNCTION_NAME PY_FUNCTION_NAME PY_FUNCTION FUNCTION_VALUE INCLUDE_VALUE STATEMENT_VALUE

%%

<YYINITIAL> "inherit" { yybegin(INCLUDE_VALUE); return BitBakeTypes.INHERIT; }
<YYINITIAL> "include" { yybegin(INCLUDE_VALUE); return BitBakeTypes.INCLUDE; }
<YYINITIAL> "require" { yybegin(INCLUDE_VALUE); return BitBakeTypes.REQUIRE; }
<YYINITIAL> "export" { return BitBakeTypes.EXPORT; }
<YYINITIAL> "EXPORT_FUNCTIONS" { yybegin(INCLUDE_VALUE); return BitBakeTypes.EXPORT; }
<YYINITIAL> addtask|deltask|addhandler|after|before { yybegin(STATEMENT_VALUE); return BitBakeTypes.STATEMENT; }

<INCLUDE_VALUE> .+ { yybegin(YYINITIAL); return BitBakeTypes.INCLUDE_REST; }
<STATEMENT_VALUE> {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
<STATEMENT_VALUE> [^\ \n\t\f]+ { yybegin(YYINITIAL); return BitBakeTypes.STATEMENT_REST; }

<YYINITIAL> ^((python|fakeroot)\s*)*({FN_TOKEN})?\s*\(\s*\)\s*\{$  { yypushback(yylength()); yybegin(FUNCTION_NAME); }
<YYINITIAL> ^(def\s+)([0-9A-Za-z_-]+)(\s*\(.*\)\s*):\s*  { yypushback(yylength()); yybegin(PY_FUNCTION_NAME); }

<FUNCTION_NAME> {
  "python" { return BitBakeTypes.PYTHON; }
  "fakeroot" { return BitBakeTypes.FAKEROOT; }
  "(" { return BitBakeTypes.LB; }
  ")" { return BitBakeTypes.RB; }
  "{" { yybegin(FUNCTION_VALUE); return BitBakeTypes.LBB; }
  {OVERRIDE} { return BitBakeTypes.OVERRIDE; }
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
  {CRLF} | {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
  [^] { return BitBakeTypes.FB; }
}

<FUNCTION_VALUE> {
  ^\} { yybegin(YYINITIAL); return BitBakeTypes.RBB; }
  [^] { return BitBakeTypes.FB; }
}

<YYINITIAL> {COMMENT} { return BitBakeTypes.COMMENT; }
<YYINITIAL> {KEY_CHARACTER}+ { return BitBakeTypes.KEY; }
<YYINITIAL> {OVERRIDE} { return BitBakeTypes.OVERRIDE; }
<YYINITIAL> {ASSIGNMENT_OPERATOR} { yybegin(WAITING_VALUE); return BitBakeTypes.OPERATOR; }
<YYINITIAL> {CRLF} | {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
<WAITING_VALUE> {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
<WAITING_VALUE> {VALUE} { yybegin(YYINITIAL); return BitBakeTypes.VALUE; }

[^] { return TokenType.BAD_CHARACTER; }
