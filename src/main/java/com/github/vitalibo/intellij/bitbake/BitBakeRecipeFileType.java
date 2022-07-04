package com.github.vitalibo.intellij.bitbake;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class BitBakeRecipeFileType extends LanguageFileType {

    public static final BitBakeRecipeFileType Instance = new BitBakeRecipeFileType();

    private BitBakeRecipeFileType() {
        super(BitBakeLanguage.Instance);
    }

    @NotNull
    @Override
    public String getName() {
        return "BitBake Recipe";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "BitBake Recipe";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "BitBake recipe file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "bb";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return BitBakeIcons.Recipe;
    }

}
