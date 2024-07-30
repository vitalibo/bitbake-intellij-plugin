package com.github.vitalibo.intellij.bitbake;

import com.github.vitalibo.intellij.bitbake.psi.BitBakeTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class BitBakeSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey ASSIGNMENT_OPERATOR =
        createTextAttributesKey("BB_ASSIGNMENT_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey VARIABLE_NAME =
        createTextAttributesKey("BB_VARIABLE_NAME", DefaultLanguageHighlighterColors.CONSTANT);
    public static final TextAttributesKey VARIABLE_VALUE =
        createTextAttributesKey("BB_VARIABLE_VALUE", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey COMMENT =
        createTextAttributesKey("BB_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BAD_CHARACTER =
        createTextAttributesKey("BB_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
    public static final TextAttributesKey FUNCTION_NAME =
        createTextAttributesKey("BB_FUNCTION_NAME", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    public static final TextAttributesKey KEYWORD =
        createTextAttributesKey("BB_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] ASSIGNMENT_OPERATOR_KEYS = new TextAttributesKey[]{ASSIGNMENT_OPERATOR};
    private static final TextAttributesKey[] VARIABLE_KEY_KEYS = new TextAttributesKey[]{VARIABLE_NAME};
    private static final TextAttributesKey[] VARIABLE_VALUE_KEYS = new TextAttributesKey[]{VARIABLE_VALUE};
    private static final TextAttributesKey[] FUNCTION_NAME_KEYS = new TextAttributesKey[]{FUNCTION_NAME};
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new BitBakeLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(BitBakeTypes.OPERATOR)) {
            return ASSIGNMENT_OPERATOR_KEYS;
        } else if (tokenType.equals(BitBakeTypes.KEY)) {
            return VARIABLE_KEY_KEYS;
        } else if (tokenType.equals(BitBakeTypes.VALUE)) {
            return VARIABLE_VALUE_KEYS;
        } else if (tokenType.equals(BitBakeTypes.COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(BitBakeTypes.BB_FUNCTION_NAME)) {
            return FUNCTION_NAME_KEYS;
        } else if (tokenType.equals(BitBakeTypes.INCLUDE) || tokenType.equals(BitBakeTypes.INHERIT) || tokenType.equals(BitBakeTypes.REQUIRE)
            || tokenType.equals(BitBakeTypes.PYTHON) || tokenType.equals(BitBakeTypes.EXPORT) || tokenType.equals(BitBakeTypes.STATEMENT)) {
            return KEYWORD_KEYS;
        } else if (tokenType.equals(BitBakeTypes.OVERRIDE)) {
            return KEYWORD_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}