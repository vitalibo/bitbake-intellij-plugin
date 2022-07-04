package com.github.vitalibo.intellij.bitbake;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class BitBakeIncFileType extends LanguageFileType {

    public static final BitBakeIncFileType Instance = new BitBakeIncFileType();

    private BitBakeIncFileType() {
        super(BitBakeLanguage.Instance);
    }

    @NotNull
    @Override
    public String getName() {
        return "BitBake Inc";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "BitBake Inc";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "BitBake include file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "inc";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return BitBakeIcons.Inc;
    }

}
