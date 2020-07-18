package com.github.vitalibo.intellij.bitbake;

import com.intellij.lang.Language;

public class BitBakeLanguage extends Language {

    public static final BitBakeLanguage Instance = new BitBakeLanguage();

    private BitBakeLanguage() {
        super("BitBake");
    }

}
