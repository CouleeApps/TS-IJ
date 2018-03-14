package com.torquescript.symbolExporter;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.DefaultProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFileFactory;
import com.torquescript.runner.TSRunConfiguration;
import com.torquescript.symbolExporter.classDump.TSClassDumpFile;
import com.torquescript.symbolExporter.classDump.TSClassDumpFileType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created on 3/13/18.
 */
public class TSSymbolExporter {
    private static HashMap<Project, TSSymbolExporter> EXPORTERS = new HashMap<>();

    public static TSSymbolExporter getExporter(TSRunConfiguration runConfiguration) {
        if (EXPORTERS.containsKey(runConfiguration.getProject())) {
            return EXPORTERS.get(runConfiguration.getProject());
        }

        TSSymbolExporter exporter = new TSSymbolExporter(runConfiguration);
        EXPORTERS.put(runConfiguration.getProject(), exporter);

        return exporter;
    }

    private TSRunConfiguration runConfiguration;
    private boolean dirty = true;
    private boolean running = false;
    private static final String LOCK = "Probably slow";
    private static String exportedConsole = null;

    private TSSymbolExporter(TSRunConfiguration runConfiguration) {
        this.runConfiguration = runConfiguration;
    }

    public void makeDirty() {
        synchronized (LOCK) {
            dirty = true;
        }
    }

    protected void updateFinished(boolean successful) {
        synchronized (LOCK) {
            running = false;
            dirty = !successful;
        }
    }

    public void checkUpdate() {
        synchronized (LOCK) {
            if (dirty && !running) {
                dirty = false;
                running = true;
                ApplicationManager.getApplication().invokeLater(this::generateSymbolList);
            }
        }
    }

    protected void generateSymbolList() {
        ProgressManager.getInstance().run(new Task.Modal(runConfiguration.getProject(), "Generate Symbol List", false) {
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                indicator.setText2("Dumping symbols from engine...");
                Executor executor = DefaultRunExecutor.getRunExecutorInstance();
                ExecutionEnvironment environment = ExecutionEnvironmentBuilder.create(executor, runConfiguration).build();
                TSSymbolExporterCommandLineState exportSymbols = new TSSymbolExporterCommandLineState(runConfiguration, environment);

                try {
                    ExecutionResult result = exportSymbols.execute(executor, new DefaultProgramRunner() {
                        @NotNull
                        @Override
                        public String getRunnerId() {
                            return "Torque App Checker";
                        }

                        @Override
                        public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
                            return true;
                        }
                    });
                    ProcessHandler handler = result.getProcessHandler();
                    if (!handler.isStartNotified()) {
                        handler.startNotify();
                    }
                    handler.waitFor();
                    exportSymbols.processTerminated();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                VirtualFile gameRoot = runConfiguration.getGameRoot();
                gameRoot.refresh(false, true);
                VirtualFile consoleLog = gameRoot.findChild("console.log");

                //Get contents of the console to parse out methods
                try {
                    if (consoleLog != null) {
                        byte[] contents = consoleLog.contentsToByteArray();
                        ProgressManager.getInstance().run(new Task.Backgroundable(getProject(), getTitle()) {
                            @Override
                            public void run(@NotNull ProgressIndicator indicator) {
                                ReadAction.run(() -> {
                                    boolean successful = extractSymbols(contents, indicator);
                                    updateFinished(successful);
                                });
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected boolean extractSymbols(byte[] contents, ProgressIndicator indicator) {
        indicator.setIndeterminate(false);
        indicator.setFraction(0);
        indicator.setText2("Extracting dumped symbols...");

        exportedConsole = new String(contents);
        String[] parts = exportedConsole.split(TSSymbolExporterCommandLineState.BOUNDARY);

        if (parts.length != 4) {
            //We have failed
            return false;
        }

        String consoleClassesText = parts[1];
        String consoleFunctionsText = parts[2];

        TSClassDumpFile file = createFile(runConfiguration.getProject(), consoleClassesText);

        indicator.setFraction(1);
        return true;
    }

    @NotNull
    public static TSClassDumpFile createFile(Project project, String text) {
        String fileName = "dummy.tscd";
        return (TSClassDumpFile) PsiFileFactory.getInstance(project).createFileFromText(fileName, TSClassDumpFileType.INSTANCE, text);
    }
}
