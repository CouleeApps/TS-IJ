package com.torquescript.completion;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.psi.PsiElement;

public class TSCaseCorrectingInsertHandler implements InsertHandler<LookupElement> {
    public static InsertHandler<LookupElement> INSTANCE = new TSCaseCorrectingInsertHandler();

    @Override
    public void handleInsert(InsertionContext context1, LookupElement item) {
        Editor editor = context1.getEditor();

        //If they typed the element we should replace the thing they're typing with the correct case
        // Even though Torque is case-insensitive it looks bad if we don't
        PsiElement editing = context1.getFile().findElementAt(context1.getStartOffset());
        if (editing != null) {
            //Delete the element they were typing (note to only go to their carat position)
            editor.getDocument().deleteString(editing.getTextOffset(), editor.getCaretModel().getCurrentCaret().getOffset());
            //And insert the correctly cased version
            EditorModificationUtil.insertStringAtCaret(editor, item.getLookupString(), true);
        }
    }

}
