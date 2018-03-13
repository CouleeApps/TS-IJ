package com.torquescript.runner;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created on 3/13/18.
 */
public class TSRunConfigurationSettingsEditor extends SettingsEditor<TSRunConfiguration> {
    private JPanel mainPanel;
    private LabeledComponent<TextFieldWithBrowseButton> appPathComponent;

    public TSRunConfigurationSettingsEditor(@NotNull Project project) {
        TextFieldWithBrowseButton appPath = appPathComponent.getComponent();
        appPath.addBrowseFolderListener("App Path", "Specify path to Torque App executable", project, FileChooserDescriptorFactory.createSingleFileDescriptor());
    }

    @Override
    protected void resetEditorFrom(@NotNull TSRunConfiguration s) {

    }

    @Override
    protected void applyEditorTo(@NotNull TSRunConfiguration s) throws ConfigurationException {
        s.setAppPath(appPathComponent.getComponent().getText().trim());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }
}
