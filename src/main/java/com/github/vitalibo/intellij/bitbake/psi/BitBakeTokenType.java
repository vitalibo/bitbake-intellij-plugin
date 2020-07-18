package com.github.vitalibo.intellij.bitbake.psi;

import com.github.vitalibo.intellij.bitbake.BitBakeLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class BitBakeTokenType extends IElementType {

    public BitBakeTokenType(@NotNull @NonNls String debugName) {
        super(debugName, BitBakeLanguage.Instance);
    }

    @Override
    public String toString() {
        return "BitBakeTokenType." + super.toString();
    }

}
