package com.github.vitalibo.intellij.bitbake;

import com.github.vitalibo.intellij.bitbake.psi.BitBakeTypes;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class BitBakeCompletionContributor extends CompletionContributor {

    private static final List<String> GLOSSARY = load("/glossary");

    public BitBakeCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(BitBakeTypes.KEY),
            new CompletionProvider<CompletionParameters>() {
                public void addCompletions(@NotNull CompletionParameters parameters,
                                           @NotNull ProcessingContext context,
                                           @NotNull CompletionResultSet resultSet) {
                    GLOSSARY.forEach(key ->
                        resultSet.addElement(LookupElementBuilder.create(key)));
                }
            }
        );
    }

    private static List<String> load(String resource) {
        return new BufferedReader(new InputStreamReader(BitBakeCompletionContributor.class.getResourceAsStream(resource)))
            .lines()
            .collect(Collectors.toList());
    }

}
