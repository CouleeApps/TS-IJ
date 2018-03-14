package com.torquescript.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.torquescript.symbolExporter.TSSymbolExporter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 3/13/18.
 */
public class TSRunConfiguration extends RunConfigurationBase {

    private static final String APP_PATH_KEY = "appPath";
    private static final String MAIN_CS_PATH_KEY = "mainCSPath";

    private String appPath;
    private String mainCSPath;

    public TSRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new TSRunConfigurationSettingsEditor(getProject());
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        if (appPath == null || appPath.isEmpty()) {
            throw new RuntimeConfigurationException("No app specified");
        }
        if (mainCSPath == null || mainCSPath.isEmpty()) {
            throw new RuntimeConfigurationException("No main.cs specified");
        }
        if (getGameRoot() == null) {
            throw new RuntimeConfigurationException("Cannot find game root");
        }
        TSSymbolExporter.getExporter(this).checkUpdate();
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return new TSCommandLineState(this, environment);
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        String extAppPath = element.getAttributeValue(APP_PATH_KEY);
        String extMainCSPath = element.getAttributeValue(MAIN_CS_PATH_KEY);
        if (!StringUtil.isEmpty(extAppPath)) {
            appPath = extAppPath;
        }
        if (!StringUtil.isEmpty(extMainCSPath)) {
            mainCSPath = extMainCSPath;
        }
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        if (!StringUtil.isEmpty(appPath)) {
            element.setAttribute(APP_PATH_KEY, appPath);
        }
        if (!StringUtil.isEmpty(mainCSPath)) {
            element.setAttribute(MAIN_CS_PATH_KEY, mainCSPath);
        }
    }

    public VirtualFile getGameRoot() {
        if (appPath == null || appPath.isEmpty()) {
            return null;
        }
        if (mainCSPath == null || mainCSPath.isEmpty()) {
            return null;
        }

        LocalFileSystem fileSystem = LocalFileSystem.getInstance();
        VirtualFile mainCS = fileSystem.findFileByPath(getMainCSPath());
        if (mainCS != null) {
            return mainCS.getParent();
        }
        VirtualFile app = fileSystem.findFileByPath(getAppPath());
        if (app != null) {
            VirtualFile parent = app.getParent();

            //Mac apps are in bundles so we might need to go up a few directories
            for (int i = 0; i < 5; i ++) {
                if (parent == null) {
                    break;
                }
                if (parent.findChild("main.cs") != null) {
                    return parent;
                }
                parent = parent.getParent();
            }
        }

        return null;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public String getMainCSPath() {
        return mainCSPath;
    }

    public void setMainCSPath(String mainCSPath) {
        this.mainCSPath = mainCSPath;
    }
}
