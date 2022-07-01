package com.github.vitalibo.intellij.bitbake;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class BitBakeClassFileType extends LanguageFileType {

    public static final BitBakeClassFileType Instance = new BitBakeClassFileType();

    private BitBakeClassFileType() {
        super(BitBakeLanguage.Instance);
    }

    @NotNull
    @Override
    public String getName() {
        return "BitBake Class";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "BitBake Class";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "BitBake classes file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "bbclass";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return BitBakeIcons.Class;
    }

}
