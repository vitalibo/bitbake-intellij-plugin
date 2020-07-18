package com.github.vitalibo.intellij.bitbake;

import com.intellij.lexer.FlexAdapter;

public class BitBakeLexerAdapter extends FlexAdapter {

    public BitBakeLexerAdapter() {
        super(new BitBakeLexer(null));
    }

}
