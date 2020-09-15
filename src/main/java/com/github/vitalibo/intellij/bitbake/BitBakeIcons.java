package com.github.vitalibo.intellij.bitbake;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BitBakeIcons {

    public static final @NotNull Icon Recipe = load("/fileTypes/bb.svg");
    public static final @NotNull Icon Append = load("/fileTypes/bbappend.svg");
    public static final @NotNull Icon Class = load("/fileTypes/bbclass.svg");
    public static final @NotNull Icon Inc = load("/fileTypes/bbinc.svg");

    private BitBakeIcons() {
    }

    private static @NotNull Icon load(@NotNull String path) {
        return IconLoader.getIcon(path, BitBakeIcons.class);
    }

}
