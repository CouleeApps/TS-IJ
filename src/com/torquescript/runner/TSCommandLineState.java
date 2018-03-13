package com.torquescript.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/13/18.
 */
public class TSCommandLineState extends CommandLineState {
    private TSRunConfiguration runConfiguration;

    public TSCommandLineState(TSRunConfiguration runConfiguration, ExecutionEnvironment environment) {
        super(environment);
        this.runConfiguration = runConfiguration;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        String appPath = runConfiguration.getAppPath();
        final List<String> commandString = new ArrayList<>();
        commandString.add(appPath);
        final GeneralCommandLine commandLine = new GeneralCommandLine(commandString);
        return new TSProcessHandler(commandLine);
    }
}
