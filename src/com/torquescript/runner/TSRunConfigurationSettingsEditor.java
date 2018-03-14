package com.torquescript.runner;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.torquescript.TSFileType;
import com.torquescript.symbolExporter.TSSymbolExporter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created on 3/13/18.
 */
public class TSRunConfigurationSettingsEditor extends SettingsEditor<TSRunConfiguration> {
    private JPanel mainPanel;
    private LabeledComponent<TextFieldWithBrowseButton> appPathComponent;
    private LabeledComponent<TextFieldWithBrowseButton> mainCSPathComponent;

    public TSRunConfigurationSettingsEditor(@NotNull Project project) {
        TextFieldWithBrowseButton appPath = appPathComponent.getComponent();
        appPath.addBrowseFolderListener("App Path", "Specify path to Torque App executable", project, FileChooserDescriptorFactory.createSingleFileDescriptor());

        TextFieldWithBrowseButton mainCSPath = mainCSPathComponent.getComponent();
        //noinspection DialogTitleCapitalization
        mainCSPath.addBrowseFolderListener("main.cs Path", "Specify path to main.cs", project, FileChooserDescriptorFactory.createSingleFileDescriptor(TSFileType.INSTANCE));
    }

    @Override
    protected void resetEditorFrom(@NotNull TSRunConfiguration s) {
        appPathComponent.getComponent().setText(s.getAppPath());
        mainCSPathComponent.getComponent().setText(s.getMainCSPath());
    }

    @Override
    protected void applyEditorTo(@NotNull TSRunConfiguration s) throws ConfigurationException {
        s.setAppPath(appPathComponent.getComponent().getText().trim());
        s.setMainCSPath(mainCSPathComponent.getComponent().getText().trim());

        TSSymbolExporter.getExporter(s).makeDirty();
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }
}
