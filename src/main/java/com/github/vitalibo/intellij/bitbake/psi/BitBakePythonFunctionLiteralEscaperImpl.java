package com.github.vitalibo.intellij.bitbake.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import org.jetbrains.annotations.NotNull;

public class BitBakePythonFunctionLiteralEscaperImpl extends LiteralTextEscaper<BitBakePythonFunctionImpl> {

    protected BitBakePythonFunctionLiteralEscaperImpl(@NotNull BitBakePythonFunctionImpl host) {
        super(host);
    }

    @Override
    public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
        outChars.append(myHost.getText(), rangeInsideHost.getStartOffset(), rangeInsideHost.getEndOffset());
        return true;
    }

    @Override
    public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
        int offset = offsetInDecoded + rangeInsideHost.getStartOffset();
        if (offset < rangeInsideHost.getStartOffset()) offset = rangeInsideHost.getStartOffset();
        if (offset > rangeInsideHost.getEndOffset()) offset = rangeInsideHost.getEndOffset();
        return offset;
    }

    @Override
    public boolean isOneLine() {
        return false;
    }
}
