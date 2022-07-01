package com.github.vitalibo.intellij.bitbake;

import com.github.vitalibo.intellij.bitbake.psi.BitBakeTypes;
import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.*;

public class BitBakeFormattingModelBuilder implements FormattingModelBuilder {

    @NotNull
    @Override
    public FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        return FormattingModelProvider
            .createFormattingModelForPsiFile(formattingContext.getPsiElement().getContainingFile(),
                new BitBakeBlock(formattingContext.getPsiElement().getNode(),
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    createSpaceBuilder(formattingContext.getCodeStyleSettings())),
                formattingContext.getCodeStyleSettings());
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, BitBakeLanguage.Instance)
            .around(BitBakeTypes.OPERATOR)
            .spaceIf(settings.getCommonSettings(BitBakeLanguage.Instance.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
            .before(BitBakeTypes.VARIABLE)
            .none();
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }
}
