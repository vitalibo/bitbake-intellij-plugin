package com.github.vitalibo.intellij.bitbake.psi;

import com.github.vitalibo.intellij.bitbake.BitBakeLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class BitBakeElementType extends IElementType {

    public BitBakeElementType(@NotNull @NonNls String debugName) {
        super(debugName, BitBakeLanguage.Instance);
    }

}
