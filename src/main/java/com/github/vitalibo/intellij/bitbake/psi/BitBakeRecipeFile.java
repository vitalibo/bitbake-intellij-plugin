package com.github.vitalibo.intellij.bitbake.psi;

import com.github.vitalibo.intellij.bitbake.BitBakeLanguage;
import com.github.vitalibo.intellij.bitbake.BitBakeRecipeFileType;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class BitBakeRecipeFile extends PsiFileBase {

    public BitBakeRecipeFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, BitBakeLanguage.Instance);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return BitBakeRecipeFileType.Instance;
    }

    @Override
    public String toString() {
        return "BitBake Recipe File";
    }

}
