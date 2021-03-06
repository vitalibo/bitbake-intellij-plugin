package com.github.vitalibo.intellij.bitbake.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;

public abstract class BitBakeNativePythonFunctionImpl extends ASTWrapperPsiElement implements PsiLanguageInjectionHost {

    public BitBakeNativePythonFunctionImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isValidHost() {
        return true;
    }

    @Override
    public PsiLanguageInjectionHost updateText(@NotNull String text) {
        throw new IllegalStateException();
    }

    @Override
    public @NotNull LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
        return new BitBakeNativePythonFunctionLiteralEscaperImpl(this);
    }

}
