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

CRLF = \R
WS = [\ \t\f]
WHITE_SPACE = [\ \n\t\f]
VALUE_CHARACTER = [^\n\f\\\"] | "\\"{CRLF} | "\\".
COMMENT = (("#")[^\r\n]*)
ASSIGNMENT_OPERATOR = ("="|"?="|"??="|":="|"+="|"=+"|".="|"=.")
KEY_CHARACTER = [^?=:+.\ \n\t\f\\\(\)\[\]] | "\\ "
OVERRIDE = (":" {KEY_CHARACTER}+)
BROKEN_OVERRIDE = (":"+ {KEY_CHARACTER}+)
FN_NAME = ([\w\.\-\+\{\}\$]+)
FN_TOKEN = ({FN_NAME} {BROKEN_OVERRIDE}*)
VALUE = (("'" {VALUE_CHARACTER}* "'") | (\" {VALUE_CHARACTER}* \"))

%state WAITING_VALUE FUNCTION_NAME PY_FUNCTION_NAME PY_FUNCTION FUNCTION_VALUE INCLUDE_VALUE STATEMENT_VALUE
%state EXPORT_STATEMENT EXPORT_FUNC_STATEMENT
%state LINE_WITH_FLAG FLAG
%state ADD_TASK_STATEMENT

%%

<YYINITIAL> ^"inherit" { yybegin(INCLUDE_VALUE); return BitBakeTypes.INHERIT; }
<YYINITIAL> ^"include" { yybegin(INCLUDE_VALUE); return BitBakeTypes.INCLUDE; }
<YYINITIAL> ^"require" { yybegin(INCLUDE_VALUE); return BitBakeTypes.REQUIRE; }
<YYINITIAL> ^"export" { yybegin(EXPORT_STATEMENT); return BitBakeTypes.EXPORT; }
<YYINITIAL> ^"EXPORT_FUNCTIONS" { yybegin(EXPORT_FUNC_STATEMENT); return BitBakeTypes.EXPORT_FUNC; }
<YYINITIAL> ^"addtask" { yybegin(ADD_TASK_STATEMENT); return BitBakeTypes.ADD_TASK; }
<YYINITIAL> ^("deltask"|"addhandler") { yybegin(STATEMENT_VALUE); return BitBakeTypes.STATEMENT; }

<INCLUDE_VALUE> .+ { yybegin(YYINITIAL); return BitBakeTypes.INCLUDE_REST; }
<STATEMENT_VALUE> {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
<STATEMENT_VALUE> [^\ \n\t\f]+ { yybegin(YYINITIAL); return BitBakeTypes.STATEMENT_REST; }

<YYINITIAL> ^((python|fakeroot)\s*)*({FN_TOKEN})?\s*\(\s*\){WS}*\{$  { yypushback(yylength()); yybegin(FUNCTION_NAME); }
<YYINITIAL> ^(def\s+)([0-9A-Za-z_-]+)(\s*\(.*\)\s*):\s*  { yypushback(yylength()); yybegin(PY_FUNCTION_NAME); }
<YYINITIAL> ^({KEY_CHARACTER}+ {OVERRIDE}* "[" .* "]" ) { yypushback(yylength()); yybegin(LINE_WITH_FLAG); }

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
  <<EOF>> { yybegin(YYINITIAL); return BitBakeTypes.RBB; }
  {CRLF} | {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
  [^] { return BitBakeTypes.FB; }
}

<FUNCTION_VALUE> {
  ^\} { yybegin(YYINITIAL); return BitBakeTypes.RBB; }
  [^] { return BitBakeTypes.FB; }
}

<EXPORT_STATEMENT> {
  {VALUE} { yybegin(YYINITIAL); return BitBakeTypes.VALUE; }
  {KEY_CHARACTER}+ { return BitBakeTypes.KEY; }
  {ASSIGNMENT_OPERATOR} { return BitBakeTypes.OPERATOR; }
  {CRLF}+ { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
  {WS}+ { return TokenType.WHITE_SPACE; }
}

<EXPORT_FUNC_STATEMENT> {
  [\w\.\-\+\{\}\$]+ { return BitBakeTypes.BB_FUNCTION_NAME; }
  {CRLF}+ { yybegin(YYINITIAL); return BitBakeTypes.CRLF; }
  {WS}+ { return TokenType.WHITE_SPACE; }
}

<LINE_WITH_FLAG> {
  {KEY_CHARACTER}+ { return BitBakeTypes.KEY; }
  {OVERRIDE} { return BitBakeTypes.OVERRIDE; }
  {WHITE_SPACE}+ { return TokenType.BAD_CHARACTER; }
  \[ { yybegin(FLAG); return BitBakeTypes.ALB; }
}

<FLAG> {
   [\w\.\-\+]+ { return BitBakeTypes.FLAG; }
   \] { yybegin(YYINITIAL); return BitBakeTypes.ARB; }
}

<ADD_TASK_STATEMENT> {
  "after" { return BitBakeTypes.AFTER; }
  "before" { return BitBakeTypes.BEFORE; }
  {KEY_CHARACTER}+ { return BitBakeTypes.BB_FUNCTION_NAME; }
  {WS}+ { return TokenType.WHITE_SPACE; }
  {CRLF}+ { yybegin(YYINITIAL); return BitBakeTypes.CRLF; }
}

<YYINITIAL> ^{COMMENT} { return BitBakeTypes.COMMENT; }
<YYINITIAL> ^{KEY_CHARACTER}+ { return BitBakeTypes.KEY; }
<YYINITIAL> {OVERRIDE} { return BitBakeTypes.OVERRIDE; }
<YYINITIAL> {ASSIGNMENT_OPERATOR} { yybegin(WAITING_VALUE); return BitBakeTypes.OPERATOR; }
<YYINITIAL> {CRLF} | {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
<WAITING_VALUE> {WHITE_SPACE}+ { return TokenType.WHITE_SPACE; }
<WAITING_VALUE> {VALUE} { yybegin(YYINITIAL); return BitBakeTypes.VALUE; }

[^] { return TokenType.BAD_CHARACTER; }
