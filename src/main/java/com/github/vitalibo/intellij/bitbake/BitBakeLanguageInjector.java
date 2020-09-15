package com.github.vitalibo.intellij.bitbake;

import com.github.vitalibo.intellij.bitbake.psi.BitBakeNativePythonFunctionImpl;
import com.github.vitalibo.intellij.bitbake.psi.BitBakePythonFunctionImpl;
import com.github.vitalibo.intellij.bitbake.psi.impl.BitBakeBbBashFunctionImpl;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;

public class BitBakeLanguageInjector implements LanguageInjector {

    @Override
    public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if (host instanceof BitBakeBbBashFunctionImpl) {
            final Language language = ((LanguageFileType) FileTypeManager.getInstance().getFileTypeByExtension("sh")).getLanguage();
            injectionPlacesRegistrar.addPlace(language, new TextRange(0, host.getText().length()), "\n", "\n");
        }

        if (host instanceof BitBakeNativePythonFunctionImpl) {
            final Language language = ((LanguageFileType) FileTypeManager.getInstance().getFileTypeByExtension("py")).getLanguage();
            injectionPlacesRegistrar.addPlace(language, new TextRange(0, host.getText().length()), "import oe\nimport bb\nimport os\n\n", "\n");
        }

        if (host instanceof BitBakePythonFunctionImpl) {
            final Language language = ((LanguageFileType) FileTypeManager.getInstance().getFileTypeByExtension("py")).getLanguage();
            String text = host.getText();
            injectionPlacesRegistrar.addPlace(language, new TextRange(text.indexOf('{') + 2, text.lastIndexOf('}')), "import oe\nimport bb\nimport os\n\ndef name(d):", "\n");
        }
    }
}
