package com.github.vitalibo.intellij.bitbake;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class BitBakeAppendFileType extends LanguageFileType {

    public static final BitBakeAppendFileType Instance = new BitBakeAppendFileType();

    private BitBakeAppendFileType() {
        super(BitBakeLanguage.Instance);
    }

    @NotNull
    @Override
    public String getName() {
        return "BitBake Append";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "BitBake recipe append file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "bbappend";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return BitBakeIcons.Append;
    }

}
