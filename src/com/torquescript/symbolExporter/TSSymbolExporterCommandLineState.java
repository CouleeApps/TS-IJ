package com.torquescript.symbolExporter;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.torquescript.runner.TSProcessHandler;
import com.torquescript.runner.TSRunConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/13/18.
 */
public class TSSymbolExporterCommandLineState extends CommandLineState {
    public static final String BOUNDARY = "=========================";

    private static final String EXPORT_MAIN_CS =
            "setLogMode(6);" +
            "echo(\"" + BOUNDARY + "\");" +
            "dumpConsoleClasses();" +
            "echo(\"" + BOUNDARY + "\");" +
            "dumpConsoleFunctions();" +
            "echo(\"" + BOUNDARY + "\");" +
            "schedule(2000, 0, quit);";

    @NotNull
    private TSRunConfiguration runConfiguration;

    private VirtualFile mainCS;
    private VirtualFile mainCSTmp;


    public TSSymbolExporterCommandLineState(@NotNull TSRunConfiguration runConfiguration, ExecutionEnvironment environment) {
        super(environment);
        setConsoleBuilder(null);
        this.runConfiguration = runConfiguration;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        String appPath = runConfiguration.getAppPath();
        final List<String> commandString = new ArrayList<>();
        commandString.add(appPath);
        commandString.add("-dedicated");
        final GeneralCommandLine commandLine = new GeneralCommandLine(commandString);

        VirtualFile gameRoot = runConfiguration.getGameRoot();
        if (gameRoot == null) {
            throw new ExecutionException("Cannot find game root");
        }

        mainCS = gameRoot.findChild("main.cs");

        WriteCommandAction.runWriteCommandAction(runConfiguration.getProject(), () -> {
            try {
                mainCS.rename(this, "main.cs.tmp");
                mainCSTmp = gameRoot.createChildData(this, "main.cs");
                mainCSTmp.setBinaryContent(EXPORT_MAIN_CS.getBytes());
            } catch (IOException e) {
                mainCSTmp = mainCS;
                mainCS = gameRoot.findChild("main.cs.tmp");
                e.printStackTrace();
            }
        });

        return new TSProcessHandler(commandLine);
    }

    public void processTerminated() {
        WriteCommandAction.runWriteCommandAction(runConfiguration.getProject(), () -> {
            try {
                if (mainCSTmp != null) {
                    mainCSTmp.delete(this);
                }
                mainCS.rename(this, "main.cs");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
