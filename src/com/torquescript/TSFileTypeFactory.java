package com.torquescript;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import org.jetbrains.annotations.NotNull;

public class TSFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(TSFileType.INSTANCE, "cs");
        fileTypeConsumer.consume(TSFileType.INSTANCE, "gui");
    }
}
