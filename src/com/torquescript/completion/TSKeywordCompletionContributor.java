package com.torquescript.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class TSKeywordCompletionContributor extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        for (String keyword : keywords) {
            result.addElement(LookupElementBuilder.create(keyword));
        }
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
