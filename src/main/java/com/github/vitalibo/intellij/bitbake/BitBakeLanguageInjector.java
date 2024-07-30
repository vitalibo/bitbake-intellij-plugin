package com.github.vitalibo.intellij.bitbake;

import com.github.vitalibo.intellij.bitbake.psi.BitBakeNativePythonFunctionImpl;
import com.github.vitalibo.intellij.bitbake.psi.BitBakePythonFunctionImpl;
import com.github.vitalibo.intellij.bitbake.psi.impl.BitBakeBbBashFunctionImpl;
import com.intellij.ide.plugins.PluginManagerConfigurable;
import com.intellij.lang.Language;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;

public class BitBakeLanguageInjector implements LanguageInjector {

    @Override
    public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if (host instanceof BitBakeBbBashFunctionImpl) {

            final FileType fileType = FileTypeManager.getInstance().getFileTypeByExtension("sh");
            if (!(fileType instanceof LanguageFileType)) {
                showRecommendedPluginNotification(host.getProject());
                return;
            }
            final Language language = ((LanguageFileType) fileType).getLanguage();
            String text = host.getText();
            injectionPlacesRegistrar.addPlace(language, new TextRange(text.indexOf('{') + 2, text.lastIndexOf('}')), "\n", "\n");

        } else if ((host instanceof BitBakeNativePythonFunctionImpl) || (host instanceof BitBakePythonFunctionImpl)) {

            final FileType fileType = FileTypeManager.getInstance().getFileTypeByExtension("py");
            if (!(fileType instanceof LanguageFileType)) {
                showRecommendedPluginNotification(host.getProject());
                return;
            }
            final Language language = ((LanguageFileType) FileTypeManager.getInstance().getFileTypeByExtension("py")).getLanguage();
            if (host instanceof BitBakeNativePythonFunctionImpl) {
                injectionPlacesRegistrar.addPlace(language, new TextRange(0, host.getText().length()), "import oe\nimport bb\nimport os\n\n", "\n");
            } else if (host instanceof BitBakePythonFunctionImpl) {
                String text = host.getText();
                injectionPlacesRegistrar.addPlace(language, new TextRange(text.indexOf('{') + 2, text.lastIndexOf('}')), "import oe\nimport bb\nimport os\n\ndef name(d):", "\n");
            }
        }
    }

    private static boolean recommendedPluginNotificationShown = false;

    private static void showRecommendedPluginNotification(Project project) {
        if (BitBakeLanguageInjector.recommendedPluginNotificationShown) {
            return;
        }
        BitBakeLanguageInjector.recommendedPluginNotificationShown = true;
        final Notification notification = new Notification(
                "com.github.vitalibo.intellij.bitbake",
                "Recommended Syntax Highlighters",
                "It is recommended to install the Python and Shell Script plugins for full syntax highlighting.",
                NotificationType.INFORMATION
        );
        notification.addAction(NotificationAction.createSimple("Visit Marketplace", () -> {
            ShowSettingsUtil.getInstance().showSettingsDialog(
                    ProjectManager.getInstance().getDefaultProject(),
                    PluginManagerConfigurable.class,
                    c -> c.openMarketplaceTab(
                            "script /tag:\"Programming Language\" /organization:\"JetBrains s.r.o.\""
                    )
            );
        }));
        notification.notify(project);
    }
}
