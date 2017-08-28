package com.torquescript;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.util.ThrowableRunnable;

public class TSPluginComponent implements ProjectComponent {
    @Override
    public void projectOpened() {
        try {
            WriteAction.run(new ThrowableRunnable<Throwable>() {
                @Override
                public void run() throws Throwable {
                    String ignoredFiles = FileTypeManager.getInstance().getIgnoredFilesList();
                    if (ignoredFiles.length() == 0) {
                        ignoredFiles = "*.dso";
                    } else {
                        ignoredFiles = ignoredFiles + ";*.dso";
                    }
                    FileTypeManager.getInstance().setIgnoredFilesList(ignoredFiles);
                }
            });
        } catch (Throwable ignored) {

        }
    }
}
