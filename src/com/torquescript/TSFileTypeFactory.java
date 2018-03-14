package com.torquescript;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.torquescript.symbolExporter.classDump.TSClassDumpFileType;
import org.jetbrains.annotations.NotNull;

public class TSFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(TSFileType.INSTANCE, "cs" + FileTypeConsumer.EXTENSION_DELIMITER + "gui");
        fileTypeConsumer.consume(TSMisFileType.INSTANCE, "mis");
        fileTypeConsumer.consume(TSClassDumpFileType.INSTANCE, "tscd");
    }
}
