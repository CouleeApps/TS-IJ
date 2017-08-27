package com.torquescript;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.torquescript.psi.TSTypes;
import org.jetbrains.annotations.NotNull;

public class TSCompletionContributor extends CompletionContributor {
    public TSCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(TSTypes.ID).withLanguage(TSLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                        for (String keyword : keywords) {
                            result.addElement(LookupElementBuilder.create(keyword));
                        }
                    }
                });
    }

    String keywords[] = new String[] {
            "function",
            "package",
            "if",
            "else",
            "while",
            "for",
            "switch",
            "switch$",
            "case",
            "default",
            "or",
            "break",
            "continue",
            "return",
            "datablock",
            "new"
    };

}
