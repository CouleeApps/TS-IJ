package com.torquescript.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;

/**
 * Created on 3/13/18.
 */
public class TSProcessHandler extends KillableColoredProcessHandler {
    public TSProcessHandler(@NotNull GeneralCommandLine commandLine) throws ExecutionException {
        super(commandLine);
    }
}
